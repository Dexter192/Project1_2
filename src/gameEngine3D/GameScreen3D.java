package gameEngine3D;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import Obstacles.Hole;
import Obstacles.Obstacle;
import Obstacles.ObstacleBox;
import ai.AStar;
import collisionDetector.CollisionDetector;
import menu.AbstractScreen;
import physics.VectorComputation;
import physics.DifferentialEquationSolver;
import physics.Physics;

/**
 * To make the axis a bit more clear set showaxis to true. When doing that, the
 * x axis is shown in red, the y axis is shown in blue, the z axis is shown in
 * green
 * 
 * @author Daniel
 *
 */
public class GameScreen3D extends AbstractScreen {

	private PerspectiveCamera camera;
	private boolean showAxis = false;
	private Obstacle collisionBox;
	private DifferentialEquationSolver ode;
	private Golfball golfball;
	private Hole hole;
	private ModelBatch modelBatch;
	private Environment environment;
	private CameraInputController camController;
	private CollisionDetector collisionDetector;
	private InputMultiplexer inputMultiplexer;
	public LineIndicator indicatorLine;
	public LineIndicator[] axis = new LineIndicator[3];
	public static BoundingBox courseDimensions;
	private AStar aStar;
	private boolean findPath = true;

	private Mesh m;
	private Physics physics; 
	
	private final Matrix3 normalMatrix = new Matrix3();

	private static final float[] lightPosition = { 5, 35, 5 };
	private static final float[] ambientColor = { 0.2f, 0.2f, 0.2f, 1.0f };
	private static final float[] diffuseColor = { 0.5f, 0.5f, 0.5f, 1.0f };
	private static final float[] specularColor = { 0.7f, 0.7f, 0.7f, 1.0f };
	
	private static final float[] fogColor = { 0.2f, 0.1f, 0.6f, 1.0f };
	
	private Matrix4 model = new Matrix4();
	private Matrix4 modelView = new Matrix4();
	private static Matrix4 matrix = new Matrix4().idt();

//	private static String vertexShader = "attribute vec4 a_position;    \n" + 
//            "attribute vec4 a_color;\n" +
//            "attribute vec2 a_texCoord0;\n" + 
//            "uniform mat4 u_projTrans;\n" + 
//            "varying vec4 v_color;" + 
//            "varying vec2 v_texCoords;" + 
//            "void main()                  \n" + 
//            "{                            \n" + 
//            "   v_color = vec4(1, 0, 1, 1); \n" + 
//            "   v_texCoords = a_texCoord0; \n" + 
//            "   gl_Position =  u_projTrans * a_position;  \n"      + 
//            "}                            \n" ;
//	private static String fragmentShader = "#ifdef GL_ES\n" +
//              "precision mediump float;\n" + 
//              "#endif\n" + 
//              "varying vec4 v_color;\n" + 
//              "varying vec2 v_texCoords;\n" + 
//              "uniform sampler2D u_texture;\n" + 
//              "void main()                                  \n" + 
//              "{                                            \n" + 
//              "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
//              "}";
//	
	private final static String vertexShader =
	        "attribute vec4 a_position; \n" +
	        "attribute vec3 a_normal; \n" +
	        "attribute vec2 a_texCoord; \n" +
	        "attribute vec4 a_color; \n" +

	        "uniform mat4 u_MVPMatrix; \n" +
	        "uniform mat3 u_normalMatrix; \n" +

	        "uniform vec3 u_lightPosition; \n" +

	        "varying float intensity; \n" +
	        "varying vec2 texCoords; \n" +
	        "varying vec4 v_color; \n" +

	        "void main() { \n" +
	        "    vec3 normal = normalize(u_normalMatrix * a_normal); \n" +
	        "    vec3 light = normalize(u_lightPosition); \n" +
	        "    intensity = max( dot(normal, light) , 0.0); \n" +

	        "    v_color = a_color; \n" +
	        "    texCoords = a_texCoord; \n" +

	        "    gl_Position = u_MVPMatrix * a_position; \n" +
	        "}";

	private final static String fragmentShader =
	        "#ifdef GL_ES \n" +
	        "precision mediump float; \n" +
	        "#endif \n" +

	        "uniform vec4 u_ambientColor; \n" +
	        "uniform vec4 u_diffuseColor; \n" +
	        "uniform vec4 u_specularColor; \n" +

	        "uniform sampler2D u_texture; \n" +
	        "varying vec2 texCoords; \n" +
	        "varying vec4 v_color; \n" +

	        "varying float intensity; \n" +

	        "void main() { \n" +
	        "    gl_FragColor = v_color * intensity * texture2D(u_texture, texCoords); \n" +
	        "}";
	
	private static ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
	private Texture texture;
	

	private Set<Obstacle> obstacleList = new HashSet<Obstacle>();
	private Set<Obstacle> pathIndicator = new HashSet<Obstacle>();

	@Override
	public void buildStage() {
		// initialize environment
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		
		modelBatch = new ModelBatch();
		collisionDetector = new CollisionDetector();

		//ShaderProgram.pedantic = false;
		
		//shader = new ShaderProgram(vertexShader, fragmentShader);
		
		// initialize camera
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0f, 50f, 0f);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
		camController = new CameraInputController(camera);

		initObstacles();
		


		golfball = new Golfball();
		float[] a = { 0.01f,0.1f };
		float[] b = { 0.01f,0.1f };
		Physics physics = new Physics(a, b);

		hole = new Hole(-70, 0.1f, 0, golfball.getRadius()*2);
		ode = new DifferentialEquationSolver(physics, golfball.getMass());
		golfball.setODE(ode);

		obstacleList.add(hole);
		
		//m = createFullScreenQuad();
		
		// inizialize hit indicator line
		indicatorLine = new LineIndicator();

		// initialize input
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new InputListener(this, indicatorLine));
		inputMultiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(inputMultiplexer);

		if (showAxis) {
			axis[0] = new LineIndicator();
			axis[1] = new LineIndicator();
			axis[2] = new LineIndicator();
		}
		
		calculateCouseDimensions(obstacleList);

		aStar = new AStar(this);
//		aStar.findPathToHole();
	}

	/**
	 * This method updates the game. So every drawing, collision detection etc.
	 * should be called here
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camController.update();
		modelBatch.begin(camera);
		modelBatch.render(golfball.getBallInstance());
		// Dis-/Enabling the axis. Makes axis a bit more clear
		if (showAxis) {
			modelBatch.render(axis[0].getInstance());
			modelBatch.render(axis[1].getInstance());
			modelBatch.render(axis[2].getInstance());
		}

		modelBatch.render(indicatorLine.getInstance());		

		modelBatch.render(hole.getInstance());
		
		if (showAxis) {
			axis[0].setLine(new Vector3(100, 0, 0), new Vector3(-100, 0, 0), Color.RED);
			axis[1].setLine(new Vector3(0, 100, 0), new Vector3(0, -100, 0), Color.BLUE);
			axis[2].setLine(new Vector3(0, 0, 100), new Vector3(0, 0, -100), Color.LIME);
		}

		// First update, than draw or the other way around?
		golfball.update();
		updateCameraPosition();
		modelBatch.end();

		Vector3 mousePosition = getWorldCoords();
		indicatorLine.updateLine(golfball.getPosition(), mousePosition);
	
		if(golfball.getVelocity().isZero()) {

		//	System.out.println(VectorComputation.getInstance().getDistanceXZ(golfball.getPosition(), hole.getBoundingBox().getCenter(new Vector3())) + " " + golfball.getPosition() + " " + hole.getBoundingBox().getCenter(new Vector3()));

//			System.out.println(VectorComputation.getInstance().getDistanceXZ(golfball.getPosition(), hole.getBoundingBox().getCenter(new Vector3())) + " " + golfball.getPosition() + " " + hole.getBoundingBox().getCenter(new Vector3()));

		
//		matrix.rotate(new Vector3(0,1,0), 15);
//		shader.begin();
//		shader.setUniformMatrix("u_projTrans", matrix);
//		shader.setUniformi("u_texture", 0);
//		m.render(shader, GL20.GL_TRIANGLES);
//		shader.end();
	    
	   /* Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	    
		texture.bind();
	    shader.begin();

	    shader.setUniformMatrix("u_MVPMatrix", camera.combined);
	    shader.setUniformMatrix("u_normalMatrix", normalMatrix.set(modelView).inv().transpose());

	    shader.setUniform3fv("u_lightPosition", lightPosition, 0, 3);
	    shader.setUniform4fv("u_ambientColor", ambientColor, 0, 4);
	    shader.setUniform4fv("u_diffuseColor", diffuseColor, 0, 4);
	    shader.setUniform4fv("u_specularColor", specularColor, 0, 4);

	    shader.setUniformi("u_texture", 0);

	    m.render(shader, GL20.GL_TRIANGLES);

	    shader.end();*/
//		collisionBox.rotate(new Vector3(0,0,1), 1);
		}
		for (Obstacle o : pathIndicator) {
			modelBatch.render(o.getInstance());
		}
		for (Obstacle o : obstacleList) {
			modelBatch.render(o.getInstance());
			collisionDetector.detectCollision(golfball, o);
		}		
		
	}

	/**
	 * Get the golfball from the current game
	 * @return the golfball of the game
	 */
	public Golfball getGolfball() {
		return golfball;
	}

	/**
	 * Update the camera position according to the ball velocity
	 */
	public void updateCameraPosition() {
		// TODO: we might have to do this manually --> ask in project meeting, prehaps
		// ask pietro
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			camera.rotateAround(golfball.getPosition(), new Vector3(0, 1, 0), -2f);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			camera.rotateAround(golfball.getPosition(), new Vector3(0, 1, 0), 2f);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			camera.translate(new Vector3(0,-1,0));
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			camera.translate(new Vector3(0,1,0));
		}
		
		camera.lookAt(golfball.getPosition());
		camera.translate(golfball.getVelocity());
		camera.update(); //
	}
	
	/**
	 * Initialize all ostacles with position and add them to the obstacle list
	 */
	private void initObstacles() {
		Obstacle box = new ObstacleBox(0, 0, 0, 200f, 1f, 200f);
		obstacleList.add(box);					
		
		for(int i = -20; i <= 20; i = i + 10) {
			collisionBox = new ObstacleBox(-90, 0, i, 10f, 10f, 10f);
			collisionBox.setColor(Color.BLUE);
			obstacleList.add(collisionBox);	
		}
	
		for(int i = -20; i <= 10; i = i + 10) {
			collisionBox = new ObstacleBox(-50, 0, i, 10f, 10f, 10f);
			collisionBox.setColor(Color.BLUE);
			obstacleList.add(collisionBox);	
		}

		for(int i = -10; i <= 20; i = i + 10) {
			collisionBox = new ObstacleBox(-30, 0, i, 10f, 10f, 10f);
			collisionBox.setColor(Color.BLUE);
			obstacleList.add(collisionBox);	
		}
		
		for(int i = -20; i <= 10; i = i + 10) {
			collisionBox = new ObstacleBox(-10, 0, i, 10f, 10f, 10f);
			collisionBox.setColor(Color.BLUE);
			obstacleList.add(collisionBox);	
		}
		
		
		for(int i = -20; i <= 30; i = i + 10) {
			collisionBox = new ObstacleBox(10, 0, i, 10f, 10f, 10f);
			collisionBox.setColor(Color.BLUE);
			obstacleList.add(collisionBox);	
		}
		
		for(int i = -90; i <= 10; i = i + 10) {
			collisionBox = new ObstacleBox(i, 0, -30, 10f, 10f, 10f);
			collisionBox.setColor(Color.BLUE);
			obstacleList.add(collisionBox);	
		
			collisionBox = new ObstacleBox(i, 0, 30, 10f, 10f, 10f);
			collisionBox.setColor(Color.BLUE);
			obstacleList.add(collisionBox);	
		}
		

		
	}
	
	

	/**
	 * Calculate the bounding box of the whole couse. 
	 * The resulting boundingbox will be used for the A*-path finding, to avoid searching areas out of the course
	 * 
	 * @param obstacleList The list of all obstacles in the course
	 */
	private void calculateCouseDimensions(Set<Obstacle> obstacleList) {
		courseDimensions = new BoundingBox();
		Vector3 min = golfball.getPosition();
		Vector3 max = golfball.getPosition();
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
		courseDimensions.set(min, max);
	}
	
	/**
	 * Retrieve the bounding box spanning over the hole course
	 * @return the boundingbox containing all elements in the course
	 */
	public BoundingBox getCouserDimensions() {
		if(courseDimensions == null) {
			calculateCouseDimensions(obstacleList);
		}
		return courseDimensions;
	}

	public Obstacle getHole() {
		return hole;
	}
	
	public Set<Obstacle> getAllObstacles() {
		return obstacleList;
	}
	
	public AStar getAi() {
		return aStar;
	}
	
	public void dispose() {
		modelBatch.dispose();
		golfball.getBallModel().dispose();
	}
	
	public void addObstacles(Collection<Obstacle> obstacles) {
		pathIndicator.clear();
		pathIndicator.addAll(obstacles);
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
	}

	private long delta;
	
	@Override
	public void pause() {
	}
	
	@Override
	public void resume() {
	
	}

	@Override
	public void hide() {
	}

	
	/*public Mesh createFullScreenQuad() {
	    // position, normal, color, texture
		texture = new Texture(Gdx.files.internal("img/GrassTexture.jpg"));
	    int vertexSize = 3 + 3 + 1 + 2;  

	    Splines spline = new Splines(32, 32, vertexSize, physics, "img/4k.jpg");



	    Mesh mesh = new Mesh(true, spline.vertices.length / 3, spline.indices.length,
	            new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
	            new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE),
	            new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
	            new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE));

	    mesh.setVertices(spline.vertices);
	    mesh.setIndices(spline.indices);

		return mesh;
//		  float[] verts = new float[20];
//		  int i = 0;
//
//		  verts[i++] = -1f; // x1
//		  verts[i++] = -1; // y1
//		  verts[i++] = 0;
//		  verts[i++] = 0f; // u1
//		  verts[i++] = 0f; // v1
//
//		  verts[i++] = 1f; // x2
//		  verts[i++] = -1; /s/ y2
//		  verts[i++] = 0;
//		  verts[i++] = 1f; // u2
//		  verts[i++] = 0f; // v2
//
//		  verts[i++] = 1f; // x3
//		  verts[i++] = 1f; // y2
//		  verts[i++] = 0;
//		  verts[i++] = 1f; // u3
//		  verts[i++] = 1f; // v3
//
//		  verts[i++] = -1; // x4
//		  verts[i++] = 1f; // y4
//		  verts[i++] = 0;
//		  verts[i++] = 0f; // u4
//		  verts[i++] = 1f; // v4
//
//		  Mesh mesh = new Mesh( true, 4, 0,  // static mesh with 4 vertices and no indices
//		    new VertexAttribute( Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
//		    new VertexAttribute( Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );
//
//		  mesh.setVertices( verts );
//		  
//		  return mesh;
		}*/
	
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
}
