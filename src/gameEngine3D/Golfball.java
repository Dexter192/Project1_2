package gameEngine3D;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import physics.DifferentialEquationSolver;

/**
 * A class representing the Golfball as a 3D sphere.
 * 
 * @author Daniel
 *
 */
public class Golfball {
	private Model ballModel;
	private ModelInstance ballInstance;
	private ModelBuilder modelBuilder;
	private BoundingBox boundingBox;
	private Vector3 position;
	private float mass;
	private Vector3[] veloAccel = {new Vector3(0,0,0), new Vector3(0,0,0)};
	// Note that veloAccel[0] is the direction vector	
	private static DifferentialEquationSolver ode;
	private static float radius = 1;
	private int index;
	private int score =0;
	private Vector3 initialPosition;
	public Golfball() {
		mass = 5;
		modelBuilder = new ModelBuilder();

		ballModel = modelBuilder.createSphere(radius * 2, radius * 2, radius * 2, 50, 50, new Material(),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
						| VertexAttributes.Usage.TextureCoordinates);


		position = new Vector3(0, radius * 2, 10);
		this.initialPosition = this.position;

		ballInstance = new ModelInstance(ballModel);
		ballInstance.transform.translate(position);
		getBoundingBox();
	}
	public Golfball(float a, float b) {
		mass = 5;
		modelBuilder = new ModelBuilder();

		ballModel = modelBuilder.createSphere(radius * 2, radius * 2, radius * 2, 50, 50, new Material(),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
						| VertexAttributes.Usage.TextureCoordinates);


		this.position = new Vector3(a,radius*2,b);
		this.initialPosition = this.position;
		ballInstance = new ModelInstance(ballModel);
		ballInstance.transform.translate(position);
		getBoundingBox();
	}
	public void setColour(int i ) {
		index = i;
		Attribute colourAttribute = ColorAttribute.createDiffuse(Color.WHITE);
		if(i == 0) colourAttribute = ColorAttribute.createDiffuse(Color.RED);		
		if(i == 1) colourAttribute = ColorAttribute.createDiffuse(Color.BLUE);
		if(i == 2) colourAttribute = ColorAttribute.createDiffuse(Color.YELLOW);		
		if(i == 3) colourAttribute = ColorAttribute.createDiffuse(Color.ORANGE);
		if(i == 4) colourAttribute = ColorAttribute.createDiffuse(Color.OLIVE);
		if(i == 5) colourAttribute = ColorAttribute.createDiffuse(Color.FOREST);
		ballInstance.materials.get(0).set(colourAttribute);
	}
	public int getIndex() {
		return index;
	}
	/**
	 * 
	 * @return The Model of the ball
	 */
	public Model getBallModel() {
		return ballModel;
	}

	
	/**
	 * 
	 * @return The ModelInstance of the ball
	 */
	public ModelInstance getBallInstance() {
		return ballInstance;
	}

	
	public Vector3 getVector() {
		return veloAccel[0];
	}

	/**
	 * Updates the ball. Primarily its position.
	 */
	public void update() {
		ignoreMinimalVelocity();
		position.add(veloAccel[0]); 
		ballInstance.transform.setTranslation(position);

		Vector3 min = new Vector3(-radius, -radius, -radius);
		Vector3 max = new Vector3( radius,  radius,  radius);
		boundingBox = boundingBox.set(min.add(position), max.add(position));

		if(Math.abs(veloAccel[0].x)>0 || Math.abs(veloAccel[0].z)>0)	veloAccel = ode.rungeKutterMethod(veloAccel, position);

	}
	
	private void ignoreMinimalVelocity() {
		if (Math.abs(veloAccel[0].x) <= 0.001) veloAccel[0].x = 0;
		if (Math.abs(veloAccel[0].z) <= 0.001) veloAccel[0].z = 0;
		if(Math.abs(veloAccel[0].y) <= 0.001) veloAccel[0].y = 0;
	}

	
	public Vector3 getVelocity() {
		return veloAccel[0];
	}

	public void setInitialPosition(Vector3 initialPos) {
		this.initialPosition = initialPos;
	}
	public void setVelocity(Vector3 directionVector) {
		veloAccel[0] = directionVector;
	}
	public boolean isMoving () {
		if(veloAccel[0].x == 0 && veloAccel[0].y == 0 && veloAccel[0].z == 0) return false;
		else return true;
	}
	
	public void addVelocity(Vector3 directionVector) {
		veloAccel[0].add(directionVector);
		
	}
	
	public Vector3 getPosition() {
		return ballInstance.transform.getTranslation(new Vector3());
	}
	
	
	public void setPosition(Vector3 newPosition) {
		this.position = newPosition;
	}
	public void setBack() {
		this.position = this.initialPosition;
	}
	public Vector3 getInitialPosition() {
		return initialPosition;
	}
	

	public void bounceOff(Vector3 axis) {
		veloAccel[0].scl(axis);
	}

	
	public float getRadius() {
		return radius;
	}
	
	
	public BoundingBox getBoundingBox() {
		if (boundingBox == null) {
			boundingBox = new BoundingBox();
			boundingBox = ballInstance.calculateBoundingBox(boundingBox);
			Vector3 min = new Vector3();
			Vector3 max = new Vector3();
			boundingBox.set(min.add(position), max.add(position));
		}
		return boundingBox;
	}
	

	public float getMass() {
		return mass;
	}
	
	public void incrementScore() {
		score++;
	}
	public int getScore() {
		return score;
	}
	public void setODE(DifferentialEquationSolver input){
		ode = input;
	}
	
	public DifferentialEquationSolver getODE(){
		return ode;
	}
}
