//Copyright Isaac Shannon, All Rights reserved, 2014
package com.kilobolt.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.kilobolt.GameWorld.GameWorld.GameState;
import com.kilobolt.GameWorld.ShipWorld;
import com.kilobolt.GameWorld.LevelWorld;

import Helpers.AssetLoader;

import java.util.ArrayList;

import GameObjects.Block;
import GameObjects.Drone;
import GameObjects.Ship;
import GameObjects.Target;
import GameObjects.Launcher;
import GameObjects.Missile;
import GameObjects.Rock;
import GameObjects.Enemy;
import GameObjects.EnemyMissile;
import GameObjects.Thruster;

public class GameRenderer {

	private ShipWorld shipWorld;
	private GameWorld myWorld;
	private LevelWorld levelWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;
	private Sprite sprite,targetSprite,launcherSprite,missileSprite,rockSprite,thrusterSprite,
	armorSprite,sparkSprite,blankSprite,enemyMissileSprite,gattlingSprite,bulletSprite,
	gaussSprite,explosionSprite,shipExplosionSprite,citySprite,shieldSprite,mineSprite,
	destrockSprite,enemy1Sprite,enemy2Sprite,enemy3Sprite,enemy4Sprite,enemy5Sprite,
	enemy6Sprite,enemy7Sprite,enemy8Sprite,nukeSprite,nukeExplosionSprite,backgroundSprite,
	rayGunSprite,carrierSprite,disablerSprite,droneSprite,pointerSprite,overlordSprite,
	enemy21Sprite,enemy22Sprite,enemy23Sprite,enemy24Sprite,enemy25Sprite,enemy26Sprite,
	enemy31Sprite,enemy32Sprite,enemy33Sprite,enemy34Sprite,enemy35Sprite,enemy36Sprite,
	background2Sprite,rock2Sprite,addBlockSprite,lockSprite,unlockSprite,upgradeSprite,
	buttonFrameSprite,background3Sprite,rock3Sprite;

	public static TextureRegion colorBlock;

	private Ship ship;
	private ArrayList<Block> blocks;
	private ArrayList<Target> targets;
	private ArrayList<Launcher> launchers;
	private ArrayList<Missile> missiles;
	private ArrayList<Rock> rocks;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyMissile> enemyMissiles;
	private ArrayList<Thruster> thrusters;

	private BitmapFont font;

	private GameState currentState;
	private Vector2 shipPosition;

	public void create() {
		font = new BitmapFont();
		font.setColor(Color.RED);
	}

	//public GameRenderer(GameWorld world,BuildWorld buildWorld) {
	public GameRenderer(GameWorld world,ShipWorld shipWorld, LevelWorld levelWorld,
			float screenWidth,float gameHeight) {

		//System.out.println(screenWidth +" " + screenHeight);
		myWorld = world;
		this.shipWorld = shipWorld;
		this.levelWorld = levelWorld;
		currentState = myWorld.getCurrentState();
		cam = new OrthographicCamera();
		//cam.setToOrtho(true, 137, 204);
		//cam.setToOrtho(false, screenWidth, screenHeight);
		//System.out.println(gameHeight);
		cam.setToOrtho(false, 272, gameHeight*2);
		//cam.setToOrtho(false, 272, 408);
		font = new BitmapFont();
		font.setColor(Color.RED);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);

		ship = myWorld.getShip();
		shipPosition = ship.getPosition();
		//initAssets();
		initGameObjects();
	}

	public void render(){
		updateGameObjects();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//resets the screen back to original format when exiting level builder
		if(!currentState.equals(myWorld.getCurrentState())&&myWorld.getCurrentState().toString().equals("LEVELWORLD")){
			batcher.dispose();
			batcher = new SpriteBatch();}

		//set the sprites to the smaller format when the game is running
		if(!currentState.equals(myWorld.getCurrentState())&&myWorld.getCurrentState().toString().equals("RUNNING")){
			gattlingSprite.setSize(4,4);
			gaussSprite.setSize(4,4);
			launcherSprite.setSize(4,4);
			shieldSprite.setSize(4,4);
			mineSprite.setSize(4,4);
			sprite.setSize(4,4);
			rayGunSprite.setSize(4,4);
			carrierSprite.setSize(4,4);
			disablerSprite.setSize(4,4);
			thrusterSprite.setSize(4,4);}

		//reset the orientation of the sprites when the game returns to the ship building screen
		if(!currentState.equals(myWorld.getCurrentState())&&myWorld.getCurrentState().toString().equals("SHIPWORLD")){
			gattlingSprite.setRotation(0);
			gaussSprite.setRotation(0);
			launcherSprite.setRotation(0);
			sprite.setRotation(0);
			shieldSprite.setRotation(0);
			mineSprite.setRotation(0);
			thrusterSprite.setRotation(0);
			rayGunSprite.setRotation(0);
			carrierSprite.setRotation(0);
			disablerSprite.setRotation(0);
		}

		currentState = myWorld.getCurrentState();
		switch (currentState) {
		case SHIPWORLD:
			renderBuildingShip();
			break;
		case RUNNING:
			renderRunning();
			break;
		case LEVELWORLD:
			break;
		case HELP:
			break;
		case SHOP:
			renderShop();
			break;
		case TUTORIAL:
			renderTutorial();
			break;
		default:
			renderRunning();
			break;
		}

	}

	public void renderBuildingShip() {
		batcher.begin();
		batcher.enableBlending();
		batcher.setProjectionMatrix(cam.combined);

		runningDrawBackground();
		buildShipDrawBuildSquares();
		buildShipDrawMsgBanner();
		//buildShipDrawPallet();
		buildShipDrawMenu();



		batcher.end();
		/*shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.rect(0,360,400,4);
		shapeRenderer.rect(145,0,4,360);
		shapeRenderer.end();*/


	}

	public void renderRunning() {

		batcher.begin();
		batcher.enableBlending();

		runningDrawBackground();
		runningDrawRocks();
		runningDrawBlocks();
		runningDrawThrusters();
		runningDrawLaunchers();
		runningDrawTargets();
		runningDrawEnemies();
		runningDrawMissiles();
		runningDrawEnemyMissiles();
		runningDrawDrones();
		runningDrawMenu();
		runningDrawAnimation();
		runningDrawIntroText();

		batcher.end();
		runningDrawLifeBars();
		runningDrawDefense();

	}


	private void runningDrawAnimation() {
		double time = myWorld.getGameTime();

		if(time<5){
			double transparencyX = (2.5-Math.abs(time-2.5))/2.5;
			if(transparencyX>0.5)
				transparencyX=0.5;
			backgroundSprite.setPosition(0,0);
			//backgroundSprite.draw(batcher);
			overlordSprite.setPosition(75,150);
			overlordSprite.setColor(1, 1, 1,(float) (transparencyX));
			overlordSprite.draw(batcher);}


	}

	//draws the story text at beginning of game
	private void runningDrawIntroText(){
		String[] intros = new String[]{"","","",""};
		String word ="";
		double time = myWorld.getGameTime();
		int j=0;
		int lineLength = 40;
		
		// the current player level
		int maxStage = myWorld.getShipWorld().getMaxStage();
		
		//get a new msg every 2 levels
		int msgNumber = (maxStage+3)/2;
		
		//the first 3 levels get messages
		if(maxStage < 4)
			msgNumber = maxStage;
		
		//there are only 30 messages to fetch
		if(msgNumber>30)
			msgNumber = 30;

		if(myWorld.getGameTime()<10){
			double transparencyX = (5-Math.abs(time-5))/5;
			String intro = AssetLoader.intros[msgNumber];
			//System.out.println(intro);
			if(intro.length()>lineLength){
				for(int i=0;i<intro.length()-1;i++){
					j = (int)i/lineLength;
					word = word + intro.charAt(i);
					//System.out.println(intro.charAt(i));
					if(intro.charAt(i)==' '){
						intros[j] =  intros[j]+word;
						word = "";}
					else
						if(i==intro.length()-2){
							intros[j] =  intros[j]+word;
							word = "";}
				}
			}
			else
				intros[0]=intro;

			//draw the 3 parts of the messages 
			if(maxStage%2==0 || msgNumber<4){
				AssetLoader.font.setScale((float)0.15,(float)0.15);
				AssetLoader.font.setColor(1,0,0,(float) transparencyX);
				AssetLoader.font.draw(batcher,intros[0], 20, 110);
				AssetLoader.font.draw(batcher,intros[1], 20, 80);
				AssetLoader.font.draw(batcher,intros[2], 20, 50);
				intros[0]="";
				intros[1]="";
				intros[2]="";
				intros[3]="";
				AssetLoader.font.setScale((float)0.2,(float)0.2);
				AssetLoader.font.setColor(1,1,1,1);}
		}
	}


		/**
		 * render the shop menus, logos and icons
		 */
	public void renderShop(){
		batcher.begin();
		shopRenderMenu();
		shopRenderPallet();
		drawMsgBanner();
		batcher.end();
		/*shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.rect(0,370,400,4);
		shapeRenderer.rect(120,0,4,370);
		shapeRenderer.end();*/
	}

	/**
	 * render the tutorial icon and text
	 */
	public void renderTutorial(){
		if(!myWorld.displayTutorial2())
			renderBuildingShip();
		else
			renderShop();

		batcher.begin();
		pointerSprite.setPosition(myWorld.getTutorialPointer().x, myWorld.getTutorialPointer().y);
		pointerSprite.draw(batcher);
		batcher.end();

		//shapeRenderer.begin(ShapeType.Filled);
		//shapeRenderer.setColor(1, 0, 0, 1);
		//shapeRenderer.circle(100, 100, 5);
		//shapeRenderer.end();
	}


	private void shopRenderPallet(){
		gattlingSprite.setPosition(10, 349);
		gattlingSprite.setSize(12,12);
		gattlingSprite.draw(batcher);

		launcherSprite.setPosition(10, 309);
		launcherSprite.setSize(12,12);
		launcherSprite.draw(batcher);

		shieldSprite.setPosition(10, 269);
		shieldSprite.setSize(12,12);
		shieldSprite.draw(batcher);

		if(shipWorld.gaussLevel>0){
			gaussSprite.setPosition(10, 229);
			gaussSprite.setSize(12,12);
			gaussSprite.draw(batcher);}

		if(shipWorld.mineLevel>0){
			mineSprite.setPosition(10, 189);
			mineSprite.setSize(12,12);
			mineSprite.draw(batcher);}

		if(shipWorld.disablerLevel>0){
			disablerSprite.setPosition(10, 149);
			disablerSprite.setSize(12,12);
			disablerSprite.draw(batcher);}

		if(shipWorld.carrierLevel>0){
			carrierSprite.setPosition(10, 109);
			carrierSprite.setSize(12,12);
			carrierSprite.draw(batcher);}

		sprite.setPosition(10, 69);
		sprite.setSize(12,12);
		sprite.draw(batcher);
	}

	private void shopRenderMenu(){
		AssetLoader.font.setScale((float)0.15,(float)0.15);
		AssetLoader.font.draw(batcher,"CREDITS: "+Integer.toString(shipWorld.getCredits())+"$", 10, 390);


		buttonFrameSprite.setSize(110, 32);//THREE MAIN MENU FRAMES
		buttonFrameSprite.setPosition(0,334);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setPosition(0,294);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setPosition(0,254);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setPosition(0,214);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setPosition(0,174);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setPosition(0,134);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setPosition(0,94);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setSize(110, 20);
		buttonFrameSprite.setPosition(0,65);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setPosition(0,35);
		buttonFrameSprite.draw(batcher);

		AssetLoader.font.draw(batcher,"DEFENSE", 50, 360);
		//AssetLoader.font.draw(batcher,"UPGRADE", 130, 360);
		upgradeSprite.setPosition(130, 344);
		upgradeSprite.draw(batcher);
		AssetLoader.font.draw(batcher,"LVL: ", 210, 360);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.gattlingLevel), 230, 360);

		AssetLoader.font.draw(batcher,"MISSILE", 50, 320);
		//AssetLoader.font.draw(batcher,"UPGRADE", 130, 320);
		upgradeSprite.setPosition(130, 304);
		upgradeSprite.draw(batcher);
		AssetLoader.font.draw(batcher,"LVL: ", 210, 320);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.launcherLevel), 230, 320);

		AssetLoader.font.draw(batcher,"SHIELD", 50, 280);
		//AssetLoader.font.draw(batcher,"UPGRADE", 130, 280);
		upgradeSprite.setPosition(130, 264);
		upgradeSprite.draw(batcher);
		AssetLoader.font.draw(batcher,"LVL: ", 210, 280);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.shieldLevel), 230, 280);


		if(shipWorld.gaussLevel>0){
			//AssetLoader.font.draw(batcher,"UPGRADE", 130, 240);
			upgradeSprite.setPosition(130, 224);
			upgradeSprite.draw(batcher);
			AssetLoader.font.draw(batcher,"LVL: ", 210, 240);
			AssetLoader.font.draw(batcher,Integer.toString(shipWorld.gaussLevel), 230, 240);
			AssetLoader.font.draw(batcher,"CANNON", 50, 240);}
		else{
			AssetLoader.font.draw(batcher,"CANNON ", 10, 240);
			unlockSprite.setPosition(135, 225);
			unlockSprite.draw(batcher);
			lockSprite.setPosition(65, 225);
			lockSprite.draw(batcher);}

		if(shipWorld.mineLevel>0){
			//AssetLoader.font.draw(batcher,"UPGRADE", 130, 200);
			upgradeSprite.setPosition(130, 184);
			upgradeSprite.draw(batcher);
			AssetLoader.font.draw(batcher,"LVL: ", 210, 200);
			AssetLoader.font.draw(batcher,Integer.toString(shipWorld.mineLevel), 230, 200);
			AssetLoader.font.draw(batcher,"MINES", 50, 200);}
		else{
			AssetLoader.font.draw(batcher,"MINES ", 10, 200);
			unlockSprite.setPosition(135, 185);
			unlockSprite.draw(batcher);
			lockSprite.setPosition(65, 185);
			lockSprite.draw(batcher);}

		if(shipWorld.disablerLevel>0){
			//AssetLoader.font.draw(batcher,"UPGRADE", 130, 160);
			upgradeSprite.setPosition(130, 144);
			upgradeSprite.draw(batcher);
			AssetLoader.font.draw(batcher,"LVL: ", 210, 160);
			AssetLoader.font.draw(batcher,Integer.toString(shipWorld.disablerLevel), 230, 160);
			AssetLoader.font.draw(batcher,"DISABLER", 50, 160);}
		else{
			AssetLoader.font.draw(batcher,"DISABLER ", 10, 160);
			unlockSprite.setPosition(135, 145);
			unlockSprite.draw(batcher);
			lockSprite.setPosition(65, 145);
			lockSprite.draw(batcher);}


		if(shipWorld.carrierLevel>0){
			AssetLoader.font.draw(batcher,"CARRIER", 50, 120);
			upgradeSprite.setPosition(130, 104);
			upgradeSprite.draw(batcher);
			AssetLoader.font.draw(batcher,"LVL: ", 210, 120);
			AssetLoader.font.draw(batcher,Integer.toString(shipWorld.carrierLevel), 230, 120);}
		//AssetLoader.font.draw(batcher,"UPGRADE", 130, 120);}
		else{
			AssetLoader.font.draw(batcher,"CARRIER ", 10, 120);
			unlockSprite.setPosition(135, 105);
			unlockSprite.draw(batcher);
			lockSprite.setPosition(65, 105);
			lockSprite.draw(batcher);}

		AssetLoader.font.draw(batcher,"HULL", 50, 80);
		AssetLoader.font.draw(batcher,"FREE", 130, 78);

		AssetLoader.font.setColor(0f, 1.0f, 0f, 1.0f);
		AssetLoader.font.draw(batcher,"ADDITIONAL BRICK", 10, 50);

		addBlockSprite.setPosition(130, 38);
		addBlockSprite.draw(batcher);
		AssetLoader.font.setColor(1f, 1.0f, 1f, 1.0f);
		AssetLoader.font.setScale((float)0.20,(float)0.20);
		AssetLoader.font.draw(batcher,"EXIT", 10, 20);

		AssetLoader.font.setScale((float)0.12,(float)0.12);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.gattlingUpgrade*(1+shipWorld.gattlingLevel))+"$", 135, 345);
		AssetLoader.font.draw(batcher,"DMG:"+shipWorld.getGattlingDmg(), 10, 345);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.launcherUpgrade*(1+shipWorld.launcherLevel))+"$", 135, 305);
		AssetLoader.font.draw(batcher,"DMG:"+shipWorld.getLauncherDmg(), 10, 305);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.shieldUpgrade*(1+shipWorld.shieldLevel))+"$", 135, 265);
		AssetLoader.font.draw(batcher,"SHIELD:"+ship.getMaxShield(), 10, 265);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.gaussUpgrade*(1+shipWorld.gaussLevel))+"$", 135, 225);
		AssetLoader.font.draw(batcher,"DMG:"+shipWorld.getGaussDmg(), 10, 225);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.mineUpgrade*(1+shipWorld.mineLevel))+"$", 135, 185);
		AssetLoader.font.draw(batcher,"DMG:"+shipWorld.getMineDmg(), 10, 185);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.disablerUpgrade*(1+shipWorld.disablerLevel))+"$", 135, 145);
		AssetLoader.font.draw(batcher,"DMG:"+shipWorld.getDisablerDmg() +"%", 10, 145);
		AssetLoader.font.draw(batcher,Integer.toString(shipWorld.carrierUpgrade*(1+shipWorld.carrierLevel))+"$", 135, 105);
		AssetLoader.font.draw(batcher,"DMG:"+shipWorld.getCarrierDmg(), 10, 105);
		AssetLoader.font.draw(batcher,shipWorld.getBrickCost()+"$", 135, 40);
		AssetLoader.font.setScale((float)0.20,(float)0.20);
	}

	private void updateGameObjects(){
		shipPosition = ship.getPosition();
		blocks = myWorld.getBlocks();
		targets = myWorld.getTargets();
		launchers = myWorld.getLaunchers();
		missiles = myWorld.getMissiles();
		enemyMissiles = myWorld.getEnemyMissiles();
		rocks = myWorld.getRocks();
		enemies = myWorld.getEnemies();
		thrusters = myWorld.getThrusters();
		ship = myWorld.getShip();
	}


	private void initGameObjects() {
		blocks = myWorld.getBlocks();
		targets = myWorld.getTargets();
		launchers = myWorld.getLaunchers();
		missiles = myWorld.getMissiles();
		enemyMissiles = myWorld.getEnemyMissiles();
		rocks = myWorld.getRocks();
		enemies = myWorld.getEnemies();
		thrusters = myWorld.getThrusters();
		ship = myWorld.getShip();

		sprite = new Sprite(AssetLoader.mediumGrey, 0, 0, 4, 4);
		blankSprite = new Sprite(AssetLoader.lightGrey, 0, 0, 4, 4);
		targetSprite = new Sprite(AssetLoader.fullRed, 0, 0, 2, 2);
		launcherSprite = new Sprite(AssetLoader.green, 0, 0, 4, 4);
		gattlingSprite = new Sprite(AssetLoader.pink, 0, 0, 4, 4);
		missileSprite = new Sprite(AssetLoader.yellow, 0, 0, 2, 2);
		bulletSprite = new Sprite(AssetLoader.fullPink, 0, 0, 2, 2);
		gaussSprite = new Sprite(AssetLoader.red, 0, 0, 4, 4);
		enemyMissileSprite = new Sprite(AssetLoader.cyan,0,0,2,2);
		rockSprite = new Sprite(AssetLoader.rock, 0, 0, 16, 16);
		rockSprite.scale((float)0.5);
		rock2Sprite = new Sprite(AssetLoader.rock2, 0, 0, 16, 16);
		rock2Sprite.scale((float)0.5);
		rock3Sprite = new Sprite(AssetLoader.rock3Texture, 0, 0, 16, 16);
		rock3Sprite.scale((float)0.5);
		enemy1Sprite = new Sprite(AssetLoader.enemy1, 0, 0, 16, 16);//butterfly
		enemy2Sprite = new Sprite(AssetLoader.enemy2, 0, 0, 16, 16);//tie-fighter
		enemy3Sprite = new Sprite(AssetLoader.enemy3, 0, 0, 16, 16);//purple-cyclops
		enemy4Sprite = new Sprite(AssetLoader.enemy4, 0, 0, 16, 16);//Saucer
		enemy5Sprite = new Sprite(AssetLoader.enemy5, 0, 0, 16, 16);//skull
		enemy5Sprite.scale((float) 1.2);
		enemy6Sprite = new Sprite(AssetLoader.enemy6, 0, 0, 16, 16);
		enemy7Sprite = new Sprite(AssetLoader.enemy7, 0, 0, 16, 16);
		enemy8Sprite = new Sprite(AssetLoader.enemy8, 0, 0, 16, 16);
		enemy21Sprite = new Sprite(AssetLoader.enemy21, 0, 0, 16, 16);
		enemy22Sprite = new Sprite(AssetLoader.enemy22, 0, 0, 16, 16);
		enemy23Sprite = new Sprite(AssetLoader.enemy23, 0, 0, 16, 16);
		enemy24Sprite = new Sprite(AssetLoader.enemy24, 0, 0, 16, 16);
		enemy25Sprite = new Sprite(AssetLoader.enemy25, 0, 0, 16, 16);
		enemy25Sprite.scale((float) 1.2);
		enemy26Sprite = new Sprite(AssetLoader.enemy26, 0, 0, 16, 16);
		enemy31Sprite = new Sprite(AssetLoader.enemy31, 0, 0, 16, 16);
		enemy32Sprite = new Sprite(AssetLoader.enemy32, 0, 0, 16, 16);
		enemy33Sprite = new Sprite(AssetLoader.enemy33, 0, 0, 16, 16);
		enemy34Sprite = new Sprite(AssetLoader.enemy34, 0, 0, 16, 16);
		enemy35Sprite = new Sprite(AssetLoader.enemy35, 0, 0, 16, 16);
		enemy35Sprite.scale((float) 1.2);
		enemy36Sprite = new Sprite(AssetLoader.enemy36, 0, 0, 16, 16);
		disablerSprite = new Sprite(AssetLoader.orangeCross, 0, 0, 4, 4);
		carrierSprite = new Sprite(AssetLoader.blackSpot, 0, 0, 4, 4);
		rayGunSprite = new Sprite(AssetLoader.rainbow, 0, 0, 4, 4);
		thrusterSprite = new Sprite(AssetLoader.thruster, 0, 0, 4, 4);
		armorSprite = new Sprite(AssetLoader.darkGrey, 0, 0, 4, 4);
		sparkSprite = new Sprite(AssetLoader.pink, 0, 0, 1, 1);
		explosionSprite = new Sprite(AssetLoader.explosion,0,0,8,8);
		shipExplosionSprite = new Sprite(AssetLoader.shipExplosion,0,0,16,16);
		citySprite = new Sprite(AssetLoader.city,0,0,16,16);
		citySprite.scale(2);
		shieldSprite = new Sprite(AssetLoader.blue, 0, 0, 4, 4);
		mineSprite = new Sprite(AssetLoader.darkGreen,0,0,4,4);
		destrockSprite = new Sprite(AssetLoader.destrock,0,0,16,16);
		nukeSprite = new Sprite(AssetLoader.nukeSymbol,0,0,32,32);
		nukeExplosionSprite = new Sprite(AssetLoader.nukeExplosion,0,0,32,32);
		nukeExplosionSprite.scale(5);
		nukeExplosionSprite.scale(1);
		backgroundSprite = new Sprite(AssetLoader.background,0,0,400,800);
		background2Sprite = new Sprite(AssetLoader.background2,0,0,400,800);
		background3Sprite = new Sprite(AssetLoader.background3,0,0,400,800);
		droneSprite = new Sprite(AssetLoader.drone,0,0,16,16);
		pointerSprite = new Sprite(AssetLoader.pointer,0,0,32,32);
		overlordSprite = new Sprite(AssetLoader.overlordTexture,0,0,128,128);
		overlordSprite.scale((float)1.5);
		addBlockSprite = new Sprite(AssetLoader.addBlock,0,0,32,16);
		upgradeSprite = new Sprite(AssetLoader.upgrade,0,0,32,16);
		upgradeSprite.setSize(48,21);
		lockSprite = new Sprite(AssetLoader.lock,0,0,16,16);
		lockSprite.scale((float)0.7);
		unlockSprite = new Sprite(AssetLoader.unlock,0,0,16,16);
		unlockSprite.scale((float)0.7);
		buttonFrameSprite = new Sprite(AssetLoader.buttonFrame,0,0,128,32);
	}

	private void runningDrawDrones() {
		ArrayList<Drone> drones = myWorld.getDrones();
		if(!(drones==null)){
			for(int i=0;i<drones.size()&&drones!=null;i++){
				Drone drone = drones.get(i);{
					droneSprite.setPosition(drone.getPosition().x,
							drone.getPosition().y);
					droneSprite.draw(batcher);}
			}
		}
	}

	private void runningDrawBlocks() {
		// draw the ship blocks
		if(blocks!=null){
			for (int i = 0; i < blocks.size(); i++) {
				Block block = (Block) blocks.get(i);

				if(block.getType().equals("block")){
					sprite.setPosition(block.getPosition().x, block.getPosition().y);
					sprite.setRotation(ship.getRotation());
					sprite.draw(batcher);}
				if(block.getType().equals("shield")){
					shieldSprite.setPosition(block.getPosition().x, block.getPosition().y);
					shieldSprite.setRotation(ship.getRotation());
					shieldSprite.draw(batcher);}
			}
		}
	}

	private void runningDrawThrusters() {
		// draw the ship thrusters
		for (int i = 0; i < thrusters.size(); i++) {
			Thruster thruster = (Thruster) thrusters.get(i);

			thrusterSprite.setPosition(thruster.getPosition().x,
					thruster.getPosition().y);
			thrusterSprite.setRotation(ship.getRotation());
			thrusterSprite.draw(batcher);
		}
	}


	private void runningDrawLaunchers() {
		// draw the ship launchers
		if(launchers != null){
			for (int i = 0; i < launchers.size(); i++) {
				Launcher launcher = (Launcher) launchers.get(i);
				float x = launcher.getPosition().x;
				float y = launcher.getPosition().y;

				//test that all launchers are drawn
				launcherSprite.setPosition(x,y);
				launcherSprite.setRotation(ship.getRotation());
				launcherSprite.draw(batcher);

				//draw missile launchers
				if(launcher.getType()=="missile"){
					launcherSprite.setPosition(x,y);
					launcherSprite.setRotation(ship.getRotation());
					launcherSprite.draw(batcher);}

				//draw missile launchers
				if(launcher.getType()=="gattling"){
					gattlingSprite.setPosition(x,y);
					gattlingSprite.setRotation(ship.getRotation());
					gattlingSprite.draw(batcher);}

				//draw gauss rifles
				if(launcher.getType()=="gauss"){
					gaussSprite.setPosition(x,y);
					gaussSprite.setRotation(ship.getRotation());
					gaussSprite.draw(batcher);}

				//draw mines
				if(launcher.getType()=="mine"){
					mineSprite.setPosition(x,y);
					mineSprite.setRotation(ship.getRotation());
					mineSprite.draw(batcher);}

				//draw rayGuns
				if(launcher.getType()=="ray"){
					rayGunSprite.setPosition(x,y);
					rayGunSprite.setRotation(ship.getRotation());
					rayGunSprite.draw(batcher);}

				//draw disablers
				if(launcher.getType()=="disabler"){
					disablerSprite.setPosition(x,y);
					disablerSprite.setRotation(ship.getRotation());
					disablerSprite.draw(batcher);}

				//draw carriers
				if(launcher.getType()=="carrier"){
					carrierSprite.setPosition(x,y);
					carrierSprite.setRotation(ship.getRotation());
					carrierSprite.draw(batcher);}
			}
		}
	}


	private void runningDrawTargets() {
		// draw the targets
		//for (int i = 0; i < targets.size(); i++) {
		//Target target = (Target) targets.get(i);
		//targetSprite.setPosition(target.getPosition().x,
		//	target.getPosition().y);
		//targetSprite.draw(batcher);
		//}
	}

	private void drawHud(Enemy enemy){
		if(myWorld.getGameTime()>5){
			float angle = (shipPosition.cpy().sub(enemy.getPosition()).angle()-90)/58;
			float xPos = (float)Math.sin(angle)*130+137;
			float yPos =-(float)Math.cos(angle)*170+190;
			targetSprite.setPosition(xPos,yPos) ;
			targetSprite.draw(batcher);}
	}

	private void runningDrawBackground(){
		if(shipWorld.getMaxStage()<=17 ||shipWorld.getMaxStage()>60)
			drawBackground1();
		if(shipWorld.getMaxStage()>=18 && shipWorld.getMaxStage()<=37)
			drawBackground2();
		if(shipWorld.getMaxStage()>=38 &&shipWorld.getMaxStage()<=60)
			drawBackground3();
	}

	private void drawBackground1(){
		Vector2 mPos = myWorld.getBackground().getPosition().cpy();
		backgroundSprite.setPosition(mPos.x,mPos.y);
		backgroundSprite.draw(batcher);
		backgroundSprite.setPosition(mPos.x+399,mPos.y+799);
		backgroundSprite.draw(batcher);
		backgroundSprite.setPosition(mPos.x-399,mPos.y+799);
		backgroundSprite.draw(batcher);
		backgroundSprite.setPosition(mPos.x+399,mPos.y-799);
		backgroundSprite.draw(batcher);
		backgroundSprite.setPosition(mPos.x-399,mPos.y-799);
		backgroundSprite.draw(batcher);
		backgroundSprite.setPosition(mPos.x,mPos.y-799);
		backgroundSprite.draw(batcher);
		backgroundSprite.setPosition(mPos.x,mPos.y+799);
		backgroundSprite.draw(batcher);
		backgroundSprite.setPosition(mPos.x+399,mPos.y);
		backgroundSprite.draw(batcher);
		backgroundSprite.setPosition(mPos.x-399,mPos.y);
		backgroundSprite.draw(batcher);
	}

	private void drawBackground2(){
		Vector2 mPos = myWorld.getBackground().getPosition().cpy();
		background2Sprite.setPosition(mPos.x,mPos.y);
		background2Sprite.draw(batcher);
		background2Sprite.setPosition(mPos.x+399,mPos.y+799);
		background2Sprite.draw(batcher);
		background2Sprite.setPosition(mPos.x-399,mPos.y+799);
		background2Sprite.draw(batcher);
		background2Sprite.setPosition(mPos.x+399,mPos.y-799);
		background2Sprite.draw(batcher);
		background2Sprite.setPosition(mPos.x-399,mPos.y-799);
		background2Sprite.draw(batcher);
		background2Sprite.setPosition(mPos.x,mPos.y-799);
		background2Sprite.draw(batcher);
		background2Sprite.setPosition(mPos.x,mPos.y+799);
		background2Sprite.draw(batcher);
		background2Sprite.setPosition(mPos.x+399,mPos.y);
		background2Sprite.draw(batcher);
		background2Sprite.setPosition(mPos.x-399,mPos.y);
		background2Sprite.draw(batcher);
	}

	private void drawBackground3(){
		Vector2 mPos = myWorld.getBackground().getPosition().cpy();
		background3Sprite.setPosition(mPos.x,mPos.y);
		background3Sprite.draw(batcher);
		background3Sprite.setPosition(mPos.x+399,mPos.y+799);
		background3Sprite.draw(batcher);
		background3Sprite.setPosition(mPos.x-399,mPos.y+799);
		background3Sprite.draw(batcher);
		background3Sprite.setPosition(mPos.x+399,mPos.y-799);
		background3Sprite.draw(batcher);
		background3Sprite.setPosition(mPos.x-399,mPos.y-799);
		background3Sprite.draw(batcher);
		background3Sprite.setPosition(mPos.x,mPos.y-799);
		background3Sprite.draw(batcher);
		background3Sprite.setPosition(mPos.x,mPos.y+799);
		background3Sprite.draw(batcher);
		background3Sprite.setPosition(mPos.x+399,mPos.y);
		background3Sprite.draw(batcher);
		background3Sprite.setPosition(mPos.x-399,mPos.y);
		background3Sprite.draw(batcher);
	}

	private void runningDrawDefense(){
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 0, 0, 1);
		for (int i = 0; i < missiles.size(); i++) {
			Missile missile = missiles.get(i);

			//draw defense laser line
			if(missile.getType().equals("gattling")&&missile.getDeathCount()==0&&
					missile.getPosition().dst(missile.getDestination())<20){
				shapeRenderer.line(missile.getDestination(), missile.getLauncher().getPosition());
			}
			if(missile.getType().equals("disabler")&&missile.getDeathCount()==0&&
					missile.getPosition().dst(missile.getDestination())<20){
				shapeRenderer.setColor(0, 1, 1, 1);
				shapeRenderer.line(missile.getDestination().cpy().add(new Vector2(5,5)), missile.getLauncher().getPosition());
			}
		}
		shapeRenderer.end();
	}

	private void runningDrawEnemies() {
		// draw the enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			drawHud(enemy);

			if(enemy.getDeathCount()>0){
				explosionSprite.setSize(32, 32);
				explosionSprite.setPosition(enemy.getPosition().x,enemy.getPosition().y+10);
				explosionSprite.draw(batcher);
				AssetLoader.font.draw(batcher, "+"+enemy.getValue()+"$", enemy.getPosition().x, enemy.getPosition().y);
				explosionSprite.setSize(4, 4);
			}
			else{
				if(shipWorld.getMaxStage()<=17 ||shipWorld.getMaxStage()>60)
					drawEnemies1(enemy);
				if(shipWorld.getMaxStage()>=18 && shipWorld.getMaxStage()<=37)
					drawEnemies2(enemy);
				if(shipWorld.getMaxStage()>=38 &&shipWorld.getMaxStage()<=60)
					drawEnemies3(enemy);
			}
		}
	}

	private void drawEnemies1(Enemy enemy){
		if(enemy.getType().equals("city")){
			citySprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			citySprite.draw(batcher);}
		if(enemy.getType().equals("rock")){
			destrockSprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			destrockSprite.draw(batcher);}
		if(enemy.getType().equals("0001")){
			enemy1Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy1Sprite.draw(batcher);}
		if(enemy.getType().equals("0002")){
			enemy2Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy2Sprite.draw(batcher);}
		if(enemy.getType().equals("0003")){
			enemy3Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy3Sprite.draw(batcher);}
		if(enemy.getType().equals("0004")){
			enemy4Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy4Sprite.draw(batcher);}
		if(enemy.getType().equals("0005")){
			enemy5Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy5Sprite.draw(batcher);}
		if(enemy.getType().equals("0006")){
			enemy6Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy6Sprite.draw(batcher);}
		if(enemy.getType().equals("0007")){
			enemy1Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy1Sprite.draw(batcher);}
		if(enemy.getType().equals("0008")){
			enemy2Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy2Sprite.draw(batcher);}
		if(enemy.getType().equals("ship")){
			enemy3Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy3Sprite.draw(batcher);}
	}

	private void drawEnemies2(Enemy enemy){
		if(enemy.getType().equals("city")){
			citySprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			citySprite.draw(batcher);}
		if(enemy.getType().equals("rock")){
			destrockSprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			destrockSprite.draw(batcher);}
		if(enemy.getType().equals("0001")){
			enemy21Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy21Sprite.draw(batcher);}
		if(enemy.getType().equals("0002")){
			enemy22Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy22Sprite.draw(batcher);}
		if(enemy.getType().equals("0003")){
			enemy23Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy23Sprite.draw(batcher);}
		if(enemy.getType().equals("0004")){
			enemy24Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy24Sprite.draw(batcher);}
		if(enemy.getType().equals("0005")){
			enemy25Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy25Sprite.draw(batcher);}
		if(enemy.getType().equals("0006")){
			enemy26Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy26Sprite.draw(batcher);}
		if(enemy.getType().equals("0007")){
			enemy21Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy21Sprite.draw(batcher);}
		if(enemy.getType().equals("0008")){
			enemy22Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy22Sprite.draw(batcher);}
		if(enemy.getType().equals("ship")){
			enemy23Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy23Sprite.draw(batcher);}
	}

	private void drawEnemies3(Enemy enemy){
		if(enemy.getType().equals("city")){
			citySprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			citySprite.draw(batcher);}
		if(enemy.getType().equals("rock")){
			destrockSprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			destrockSprite.draw(batcher);}
		if(enemy.getType().equals("0001")){
			enemy31Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy31Sprite.draw(batcher);}
		if(enemy.getType().equals("0002")){
			enemy32Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy32Sprite.draw(batcher);}
		if(enemy.getType().equals("0003")){
			enemy33Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy33Sprite.draw(batcher);}
		if(enemy.getType().equals("0004")){
			enemy34Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy34Sprite.draw(batcher);}
		if(enemy.getType().equals("0005")){
			enemy35Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy35Sprite.draw(batcher);}
		if(enemy.getType().equals("0006")){
			enemy36Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy36Sprite.draw(batcher);}
		if(enemy.getType().equals("0007")){
			enemy31Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy31Sprite.draw(batcher);}
		if(enemy.getType().equals("0008")){
			enemy32Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy32Sprite.draw(batcher);}
		if(enemy.getType().equals("ship")){
			enemy33Sprite.setPosition(enemy.getPosition().x,enemy.getPosition().y);
			enemy33Sprite.draw(batcher);}
	}

	private void runningDrawMissiles() {
		// draw the missiles
		for (int i = 0; i < missiles.size(); i++) {
			Missile missile = missiles.get(i);

			if(missile.getDeathCount() >0 && !missile.getType().equals("gattling") &&!missile.getType().equals("disabler")){
				explosionSprite.setSize(16, 16);
				explosionSprite.setPosition(missile.getPosition().x,
						missile.getPosition().y);
				explosionSprite.draw(batcher);
				if(missile.getType().equals("nuke")){
					nukeExplosionSprite.setPosition(missile.getPosition().x,missile.getPosition().y);
					nukeExplosionSprite.draw(batcher);
				}
			}
			else{
				//draw missiles
				if(missile.getType().equals("missile")||missile.getType().equals("nuke")
						||missile.getType().equals("ray")){
					missileSprite.setPosition(missile.getPosition().x,
							missile.getPosition().y);
					missileSprite.draw(batcher);}

				//draw bullets
				if(missile.getType().equals("gauss")
						||missile.getType().equals("mine")||missile.getType().equals("drone") ){
					bulletSprite.setPosition(missile.getPosition().x,
							missile.getPosition().y);
					bulletSprite.draw(batcher);}
			}

		}
	}

	private void runningDrawEnemyMissiles() {
		// draw the enemy missiles
		for (int i = 0; i < enemyMissiles.size(); i++) {
			EnemyMissile enemyMissile = enemyMissiles.get(i);

			if(enemyMissile.getDeathCount() >0){
				//System.out.println("Missile explosion drawn");
				explosionSprite.setPosition(enemyMissile.getPosition().x,
						enemyMissile.getPosition().y);
				explosionSprite.draw(batcher);}
			else{
				//draw missiles
				enemyMissileSprite.setPosition(enemyMissile.getPosition().x,
						enemyMissile.getPosition().y);
				enemyMissileSprite.draw(batcher);}
		}
	}

	private void runningDrawRocks() {
		// draw the rocks
		for (int i = 0; i < rocks.size(); i++) {
			Rock rock = (Rock) rocks.get(i);
			if(shipWorld.getMaxStage()<=17||shipWorld.getMaxStage()>60){
				rockSprite.setPosition(rock.getPosition().x-5, rock.getPosition().y-5);
				rockSprite.draw(batcher);}
			if(shipWorld.getMaxStage()>=18 && shipWorld.getMaxStage()<=37){
				rock2Sprite.setPosition(rock.getPosition().x-5, rock.getPosition().y-5);
				rock2Sprite.draw(batcher);}
			if(shipWorld.getMaxStage()>=38 && shipWorld.getMaxStage()<=60){
				rock3Sprite.setPosition(rock.getPosition().x-5, rock.getPosition().y-5);
				rock3Sprite.draw(batcher);}
		}

	}
	private void runningDrawLifeBars(){
		if(myWorld.getGameTime()>5){
			float ds = (30*ship.getShield()/(ship.getMaxShield()));
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1, 0, 0, 1);
			shapeRenderer.rect(20,(float) (200-2*ship.getLife()),3 , 4*ship.getLife());
			shapeRenderer.setColor(0, 0, 1, 1);
			shapeRenderer.rect(25, (float) (200-2*ds),3 , 4*ds);
			shapeRenderer.end();}
	}

	private void runningDrawMenu() {
		if(myWorld.getGameTime()>5){
			// draw ship Life
			AssetLoader.font.setScale((float)0.15,(float)0.15);;
			AssetLoader.font.draw(batcher,levelWorld.getLevelDescription(), 150, 20);
			if(myWorld.isPaused())
				AssetLoader.font.draw(batcher, "EXIT", 10, 370);
			else
				AssetLoader.font.draw(batcher, "PAUSE", 10, 370);
			AssetLoader.font.draw(batcher, "$"+shipWorld.getCredits(), 10, 350);

			AssetLoader.font.draw(batcher, "TIME: "+(int)myWorld.getGameTime(), 10, 20);
			if(levelWorld.getKillQuota()>0)
				AssetLoader.font.draw(batcher, "ALIENS DESTROYED: "+myWorld.getEnemyKills()+"/"+levelWorld.getKillQuota(), 10, 40);
			else
				AssetLoader.font.draw(batcher, "ALIENS DESTROYED: "+myWorld.getEnemyKills(), 10, 40);
			AssetLoader.font.setScale((float).30,(float) 0.30);
			AssetLoader.font.draw(batcher, myWorld.getMsgBanner(), 40, 230);
			AssetLoader.font.setScale((float)0.20,(float)0.20);
			nukeSprite.setPosition(220, 350);
			nukeSprite.draw(batcher);
			if(!myWorld.nukeIsReady())
				AssetLoader.font.draw(batcher, String.valueOf((int)myWorld.getNukeCountDown()),227, 374);
			//runningDrawLifeBars();
		}
	}

	private void drawMsgBanner(){
		AssetLoader.font.setColor(1, 0, 0, 1);
		AssetLoader.font.draw(batcher, myWorld.getMsgBanner(), 40, 230);
		AssetLoader.font.setColor(1, 1, 1, 1);
	}

	private void buildShipDrawMsgBanner(){
		AssetLoader.font.setColor(1, 0, 0, 1);
		AssetLoader.font.draw(batcher, shipWorld.getWarningMsg(), 40, 180);
		AssetLoader.font.setColor(1, 1, 1, 1);
	}

	// draw the squares that will become the ship
	private void buildShipDrawBuildSquares() {

		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 15; j++) {
				if(shipWorld.getBlock(i,j).equals( "empty")){
					blankSprite.setPosition(i*18+20, j*18+20);
					blankSprite.setSize(12,12);}
				//blankSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "block")){
					sprite.setPosition(i*18+20, j*18+20);
					sprite.setSize(12,12);
					sprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "launcher")){
					launcherSprite.setPosition(i*18+20, j*18+20);
					launcherSprite.setSize(12,12);
					launcherSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "armor")){
					armorSprite.setPosition(i*18+20, j*18+20);
					armorSprite.setSize(12,12);
					armorSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "thruster")){
					thrusterSprite.setPosition(i*18+20, j*18+20);
					thrusterSprite.setSize(12,12);
					thrusterSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "gattling")){
					gattlingSprite.setPosition(i*18+20, j*18+20);
					gattlingSprite.setSize(12,12);
					gattlingSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "gauss")){
					gaussSprite.setPosition(i*18+20, j*18+20);
					gaussSprite.setSize(12,12);
					gaussSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "shield")){
					shieldSprite.setPosition(i*18+20, j*18+20);
					shieldSprite.setSize(12,12);
					shieldSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "mine")){
					mineSprite.setPosition(i*18+20, j*18+20);
					mineSprite.setSize(12,12);
					mineSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "carrier")){
					carrierSprite.setPosition(i*18+20, j*18+20);
					carrierSprite.setSize(12,12);
					carrierSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "ray")){
					rayGunSprite.setPosition(i*18+20, j*18+20);
					rayGunSprite.setSize(12,12);
					rayGunSprite.draw(batcher);}
				if(shipWorld.getBlock(i,j).equals( "disabler")){
					disablerSprite.setPosition(i*18+20, j*18+20);
					disablerSprite.setSize(12,12);
					disablerSprite.draw(batcher);}
			}
		}
	}

	private void buildShipDrawMenu() {
		buttonFrameSprite.setSize(128, 26);//THREE MAIN MENU FRAMES
		buttonFrameSprite.setPosition(140,331);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setPosition(140,301);
		buttonFrameSprite.draw(batcher);
		buttonFrameSprite.setPosition(140,271);
		buttonFrameSprite.draw(batcher);

		buttonFrameSprite.setSize(100, 18);//RESET BUTTON FRAME
		buttonFrameSprite.setPosition(168,0);
		buttonFrameSprite.draw(batcher);

		buttonFrameSprite.setSize(128, 90);//PLAYER INFO FRAME
		buttonFrameSprite.setPosition(2,271);
		buttonFrameSprite.draw(batcher);

		AssetLoader.font.draw(batcher, "START GAME", 155, 350);
		AssetLoader.font.draw(batcher, "TUTORIAL", 155, 320);
		AssetLoader.font.draw(batcher, "ADD A BRICK", 155, 290);
		AssetLoader.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);

		AssetLoader.font.setColor(1f, 0f, 0.0f, 1.0f);
		AssetLoader.font.draw(batcher, "RESET GAME", 175, 15);
		AssetLoader.font.setColor(1f, 1f, 1f, 1.0f);

		//deisplay position of last click for debugging
		//AssetLoader.font.draw(batcher, "LC "+myWorld.getScreenX()+" "+myWorld.getScreenY(), 160, 230);
		AssetLoader.font.draw(batcher, "PLAYER LEVEL: "+levelWorld.getCurrentStage(), 13, 350);

		AssetLoader.font.setScale((float)0.3,(float)0.3);
		AssetLoader.font.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		AssetLoader.font.draw(batcher, "** CLUSTERSHIP **", 30, 390);
		AssetLoader.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		AssetLoader.font.setScale((float)0.20,(float)0.20);
		AssetLoader.font.draw(batcher, "LIFE: "+String.valueOf(ship.getLife()), 90, 15);
		AssetLoader.font.draw(batcher, "SHIELD: "+String.valueOf((int)ship.getMaxShield()), 10, 15);

		AssetLoader.font.setScale((float)0.12,(float)0.12);
		AssetLoader.font.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		AssetLoader.font.draw(batcher, shipWorld.getMsgBanner(), 9, 290);
		AssetLoader.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		AssetLoader.font.draw(batcher,"$"+shipWorld.getCredits(), 9, 330);
		AssetLoader.font.draw(batcher,"BRICKS LEFT: "+ shipWorld.getBlocksLeft(), 9, 320);
		AssetLoader.font.setScale((float)0.20,(float)0.20);

		AssetLoader.font.draw(batcher, myWorld.getMsgBanner(), 40, 215);

	}





	private void buildLevelDrawBoundary(){

		shapeRenderer.setColor(1, 1, 0, 1);
		shapeRenderer.line(50, 0, 50, 2000);

	}


}
