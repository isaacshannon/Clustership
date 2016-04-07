//Copyright Isaac Shannon, All Rights reserved, 2014
package com.kilobolt.GameWorld;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.*;

import GameObjects.Block;
import GameObjects.Enemy;
import GameObjects.Launcher;
import GameObjects.Thruster;
import GameObjects.Ship;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.GameWorld.GameWorld.GameState;
import com.kilobolt.GameWorld.LevelWorld;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ShipWorld {
	private String[][] blocks; 
	private BlockType currentType;
	private ArrayList<Launcher> launcherArray;
	private ArrayList<Block> blockArray;
	private ArrayList<Thruster> thrusterArray;
	private Ship ship;
	private GameWorld gameWorld;
	private LevelWorld levelWorld;

	private float screenWidth,screenHeight;
	static com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("My_state");
	private String msgBanner;
	private int blocksLeft;
	private float touchWait;
	private int maxStage;
	private int maxNumberBlocks;
	private int credits;
	private boolean askForReset;
	private String warningMsg;

	private float gattlingX;
	private float launcherX;
	private float gaussX;
	private float mineX;
	private float shieldX;
	private float disablerX;
	private float carrierX;

	public int gattlingLevel;
	public int launcherLevel;
	public int gaussLevel;
	public int shieldLevel;
	public int disablerLevel;
	public int carrierLevel;
	public int mineLevel;
	private int boughtBricks;

	private double diffConstant[];

	final int gattlingUpgrade = 300;
	final int launcherUpgrade = 300;
	final int shieldUpgrade = 500;
	final int gaussUpgrade = 500;
	final int mineUpgrade = 700;
	final int disablerUpgrade = 700;
	final int carrierUpgrade = 700;
	final int additionalBrick = 1000;
	final int shipWidth = 13;

	private boolean[][] connectedBlocks = new boolean[shipWidth][20];

	public enum BlockType {
		BLOCK,LAUNCHER,ARMOR,GATTLING,GAUSS,SHIELD,MINE,DISABLER,CARRIER
	}

	//public BuildWorld(GameWorld gameWorld){
	public ShipWorld(GameWorld world,LevelWorld levelWorld,float screenWidth,float screenHeight){
		launcherArray = new ArrayList<Launcher>();
		blockArray = new ArrayList<Block>();
		thrusterArray = new ArrayList<Thruster>();
		msgBanner = "BUILD YOUR SHIP!";
		blocksLeft=0;
		touchWait=0;
		maxStage=1;
		maxNumberBlocks=5;
		credits = 0;

		launcherX=1;
		gaussX=1;
		shieldX=1;
		gattlingX=1;
		mineX=1;
		carrierX=1;
		disablerX=1;

		gattlingLevel=1;
		launcherLevel=1;
		gaussLevel=1;
		shieldLevel=1;
		carrierLevel=1;
		disablerLevel=1;
		mineLevel=1;
		boughtBricks=0;

		diffConstant= new double[]{1,1,1,1,1,1};

		warningMsg="";

		askForReset=false;

		this.gameWorld = world;
		this.levelWorld = levelWorld;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;

		gameWorld.setShipWorld(this);

		blocks = new String[shipWidth][15]; 

		currentType = BlockType.LAUNCHER;

		for(int i=0; i<blocks.length;i++)//set all the slots to empty at start
		{for(int j=0; j<blocks[0].length;j++)
		{blocks[i][j]="empty";}}


		loadShip();

		blocks[5][0] = "thruster";
		blocks[6][0] = "thruster";
		blocks[7][0] = "thruster";
		blocks[5][1] = "block";
		blocks[6][1] = "block";
		blocks[7][1] = "block";

	}


	//the actions to be taken onClick are
	public void onClick(float screenX, float screenY){
		//break the tutorial
		if(gameWorld.getCurrentState().equals(GameState.TUTORIAL)){
			gameWorld.setCurrentStateBuildShip();
			msgBanner="";
			gameWorld.setMsgBanner("");
		}

		if(touchWait==0){//prevent double taps!
			touchWait=(float) 0.01;
			gameWorld.getShip().calculateLifeAndShield(blockArray);

			//actions for the main ship building screen
			if(gameWorld.getCurrentState().equals(GameState.SHIPWORLD)){
				clickOnMenu(screenX,screenY);
				saveShip();
				updateMsgBanner();
				addShipBlock(screenX,screenY);
			}
			else{
				//actions for the select block screen
				if(gameWorld.getCurrentState().equals(GameState.SHOP)){
					//exit shop
					if(screenX>0 && screenX<20 && screenY>180 && screenY<300){
						gameWorld.setCurrentStateBuildShip();
					}
					setBlockType(screenX,screenY);
				}

			}
		}
		saveShip();

	}

	public String getBlock(int i, int j) {
		return blocks[i][j];
	}

	public Ship getShip() {
		return ship;
	}

	public String getBlockType() {
		switch (currentType) {
		case BLOCK:
			return"block";
		case ARMOR:
			return "armor";
		case LAUNCHER:
			return"launcher";
		case GATTLING:
			return"gattling";
		case GAUSS:
			return"gauss";
		case SHIELD:
			return"shield";
		case MINE:
			return"mine";
		case DISABLER:
			return"disabler";
		case CARRIER:
			return"carrier";
		default:
			return "block";
		}
	}

	public GameWorld getWorld() {
		return gameWorld;
	}

	public String getMsgBanner(){
		return msgBanner;
	}

	public int getMaxBlocks(){

		return maxNumberBlocks;
	}

	public void incrementMaxBlocks(){
		maxNumberBlocks++;
	}

	public int getMaxStage(){
		return maxStage;
	}

	public void incrementMaxStage(){
		maxStage++;
	}

	public int getBlocksLeft(){
		blocksLeft=maxNumberBlocks-getNumberOfBlocks()+boughtBricks;
		//if(blocksLeft<0)
		//blocksLeft=0;
		return blocksLeft;
	}

	public void updateTouch(float delta){
		//System.out.println("Updating Touch"+touchWait);

		if(touchWait > 0)
			touchWait+=delta;
		if(touchWait>0.3)
			touchWait=0;
	}

	//save the ship to a file
	public void saveShip(){
		//prefs.clear();
		for(int i=0;i<shipWidth;i++){
			for(int j=0;j<15;j++){
				String index1 =String.valueOf(i);
				String index2;
				if(j>9)
					index2 = String.valueOf(j);
				else
					index2 = "0" + String.valueOf(j); //add a leading zero to make all strings of the same length


				String key = index1+index2;
				String value = blocks[i][j];
				prefs.putString(key, value);

			}
		}
		for(int i=0;i<diffConstant.length;i++){
			prefs.putFloat("diffConstant"+i, (float)diffConstant[i]);
		}

		prefs.putInteger("boughtBricks", boughtBricks);
		prefs.putInteger("launcherLvl", launcherLevel);
		prefs.putInteger("gattlingLvl", gattlingLevel);
		prefs.putInteger("shieldLvl", shieldLevel);
		prefs.putInteger("gaussLvl", gaussLevel);
		prefs.putInteger("mineLvl", mineLevel);
		prefs.putInteger("disablerLvl", disablerLevel);
		prefs.putInteger("carrierLvl", carrierLevel);
		prefs.putInteger("maxNumberBlocks",maxNumberBlocks);
		prefs.putInteger("maxStage", maxStage);
		prefs.putInteger("credits", credits);

		//for testing purposes, save a level to the prefs of the phone
		//prefs.putInteger("maxStage", 37);
		//prefs.putInteger("credits", 10000000);

		prefs.flush();
	}

	public void addCredits(Enemy enemy){
		int dc = 0;
		int currentStage = levelWorld.getCurrentStage();

		//calculate how many credits the enemy is worth
		dc = (int) ((int)5*currentStage);

		//add the credits to the total
		credits += dc;

		//set the value of the enemy for display
		enemy.setValue(dc);
	}

	public void addCredits(int creds){
		credits+=creds;
	}

	public int getCredits(){
		return credits;
	}

	public void updateMultipliers(){
		gattlingX=1+(float)gattlingLevel/5;
		launcherX=1+(float)launcherLevel/5;
		shieldX=1+(float)shieldLevel/5;
		gaussX=1+(float)gaussLevel/5;
		disablerX=1+(float)disablerLevel/5;
		carrierX=1+(float)carrierLevel/5;
		mineX=1+(float)mineLevel/5;
		//System.out.println("Updated shieldX:"+shieldX);
	}

	public String getGattlingDmg(){
		return String.valueOf((int) (80*gattlingX));}
	public String getLauncherDmg(){
		return String.valueOf((int) (200*launcherX));}
	public String getGaussDmg(){
		return String.valueOf((int) (700*gaussX));}
	public String getMineDmg(){
		return String.valueOf((int) (1600*mineX));}
	public String getDisablerDmg(){
		return String.valueOf((int) (13.0*disablerX));}
	public String getCarrierDmg(){
		return String.valueOf((int) (60*carrierX));}

	/**
	 * get the number of blocks that compose the ship
	 */
	private int getNumberOfBlocks(){
		int count = 0;

		for(int i=0; i<blocks.length;i++)//set all the slots to empty
		{
			for(int j=0; j<blocks[0].length;j++)
			{
				if(blocks[i][j].equals("launcher")||blocks[i][j].equals("gattling")||
						blocks[i][j].equals("shield")||blocks[i][j].equals("gauss")||
						blocks[i][j].equals("disabler")||blocks[i][j].equals("carrier")||
						blocks[i][j].equals("ray")||blocks[i][j].equals("gauss")||
						blocks[i][j].equals("mine"))
					count++;
			}
		}

		return count;
	}

	private void updateMsgBanner(){
		msgBanner = "VALID SHIP CONFIGURATION";
		if(getNumberOfBlocks()>maxNumberBlocks+boughtBricks)
			msgBanner = "TOO MANY BLOCKS ON SHIP";
		if(!allBlocksConnected())
			msgBanner = "UNCONNECTED BLOCKS";

	}

	private void loadShip(){
		String blockType = "";
		 blocks = new String[shipWidth][15];
		//reset the shiBlocks before loading a new ship
		for(int i=0; i<blocks.length;i++)//set all the slots to empty
		{
			for(int j=0; j<blocks[0].length;j++)
			{
				blocks[i][j]=null;
				blocks[i][j]=new String("empty");
			}
		}

		//load a ship from libgdx preferences "prefs"
		for(int i=0;i<shipWidth;i++){
			for(int j=0;j<15;j++){
				String index1=String.valueOf(i);

				String index2 = "";
				if(j>9) //add a leading zero to make all index2 strings of the same length
					index2 = String.valueOf(j);
				else
					index2 = "0" + String.valueOf(j); 

				//get the value(string) of the prefs key
				blockType = prefs.getString(index1+index2);
				//System.out.println(i+";"+j+";"+blockType);

				//set the block to the proper type
				blocks[i][j]=(String)blockType;
				//System.out.println(blocks[i][j]);
			}
		}

		//load the player's max Level
		if(prefs.contains("maxStage")){
			maxStage=prefs.getInteger("maxStage");}
		else{
			gattlingLevel=1;
			launcherLevel=1;
			shieldLevel=1;
			//System.out.println("No saved maxStage");
			maxStage=1;}

		//load the player's max number of blocks
		if(prefs.contains("maxNumberBlocks")){
			maxNumberBlocks=prefs.getInteger("maxNumberBlocks");}
		else{
			maxNumberBlocks=3;}

		//load the player's max number of blocks
		if(prefs.contains("credits")){
			credits=prefs.getInteger("credits");}
		else{
			credits=0;}

		//make sure the player can add at least two weapons
		if(maxNumberBlocks<3)
			maxNumberBlocks=3;

		//load the player's xp, convert from legacy
		//if(prefs.contains("xp")){
		//maxStage = prefs.getInteger("xp")/1000;
		//maxNumberBlocks = maxStage+5;
		//credits=prefs.getInteger("xp")*2;
		//}

		for(int i=0;i<diffConstant.length;i++){
			if(prefs.contains("diffConstant"+i)){
				diffConstant[i] = prefs.getFloat("diffConstant"+i);
			}
			else
				diffConstant[i]=1;
		}


		if(prefs.contains("gattlingLvl")){
			gattlingLevel = prefs.getInteger("gattlingLvl");}
		else
			gattlingLevel=1;
		if(prefs.contains("launcherLvl")){
			launcherLevel = prefs.getInteger("launcherLvl");}
		else
			launcherLevel=1;
		if(prefs.contains("gaussLvl")){
			gaussLevel = prefs.getInteger("gaussLvl");}
		else
			gaussLevel=0;
		if(prefs.contains("shieldLvl")){
			shieldLevel = prefs.getInteger("shieldLvl");}
		else
			shieldLevel=1;
		if(prefs.contains("mineLvl")){
			mineLevel = prefs.getInteger("mineLvl");}
		else
			mineLevel=0;
		if(prefs.contains("carrierLvl")){
			carrierLevel = prefs.getInteger("carrierLvl");}
		else
			carrierLevel=0;
		if(prefs.contains("disablerLvl")){
			disablerLevel = prefs.getInteger("disablerLvl");}
		else
			disablerLevel=0;
		if(prefs.contains("boughtBricks")){
			boughtBricks = prefs.getInteger("boughtBricks");}
		else
			boughtBricks=0;

		//maxStage=14;
		//credits=100000;
		/*gattlingLevel=1;
		launcherLevel=1;
		shieldLevel=1;
		gaussLevel=0;
		mineLevel=0;
		disablerLevel=0;
		carrierLevel=0;

		credits=100000;
		maxNumberBlocks=6;
		maxStage=1;*/
		updateMultipliers();




		//Best starting combo for level 1 player
		if(maxNumberBlocks==3){
			blocks[5][2]="gattling";
			blocks[6][2]="shield";
			blocks[7][2]="launcher";
			blocks[5][1]="block";
			blocks[6][1]="block";
			blocks[7][1]="block";
			blocks[5][0]="thruster";
			blocks[6][0]="thruster";
			blocks[7][0]="thruster";}

		levelWorld.setCurrentStage(maxStage);
		levelWorld.loadLevel();
		exportShip();
	}

	private void exportShip() {
		//empty the old arrays before loading the ship
		while(launcherArray.size()>0)
			launcherArray.remove(launcherArray.size()-1);
		while(blockArray.size()>0)
			blockArray.remove(blockArray.size()-1);
		while(thrusterArray.size()>0)
			thrusterArray.remove(thrusterArray.size()-1);
		ship=null;

		//export the ship into the arrays, then create a ship
		int midShip = findMidShip();

		if(true){
			for(int i=0; i<blocks.length;i++)
			{
				for(int j=0; j<blocks[0].length;j++)
				{
					if(blocks[i][j].equals( "disabler"))
						launcherArray.add(new Launcher((j-midShip)*-4,(i-6)*4,"disabler",disablerX));
					if(blocks[i][j].equals( "carrier"))
						launcherArray.add(new Launcher((j-midShip)*-4,(i-6)*4,"carrier",carrierX));
					if(blocks[i][j].equals( "gattling"))
						launcherArray.add(new Launcher((j-midShip)*-4,(i-6)*4,"gattling",gattlingX));
					if(blocks[i][j].equals( "launcher"))
						launcherArray.add(new Launcher((j-midShip)*-4,(i-6)*4,"missile",launcherX));
					if(blocks[i][j].equals("gauss"))
						launcherArray.add(new Launcher((j-midShip)*-4,(i-6)*4,"gauss",gaussX));
					if(blocks[i][j].equals("mine"))
						launcherArray.add(new Launcher((j-midShip)*-4,(i-6)*4,"mine",mineX));
					if(blocks[i][j].equals( "block"))
						blockArray.add(new Block((j-midShip)*-4,(i-6)*4));
					if(blocks[i][j].equals("thruster"))
						thrusterArray.add(new Thruster((j-midShip)*-4,(i-6)*4));
					if(blocks[i][j].equals("shield"))
						blockArray.add(new Block((j-midShip)*-4,(i-6)*4,"shield"));

				}
			}
			ship = new Ship(137,204,blockArray,launcherArray,thrusterArray);
			ship.setShipLength(midShip);
			gameWorld.setShip(ship);
			gameWorld.getShip().setShieldX(shieldX);
			gameWorld.getShip().calculateLifeAndShield(blockArray);
			saveShip();
		}
	}

	private void loadLevel(){
		levelWorld.loadLevel();
	}

	private int findMidShip(){
		int length = 0;
		int maxLength=0;
		for(int i=0;i<blocks.length;i++){
			for(int j=0;j<blocks[0].length;j++){
				if(!blockIsEmpty(i,j))
					length++;
			}
			if(length>maxLength)
				maxLength=length;
			length=0;
		}

		for(int k=0;k<(blocks[0].length);k++){
			for(int l=0;l<blocks.length;l++){
				if(!blockIsEmpty(l,k))
					length++;
			}
			if(length>maxLength)
				maxLength=length;
			length=0;
		}

		if(ship!=null)
			gameWorld.getShip().setShipLength(maxLength);
		return (int)maxLength/2;
	}

	//check that all the blocks are connected
	private boolean allBlocksConnected(){
		//reset all the connected blocks
		//printBlocks();
		for(int i=0;i<blocks.length;i++){
			for(int j=0;j<blocks[0].length;j++){
				connectedBlocks[i][j]=false;
			}
		}

		//block 3,0 is the middle thruster
		connectedBlocks[8][0] = true;
		//flood the blocks
		floodBlocks(8,0);

		//check that all the non-empty blocks are connected
		for(int i=0;i<blocks.length;i++){
			for(int j=0;j<blocks[0].length;j++){
				if(!connectedBlocks[i][j] && !blockIsEmpty(i,j))
					return false;
			}
		}

		return true;
	}

	private void printBlocks(){
		System.out.println("Printing Blocks:");
		for(int i=0;i<blocks.length;i++){
			for(int j=0;j<blocks[0].length;j++){
				System.out.println(i+";"+j+": "+blocks[i][j]+": "+ connectedBlocks[i][j]);
			}
		}
	}

	//flood the blocks before checking if they are connected
	private void floodBlocks(int x, int y){
		//check that the index isn't out of range
		int minX=x-1;
		minX = (minX<0)?0:minX;
		int maxX=x+1;
		maxX = (maxX>12)?12:maxX;
		int minY=y-1;
		minY = (minY<0)?0:minY;
		int maxY=y+1;
		maxY = (maxY>14)?14:maxY;

		//check around the block for connected blocks, if found this block is connected
		connectedBlocks[x][y] = (connectedBlocks[minX][y] ||connectedBlocks[maxX][y]||
				connectedBlocks[x][minY]||connectedBlocks[x][maxY]);

		//flood the surrounding blocks
		if(!connectedBlocks[minX][y] && !blockIsEmpty(minX,y))
			floodBlocks(minX,y);
		//return;
		if(!connectedBlocks[maxX][y] && !blockIsEmpty(maxX,y))
			floodBlocks(maxX,y);
		//return;
		if(!connectedBlocks[x][minY] && !blockIsEmpty(x,minY))
			floodBlocks(x,minY);
		//return;
		if(!connectedBlocks[x][maxY] && !blockIsEmpty(x,maxY))
			floodBlocks(x,maxY);
		//return;
	}

	private void addShipBlock(float screenX, float screenY){

		int posX = (int)((screenX-10)/8.57);
		int posY = (int)((screenY-64)/9.23) ;

		if(posX>12)
			posX = 12;
		posY = 13-posY;
		if(posY>13)
			posY =13;
		if(posX<0)
			posX =0;
		if(posY<1)
			posY =1;


		if(screenX>10 && screenX<200&& screenY>65 && screenY<373){
			//protect the six starting blocks
			if(posX==5&&posY==1||posX==6&&posY==1||posX==7&&posY==1
					||posX==5&&posY==0||posX==6&&posY==0||posX==7&&posY==0){
				blocks[posX][posY] = blocks[posX][posY];
			}
			else{
				//add an infinite number of grey blocks
				if(blockIsEmpty(posX,posY)&&!allAdjacentBlocksEmpty(posX,posY)&&
						getBlockType().equals("block")){
					blocks[posX][posY] = getBlockType();
				}
				//limit the number of weapon blocks
				else{
					if(blockIsEmpty(posX,posY)&&!allAdjacentBlocksEmpty(posX,posY)&&
							getNumberOfBlocks()<maxNumberBlocks+boughtBricks){
						//System.out.println("no adjacent blocks");
						blocks[posX][posY] = getBlockType();}	
					else{
						blocks[posX][posY] = "empty";
					}
				}
			}
		}

		exportShip();
		gameWorld.getShip().calculateLifeAndShield(blockArray);
	}

	//check that the block x,y is empty
	//method created in response to android glitching when loading blocktypes from prefs
	private boolean blockIsEmpty(int x,int y){
		String blockType = blocks[x][y];

		if((blockType.equals("thruster")||blockType.equals("gauss")
				||blockType.equals("gattling")||blockType.equals("block")
				||blockType.equals("shield")||blockType.equals("mine")
				||blockType.equals("disabler")||blockType.equals("carrier")
				||blockType.equals("ray")||blockType.equals("mine")
				||blockType.equals("launcher")))
			return false;
		else
			return true;
	}


	//check that all adjacent blocks are empty
	private boolean allAdjacentBlocksEmpty(int x,int y){
		//the coordinates to be used to check if there are blocks around the one being placed
		int maxX = x+1;
		int minX = x-1;
		int maxY = y+1;
		int minY = y-1;


		if(minX<0)
			minX = 0;
		if(minY<0)
			minY=0;
		if(maxX>12)
			maxX=12;
		if(maxY>15)
			maxY=15;

		if(!blockIsEmpty(minX,y)||!blockIsEmpty(maxX,y)
				||!blockIsEmpty(x,minY)||!blockIsEmpty(x,maxY))
			return false;
		else
			return true;
	}

	private void setBlockType(float screenX,float screenY){

		if(screenX>0 && screenX<50 && screenY>15 && screenY<35 && gattlingLevel>0){
			gameWorld.setCurrentStateBuildShip();
			currentType=BlockType.GATTLING;}
		if(screenX>64 && screenX<100 && screenY>15 && screenY<35 
				&& credits>gattlingUpgrade*(1+gattlingLevel)){
			credits-=gattlingUpgrade*(1+gattlingLevel);
			gattlingLevel++;}

		if(screenX>0 && screenX<50 && screenY>35 && screenY<55 && launcherLevel>0){
			gameWorld.setCurrentStateBuildShip();
			currentType=BlockType.LAUNCHER;}
		if(screenX>64 && screenX<100 && screenY>35 && screenY<55
				&&credits>launcherUpgrade*(1+launcherLevel)){
			credits-=launcherUpgrade*(1+launcherLevel);
			launcherLevel++;}

		if(screenX>0 && screenX<50 && screenY>55 && screenY<75 && shieldLevel>0){
			gameWorld.setCurrentStateBuildShip();
			currentType=BlockType.SHIELD;}
		if(screenX>64 && screenX<100 && screenY>55 && screenY<75 
				&& credits>shieldUpgrade*(1+shieldLevel)){
			credits-=shieldUpgrade*(1+shieldLevel);
			shieldLevel++;}

		if(screenX>0 && screenX<50 && screenY>75 && screenY<95 && gaussLevel>0){
			gameWorld.setCurrentStateBuildShip();
			currentType=BlockType.GAUSS;}
		if(screenX>64 && screenX<100 && screenY>75 && screenY<95 
				&& credits>gaussUpgrade*(1+gaussLevel)){
			credits-=gaussUpgrade*(1+gaussLevel);
			gaussLevel++;}

		if(screenX>0 && screenX<50 && screenY>95 && screenY<115 && mineLevel>0){
			gameWorld.setCurrentStateBuildShip();
			currentType=BlockType.MINE;}
		if(screenX>64 && screenX<100 && screenY>95 && screenY<115
				&& credits>mineUpgrade*(1+mineLevel)){
			credits-=mineUpgrade*(1+mineLevel);
			mineLevel++;}

		if(screenX>0 && screenX<50 && screenY>115 && screenY<135 && disablerLevel>0){
			gameWorld.setCurrentStateBuildShip();
			currentType=BlockType.DISABLER;}
		if(screenX>64 && screenX<100 && screenY>115 && screenY<135
				&& credits>disablerUpgrade*(1+disablerLevel)){
			credits-=disablerUpgrade*(1+disablerLevel);
			disablerLevel++;}

		if(screenX>0 && screenX<50 && screenY>135 && screenY<155 && carrierLevel>0){
			gameWorld.setCurrentStateBuildShip();
			currentType=BlockType.CARRIER;}
		if(screenX>64 && screenX<100 && screenY>135 && screenY<155 
				&& credits>carrierUpgrade*(1+carrierLevel)){
			credits-=carrierUpgrade*(1+carrierLevel);
			carrierLevel++;}

		if(screenX>0 && screenX<50 && screenY>155 && screenY<175){
			gameWorld.setCurrentStateBuildShip();
			currentType=BlockType.BLOCK;}

		if(screenX>0 && screenX<100 && screenY>175 && screenY<200
				&& credits>additionalBrick*(boughtBricks+1)){
			credits-=additionalBrick*(boughtBricks+1);
			boughtBricks++;}

		updateMultipliers();
	}

	public String getWarningMsg(){
		return warningMsg;
	}

	private void clickOnMenu(float screenX,float screenY){
		//click on start the game
		if(screenX>75 && screenX<130 && screenY>25 && screenY<40){
			loadLevel();
			//check that all the blocks are connected and max number of blocks
			if(allBlocksConnected()&&getNumberOfBlocks()<=maxNumberBlocks+boughtBricks)
				gameWorld.setCurrentStateRunning();
			exportShip();}

		//click on choose block
		if(screenX>75 && screenX<130 && screenY>40 && screenY<55){
			gameWorld.setCurrentStateTutorial();
		}

		//click on choose block
		if(screenX>75 && screenX<130 && screenY>55 && screenY<70){
			gameWorld.setCurrentStateShop();
		}

		//click on reset ship
		if(screenX>75 && screenX<130 && screenY>190 && screenY<200){
			if(!askForReset){
				warningMsg="TAP AGAIN TO RESET GAME";
				askForReset=true;}
			else{
				askForReset=false;
				resetShip();
				warningMsg="";}
		}
		else
			//if user does not tap the reset a second time, unprime the reset
			if(askForReset){
				askForReset=false;
				warningMsg="";
			}
	}


	private void resetShip() {
		prefs.clear();
		loadShip();
		launcherLevel=1;
		gattlingLevel=1;
		shieldLevel=1;
		gaussLevel=0;
		mineLevel=0;
		disablerLevel=0;
		carrierLevel=0;
		maxNumberBlocks=3;
		boughtBricks=0;
		maxStage=1;
		credits=0;
		saveShip();
		loadShip();
		levelWorld.loadLevel();

	}


	public String getBrickCost() {
		return String.valueOf(additionalBrick*(boughtBricks+1));
	}


	public void decrementDiffConstant(int x) {
		diffConstant[x]-=0.20;
	}


	public void incrementDiffConstant(int x) {
		diffConstant[x]+=1;
	}


	public double[] getDiffConstant() {
		return diffConstant;
	}


}
