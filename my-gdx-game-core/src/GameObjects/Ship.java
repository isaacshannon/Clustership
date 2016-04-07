//Copyright Isaac Shannon, All Rights reserved, 2014
package GameObjects;

import com.badlogic.gdx.math.*;

import java.util.ArrayList;
import java.util.Arrays;

import GameObjects.Block;
import GameObjects.Ship;
import GameObjects.Missile;
import GameObjects.Launcher;
import GameObjects.Rock;

public class Ship {

	private Vector2 position;
	private Vector2 destination;
	private Vector2 velocity;
	private Vector2 scaledVelocity;
	private Vector2 acceleration;

	private float speed;
	private float rotation;
	private int shipLength;

	private int rows;
	private int columns;

	private Block[][] shipBlocks;
	private ArrayList<Missile> missiles;

	private float life;
	private float shield;
	private float shieldRegen;
	private float maxShield;
	private float shieldX;

	private ArrayList<Launcher> launcherArray;
	private ArrayList<Block> blockArray;
	private ArrayList<Thruster> thrusterArray;
	private ArrayList<Rock> rocks;


	public Ship(float x, float y, int life) {
		speed = (float)1000;
		position = new Vector2(x, y);
		destination = new Vector2(x,x);
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(0, 0);
		this.missiles = new ArrayList<Missile>();
		this.life = life;
		rocks = null;
		shield = 0;
		scaledVelocity=new Vector2(0, 0);
		shieldX=1;

		rows = 20;//maximum size of the ship, row x column
		columns = 20;
		shieldRegen = 0;
	}

	public Ship(float x, float y, ArrayList<Block> blockArray,
			ArrayList<Launcher> launcherArray,
			ArrayList<Thruster> thrusterArray) {
		speed = 80;
		position = new Vector2(x, y);
		destination = new Vector2(x,x);
		velocity = new Vector2(0, 0);
		acceleration = new Vector2(0, 0);
		this.missiles = new ArrayList<Missile>();
		life = blockArray.size()*3;
		calculateLifeAndShield(blockArray);
		rocks = null;
		shieldRegen=0;
		scaledVelocity=new Vector2(0, 0);
		shieldX=1;
		rows = 20;//maximum size of the ship, row x column
		columns = 20;
		shipLength=30;

		this.blockArray = blockArray;
		this.launcherArray = launcherArray;
		this.thrusterArray = thrusterArray;
	}

	public void setShieldX(float x){
		shieldX=x;
	}
	
	public void setShipLength(int len){
		System.out.println("Ship Length Set! " + len);
		shipLength = len*8;
		if(shipLength<25)
			shipLength=25;
	}

	public void calculateLifeAndShield(ArrayList<Block> blockArray){
		float shieldBlocks = 0;
		for(int i=0;i<blockArray.size();i++){
			if(blockArray.get(i).getType().equals("shield"))
				shieldBlocks++;
		}

		life = 30;
		shield=(float)1.5*shieldBlocks*shieldBlocks;

		if(shield > shieldBlocks*18)
			shield = shieldBlocks*18;
		else{
			if(shield>0 && shield < 1)
				shield =1;}
		//System.out.println("Shield:"+shield);
		//System.out.println("Shieldx:"+shieldX);
		maxShield = shield*shieldX;
		shield=maxShield;

	}

	public void update(float delta) {
		//System.out.println("Position: "+position);
		//System.out.println("Destination: "+destination);
		Vector2 path = destination.cpy().sub(position);
		float angle = path.angle();	// angle of the vector
		calculateRotation();
		regenShield(delta);

		velocity.x = (int)speed*(float) Math.cos(Math.toRadians(angle));
		velocity.y = (int)speed*(float) Math.sin(Math.toRadians(angle));


		avoidRocks();	

		//velocity.add(acceleration.scl(delta));

		//slow the ship at max speed
		if(velocity.len()>150)
			velocity.scl(1-delta);

		//allow for a total stop of the ship
		if(velocity.x*velocity.x<1)
			velocity.x=0;
		if(velocity.y*velocity.y<1)
			velocity.y=0;

		//slow the ship as it approaches destination
		if(destination.dst(position)<1)
			velocity.scl(9/10);

		scaledVelocity = velocity.cpy().scl(delta);

		//update the destination
		if(position.dst(destination)> 10)
			destination.sub(scaledVelocity);
		else{
			destination = position.cpy();
			acceleration.sub(acceleration);
			velocity.sub(velocity);}

	}

	private void avoidRocks(){
		Rock rock = null;
		//System.out.println("ShipLength: "+shipLength);

		if(rocks!=null){
			for(int i=0;i<rocks.size();i++){
				rock = rocks.get(i);
				Vector2 rPos = rock.getPosition();
				Vector2 pos = position;
				if(position.dst(rPos)<shipLength){
					Vector2 nPos = rPos.cpy().add(new Vector2(0,15));
					Vector2 sPos = rPos.cpy().add(new Vector2(0,-15));
					Vector2 ePos = rPos.cpy().add(new Vector2(15,0));
					Vector2 wPos = rPos.cpy().add(new Vector2(-15,0));

					if(pos.dst(nPos)<pos.dst(sPos) && pos.dst(nPos)<pos.dst(ePos) &&
							pos.dst(nPos)<pos.dst(wPos) && velocity.y>0){
						velocity.y=0;
					}
					if(pos.dst(sPos)<pos.dst(nPos) && pos.dst(sPos)<pos.dst(ePos) &&
							pos.dst(sPos)<pos.dst(wPos) && velocity.y<0){
						velocity.y=0;
					}
					if(pos.dst(ePos)<pos.dst(sPos) && pos.dst(ePos)<pos.dst(sPos) &&
							pos.dst(ePos)<pos.dst(wPos) && velocity.x>0){
						velocity.x=0;
					}
					if(pos.dst(wPos)<pos.dst(sPos) && pos.dst(wPos)<pos.dst(ePos) &&
							pos.dst(wPos)<pos.dst(nPos)&& velocity.x<0){
						velocity.x=0;
					}
				}
			}
		}
	}

	public void setRocks(ArrayList<Rock> boulders){
		rocks = boulders;
	}

	private void regenShield(float delta){
		shieldRegen+=delta;
		if(shieldRegen>1)
			shieldRegen=1;
		if(shieldRegen>=1&&shield<maxShield){
			shieldRegen=0;
			shield+=1+maxShield/10;}
		if(shield<0)
			shield = 0;
		if(shield>maxShield)
			shield=maxShield;
	}


	public Vector2 getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public ArrayList<Launcher> getLaunchers() {
		return launcherArray;
	}


	public ArrayList<Block> getBlocks() {
		return blockArray;
	}

	public ArrayList<Thruster> getThrusters() {
		return thrusterArray;
	}

	public ArrayList<Missile> getMissiles() {
		return missiles;
	}

	public Vector2 getVelocity(){
		//return velocity;
		return scaledVelocity;
	}

	public ArrayList<Block> getArrayShipBlocks(){
		return blockArray;
	}

	public int getRows(){
		return rows;
	}

	public int getColumns(){
		return columns;
	}

	public void hit(float damage){
		if(shield>damage){
			shield-=damage;}
		else{
			damage-=shield;
			shield=0;
			life-=damage;
		}
	}

	public int getLife() {
		return (int)life;
	}

	public void setLife(int x){
		life = x;
	}

	private void calculateRotation(){
		Vector2 path = destination.cpy().sub(position);
		float angle = path.angle();	// angle of the vector
		float deltaAngle = 0;

		if (rotation > 360)
			rotation -= 360;
		if (rotation < 0)
			rotation += 360;

		if(rotation > angle && angle!=0){
			deltaAngle = rotation-angle;
			if (deltaAngle<180)
				rotation -= 12;
			else
				rotation +=12;
		}

		if(angle > rotation && angle !=0){
			deltaAngle = angle-rotation;
			if (deltaAngle<180)
				rotation += 12;
			else
				rotation -= 12;
		}
	}

	public void setDestination(float screenX, float screenY){
		destination.x =272-2*screenX;
		destination.y = 2*screenY;
	}

	public float getShield(){
		return shield;
	}

	public float getMaxShield(){
		return maxShield;
	}


}

