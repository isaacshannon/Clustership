//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import GameObjects.Block;

public class Thruster extends Block {

	private Vector2 shipPosition; // position of the block within the ship
	private Vector2 position; // position of the block on the map
	private Vector2 center; // center of the screen
	private String type; // center of the screen

	public Thruster(float x, float y) {
		super(x,y,"thruster");
		shipPosition = new Vector2(x, y);
		position = new Vector2(0, 0);
		center = new Vector2(137,204);
		this.type = "thruster";
	}

	public void update(float rotation){
		Vector2 dP = shipPosition.cpy().rotate(rotation); //delta x and y, to be added to the center
		position = center.cpy().add(dP);
	}
	
	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getShipPosition() {
		return shipPosition;
	}
	
	public String getType() {
		return type;
	}
		
	public void setPosition(Vector2 v){
		position= v;
	}
	
	



}

