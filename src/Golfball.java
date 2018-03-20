import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Sphere;

public class Golfball {

	private Model ballModel;
	private ModelInstance ballInstance;
	private ModelBuilder modelBuilder;
	private Sphere golfball;
	private Vector3 position;
	
	private final long attributes = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal;

	public Golfball(float radius) {
		position = new Vector3(0,0.15f,0);
		golfball = new Sphere(position, 10f);

		modelBuilder = new ModelBuilder();
		
		ballModel = modelBuilder.createSphere(radius*2, radius*2, radius*2,
                50,50,
                new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

		ballInstance = new ModelInstance(ballModel);
	}
	
	public Model getBallModel() {
		return ballModel;
	}
	
	public ModelInstance getBallInstance() {
		return ballInstance;
	}
	
	public void update() {
		   if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			   position.x = 0.4f;
		   }
		   if(Gdx.input.isKeyPressed(Keys.UP)) {
			   position.x = -0.4f;
		   }
		   if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			   position.z = 0.4f;
		   }
		   if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			   position.z = -0.4f;
		   }
		   ballInstance.transform.translate(position);
		   position = position.scl(0.9f);
	}
	
}
