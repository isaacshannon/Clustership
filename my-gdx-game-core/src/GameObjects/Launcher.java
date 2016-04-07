//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import GameObjects.Missile;

import java.util.ArrayList;

public class Launcher extends Block {

	private Vector2 shipPosition; // position of the block within the ship
	private Vector2 position; // position of the block on the map
	private ArrayList<Missile> missiles;
	private Missile missile;
	private Vector2 center;
	private String type;
	private double fireRate;
	private float fireCount;
	private float fireConstant;
	private float range;
	private float multiplier;

	public Launcher(float x, float y, String type,float mult){
		super(x,y,"launcher");
		range = 5;
		fireConstant = 5;
		
		multiplier = mult;
		
		if(type.equals("missile")){
			fireConstant = 5;
			range = 150;}
		
		if(type.equals("gattling")){
			fireConstant = 3;
			range = 130;}
		
		if(type.equals("gauss")){
			fireConstant = 11;
			range = 400;}
		
		if(type.equals("mine")){
			fireConstant = 5;
			range = 1000;}
		
		if(type.equals("disabler")){
			fireConstant = 5;
			range = 170;}
		
		if(type.equals("carrier")){
			fireConstant = 9999;
			range = 1;}
		
		
		fireCount =(float)Math.random()*fireConstant;//random initial firecount
		fireRate = fireConstant+Math.random();
		shipPosition = new Vector2(x, y);
		position = new Vector2(0, 0);
		missiles = new ArrayList<Missile>();
		center  = new Vector2(137,204);
		this.type = type;
	}
	
	public void update(float rotation,float delta){
		Vector2 dP = shipPosition.cpy().rotate(rotation); //delta x and y, to be added to the center
		position = center.cpy().add(dP);
		fireCount+=10*delta;
		if(fireCount/multiplier > fireRate){
			fireCount = 0;
			fireRate = fireConstant + Math.random()*fireConstant;//constantly vary the firing rate to prevent firing all together
		}
	}	
	
	public void setMultiplier(float x){
		multiplier=x;
	}
	
	public float getMultiplier(){
		return multiplier;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getShipPosition() {
		return shipPosition;
	}
	
	public float getFireCount() {
		return fireCount;
	}
	
	public void setPosition(Vector2 v){
		position= v;
	}
	
	public ArrayList<Missile> getMissiles(){
		return missiles;
	}
	
	public float getRange(){
		return range;
	}
	
	public String getType(){
		return type;
	}



}

