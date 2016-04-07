//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Drone {
	private Vector2 position;
	private float speed;
	private Vector2 velocity;
	private Vector2 acceleration;
	private Vector2 destination;
	private int count;
	private double randomX = 0;
	private double randomY = 0;
	private float randomSpeed = 0;
	private double fireRate;
	private float fireCount;
	private float fireConstant;
	private float life;
	private float los;
	private float attack;
	private float rate;
	private float missileLife;
	private ArrayList<Rock> rocks;
	private String type;
	private float range;
	private float deathCount;
	private boolean aggroed;
	private double randValue;
	private float disableX;
	private float multiplier;
	private int lvl;

	
	public Drone(Vector2 position,ArrayList<Rock> rocks,int lvl){
		//fireConstant = 5;//a lower fireConstant equals a faster firing rate
		this.rate =(float) 0.4;
		fireConstant = rate;//a lower fireConstant equals a faster firing rate
		fireCount = (float)Math.random()*fireConstant;//random initial fireCount
		fireRate = fireConstant + Math.random();
		this.position = position;
		this.speed = (float)120;
		this.rocks = rocks;
		velocity = new Vector2(0,0);
		acceleration = new Vector2(0,0);
		destination = new Vector2(0,0);
		this.missileLife = 10;
		count = 0;
		this.life=10;
		this.los=3000;
		this.type = "ship";
		range = 150;
		deathCount = 0;
		aggroed = true;
		randValue=200;
		multiplier = 1;
		this.lvl=lvl;
	}
	
	public float getMultiplier(){
		return 1+(float)lvl/5;
	}
	
	public void setMultiplier(float mult){
		multiplier=mult;
	}
	//the destination of the drone is set in the gameworld missile fire section
	public void update(Vector2 dest,float delta){
		fireCount+=delta;
		position.add(velocity);	
		respawnAtShip();

		if(aggroed){
			fireSequence(delta);
			//this.destination=destination;
			float angle = destination.cpy().sub(position).angle(); 
			velocity.x = speed*(float) Math.cos(Math.toRadians(angle));
			velocity.y = speed*(float) Math.sin(Math.toRadians(angle));

			//enemy will back away if closer than 100 from the ship
			if(position.dst(destination)<50)
				velocity.rotate(3*(50-position.dst(destination)));

			velocity.add(randomMovement());
			avoidRocks();
			velocity.scl(delta);
		}
	}

	public void updatePosition(Vector2 v){
		position.add(v);
	}
	
	public void setDestination(Vector2 dest){
		destination=dest;
	}
	
	private void respawnAtShip(){
		Vector2 midPos = new Vector2(136,204);
		if(midPos.dst(position)>200)
			position=midPos;
	}
	private void avoidRocks(){
		Rock rock;
		randValue=200;
		if(rocks!=null){
			if(rocks!=null){
				for(int i=0;i<rocks.size();i++){
					rock = rocks.get(i);
					Vector2 rPos = rock.getPosition();
					Vector2 pos = position;
					if(position.dst(rPos)<25){
						Vector2 nPos = rPos.cpy().add(new Vector2(0,15));
						Vector2 sPos = rPos.cpy().add(new Vector2(0,-15));
						Vector2 ePos = rPos.cpy().add(new Vector2(15,0));
						Vector2 wPos = rPos.cpy().add(new Vector2(-15,0));

						if(pos.dst(nPos)<pos.dst(sPos) && pos.dst(nPos)<pos.dst(ePos) &&
								pos.dst(nPos)<pos.dst(wPos) && velocity.y<0){
							velocity.y=0;
						}
						if(pos.dst(sPos)<pos.dst(nPos) && pos.dst(sPos)<pos.dst(ePos) &&
								pos.dst(sPos)<pos.dst(wPos) && velocity.y>0){
							velocity.y=0;
						}
						if(pos.dst(ePos)<pos.dst(sPos) && pos.dst(ePos)<pos.dst(sPos) &&
								pos.dst(ePos)<pos.dst(wPos) && velocity.x<0){
							velocity.x=0;
						}
						if(pos.dst(wPos)<pos.dst(sPos) && pos.dst(wPos)<pos.dst(ePos) &&
								pos.dst(wPos)<pos.dst(nPos)&& velocity.x>0){
							velocity.x=0;
						}
					}
				}
			}
		}
	}

	private Vector2 randomMovement(){
		count++;
		Vector2 randomMovement = new Vector2(0,0);
		if (count > randValue*randomX){
			count = 0;
			randomX = Math.random();
			randomY = Math.random();
			randomSpeed = 30*(float)Math.random();
		}

		if(randomX > 0.5)
			randomMovement.add(randomSpeed,0);
		else
			randomMovement.sub(randomSpeed,0);

		if(randomY > 0.5)
			randomMovement.add(0,randomSpeed);
		else
			randomMovement.sub(0,randomSpeed);

		return randomMovement;
	}

	private void fireSequence(float delta){

		//the gameworld will add a missile when the firecount is returned to 0
		if(fireCount > fireRate){
			fireCount = 0;
			fireRate = fireConstant+Math.random()*fireConstant;
		}
	}

	public float getFireCount() {
		return fireCount;
	}

	public float getDeathCount(){
		return deathCount;
	}

	public void startDeathCount(){
		deathCount = 1;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setRocks(ArrayList<Rock> rocks){
		this.rocks = rocks;
	}

	public int hit(float dmg) {
		aggroed=true;
		life-=dmg;
		return (int) life;
	}

	public float getLos(){
		return los;
	}

	public String getType(){
		return type;
	}

	public float getRange(){
		return range;
	}

	public float getAttack(){
		return attack;
	}

	public float getLife(){
		return life;
	}

	public float getRate(){
		return rate;
	}

	public float getMissileLife(){
		return missileLife;
	}
}
