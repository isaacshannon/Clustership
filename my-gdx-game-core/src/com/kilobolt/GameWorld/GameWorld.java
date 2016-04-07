//Copyright Isaac Shannon, All Rights reserved, 2014

package com.kilobolt.GameWorld;

import GameObjects.Block;
import GameObjects.Ship;
import GameObjects.Background;
import GameObjects.Missile;
import GameObjects.Target;
import GameObjects.Enemy;
import GameObjects.Launcher;
import GameObjects.Rock;
import GameObjects.Drone;
import GameObjects.Thruster;
import GameObjects.EnemyMissile;
import Helpers.AssetLoader;
import Helpers.GameWorldUpdater;

import com.badlogic.gdx.math.Vector2;
import com.kilobolt.GameWorld.ShipWorld;
import com.kilobolt.GameWorld.LevelWorld;

import java.io.IOException;
import java.util.ArrayList;

public class GameWorld {
	private ShipWorld shipWorld;
	private LevelWorld levelWorld;
	private GameWorldUpdater gameWorldUpdater;
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<Rock> rocks = new ArrayList<Rock>();
	private Rock rock;
	private ArrayList<Target> targets = new ArrayList<Target>();
	private Target target;
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private Enemy enemy;
	private ArrayList<Enemy> objectives = new ArrayList<Enemy>();
	private Enemy objective;
	private ArrayList<Launcher> launchers = new ArrayList<Launcher>();
	private Launcher launcher;
	private ArrayList<Missile> missiles = new ArrayList<Missile>();
	private Missile missile;
	private ArrayList<EnemyMissile> enemyMissiles = new ArrayList<EnemyMissile>();
	private EnemyMissile enemyMissile;
	private ArrayList<Thruster> thrusters = new ArrayList<Thruster>();
	private Thruster thruster;
	private ArrayList<Drone> drones = new ArrayList<Drone>();
	private Drone drone;
	private ArrayList<Rock> nearRocks = new ArrayList<Rock>();
	private Ship ship;
	private boolean hasObjectives;
	private boolean objectivesDestroyed;
	private Vector2 shipPosition;
	private float screenWidth,screenHeight;
	private boolean nuke;
	private boolean nukeReady;
	private float nukeCount;
	private float screenDelay;
	private String msgBanner;
	private int currentStage;
	private int xp;
	private Background background;
	private float lastScreenX;
	private float lastScreenY;
	private boolean paused;
	private int touchDelay;
	private float tutorialCount;
	private Vector2 tutorialPointer;
	private boolean tutorial2;
	private double delayedUpdateCount;
	private boolean bossFight;
	private double gameTime;
	private boolean spawned;
	private int enemyKills;
	//private double diffConstant;



	private GameState currentState;



	public enum GameState {
		SHIPWORLD, RUNNING, GAMEOVER,LEVELWORLD,HELP,SHOP,TUTORIAL
	}

	public GameWorld(float screenWidth,float screenHeight){
		currentState = GameState.SHIPWORLD;
		generateTargets();
		generateRocks();
		generateEnemies();
		generateDrones();
		ship = new Ship(0,0,15);
		gameWorldUpdater = new GameWorldUpdater();
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		nukeReady = true;
		nuke = false;
		nukeCount = 0;
		screenDelay=0;
		msgBanner ="";
		xp=0;
		background = new Background();
		lastScreenX=0;
		lastScreenY=0;
		paused = false;
		touchDelay=0;
		currentStage = 1;
		delayedUpdateCount=0;
		tutorialCount=0;
		bossFight = false;
		gameTime=0;
		spawned=false;
		enemyKills=0;
		tutorialPointer = new Vector2(0,0);
		tutorial2 = false; //choose between tutorial for select block and block upgrades
	
	}

	public void setBossFight(boolean boss){
		bossFight = boss;
	}

	public boolean isBossFight(){
		return bossFight;
	}

	public void update(float delta) {
		if(nuke)
			fireNuke();

		switch (currentState) {
		case TUTORIAL:
			screenDelay=0;
			manageTutorial(delta);
			//background.randomMove();
			shipWorld.updateTouch(delta);
			break;
		case SHIPWORLD:
			playMusic();
			background.randomMove();
			screenDelay=0;
			//shipWorld.saveShip();
			shipWorld.updateTouch(delta);
			break;
		case LEVELWORLD:
			//updateRunning(delta);
			break;
		case SHOP:
			shipWorld.updateTouch(delta);
			//updateRunning(delta);
			break;
		case RUNNING:
			if(touchDelay>0)
				touchDelay++;
			if(touchDelay>10)
				touchDelay=0;
			screenDelay=0;
			if(gameIsWon()){
				shipWorld.incrementDiffConstant(levelWorld.getLastLevel());
				manageVictory();}
			if(ship.getLife()<1){
				msgBanner = "SHIP DESTROYED...";
				shipWorld.decrementDiffConstant(levelWorld.getLastLevel());
				currentState = GameState.GAMEOVER;}
			updateRunning(delta);
			break;
		case HELP:
			break;
		case GAMEOVER:
			enemyKills=0;
			nukeCount = 0;
			screenDelay+=delta;
			if(screenDelay > 2){
				gameTime=0;
				shipWorld.saveShip();
				msgBanner="";
				ship.setLife(15);
				generateTargets();
				//cleanup the gameWorld between games
				enemies.clear();
				rocks.clear();
				missiles.clear();
				enemyMissiles.clear();
				currentState = GameState.SHIPWORLD;}
			break;
		default:
			updateRunning(delta);
			break;
		}
	}


	private boolean gameIsWon() {


		if(levelWorld.getKillQuota()>0 && enemyKills>levelWorld.getKillQuota())
			return true;

		if(levelWorld.getTimeQuota()>0)
			if(gameTime>levelWorld.getTimeQuota()){
				//System.out.println("Game Time"+gameTime);
				return true;}

		if(levelWorld.getKillQuota()==0 && levelWorld.getTimeQuota()==0 && enemies.size()<1)
			return true;

		return false;

	}

	public void incrementKills(){
		enemyKills++;
	}

	public int getEnemyKills(){
		return enemyKills;
	}
	//update the elements of the game
	public void updateRunning(float delta) {

		if(!paused){
			spawnEnemy();
			gameTime+=delta;
			ship.update(delta);
			shipPosition = ship.getPosition();

			if(gameTime>10 && gameTime<15)
				msgBanner = levelWorld.getLevelDescription();
			if(gameTime>15 && gameTime<16)
				msgBanner = "";
			gameWorldUpdater.updateBackground(this);
			
			if(gameTime>2.5){
				gameWorldUpdater.updateMissiles(this,delta);
				gameWorldUpdater.updateEnemyMissiles(this,delta);
				gameWorldUpdater.updateTargets(this,delta);
				gameWorldUpdater.updateRocks(this);
				gameWorldUpdater.updateBlocks(this);
				gameWorldUpdater.updateLaunchers(this,delta);
				gameWorldUpdater.updateEnemies(this,delta,shipPosition);
				gameWorldUpdater.updateThrusters(this);
				gameWorldUpdater.updateDrones(this,delta);

				updateNuke(delta);
				fireLaunchers();
				fireDrones();
				enemyFire();}
		}
	}

	public String getMsgBanner(){
		return msgBanner;
	}

	public double getGameTime(){
		return gameTime;
	}

	private void generateDrones(){
		while(drones.size()>0)
			drones.remove(drones.size()-1);
		drones = new ArrayList<Drone>();
		for(int i=0;i<launchers.size();i++){
			Launcher launcher = launchers.get(i);
			if(launcher.getType().equals("carrier")){
				drones.add(new Drone(new Vector2(137,204),rocks,shipWorld.carrierLevel));
			}
		}
		//drones.add(new Drone(new Vector2(300,300),rocks));

	}

	public ArrayList<Drone> getDrones(){
		if(drones.size()>0)
			return drones;
		else
			return null;
	}

	private void playMusic(){
		if(!AssetLoader.musicBox1.isPlaying())
			AssetLoader.musicBox1.play();
	}

	private void spawnEnemy(){
		if(((int)gameTime)%(levelWorld.getSpawnDelay())==0){
			if(!spawned && enemies.size()<40){
				spawned=true;
				for(int i=0;i<levelWorld.getSpawnNumber();i++){

					enemies.add(levelWorld.spawnEnemy());
				}
			}
		}
		else
			spawned = false;
	}


	public void setShipWorld(ShipWorld shipWorld){
		this.shipWorld=shipWorld;
	}

	public void setLevelWorld(LevelWorld levelWorld){
		this.levelWorld=levelWorld;
	}

	private void manageTutorial(float delta){
		tutorialCount+=delta;
		//System.out.println("TutorialCount:" + tutorialCount);
		if(tutorialCount>0 && tutorialCount<4){
			msgBanner = "WELCOME TO CLUSTERSHIP";
			tutorialPointer.set(100, 100);
		}
		if(tutorialCount>4 && tutorialCount<8){
			msgBanner = "TAP TO ADD OR ERASE BRICKS";
			tutorialPointer.set(102, 47);}

		if(tutorialCount>8 && tutorialCount<12){
			msgBanner = "NUMBER OF BRICKS LEFT TO ADD";
			tutorialPointer.set(58, 301);}

		if(tutorialCount>12 && tutorialCount<16){
			msgBanner = "YOUR CASH!";
			tutorialPointer.set(10, 312);}

		if(tutorialCount>16 && tutorialCount<20){
			msgBanner = "TAP HERE TO START A GAME";
			tutorialPointer.set(180, 330);}

		if(tutorialCount>20 && tutorialCount<24){
			msgBanner = "TAP HERE TO SELECT A BRICK";
			tutorialPointer.set(180, 270);}

		if(tutorialCount>24&& tutorialCount<28){
			msgBanner = "WELCOME TO BRICK SELECTION";
			tutorial2=true;
			tutorialPointer.set(0,0);}

		if(tutorialCount>28&& tutorialCount<32){
			msgBanner = "TAP HERE TO SELECT A BRICK";
			tutorial2=true;
			tutorialPointer.set(3,300);}

		if(tutorialCount>32&& tutorialCount<36){
			msgBanner = "TAP HERE TO UPGRADE BRICKS";
			tutorial2=true;
			tutorialPointer.set(145,300);}

		if(tutorialCount>36&& tutorialCount<40){
			msgBanner = "TAP HERE TO UNLOCK NEW BRICKS";
			tutorial2=true;
			tutorialPointer.set(130,135);}
		
		if(tutorialCount>40&& tutorialCount<44){
			msgBanner = "ADD UNLIMITED HULL BRICKS";
			tutorial2=true;
			tutorialPointer.set(10,60);}
		
		if(tutorialCount>44&& tutorialCount<48){
			msgBanner = "INCREASE MAX NUMBER OF BRICKS";
			tutorial2=true;
			tutorialPointer.set(13,28);}


		if(tutorialCount>48){
			tutorialCount=0;
			msgBanner = "";
			tutorial2=false;
			currentState=GameState.SHIPWORLD;}
	}

	public boolean displayTutorial2(){
		return tutorial2;
	}

	private void updateNuke(float delta){
		if(!nukeReady)
			nukeCount-=delta;

		if(nukeCount<0){
			nukeCount = 0;
			nukeReady = true;
		}
	}

	//fire all types of weapons, which are launchers
	private void fireLaunchers(){
		for(int j=0;j<launchers.size();j++){
			launcher = launchers.get(j);

			if(launcher.getType().equals("missile"))
				fireMissile(launcher,"missile");
			if(launcher.getType().equals("disabler"))
				fireMissile(launcher,"disabler");
			if(launcher.getType().equals("ray"))
				fireMissile(launcher,"ray");
			if(launcher.getType().equals("gattling"))
				fireGattling(launcher);
			if(launcher.getType().equals("gauss"))
				fireGauss(launcher);
			if(launcher.getType().equals("mine"))
				fireMine(launcher);
		}
	}

	private void fireMine(Launcher launcher){
		Vector2 position = new Vector2(999999,999999);
		Target finalTarget = new Target(position,9999999);


		//go through the list of enemies, shoot the closest
		for(int i=0;i<enemies.size();i++){ 
			enemy = enemies.get(i); 
			if(isClosestValidTarget(finalTarget,enemy,launcher.getPosition(),launcher.getRange()))
				finalTarget = enemy;}

		if((targets.size()>0 || enemies.size()>0 || enemyMissiles.size()>0)
				&& launcher.getPosition().dst(finalTarget.getPosition()) < launcher.getRange()
				&& launcher.getFireCount() == 0 ){
			missile = new Missile(launcher.getPosition(),finalTarget,"mine",launcher.getMultiplier(),launcher);
			missiles.add(missile);}
	}

	private void fireMissile(Launcher launcher,String type){
		Vector2 position = new Vector2(999999,999999);
		Target finalTarget = new Target(position,9999999);


		//go through the list of enemies, shoot the closest
		for(int i=0;i<enemies.size();i++){ 
			enemy = enemies.get(i); 
			if(isClosestValidTarget(finalTarget,enemy,launcher.getPosition(),launcher.getRange()))
				finalTarget = enemy;}

		if((targets.size()>0 || enemies.size()>0 || enemyMissiles.size()>0)
				&& launcher.getPosition().dst(finalTarget.getPosition()) < launcher.getRange()
				&& launcher.getFireCount() == 0 ){
			missile = new Missile(launcher.getPosition(),finalTarget,type,launcher.getMultiplier(),launcher);
			AssetLoader.missileShot.play();
			missiles.add(missile);}
	}

	private void fireGauss(Launcher launcher){
		Vector2 position = new Vector2(999999,999999);
		Target finalTarget = new Target(position,9999999);

		//go through the list of enemies, shoot the closest
		for(int i=0;i<enemies.size();i++){ 
			enemy = enemies.get(i); 
			if(isClosestValidTarget(finalTarget,enemy,launcher.getPosition(),launcher.getRange()))
				finalTarget = enemy;}

		if((targets.size()>0 || enemies.size()>0 || enemyMissiles.size()>0)
				&& launcher.getPosition().dst(finalTarget.getPosition()) < launcher.getRange()
				&& launcher.getFireCount() == 0 ){
			missile = new Missile(launcher.getPosition(),finalTarget,"gauss",launcher.getMultiplier(),launcher);
			missiles.add(missile);
			AssetLoader.cannonShot.play();
		}
	}

	//gattlings fire at enemy missiles
	private void fireGattling(Launcher launcher){
		Vector2 position = new Vector2(999999,999999);
		Target finalTarget = new Target(position,9999999);

		//go through the list of enemyMissiles, shoot the closest
		//missiles have highest priority
		for(int i=0;i<enemyMissiles.size();i++){ 
			enemyMissile = enemyMissiles.get(i); 
			if(isClosestValidTarget(finalTarget,enemyMissile,launcher.getPosition(),launcher.getRange())&&
					enemyMissile.getDeathCount()==0)
				finalTarget = enemyMissile;
		}

		if((targets.size()>0 || enemies.size()>0 || enemyMissiles.size()>0)
				&& launcher.getPosition().dst(finalTarget.getPosition()) < launcher.getRange()
				&& launcher.getFireCount() == 0 ){
			missile = new Missile(launcher.getPosition(),finalTarget,"gattling",launcher.getMultiplier(),launcher);
			AssetLoader.gattlingShot.play();
			missiles.add(missile);
		}
	}

	private void fireDrones(){
		Vector2 position = new Vector2(999999,999999);
		Target finalTarget = new Target(position,9999999);
		Drone drone1;

		for(int k=0;k<drones.size();k++){
			//go through the list of enemies, shoot the closest
			drone1=drones.get(k);
			for(int i=0;i<enemies.size();i++){ 
				enemy = enemies.get(i); 
				if(isClosestValidTarget(finalTarget,enemy,ship.getPosition(),drone1.getRange()))
					finalTarget = enemy;
			}


			//go through the list of enemyMissiles, shoot the closest
			//missiles have highest priority
			for(int i=0;i<enemyMissiles.size();i++){ 
				enemyMissile = enemyMissiles.get(i); 
				if(isClosestValidTarget(finalTarget,enemyMissile,ship.getPosition(),drone1.getRange()))
					finalTarget = enemyMissile;
			}


			if(finalTarget.getPosition().dst(drone1.getPosition())>drone1.getRange()||
					ship.getPosition().dst(drone1.getPosition())>180)
				drone1.setDestination(ship.getPosition());
			else
				drone1.setDestination(finalTarget.getPosition());


			if((targets.size()>0 || enemies.size()>0 || enemyMissiles.size()>0)
					&& drone1.getPosition().dst(finalTarget.getPosition()) < drone1.getRange()
					&& drone1.getFireCount() == 0 ){
				missile = new Missile(drone1.getPosition(),finalTarget,"drone",drone1.getMultiplier(),launcher);
				AssetLoader.gattlingShot.play();
				missiles.add(missile);

				//System.out.println("missile added");
			}
			else{

			}
		}

	}

	public void setLastScreen (float x, float y){
		lastScreenX=x;
		lastScreenY = y;
	}
	public float getScreenX(){
		return lastScreenX;
	}
	public float getScreenY(){
		return lastScreenY;
	}

	public ShipWorld getShipWorld(){
		return shipWorld;
	}

	private void fireNuke(){
		nukeReady = false;
		nukeCount = 120;
		Vector2 position = new Vector2(999999,999999);
		Target finalTarget = new Target(position,9999999);
		AssetLoader.nuke.play();

		nuke = false;
		//go through the list of enemies, shoot the closest
		for(int i=0;i<enemies.size();i++){ 
			enemy = enemies.get(i); 
			if(isClosestValidTarget(finalTarget,enemy,ship.getPosition(),200))
				finalTarget = enemy;}

		Launcher l1 = new Launcher(-1000,-1000,"nuke",5);

		if(targets.size()>0 || enemies.size()>0 || enemyMissiles.size()>0){
			missile = new Missile(ship.getPosition(),finalTarget,"nuke",5,l1);
			missiles.add(missile);
		}

	}

	private void enemyFire(){

		Block finalTarget;
		if(ship.getArrayShipBlocks().size()>0){
			finalTarget = ship.getArrayShipBlocks().get((int)((Math.random())*ship.getArrayShipBlocks().size()));
			//check if the target is within range and fire

			objectivesDestroyed = true;
			for(int j=0;j<enemies.size();j++){
				enemy = enemies.get(j);
				if(enemy.getType().equals("city"))//check that the objectives are destroyed
					objectivesDestroyed = false;
				if(enemy.getPosition().dst(finalTarget.getPosition()) < enemy.getRange() && enemy.getFireCount()==0 ){
					enemyMissile = new EnemyMissile(enemy.getPosition(),finalTarget,enemy.getAttack(),enemy.getMissileLife());
					//System.out.println("Enemy Fire Attack:"+enemy.getAttack());
					AssetLoader.enemyShot.play();
					enemyMissiles.add(enemyMissile);}
			}
		}
	}

	private boolean isClosestValidTarget(Target currentTarget,Target newTarget, Vector2 lauPos,float range){
		boolean valid = true;


		if(lauPos.dst(currentTarget.getPosition()) < lauPos.dst(newTarget.getPosition())){//check that the previous target isn't closer
			valid = false;}
		if(lauPos.dst(newTarget.getPosition()) > range){//check that the enemy is within range
			valid = false;}
		return valid;
	}


	private void generateRocks(){
		//create 500 rocks and disperse them on the map
		int min = 0;
		int max = 1200;
		int xVal = 0;
		int yVal = 0;
		for (int i=0;i<90;i++){
			xVal = min + (int)(Math.random() * ((max - min) + 1));
			yVal = min + (int)(Math.random() * ((max - min) + 1));
			Vector2 pos = new Vector2(xVal,yVal);
			rock = new Rock(pos);
			rocks.add(rock);
		}
	}

	private void generateTargets(){
		//create 500 targets and disperse them on the map
		int min = 0;
		int max = 1200;
		int xVal = 0;
		int yVal = 0;
		for (int i=0;i<1;i++){
			xVal = min + (int)(Math.random() * ((max - min) + 1));
			yVal = min + (int)(Math.random() * ((max - min) + 1));
			target = new Target(new Vector2(xVal,yVal),3);
			targets.add(target);
		}


	}

	//private void spawnEnemies(){
	//enemies.add(myGen.)
	//}

	private void generateEnemies(){
		//create 500 targets and disperse them on the map
		int min = 0;
		int max = 1200;
		int xVal = 0;
		int yVal = 0;
		for (int i=0;i<50;i++){
			xVal = min + (int)(Math.random() * ((max - min) + 1));
			yVal = min + (int)(Math.random() * ((max - min) + 1));
			enemy = new Enemy(new Vector2(xVal,yVal),rocks,1000,5,5,200,10,"ship");
			enemies.add(enemy);
		}
	}

	private void manageVictory(){
		if(levelWorld.getCurrentStage()==shipWorld.getMaxStage()){
			shipWorld.incrementMaxStage();
			if(shipWorld.getMaxStage()<4)
				shipWorld.incrementMaxBlocks();
			currentStage++;
			shipWorld.addCredits(1500+levelWorld.getCurrentStage()*300);
			levelWorld.incrementLevel();}
		levelWorld.setCurrentStage(shipWorld.getMaxStage());
		msgBanner = "     VICTORY!";
		currentState = GameState.GAMEOVER;}


	public Vector2 getTutorialPointer(){
		return tutorialPointer;
	}

	public ArrayList<Rock> getRocks() {
		return rocks;
	}


	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public ArrayList<Target> getTargets() {
		return targets;
	}

	public ArrayList<Launcher> getLaunchers() {
		return launchers;
	}

	public ArrayList<Missile> getMissiles() {
		return missiles;
	}

	public ArrayList<EnemyMissile> getEnemyMissiles() {
		return enemyMissiles;
	}

	public GameState getCurrentState(){
		return currentState;
	}

	public Ship getShip() {
		return ship;
	}
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	public ArrayList<Enemy> getNearEnemies() {
		return enemies;
	}
	public ArrayList<Thruster> getThrusters() {
		return thrusters;
	}

	public void setShip(Ship vessel){

		this.ship = vessel;
		launchers = ship.getLaunchers();
		blocks = ship.getBlocks();
		thrusters = ship.getThrusters();
		ship.setRocks(rocks);
		generateDrones();
	}

	public void setEnemies(ArrayList<Enemy> enemies){
		this.enemies = enemies;
		hasObjectives = false;
		for(int i=0;i<enemies.size();i++){
			Enemy enemy = enemies.get(i);
			if(enemy.getType().equals("city")){
				hasObjectives = true;
				objectivesDestroyed = false;
				objectives.add(enemy);
			}
		}
	}

	public void setRocks(ArrayList<Rock> rocks){
		this.rocks = rocks;
	}

	public void setCurrentStateHelp(){
		currentState = GameState.HELP;
	}

	public void setCurrentStateRunning(){
		currentState = GameState.RUNNING;
	}

	public void setCurrentStateBuildWorld(){
		currentState = GameState.LEVELWORLD;
	}

	public void setCurrentStateBuildShip(){
		currentState = GameState.SHIPWORLD;
	}
	
	public void setMsgBanner(String str){
		msgBanner = str;
	}

	public void setCurrentStateTutorial(){
		currentState = GameState.TUTORIAL;
	}

	public void setXP(int x){
		xp=x;
	}

	public void setCurrentStage(int x){
		currentStage=x;
	}

	public int getXP(){
		return xp;
	}

	public void onClick(float screenX, float screenY){


		if(!paused){
			if(screenX>110 && screenX<200 && screenY>0 && screenY<26 && nukeReady && !bossFight){
				enemyMissiles.clear();
				nuke = true;}
			else
				ship.setDestination(screenX, screenY);

			if(screenX>0 && screenX<20 && screenY>0 && screenY<30 && touchDelay==0){
				touchDelay=1;
				msgBanner = "       PAUSED";
				paused = true;
			}
		}
		else{
			if(screenX>0 && screenX<20 && screenY>0 && screenY<30 && touchDelay==0){
				touchDelay=1;
				msgBanner = "       EXITING";
				paused = true;
				nukeCount = 0;
				paused = false;
				currentState = GameState.GAMEOVER;
			}
			else{
				if(touchDelay==0){
					paused = false;
					msgBanner = "";}
			}
		}

	}

	public boolean isPaused(){
		return paused;
	}

	public boolean nukeIsReady(){
		return nukeReady;
	}

	public float getNukeCountDown(){
		return nukeCount;
	}

	public Background getBackground() {
		// TODO Auto-generated method stub
		return background;
	}

	public void setCurrentStateShop() {
		currentState=GameState.SHOP;

	}

	public double[] getDiffConstant() {
		if(shipWorld!=null)
			return shipWorld.getDiffConstant();
		return new double[]{1,1,1,1,1,1};
	}



}
