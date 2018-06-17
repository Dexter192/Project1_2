package MultiPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.Input.TextInputListener;
import Obstacles.Hole;
import Obstacles.Obstacle;
import Obstacles.ObstacleBox;
import collisionDetector.CollisionDetector;
import gameEngine3D.Golfball;
import gameEngine3D.LineIndicator;
import menu.AbstractScreen;
import menu.ScreenManager;
import physics.DifferentialEquationSolver;
import physics.Physics;

public class GameScreenMultiPlayer extends AbstractScreen{
		//For different functionalities
		private boolean ballCollisionAllowed = true;
		private boolean teamMode = true;
		private boolean elasticBand = true;
		private float maxAllowedDistance = 20;
		private ArrayList<ElasticBand> elastics;
		//For the view
		private PerspectiveCamera camera;
		private ModelBatch modelBatch;
		private Environment environment;
		private CameraInputController camController;
		public Stage stage;
		//For the game
		private DifferentialEquationSolver ode;
		private ArrayList<Golfball> golfball;
		private ArrayList<Hole> holes;
		private CollisionDetector collisionDetector;
		private Set<Obstacle> obstacleList = new HashSet<Obstacle>();
		public float hitStrength = 0.5f;
		private String player; 
		private ArrayList<Integer> finished = new ArrayList<>();
		
		public int players = 0;
		public int index = 0;
		private Obstacle collisionBox;
		//UI
		public TextButton hitButton;
		public TextField strength;
		public LineIndicator indicatorLine;
		public static BoundingBox courseDimensions;
		private InputMultiplexer inputMultiplexer;
		public Skin skin;
	    private Label text;
	    private LabelStyle textStyle;
	    private BitmapFont font = new BitmapFont();

		

		
		
		@Override
		public void buildStage() {
			//Strength- Slider
			stage = new Stage();
			skin = new Skin(Gdx.files.internal("uiskin.json"));
		    Slider strength = new Slider(0f,1f,0.01f,true,skin);
		    strength.setValue(0.5f);
		    strength.addListener(new ChangeListener() {
		    	@Override
				public void changed(ChangeEvent event, Actor actor) {
		    		float x = strength.getValue();
		    		hitStrength = x;
				}
		    });
		    stage.addActor(strength);
		    //Player/Team 
		    textStyle = new LabelStyle();
		    textStyle.font = font;
		    text = new Label(player,textStyle);
		    text.setX(10f);
		    text.setY(10f);
		    stage.addActor(text);

			// Initialise environment
			environment = new Environment();
			environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
			environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
			modelBatch = new ModelBatch();
			// initialise camera
			camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			camera.position.set(40f, 15f, 40f);
			camera.lookAt(0, 0, 0);
			camera.near = 1f;
			camera.far = 300f;
			camera.update();
			camController = new CameraInputController(camera);
			
			//Initialise Players
			golfball = new ArrayList<>();
			numberOfPlayers();
			try        
			{
				while(players == 0)
			    Thread.sleep(1000);
			} 
			catch(InterruptedException ex) 
			{
			    Thread.currentThread().interrupt();
			}
			if(teamMode) makeGolfBallsTeamMode(players);
			else makeGolfBalls(players);
			player = "Player : " + (index) + " score: " + golfball.get(index).getScore();
			

	
			collisionDetector = new CollisionDetector(this);
			initObstacles();
			calculateCouseDimensions(obstacleList);
			holes = new ArrayList<>();
			if(teamMode) makeHolesTeamMode(players);
			else makeHoles(players);
			for(Hole h: holes) obstacleList.add(h);

			float[] a = { 0.01f,0.1f };
			float[] b = { 0.01f,0.1f };
			Physics physics = new Physics(a, b);
			
			ode = new DifferentialEquationSolver(physics, golfball.get(0).getMass());
			golfball.get(0).setODE(ode);
			
			// inizialize hit indicator line
			indicatorLine = new LineIndicator();
			if(teamMode) {
				elastics = new ArrayList<>();
				for(int i = 0; i < (players); i+=2) {
					elastics.add(new ElasticBand(maxAllowedDistance,golfball.get(i),golfball.get(i+1), elasticBand));
				}
			}
			// initialize input
			inputMultiplexer = new InputMultiplexer();
			inputMultiplexer.addProcessor(stage);
			inputMultiplexer.addProcessor(new InputListenerMP(this, golfball));
			inputMultiplexer.addProcessor(camController);
			Gdx.input.setInputProcessor(inputMultiplexer);
			

		}

		/**
		 * This method updates the game.
		 */
		@Override
		public void render(float delta) {
			
			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

			camController.update();
			modelBatch.begin(camera);
			stage.draw();
			for(int i = 0; i < golfball.size();i++)
				modelBatch.render(golfball.get(i).getBallInstance());
			
			modelBatch.render(indicatorLine.getInstance());	
			for(ElasticBand l : elastics) modelBatch.render(l.getInstance());
			for(Hole h : holes) modelBatch.render(h.getInstance());
			
			for(int i = 0; i < golfball.size(); i++) 
				golfball.get(i).update();
			updateCameraPosition();
			modelBatch.end();
			

			Vector3 mousePosition = getWorldCoords();
			indicatorLine.updateLine(golfball.get(index).getPosition(), mousePosition);

			for(ElasticBand l : elastics) {
				l.updateLine();
		}

			//Collisiondetection
			for (Obstacle o : obstacleList) {
				modelBatch.render(o.getInstance());
				for(Golfball g : golfball) {
					collisionDetector.detectCollision(g, o);
					OnBoard(g);
					if(ballCollisionAllowed) {
						for(Golfball h : golfball) {
							if (g!=h) collisionDetector.detectCollision(g, h);
						}
					}
				}
			}
			
			
		}

		public ArrayList<Golfball> getGolfball() {
			return golfball;
		}
		private void makeHoles(int players) {
			for(int i = 0; i < players; i++) {
				int x = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				int y = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				holes.add(new Hole(x, 0.1f, y, golfball.get(i).getRadius()*2));
				holes.get(i).setColour(i);
			}
		}
		private void makeHolesTeamMode(int players) {
			for(int i = 0; i < players; i+=2) {
				int a = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				int b = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				holes.add(new Hole(a,0.1f,b, golfball.get(i).getRadius()*2));
				holes.get(i).setColour(i);
				int c = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				int d = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				while(calculateDistance(new Vector3(a,0,b),new Vector3(c,0,d)) > (maxAllowedDistance-1)) {
					c = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
					d = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				}
				holes.add(new Hole(c,0.1f,d, golfball.get(i).getRadius()*2));
				holes.get(i+1).setColour(i+1);
			}
		}
		public Golfball getCurrentPlayer() {
			int current = index;
			if(!gameOver()){
			index++;
			while(finished.contains((int) index)) index++;
			if(index == golfball.size()) index = 0;
			while(finished.contains((int) index)) index++;
			player= "Player : " + (current+1) + " Score: " + golfball.get(index).getScore();
			text.setText(player);}
			return golfball.get(current);
		}
		public void numberOfPlayers() {
			Gdx.input.getTextInput(new TextInputListener() {
				   @Override
				   public void input (String text) {
					   int x = Integer.parseInt(text);
					   players =x;
				   }

				   @Override
				   public void canceled () {
					   players = 2;
				   }
				}, "How Many Players", "2", "");
			
		}
		public void makeGolfBallsTeamMode(int x) {
			for(int i = 0; i < x; i+=2) {
				int a = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				int b = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				golfball.add(new Golfball(a,b));
				golfball.get(i).setColour(i);
				int c = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				int d = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				while(calculateDistance(new Vector3(a,0,b),new Vector3(c,0,d)) > (maxAllowedDistance-1)) {
					c = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
					d = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				}
				golfball.add(new Golfball(c,d));
				golfball.get(i+1).setColour(i+1);
			}
		}
		
		public void makeGolfBalls(int x) {
			for(int i = 0; i < x; i++) {
				int a = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				int b = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				golfball.add(new Golfball(a,b));
				golfball.get(i).setColour(i);
			}
		}
		public float calculateDistance(Vector3 pointA, Vector3 pointB) {
	    	double x = Math.pow(pointA.x-pointB.x, 2);
	    	double y = Math.pow(pointA.y-pointB.y, 2);
	    	double z = Math.pow(pointA.z-pointB.z, 2);
	    	double distance = Math.sqrt(x+y+z);
	    	return (float) distance;
		}
		public void updateCameraPosition() {
			if (Gdx.input.isKeyPressed(Keys.LEFT)) camera.rotateAround(golfball.get(index).getPosition(), new Vector3(0, 1, 0), -2f);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))camera.rotateAround(golfball.get(index).getPosition(), new Vector3(0, 1, 0), 2f);
			if (Gdx.input.isKeyPressed(Keys.DOWN)) camera.translate(new Vector3(0,-1,0));
			if (Gdx.input.isKeyPressed(Keys.UP)) camera.translate(new Vector3(0,1,0));
			
			camera.lookAt(golfball.get(index).getPosition());
			//camera.translate(golfball.get(index).getVelocity());
			camera.translate(new Vector3(0,0,0));
			camera.update(); 
		}
		/**
		 * Initialise all obstacles with position and add them to the obstacle list
		 */
		private void initObstacles() {
			Obstacle board = new ObstacleBox(0, 0, 0, 100f, 1f, 100f);
			obstacleList.add(board);					
			/*for(int i = 0; i < 10;i++) {
				int a = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
				int b = (int)(Math.random() * ((49 - (-49)) + 1)) + (-49);
			collisionBox = new ObstacleBox(a, 0, b, 10f, 10f, 10f);
			collisionBox.setColor(Color.DARK_GRAY);
			obstacleList.add(collisionBox);
			}*/

		
		}
		/**
		 * Calculate the bounding box of the whole course. 
		 * The resulting boundingbox will be used for the A*-path finding, to avoid searching areas out of the course
		 * 
		 * @param obstacleList The list of all obstacles in the course
		 */
		public void OnBoard(Golfball g) {
			if(!courseDimensions.contains(g.getBoundingBox())) {
				g.setPosition(new Vector3(-10,g.getRadius() * 2, -10));//for the lack of a better position
				//if(teamMode) g.setPosition(newPosition); change so that falling of the board doesnt mean a game over
				
				g.setVelocity(new Vector3(0,0,0));
			}
		}
		private void calculateCouseDimensions(Set<Obstacle> obstacleList) {
			courseDimensions = new BoundingBox();
			Vector3 min = golfball.get(0).getPosition();
			Vector3 max = golfball.get(0).getPosition();
			for(Obstacle o : obstacleList) {
				if(max.x < o.getBoundingBox().max.x) 
					max.x = o.getBoundingBox().max.x;
				if(max.y < o.getBoundingBox().max.y) 
					max.y = o.getBoundingBox().max.y;
				if(max.z < o.getBoundingBox().max.z) 
					max.z = o.getBoundingBox().max.z;
				if(min.x > o.getBoundingBox().min.x) 
					min.x = o.getBoundingBox().min.x;
				if(min.y > o.getBoundingBox().min.y)
					min.y = o.getBoundingBox().min.y;
				if(min.z > o.getBoundingBox().min.z) 
					min.z = o.getBoundingBox().min.z;			
			}
			max.y = 20f;
			courseDimensions.set(min, max);
		}
		/**
		 * Retrieve the bounding box spanning over the whole course
		 * @return the boundingbox containing all elements in the course
		 */
		public BoundingBox getCouserDimensions() {
			if(courseDimensions == null) {
				calculateCouseDimensions(obstacleList);
			}
			return courseDimensions;
		}
		public Set<Obstacle> getAllObstacles() {
			return obstacleList;
		}	
		public void dispose() {
			modelBatch.dispose();
			for(int i = 0; i < golfball.size(); i++) 
			golfball.get(i).getBallModel().dispose();
			stage.dispose();
		}
		@Override
		public void show() {
		}
		@Override
		public void resize(int width, int height) {
		}
		@Override
		public void pause() {
		}
		@Override
		public void resume() {
		}
		@Override
		public void hide() {
		}
		/**
		 * returns world coordinates, relative to the screen coordinates, where the
		 * mouse is currently at. It computes the intersection from the mouse location
		 * and a plane, spanned over the xz axis
		 * 
		 * @return
		 */
		public Vector3 getWorldCoords() {
			Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());

			Plane plane = new Plane();
			plane.set(0, 1, 0, 0);// the xz plane with direction z facing screen

			plane.d = 0;// ***** the depth in 3d for the coordinates

			Vector3 worldCoords = new Vector3();
			Intersector.intersectRayPlane(ray, plane, worldCoords);
			return worldCoords;
		}
		public DifferentialEquationSolver getDifferentialEquationSolver() {
			return ode;
		}
		public float getStrength() {
			return hitStrength;
		}
		public void removeBall(int i) {
			finished.add(i);
		}
		public boolean gameOver() {
			boolean gameOver = true;
			for(int i = 0; i < players; i++) if (!finished.contains((int) i)) gameOver = false;
			if(gameOver) ScreenManager.getInstance().showScreen( ScreenEnumMP.GAMEOVERSCREEN , golfball, teamMode);	
			return gameOver;
		}

}
