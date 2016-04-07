//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Enemy extends Target {
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
	private int value;

	public Enemy(Vector2 position){
		super(position,15);
		rate = 100;
		fireConstant = 100;//a lower fireConstant equals a faster firing rate
		fireCount = (float)Math.random()*fireConstant;//random initial fireCount
		fireRate = fireConstant + Math.random();
		this.position = position;
		speed = (float)50;
		velocity = new Vector2(0,0);
		acceleration = new Vector2(0,0);
		destination = new Vector2(0,0);
		rocks = null;
		missileLife = 1;
		attack =1;
		count = 0;
		life = 50;
		type = "city";
		range =0;
		deathCount = 0;
		aggroed = false;
		randValue=200;
		disableX=1;
		value=0;

	}

	public Enemy(Vector2 position,ArrayList<Rock> rocks,float los,float rate,
			float attack,float life,float missileLife,String type){
		super(position,15);
		//fireConstant = 5;//a lower fireConstant equals a faster firing rate
		this.rate = rate;
		fireConstant = (float) (rate);//a lower fireConstant equals a faster firing rate
		fireCount = (float)Math.random()*fireConstant;//random initial fireCount
		fireRate = fireConstant + Math.random();
		this.position = position;
		this.speed = (float)120;
		this.rocks = rocks;
		velocity = new Vector2(0,0);
		acceleration = new Vector2(0,0);
		destination = new Vector2(0,0);
		this.missileLife = missileLife;
		count = 0;
		this.life=life;
		this.los=los;
		this.type = type;
		range = 150;
		this.attack = attack;
		checkIfRock();
		deathCount = 0;
		aggroed = false;
		randValue=200;
		value=0;
	}
	
	public boolean isAggroed(){
		return aggroed;
	}
	
	public void setValue(int x){
		if(x<0)
			x=0;
		value = x;
		//System.out.println("Value set"+value);
	}
	
	public int getValue(){
		return value;
	}

	private void checkIfRock(){
		if(this.type.equals("rock")){
			this.attack=0;
			this.speed=0;
			this.life=10;
			this.rate=9999;}
	}
	
	public void updatePosition(Vector2 v){
			position.add(v);
	}

	public void update(Vector2 destination,float delta){
		if(position.dst(destination)<los)
			aggroed=true;
		if(deathCount > 0)
			deathCount = deathCount + delta;
		else{
			if(!type.equals("city")&&!type.equals("rock")&& aggroed){
				position.add(velocity);	}
		}
		if(aggroed){
			fireSequence(delta);
			this.destination=destination;
			float angle = destination.cpy().sub(position).angle(); 
			velocity.x = speed*(float) Math.cos(Math.toRadians(angle));
			velocity.y = speed*(float) Math.sin(Math.toRadians(angle));

			//enemy will back away if closer than 100 from the ship
			if(position.dst(destination)<200)
				if(randomY > .5)
					velocity.rotate(200-position.dst(destination));
				else
					velocity.rotate(200+position.dst(destination));

			velocity.add(randomMovement());
			avoidRocks();
			velocity.scl(delta);
		}
	}

	private void avoidRocks(){
		Rock rock;
		randValue=200;
		if(rocks!=null){
			for(int i=0;i<rocks.size();i++){
				rock = rocks.get(i);
				Vector2 rPos = rock.getPosition();
				if(position.dst(rPos)<30){
					randValue=50;
					if(position.x>rPos.x&&velocity.x<0)
						velocity.x=0;
					if(position.x<rPos.x&&velocity.x>0)
						velocity.x=0;
					if(position.y>rPos.y&&velocity.y<0)
						velocity.y=0;
					if(position.y<rPos.y&&velocity.y>0)
						velocity.y=0;
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
		//do not increment the firecount for cities
		if(type.equals("city"))
			fireCount=(float) 0.01;
		else
			fireCount+=10*delta;
		
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
	
	public void disableHit(float dmg){
		speed-=5*dmg;
		if(speed<0)
			speed=1;
		fireConstant+=6*(dmg-1);
		
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
		//System.out.println("Enemy Attack"+attack);
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
