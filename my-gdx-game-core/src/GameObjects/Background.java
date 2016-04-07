//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import GameObjects.Block;

//a background consists of 9 background rectangles that leapfrog each other seamlessly
public class Background {

	private Vector2 position; 
	private Vector2 cPos;
	private Vector2 rPos;//randomly move around the backgournd, used in ship building screen
	private boolean direction; // direction of the random move
	private Vector2 unitaryV;// vector to be added when rotating the background

	public Background() {
		this.position = new Vector2(0,0);
		this.cPos = new Vector2(0,0);
		rPos = new Vector2(200,200);
		direction = false;
		unitaryV = new Vector2((float)0.2,(float)0.2);

	}

	public Vector2 getPosition() {
		return position.cpy().add(rPos.cpy()).sub(cPos);
	}

	public Vector2 getScreenPosition(Vector2 v) {
		//System.out.println("Rock Position: " + position);
		return position.cpy().sub(v);
	}

	public void updatePosition(Vector2 v){
		position.add(v);
		rPos.sub(rPos);

		if(cPos.x>position.x)
			cPos.x-=400;
		if(cPos.y>position.y)
			cPos.y-=800;

		if(cPos.x<position.x)
			cPos.x+=400;
		if(cPos.y<position.y)
			cPos.y+=800;
	}

	public void randomMove(){
		rPos.rotate((float)0.05);
		/*if(rPos.len()<400){
			if(direction)
				rPos.add(unitaryV);
			else
				rPos.sub(unitaryV);}
		else
			
		
		if(rPos.x > 400)
			rPos.x=400;
		if(rPos.x < -400)
			rPos.x=-400;
		if(rPos.y > 400)
			rPos.y=400;
		if(rPos.y < -400)
			rPos.y=-400;*/
	}
}

