//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import GameObjects.Target;

public class Missile {

	private Vector2 position; // position of the block within the ship
	private Vector2 destination;
	private Target target;
	private Vector2 velocity;
	private float speed;
	private float damage;
	private String type;
	private float deathCount;
	private float age;
	private float lifespan;
	private Launcher launcher;

	public Missile(Vector2 position, Target target,String type,float multiplier,Launcher launcher) {
		this.position = position.cpy();
		this.destination = target.getPosition();
		this.velocity = new Vector2(0,0);
		speed =1;
		damage = 1;
		lifespan=(float)0.1;
		age=0;
		this.launcher=launcher;

		if(type.equals("missile")){
			speed = 200;
			lifespan=(float)1;
			damage = 20*multiplier;}
		if(type.equals("disabler")){
			speed = 200;
			lifespan=(float)1;
			damage = (float)1.15*multiplier;}
		if(type.equals( "gauss")){
			speed = 300;
			lifespan=(float)1;
			damage = 60*multiplier;}
		if(type.equals( "gattling")){
			speed = 300;
			lifespan=(float)1;
			damage = 3*multiplier;}
		if(type.equals( "mine")){
			speed = 1;
			lifespan=(float)7;
			damage = 50*multiplier;}
		if(type.equals( "drone")){
			speed = 400;
			lifespan=(float)1;
			damage = 3*multiplier;}
		if(type.equals( "nuke")){
			speed = 600;
			lifespan=(float)15;
			damage = 99999;}
		this.target = target;
		this.type = type;
		deathCount = 0;
	}
	
	public Launcher getLauncher(){
		return launcher;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getScreenPosition(Vector2 v) {
		return position.cpy().sub(v.cpy());
	}

	public Vector2 getDestination() {
		return destination;
	}

	public void setPosition(Vector2 v){
		position= v;
	}

	public Target getTarget(){
		return target;
	}

	public float getDamage(){
		return damage;
	}

	public void update(float delta){// the vector the missile needs to travel
		age+=delta;
		if(age>lifespan && deathCount==0)
			deathCount =1;
		if(deathCount > 0)
			deathCount = deathCount + delta;
		else{
			destination = target.getPosition();
			float angle = destination.cpy().sub(position).angle(); 
			velocity.x = (int)speed*(float) Math.cos(Math.toRadians(angle));
			velocity.y = (int)speed*(float) Math.sin(Math.toRadians(angle));

			velocity.scl(delta);

			position.add(velocity.cpy());}

	}

	public void updatePosition(Vector2 v){
		position.add(v);
	}

	public String getType(){
		return type;
	}

	public void startDeathCount(){
		deathCount = 1;
	}

	public float getDeathCount(){
		return deathCount;
	}
	
	public float getSpeed(){
		return speed;
	}
}

