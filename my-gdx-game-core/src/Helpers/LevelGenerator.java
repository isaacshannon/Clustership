//Copyright Isaac Shannon, All Rights reserved, 2014
package Helpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import GameObjects.Block;
import GameObjects.Launcher;
import GameObjects.Thruster;
import GameObjects.Ship;
import GameObjects.Enemy;
import GameObjects.Rock;


import com.badlogic.gdx.math.Vector2;
import com.kilobolt.GameWorld.LevelWorld;



public class LevelGenerator {
	private ArrayList<Enemy> enemyArray;
	private ArrayList<Rock> rockArray;
	private float attack;
	private float rate;
	private float life;
	private float los;
	private float missileLife;
	private float baseAttack;
	private float baseLife;
	private float baseMissileLife;
	private float playerLevel;
	private int enemyQuota;
	private int rockQuota;
	private String[][] elems;
	private int levelType;
	private int x0;
	private int x1;
	private int y0;
	private int y1;
	private int enemyDensity;
	private float baseRockProb;
	private float connectRockProb;
	private boolean bossFight;
	private String enemyType;
	private int spawnDelay;
	private String levelDescription;
	private int killQuota;
	private int timeQuota;
	private int spawnNumber;
	private double diffConstant;


	public LevelGenerator(LevelWorld lvlWorld,int playerLevel,double diffConstant[]){
		ArrayList<Integer> lastLevels = lvlWorld.getLastLevels();
		boolean isNewLevel = false;

		//System.out.println("Generating map: lvl"+playerLevel);
		this.playerLevel = playerLevel;
		
		//choose the type of level
		levelType = (int)(Math.random()*6-0.01);
		
		//random type of the level
		while(!isNewLevel){
			isNewLevel=true;
			levelType = (int)(Math.random()*6-0.01);
			for(int i=0;i<lastLevels.size();i++){
				if(lastLevels.get(i) == levelType)
					isNewLevel=false;
			}
		}
		if(levelType==3&&playerLevel<9)
			levelType=0;

		if(playerLevel==1)
			levelType=0;
		if(playerLevel==2)
			levelType=1;
		if(playerLevel==3)
			levelType=2;
		
		lvlWorld.setLastLevel(levelType);
		this.diffConstant=diffConstant[levelType];

		enemyType="ship";

		enemyArray = new ArrayList<Enemy>(); //list of enemies in the level
		rockArray = new ArrayList<Rock>(); //list of rocks in the level

		updateLevelValues();
		//string array to keep track of what element is at each coordinate
		elems = new String[x1][y1];
		generateElements();
		
		//for(int i=0;i<diffConstant.length;i++){
			//System.out.println("Difficulty X"+i+" "+diffConstant[i]);
		//}
		


	}

	private void updateLevelValues(){
		System.out.println("Current Diff:"+diffConstant);
		//normal enemy strength and number----------------------------------------------------------------
		//butterflies
		if(levelType==0){
			//map coordinates
			x0=5;
			x1=50;
			y0=5;
			y1=50;

			//max number of elements in the map
			rockQuota=400;
			enemyQuota=75;

			//base values to be using in generating enemies
			rate = 4;
			if(playerLevel<4)
				baseAttack=(float) 0.1;
			else
				baseAttack=(float) (1.5*diffConstant);
			//System.out.println("Base Attack:"+baseAttack);
			baseLife=(float) (30*diffConstant);
			baseMissileLife=(float) (1*diffConstant);
			los=300;
			enemyDensity=3;
			baseRockProb=(float) 0.95;
			connectRockProb=(float)0.7;
			bossFight=false;
			enemyType="0001";
			spawnDelay=300;
			levelDescription = "DESTROY THE ALIENS";
			killQuota=0;
			timeQuota=0;
			spawnNumber=1;
		}
		//strong enemies----------------------------------------------------------------
		//purple cyclop
		if(levelType==1){
			//map coordinates
			x0=5;
			x1=50;
			y0=5;
			y1=50;

			//max number of elements in the map
			rockQuota=400;
			enemyQuota=20;

			//base values to be using in generating enemies
			rate = 5;

			if(playerLevel<4)
				baseAttack=(float) 0.1;
			else
				baseAttack=(float) (4*diffConstant);

			baseLife=(float) (120*diffConstant);
			baseMissileLife=(float) (3*diffConstant);
			los=300;
			enemyDensity=3;
			baseRockProb=(float) 0.95;
			connectRockProb=(float)0.7;
			bossFight=false;
			enemyType="0002";
			spawnDelay=3000;
			levelDescription = "DESTROY THE ALIENS";
			killQuota=0;
			timeQuota=0;
			spawnNumber=1;
		}
		//enemy swarm----------------------------------------------------------------
		//tie-fighters
		if(levelType==2){
			//map coordinates
			x0=5;
			x1=20;
			y0=5;
			y1=20;

			//max number of elements in the map
			rockQuota=100;
			enemyQuota=20;

			//base values to be using in generating enemies
			rate = 7;
			if(playerLevel<4)
				baseAttack=(float) 0.1;
			else
				baseAttack=(float) (1*diffConstant);
			baseLife=(float) (15*diffConstant);
			baseMissileLife=(float) (2*diffConstant);
			los=300;
			enemyDensity=1;
			baseRockProb=(float) 0.95;
			connectRockProb=(float)0.7;
			bossFight=false;
			enemyType="0003";
			spawnDelay=7;
			levelDescription = "DESTROY 150 ALIENS";
			killQuota=149;
			timeQuota=0;
			spawnNumber=8;
		}
		//boss fight----------------------------------------------------------------
		//king skull
		if(levelType==3){
			//map coordinates
			x0=5;
			x1=20;
			y0=5;
			y1=20;

			//max number of elements in the map
			rockQuota=10;
			enemyQuota=1;

			//base values to be using in generating enemies
			rate = (float) 2.5;
			baseAttack=(float) (8*diffConstant);
			baseLife=(float) (2000*diffConstant);
			baseMissileLife=(float)(6*diffConstant);
			los=2000;
			enemyDensity=0;
			baseRockProb=(float) 0.4;
			connectRockProb=(float)0.4;
			bossFight=true;
			enemyType="0005";
			spawnDelay=1000;
			levelDescription = "DESTROY THE BOSS";
			killQuota=0;
			timeQuota=0;
			spawnNumber=0;

		}
		//strong enemy time survival----------------------------------------------------------------

		if(levelType==4){
			//map coordinates
			x0=5;
			x1=15;
			y0=5;
			y1=15;

			//max number of elements in the map
			rockQuota=400;
			enemyQuota=20;

			//base values to be using in generating enemies
			rate = 4;
			if(playerLevel<4)
				baseAttack=(float) 0.1;
			else
				baseAttack=(float) (2*diffConstant);
			//System.out.println("Base Attack:"+baseAttack);
			baseLife=(float) (60*diffConstant);
			baseMissileLife=(float) (2*diffConstant);
			los=300;
			enemyDensity=0;
			baseRockProb=(float) 0.75;
			connectRockProb=(float)0.99;
			bossFight=false;
			enemyType="0004";
			spawnDelay=5;
			levelDescription = "SURVIVE 180 SECONDS";
			killQuota=0;
			timeQuota=180;
			spawnNumber=2;
		}
		//----------------------------------------------------------------
		
		if(levelType==5){
			//map coordinates
			x0=5;
			x1=15;
			y0=5;
			y1=15;

			//max number of elements in the map
			rockQuota=400;
			enemyQuota=20;

			//base values to be using in generating enemies
			rate = 4;
			if(playerLevel<5)
				baseAttack=(float) 0.1;
			else
				baseAttack=(float) (3*diffConstant);
			//System.out.println("Base Attack:"+baseAttack);
			baseLife=(float) (180*diffConstant);
			baseMissileLife=(float) (2*diffConstant);
			los=300;
			enemyDensity=0;
			baseRockProb=(float)0.75;
			connectRockProb=(float)0.99;
			bossFight=false;
			enemyType="0006";
			spawnDelay=9;
			levelDescription = "SURVIVE 180 SECONDS";
			killQuota=0;
			timeQuota=180;
			spawnNumber=2;
		}

	}

	public int getSpawnNumber(){
		return spawnNumber;
	}

	public String getLevelDescription(){
		return levelDescription;
	}

	public int getTimeQuota(){
		return timeQuota;
	}

	public int getKillQuota(){
		return killQuota;
	}

	public int getSpawnDelay(){
		return spawnDelay;
	}

	public boolean isBoss(){
		return bossFight;
	}

	private void generateElements(){
		double rand = Math.random();
		double rockLimit = 0.9;

		//go through all the coordinates of the level to add either rocks, enemies or nothing
		for(int i=x0;i<x1;i++)
			for(int j=y0;j<y1;j++){
				elems[i][j]="empty";
				rand = Math.random();

				if(i>0&&j>0){
					if(elems[i-1][j-1] != null )
						if(elems[i-1][j-1].equals("rock")||elems[i][j-1].equals("rock"))
							rockLimit=connectRockProb;
						else
							rockLimit=baseRockProb;
				}

				if(rand>rockLimit && rockQuota>0){
					rockQuota--;
					elems[i][j]="rock";
					rockArray.add(new Rock(new Vector2(i*30,j*30)));}

				if(!rocksInRange(i,j,enemyDensity) && enemyQuota>0 && i>x0+6 && j>y0+6){
					enemyQuota--;
					addEnemy(i,j);
					//enemyArray.add(new Enemy(new Vector2(i*30,j*30)));
					elems[i][j]="enemy";}
			}
		//enemyArray.add(new Enemy(new Vector2(1000,1000)));
	}

	public Enemy spawnEnemy(){
		double rand = Math.random();
		if(rand>0&&rand<0.25)
			return new Enemy(new Vector2(-100,(float) (Math.random()*500)),rockArray,6000,rate,attack,life,missileLife,enemyType);
		if(rand>0.25&&rand<0.50)
			return new Enemy(new Vector2(2000,(float) (Math.random()*500)),rockArray,6000,rate,attack,life,missileLife,enemyType);
		if(rand>0.50&&rand<0.75)
			return new Enemy(new Vector2((float) (Math.random()*500),-1000),rockArray,6000,rate,attack,life,missileLife,enemyType);
		return new Enemy(new Vector2((float) (Math.random()*500),2000),rockArray,6000,rate,attack,life,missileLife,enemyType);
	}

	private void addEnemy(int x, int y){
		//enemyArray.add(new Enemy(new Vector2(x*30,y*30)));
		Vector2 pos = new Vector2(x*30,y*30);
		attack = (float) (baseAttack*(1+Math.random()));
		life = (float) (baseLife*(1+Math.random()));
		missileLife = (float) (baseMissileLife*(1+Math.random()));
		//System.out.println("LevelGen Attack:"+attack);
		enemyArray.add(new Enemy(pos,rockArray,los,rate,attack,life,missileLife,enemyType));
	}


	//calculates if there are any objects with the range
	private boolean rocksInRange(int x,int y,int range){
		int minX = x-range;
		int maxX = x+range;
		if(minX<0)
			minX=0;
		if(maxX>x1)
			maxX=x1;

		int minY= y-range;
		int maxY= y+range;
		if(minY<0)
			minY=0;
		if(maxY>y1)
			maxY=y1;

		for(int i=minX;i<maxX;i++)
			for(int j=minY;j<maxY;j++){
				if(elems[i][j]!=null){
					if(!elems[i][j].equals("empty"))
						return true;
				}
			}

		return false;
	}


	public ArrayList<Enemy> getEnemyArray(){
		return enemyArray;
	}

	public ArrayList<Rock> getRockArray(){
		return rockArray;
	}

	public int getLevelType(){
		return levelType;
	}





}
