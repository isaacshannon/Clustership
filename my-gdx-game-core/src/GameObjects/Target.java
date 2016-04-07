//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Target {

	private Vector2 position; // position of the target on the screen
	private float speed; // position of the target on the screen
	private int life; // position of the target on the screen

	public Target(Vector2 position, int life) {
		this.position = position;
		speed = (float) .1;
		this.life = life;
	}
	
	public void update(float delta) {
		double xRand = Math.random();
		double yRand = Math.random();
		
		float scaledSpeed = speed*delta;
		
		if(xRand > 0.5)
			position.add(scaledSpeed,0);
		else
			position.sub(scaledSpeed,0);
			
		if(yRand > 0.5)
			position.add(0,scaledSpeed);
		else
			position.sub(0,scaledSpeed);
		
	}
	
	public void updatePosition(Vector2 v){
		position.add(v);
	}
	
	public String getType(){
		return "target";
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getScreenPosition(Vector2 v) {
		return position.cpy().sub(v);
	}
	
	public int hit(float damage) {
		life=(int) (life-damage);
		return life;
	}
	
	public int hit() {
		life--;
		return life;
	}

}

