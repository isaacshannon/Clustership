//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import GameObjects.Block;

public class Rock {

	private Vector2 position; 

	public Rock(Vector2 position) {
		this.position = position;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	public Vector2 getScreenPosition(Vector2 v) {
		//System.out.println("Rock Position: " + position);
		return position.cpy().sub(v);
	}
	
	public void updatePosition(Vector2 v){
		position.add(v);
	}
}

