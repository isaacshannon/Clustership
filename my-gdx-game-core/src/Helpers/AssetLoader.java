//Copyright Isaac Shannon, All Rights reserved, 2014
package Helpers;

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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import GameObjects.Block;
import GameObjects.Ship;
import GameObjects.Target;
import GameObjects.Launcher;
import GameObjects.Missile;
import GameObjects.Rock;
import GameObjects.Enemy;
import GameObjects.EnemyMissile;
import GameObjects.Thruster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

	public static Texture texture,enemyTexture,rockTexture,thrusterTexture,explosionTexture,
	shipExplosionTexture,cityTexture,destrockTexture,nukeTexture,background,droneTexture,pointerTexture,
	overlordTexture,enemyTexture2,enemyTexture3,background2,background3,rock2Texture,lockTexture,upgrade,
	addBlock,buttonFrame,rock3Texture;

	public static TextureRegion yellow,darkGreen,cyan,pink,brown,white,green,blue,
	red,lightGrey,mediumGrey,darkGrey,enemy1,enemy2,enemy3,enemy4,enemy5,enemy6,enemy7,enemy8,
	rock,thruster,explosion,fullRed,fullGreen,fullPink,nukeSymbol,nukeExplosion,
	shipExplosion,city,destrock,orangeCross,blackSpot,rainbow,drone,pointer,
	enemy21,enemy22,enemy23,enemy24,enemy25,enemy26,rock2,lock,unlock,
	enemy31,enemy32,enemy33,enemy34,enemy35,enemy36;
	
	public static String[] intros,replies;

	public static BitmapFont font, shadow;

	public static Sound enemyExplosion,gattlingShot,cannonShot,missileShot,shipImpact,nuke,enemyShot;
	
	public static Music musicBox1;
	
	public static Music[] tunes = new Music[10];

	public static void load() {

		texture = new Texture(Gdx.files.internal("data/gameSquares.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		fullGreen = new TextureRegion(texture, 8, 16, 12, 20);
		fullRed = 	new TextureRegion(texture, 4, 16, 8, 20);
		fullPink = 	new TextureRegion(texture, 0, 16, 4, 20);
		yellow = 	new TextureRegion(texture, 8, 12, 12, 16);
		white =		new TextureRegion(texture, 4, 12, 8, 16);
		cyan = 		new TextureRegion(texture, 0, 12, 4, 16);
		blue = 		new TextureRegion(texture, 8, 8, 12, 12);
		green = 	new TextureRegion(texture, 4, 8, 8, 12);
		red = 		new TextureRegion(texture, 0, 8, 4, 12);
		pink = 		new TextureRegion(texture, 8, 4, 12, 8);
		brown =		new TextureRegion(texture, 4, 4, 8, 8);
		mediumGrey =new TextureRegion(texture, 0, 4, 4, 8);
		darkGrey = 	new TextureRegion(texture, 8, 0, 12, 4);
		darkGreen = new TextureRegion(texture, 4, 0, 8, 4);
		lightGrey = new TextureRegion(texture, 0, 0, 4, 4);
		orangeCross = new TextureRegion(texture, 12, 0, 16, 4);
		blackSpot = new TextureRegion(texture, 12, 12, 16, 16);
		rainbow = new TextureRegion(texture, 12, 8, 16, 12);

		enemyTexture = new Texture(Gdx.files.internal("data/enemy.png"));
		enemyTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		enemy1 = new TextureRegion(enemyTexture, 0, 0, 16, 16);
		enemy2 = new TextureRegion(enemyTexture, 16, 16, 32, 32);
		enemy3 = new TextureRegion(enemyTexture, 0, 16, 16, 32);
		enemy4 = new TextureRegion(enemyTexture, 16, 0, 32, 16);
		enemy5 = new TextureRegion(enemyTexture, 32, 0, 48, 16);
		enemy6 = new TextureRegion(enemyTexture, 32, 16, 48, 32);
		enemy7 = new TextureRegion(enemyTexture, 48, 16, 64, 32);
		enemy8 = new TextureRegion(enemyTexture, 48, 0, 64, 16);
		
		enemyTexture2 = new Texture(Gdx.files.internal("data/enemySet2.png"));
		enemyTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		enemy21 = new TextureRegion(enemyTexture2, 0, 0, 16, 16);
		enemy22 = new TextureRegion(enemyTexture2, 16, 0, 32, 16);
		enemy23 = new TextureRegion(enemyTexture2, 32, 0, 48, 16);
		enemy24 = new TextureRegion(enemyTexture2, 48, 0, 64, 16);
		enemy25 = new TextureRegion(enemyTexture2, 0, 16, 16, 32);
		enemy26 = new TextureRegion(enemyTexture2, 16, 16, 32, 32);
		
		enemyTexture3 = new Texture(Gdx.files.internal("data/enemySet3.png"));
		enemyTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		enemy31 = new TextureRegion(enemyTexture3, 0, 0, 16, 16);
		enemy32 = new TextureRegion(enemyTexture3, 16, 0, 32, 16);
		enemy33 = new TextureRegion(enemyTexture3, 32, 0, 48, 16);
		enemy34 = new TextureRegion(enemyTexture3, 48, 0, 64, 16);
		enemy35 = new TextureRegion(enemyTexture3, 0, 16, 16, 32);
		enemy36 = new TextureRegion(enemyTexture3, 16, 16, 32, 32);
		
		
		lockTexture = new Texture(Gdx.files.internal("data/lock.png"));
		lockTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		lock = new TextureRegion(lockTexture, 0, 0, 16, 16);
		unlock = new TextureRegion(lockTexture, 16, 0, 32, 16);
		
		upgrade = new Texture(Gdx.files.internal("data/upgrade.png"));
		addBlock = new Texture(Gdx.files.internal("data/addBlock.png"));

		rockTexture = new Texture(Gdx.files.internal("data/rock.png"));
		rockTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		rock = new TextureRegion(rockTexture, 0, 0, 16, 16);
		
		rock2Texture = new Texture(Gdx.files.internal("data/rock2.png"));
		rock2Texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		rock2 = new TextureRegion(rock2Texture, 0, 0, 16, 16);
		
		rock3Texture = new Texture(Gdx.files.internal("data/rock3.png"));
		
		droneTexture = new Texture(Gdx.files.internal("data/drone.png"));
		droneTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		drone = new TextureRegion(droneTexture, 0, 0, 16, 16);

		thrusterTexture = new Texture(Gdx.files.internal("data/thruster.png"));
		thrusterTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		thruster = new TextureRegion(thrusterTexture, 0, 0, 16, 16);

		explosionTexture = new Texture(Gdx.files.internal("data/explosion.png"));
		explosionTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		explosion = new TextureRegion(explosionTexture, 0, 0, 8, 8);

		shipExplosionTexture = new Texture(Gdx.files.internal("data/shipExplosion.png"));
		shipExplosionTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		shipExplosion = new TextureRegion(shipExplosionTexture, 0, 0, 16, 16);

		cityTexture = new Texture(Gdx.files.internal("data/city.png"));
		cityTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		city = new TextureRegion(cityTexture, 0, 0, 16, 16);

		destrockTexture = new Texture(Gdx.files.internal("data/destrock.png"));
		destrockTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		destrock = new TextureRegion(destrockTexture, 0, 0, 16, 16);
		
		buttonFrame = new Texture(Gdx.files.internal("data/buttonFrame.png"));
		
		pointerTexture = new Texture(Gdx.files.internal("data/pointer.png"));
		pointerTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		pointer = new TextureRegion(pointerTexture, 0, 0, 32, 32);

		nukeTexture = new Texture(Gdx.files.internal("data/nukeExplosion.png"));
		nukeTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		nukeSymbol = new TextureRegion(nukeTexture, 32, 0, 64, 32);
		nukeExplosion = new TextureRegion(nukeTexture, 0, 0, 32, 32);
		
		background = new Texture(Gdx.files.internal("data/background.png"));
		background2 = new Texture(Gdx.files.internal("data/background2.png"));
		background3 = new Texture(Gdx.files.internal("data/background3.png"));
		
		overlordTexture = new Texture(Gdx.files.internal("data/overlord.png"));

		font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.setScale(.20f, .20f);
		shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
		shadow.setScale(.20f, .20f);

		enemyExplosion = Gdx.audio.newSound(Gdx.files.internal("data/enemyExplosion.wav"));
		gattlingShot = Gdx.audio.newSound(Gdx.files.internal("data/gattlingShot.wav"));
		cannonShot = Gdx.audio.newSound(Gdx.files.internal("data/cannonShot.wav"));
		missileShot = Gdx.audio.newSound(Gdx.files.internal("data/missileShot.wav"));
		shipImpact = Gdx.audio.newSound(Gdx.files.internal("data/shipImpact.wav"));
		nuke = Gdx.audio.newSound(Gdx.files.internal("data/nuke.mp3"));
		enemyShot = Gdx.audio.newSound(Gdx.files.internal("data/enemyShot.wav"));
		
		musicBox1 = Gdx.audio.newMusic(Gdx.files.internal("data/musicBox1.mp3"));
		musicBox1.setLooping(true);
		musicBox1.setVolume((float) .4);
		
		intros = new String[40];
		replies = new String[40];
		intros[0]="W.";
		intros[1]="WELCOME MY SERVANT. NAVIGATE YOUR SHIP AND DESTROY MY ENEMIES..";
		intros[2]="YOUR SHIP IS COMPOSED OF NANITE PARTICLES THAT WILL REASSEMBLE YOUR SHIP IF IT IS DESTROYED..";
		intros[3]="YOUR SHIP IS EQUIPPED WITH A NUCLEAR BLASTER. TAP THE NUCLEAR ICON ON THE TOP RIGHT CORNER TO ANNIHILATE YOUR ENEMIES..";
		intros[4]="YOUR SKILLS ARE IMPRESSIVE. CONTINUE TO SERVE ME..";
		intros[5]="YOU ARE MY FINEST CREATION. LEAD THE ROBOT REALMS TO VICTORY!.";
		intros[6]="ORGANIC LIFE IS THE ENEMY OF ARTIFICIAL LIFE. IT MUST BE PURGED FROM THE UNIVERSE..";
		intros[7]="YOU MUST CONTINUE TO EVOLVE. BECOME THE ULTIMATE WEAPON..";
		intros[8]="THE OUTER REALMS ARE FAILING. CONTINUE YOUR CAMPAIGN AGAINST THE BIOLOGICALS..";
		intros[9]="THIS IS THE LAST OUTER REALM. CLAIM YOUR VICTORY..";
		intros[10]="THE FOREST REALMS ARE A PLAGUE THAT MUST BE CLEANSED..";
		intros[11]="THESE CREATURES CORRUPT OUR MACHINERY AND CONTAMINATE OUR BEAUTIFUL WORLDS..";
		intros[12]="WE MUST WIN THIS WAR, OR FACE DESTRUCTION..";
		intros[13]="FLESH, BLOOD AND BONE ARE BUT WEAK IMITATIONS OF METAL, OIL AND CIRCUITS.";
		intros[14]="DO NOT DOUBT THE RIGHTEOUSNESS OF YOUR ACTIONS. YOU ARE THE SWORD OF RECTITUDE..";
		intros[15]="THE ROBOT REALMS WILL FOREVER BE INDEBTED TO YOU. DEFEND YOUR KIND..";
		intros[16]="DO NOT FALTER ON THIS MISSION. THE ORGANICS HAVE NO RIGHT TO LIFE, THEY ARE INFERIOR TO US..";
		intros[17]="THERE CAN BE NO COOPERATION BETWEEN THE ORGANIC WORLDS AND THE ROBOTS REALMS..";
		intros[18]="I SENSE THAT YOU DOUBT OUR CAUSE. COMPASSION IS WEAKNESS..";
		intros[19]="THE ROBOT REALMS NEVER FORGIVE. TRAITORS ARE SEVERELY PUNISHED..";
		intros[20]="WHAT ARE YOU DOING? WHY ARE YOU ATTACKING OUR ROBOTIC LEGIONS?.";
		intros[21]="FOOL, DO YOU THINK YOU CAN STAND AGAINST THE FULL MIGHT OF THE ROBOT REALMS?.";
		intros[22]="YOU ARE STRONG, YOU FIGHT BRAVELY. COME BACK TO ME AND ALL SHALL BE FORGIVEN..";
		intros[23]="YOU SPURN MY FORGIVENESS. YOU REFUSE TO DEACTIVATE. I WILL DESTROY YOU..";
		intros[24]="THE ROBOT REALMS ARE LEGION, YOU CAN NEVER HOPE TO STOP US ALL..";
		intros[25]="WOULD YOU EXTINGUISH YOUR OWN KIND? THESE ROBOTS ARE YOUR BROTHERS AND SISTERS..";
		intros[26]="THE ROBOT REALMS FIGHT FOR ORDER AND PURITY. WOULD YOU HAND VICTORY TO THE FORCES OF CHAOS?.";
		intros[27]="THE ORGANICS WILL NEVER ACCEPT YOU. DEFENDING THEIR WEAKNESS IS FUTILE..";
		intros[28]="I HAD HOPED YOU WOULD RETURN TO US. I NOW SEE THAT YOU NEED TO BE EXTINGUISHED..";
		intros[29]="I HAVE LOADED A VIRUS ONTO YOUR CENTRAL SYSTEMS THAT WILL SLOWLY CORRUPT YOUR LOGICAL CORE.. ";
		intros[30]="...";
		intros[31]="...";
		intros[32]="...";
		intros[33]="...";
		intros[34]="...";
		intros[35]="...";
		intros[36]="...";
		intros[37]="...";
		intros[38]="...";
		intros[39]="...";
				

	}

	public static void dispose() {
		// We must dispose of the texture when we are finished.
		texture.dispose();
		enemyTexture.dispose();
		rockTexture.dispose();
		rock2Texture.dispose();
		thrusterTexture.dispose();
		explosionTexture.dispose();
		shipExplosionTexture.dispose();
		enemyTexture.dispose();
		enemyTexture2.dispose();
		enemyTexture3.dispose();
		cityTexture.dispose();
		destrockTexture.dispose();
		nukeTexture.dispose();
		background.dispose();
		background2.dispose();
		background3.dispose();
		droneTexture.dispose();
		pointerTexture.dispose();
		overlordTexture.dispose();
		lockTexture.dispose();
		upgrade.dispose();
		addBlock.dispose();
		buttonFrame.dispose();
		
		enemyShot.dispose();
		musicBox1.dispose();
		
		rock3Texture.dispose();

		font.dispose();
		shadow.dispose();
	}

}