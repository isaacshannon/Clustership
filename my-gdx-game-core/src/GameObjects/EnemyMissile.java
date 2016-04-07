//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;

import com.badlogic.gdx.math.Vector2;



public class EnemyMissile extends Target {

	private Vector2 position; // position of the block within the ship
	private Vector2 destination;
	private Block target; // target a block of the ship
	private Vector2 velocity;
	private float speed;
	private float life;
	private float damage;
	private float deathCount;

	public EnemyMissile(Vector2 position, Block target,float damage,float missileLife) {
		super(position,1);
		this.position = position.cpy();
		this.destination = target.getPosition();
		this.velocity = new Vector2(0,0);
		this.speed = 150;
		//this.speed = 50;
		this.target = target;
		this.life = missileLife;
		this.damage = damage;
		deathCount = 0;
	}

	public Vector2 getPosition() {
		return position;
	}
	
	public String getType(){
		return "enemyMissile";
	}
	
	public Block getTarget() {
		return target;
	}
	
	public float getDamage() {
		return damage;
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
	
	public void update(float delta){// the vector the missile needs to travel
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
	
	public int hit(float dmg) {
		life-=dmg;
		return (int)life;
	}
	
	public void startDeathCount(){
		deathCount = 1;
	}

	public float getDeathCount(){
		return deathCount;
	}
	
	public float getLife(){
		return life;
	}
}

