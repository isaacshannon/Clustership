//Copyright Isaac Shannon, All Rights reserved, 2014
package Helpers;
import com.kilobolt.GameWorld.GameWorld;

import GameObjects.Block;
import GameObjects.Ship;
import GameObjects.Missile;
import GameObjects.Target;
import GameObjects.Launcher;
import GameObjects.Rock;
import GameObjects.Enemy;
import GameObjects.EnemyMissile;
import GameObjects.Thruster;
import GameObjects.Background;
import GameObjects.Drone;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GameWorldUpdater {
	private GameWorld gameWorld;

	public GameWorldUpdater(){
	}


	public void updateMissiles(GameWorld gameWorld,float delta){
		ArrayList<Missile> missiles = gameWorld.getMissiles();
		ArrayList<Enemy> enemies = gameWorld.getEnemies();
		//ArrayList<EnemyMissile> enemyMissiles = gameWorld.getEnemyMissiles();
		removeMissilesHittingRocks(gameWorld);

		for(int i = 0;i<missiles.size();i++){
			Missile missile = gameWorld.getMissiles().get(i);

			if(missile.getPosition().dst(missile.getDestination())<missile.getSpeed()/30 && missile.getDeathCount()==0){ //remove missiles that have made it to target
				Target targ = missile.getTarget();
				//splash effect of anti-missile weapons
				if(targ.getType().equals("enemyMissile")){
					targ.hit(missile.getDamage());
					for(int j = 0;j<gameWorld.getEnemyMissiles().size();j++){
						EnemyMissile enemyMissile = gameWorld.getEnemyMissiles().get(j);
						//ystem.out.println(enemyMissile.getLife());
						if(missile.getPosition().dst(enemyMissile.getPosition())<15){
							if(enemyMissile.hit(missile.getDamage())<=0){
								//System.out.println("splashed");
								gameWorld.getEnemyMissiles().remove(j);
								if(j>0)
									j--;
							}
						}
					}
				}
				else
					targ.hit(missile.getDamage());
				missile.startDeathCount();
			}

			//mines explode if any a=enemy goes by
			if(missile.getType().equals("mine")){
				for(int j=0;j<enemies.size();j++){
					Enemy enemy = enemies.get(j);
					if(enemy.getPosition().dst(missile.getPosition())<20 && missile.getDeathCount()==0){
						missile.startDeathCount();
						AssetLoader.cannonShot.play();
						enemy.hit(missile.getDamage());}
				}
			}

			//mines explode if any a=enemy goes by
			if(missile.getType().equals("disabler")){
				for(int j=0;j<enemies.size();j++){
					Enemy enemy = enemies.get(j);
					if(enemy.getPosition().dst(missile.getPosition())<20 && missile.getDeathCount()==0){
						missile.startDeathCount();
						AssetLoader.cannonShot.play();
						enemy.disableHit(missile.getDamage());}
				}
			}

			//the nuke is an aoe explosion
			if(missile.getType().equals("nuke")){
				if(missile.getPosition().dst(missile.getDestination())<missile.getSpeed()/15){
					for(int j=0;j<enemies.size();j++){
						Enemy enemy = enemies.get(j);
						if(enemy.getPosition().dst(missile.getPosition())<200){
							enemy.hit(missile.getDamage());}
					}
					if(missile.getDeathCount()==0)
						missile.startDeathCount();
				}
			}


			if(missile.getDeathCount()>1.2 && !missile.getType().equals("nuke"))
				missiles.remove(i);
			else
				if(missile.getDeathCount()>1.9)
					missiles.remove(i);


			missile.update(delta);
			missile.updatePosition(gameWorld.getShip().getVelocity());
		}
	}


	public void updateRocks(GameWorld gameWorld){
		ArrayList<Rock> rocks = gameWorld.getRocks();
		for(int i = 0;i<rocks.size();i++){
			Rock rock = rocks.get(i);
			rock.updatePosition(gameWorld.getShip().getVelocity());
		}
	}

	public void updateBackground(GameWorld gameWorld){
		Background bg = gameWorld.getBackground();
		bg.updatePosition(gameWorld.getShip().getVelocity());
	}

	public void updateBlocks(GameWorld gameWorld){
		ArrayList<Block> blocks = gameWorld.getBlocks();
		Float rotation = gameWorld.getShip().getRotation();

		for(int i = 0;i<blocks.size();i++){
			Block block = blocks.get(i);
			block.update(rotation);
		}
	}

	public void updateLaunchers(GameWorld gameWorld,float delta){
		ArrayList<Launcher> launchers = gameWorld.getLaunchers();
		Float rotation = gameWorld.getShip().getRotation();

		for(int i = 0;i<launchers.size();i++){
			Launcher launcher = launchers.get(i);
			launcher.update(rotation,delta);
		}
	}


	public void updateThrusters(GameWorld gameWorld){
		ArrayList<Thruster> thrusters = gameWorld.getThrusters();
		Float rotation = gameWorld.getShip().getRotation();

		for(int i = 0;i<thrusters.size();i++){
			Thruster thruster = thrusters.get(i);
			thruster.update(rotation);
		}
	}

	public void updateTargets(GameWorld gameWorld,float delta){
		ArrayList<Target> targets = gameWorld.getTargets();
		ArrayList<Missile> missiles = gameWorld.getMissiles();
		for(int i = 0;i<targets.size();i++){
			Target target = targets.get(i);
			target.update(delta); //update the position of the target randomly
			target.updatePosition(gameWorld.getShip().getVelocity());

			for(int j = 0;j<missiles.size();j++){ //remove targets that get hit
				Missile missile = missiles.get(j);
				if(missile.getPosition().cpy().dst(target.getPosition()) < 5 && targets.size() > 0){
					if(target.hit() < 1 && i<targets.size())
						targets.remove(i);
				}
			}
		}
	}

	public void updateEnemies(GameWorld gameWorld,float delta,Vector2 shipPosition){
		ArrayList<Enemy> enemies = gameWorld.getEnemies();
		Enemy enemy;
		for(int i = 0;i<enemies.size();i++){
			enemy = enemies.get(i);
			enemy.update(gameWorld.getShip().getPosition(),delta);
			enemy.updatePosition(gameWorld.getShip().getVelocity());
			if(enemy.getLife() < 1 && i<enemies.size() && enemy.getDeathCount()==0){
					AssetLoader.enemyExplosion.play();
					gameWorld.incrementKills();
					gameWorld.getShipWorld().addCredits(enemy);
					enemy.startDeathCount();}
				if(enemy.getDeathCount()>1.2){
					destrockDestruction(gameWorld,enemy);
					enemies.remove(i);}

		}
	}

	private void destrockDestruction(GameWorld gameWorld,Enemy enemy){
		ArrayList<Enemy> enemies = gameWorld.getEnemies();
		ArrayList<Rock> rocks = gameWorld.getRocks();
		if(enemy.getType().equals("rock"))
			for(int i=0;i<rocks.size();i++){
				Rock rock = rocks.get(i);
				if(rock.getPosition().dst(enemy.getPosition())<40){
					rocks.remove(i);
				}

				for(int j = 0;j<enemies.size();j++){
					Enemy enemy2 = enemies.get(j);
					if((enemy.getPosition().dst(enemy2.getPosition())<40)&&(enemy2.getType().equals("rock"))){
						//gameWorld.getShipWorld().addCredits(enemy2);
						enemy2.startDeathCount();}
				}
			}
	}

	public void updateEnemyMissiles(GameWorld gameWorld,float delta){
		ArrayList<EnemyMissile> enemyMissiles = gameWorld.getEnemyMissiles();
		removeEnemyMissilesAtDestination(gameWorld);
		//removeInterceptedEnemyMissiles(gameWorld);
		//removeEnemyMissilesHittingArmor(gameWorld);
		removeEnemyMissilesHittingRocks(gameWorld);
		for(int i = enemyMissiles.size()-1;i>=0;i--){
			EnemyMissile enemyMissile = gameWorld.getEnemyMissiles().get(i);
			enemyMissile.update(delta);
			enemyMissile.updatePosition(gameWorld.getShip().getVelocity());
		}
	}

	private void removeEnemyMissilesAtDestination(GameWorld gameWorld){
		ArrayList<EnemyMissile> enemyMissiles = gameWorld.getEnemyMissiles();

		for(int i = 0;i<enemyMissiles.size();i++){
			EnemyMissile enemyMissile = gameWorld.getEnemyMissiles().get(i);
			if(enemyMissile.getPosition().dst(enemyMissile.getDestination()) < 5 && enemyMissile.getDeathCount()==0){ //remove missiles that have made it to target
				gameWorld.getShip().hit(enemyMissile.getDamage());
				enemyMissile.startDeathCount();
				AssetLoader.shipImpact.play();
				//enemyMissiles.remove(i);
			}

			if(enemyMissile.getDeathCount()>1.4)
				enemyMissiles.remove(i);
		}
	}

	private void removeInterceptedEnemyMissiles(GameWorld gameWorld){
		ArrayList<EnemyMissile> enemyMissiles = gameWorld.getEnemyMissiles();
		ArrayList<Missile> missiles = gameWorld.getMissiles();

		for(int i = 0;i<enemyMissiles.size();i++){
			EnemyMissile enemyMissile = gameWorld.getEnemyMissiles().get(i);
			for(int j = 0;j<missiles.size();j++){ //remove missiles that get hit
				Missile missile = missiles.get(j);
				if(missile.getPosition().cpy().dst(enemyMissile.getPosition()) < 16 && enemyMissiles.size() > 0
						&& (missile.getType().equals("gattling")||missile.getType().equals("drone"))){
					if(enemyMissile.hit(missile.getDamage())<1 && enemyMissiles.size()>0 && i>-1)
						if(i<enemyMissiles.size()){
							missiles.remove(j);
							enemyMissiles.remove(i);}
				}
			}
		}
	}

	private void removeEnemyMissilesHittingRocks(GameWorld gameWorld){
		ArrayList<EnemyMissile> enemyMissiles = gameWorld.getEnemyMissiles();
		ArrayList<Rock> rocks = gameWorld.getRocks();

		for(int i = 0;i<enemyMissiles.size();i++){
			EnemyMissile enemyMissile = gameWorld.getEnemyMissiles().get(i);
			for(int j = 0;j<rocks.size();j++){ //remove missiles that get hit
				Rock rock = rocks.get(j);
				if(rock.getPosition().cpy().dst(enemyMissile.getPosition()) < 25 && enemyMissiles.size() > 0){
					if(enemyMissile.hit()<1 && enemyMissiles.size()>0 && i>-1)
						if(i<enemyMissiles.size()){
							enemyMissiles.remove(i);}
				}
			}
		}
	}

	private void removeMissilesHittingRocks(GameWorld gameWorld){
		ArrayList<Missile> missiles = gameWorld.getMissiles();
		ArrayList<Rock> rocks = gameWorld.getRocks();

		for(int i = 0;i<missiles.size();i++){
			Missile missile = gameWorld.getMissiles().get(i);
			for(int j = 0;j<rocks.size();j++){ //remove missiles that get hit
				Rock rock = rocks.get(j);
				if(rock.getPosition().cpy().dst(missile.getPosition()) < 16 && missiles.size() > 0){
					if(missiles.size()>0 && i>-1)
						if(i<missiles.size()&& !missile.getType().equals("nuke")){
							missiles.remove(i);}
				}
			}
		}
	}


	public void updateDrones(GameWorld gameWorld, float delta) {
		ArrayList<Drone> drones = gameWorld.getDrones();
		if(!(drones==null)){
			for(int i = 0;i<drones.size();i++){
				Drone drone = drones.get(i);
				//if(enemy.getPosition().dst(shipPosition)<500)
				//{
				drone.update(gameWorld.getShip().getPosition(),delta);
				drone.updatePosition(gameWorld.getShip().getVelocity());
				if(drone.getLife() < 1 && i<drones.size() && drone.getDeathCount()==0){
					AssetLoader.enemyExplosion.play();
					drone.startDeathCount();}
				if(drone.getDeathCount()>1.2){
					drones.remove(i);}
				//}
			}
		}

	}
}


