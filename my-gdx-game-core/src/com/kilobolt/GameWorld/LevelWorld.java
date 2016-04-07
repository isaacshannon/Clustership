//Copyright Isaac Shannon, All Rights reserved, 2014
package com.kilobolt.GameWorld;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import GameObjects.Block;
import GameObjects.Launcher;
import GameObjects.Thruster;
import GameObjects.Ship;
import GameObjects.Enemy;
import GameObjects.Rock;
import Helpers.AssetLoader;
import Helpers.LevelGenerator;
import Helpers.LevelLoader;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.GameWorld.GameWorld.GameState;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.files.*;

public class LevelWorld {
	private GameWorld gameWorld;
	private int stage;
	private LevelGenerator lvlGen;
	private int spawnDelay;
	private int killQuota;
	private int timeQuota;
	private String levelDescription;
	private int spawnNumber;
	private double[] diffConstant;
	private ArrayList<Integer> lastLevels = new ArrayList<Integer>();
	private int lastLevel;


	public enum ActionType {
		ENEMY, ROCK, ERASE, OBJECTIVE,DESTROCK
	}


	public LevelWorld(GameWorld gameWorld,float screenWidth,float screenHeight){
		stage=1; // Current Game Level
		this.gameWorld = gameWorld; //link to the current game objects, mostly for ship
		gameWorld.setLevelWorld(this);
		diffConstant=new double[]{1,1,1,1,1};
		lastLevel=1;
		loadLevel();//load a level
	}

	public void onClick(float screenX, float screenY) {
		return;
	}
	
	public void setLastLevel(int lvl){
		if(lastLevels.size()>2)
			lastLevels.remove(0);
		lastLevels.add(lvl);
		lastLevel=lvl;
	}

	public void incrementLevel(){
			stage++;
	}

	public void decrementLevel(){
		stage--;
		if(stage<1)
			stage=1;
	}
	
	public void setCurrentStage(int lvl){
		stage=lvl;
		loadLevel();
	}
	
	public Enemy spawnEnemy(){
		return lvlGen.spawnEnemy();
	}

	//uses a random map
	public void loadLevel(){
		diffConstant=gameWorld.getDiffConstant();
		LevelGenerator mapGen = new LevelGenerator(this,stage,diffConstant);
		lvlGen=mapGen;
		levelDescription=mapGen.getLevelDescription();
		spawnDelay = mapGen.getSpawnDelay();
		timeQuota = mapGen.getTimeQuota();
		killQuota = mapGen.getKillQuota();
		spawnNumber=mapGen.getSpawnNumber();
		gameWorld.setEnemies(mapGen.getEnemyArray());
		gameWorld.setRocks(mapGen.getRockArray());
		gameWorld.setBossFight(mapGen.isBoss());
	}

	public String getLevelDescription(){
		return levelDescription;
	}
	
	public ArrayList<Integer> getLastLevels(){
		return lastLevels;
	}
	
	public int getLastLevel(){
		return lastLevels.get(lastLevels.size()-1);
	}
	public int getCurrentStage() {
		return stage;
	}
	
	public int getSpawnNumber(){
		return spawnNumber;
	}
	
	public int getSpawnDelay(){
		return spawnDelay;
	}
	
	public int getTimeQuota(){
		return timeQuota;
	}
	
	public int getKillQuota(){
		return killQuota;
	}

	

	


}
