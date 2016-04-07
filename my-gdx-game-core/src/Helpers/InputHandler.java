//Copyright Isaac Shannon, All Rights reserved, 2014
package Helpers;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.InputProcessor;
import com.kilobolt.GameWorld.GameWorld.GameState;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.GameWorld.ShipWorld;
import com.kilobolt.GameWorld.LevelWorld;
import GameObjects.Ship;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class InputHandler implements InputProcessor {
    private Ship myShip;
    private GameState currentState;
    private GameWorld gameWorld;
    private ShipWorld shipWorld;
    private LevelWorld levelWorld;
    private float scaleFactorX;
    private float scaleFactorY;
    private float screenHeight;
    private float screenWidth;
   
    public InputHandler(GameWorld world,ShipWorld shipWorld,
    		LevelWorld levelWorld,float scaleFactorX,float scaleFactorY,
    		float screenWidth,float screenHeight) {
        gameWorld = world;
    	myShip = gameWorld.getShip();
    	this.scaleFactorX = scaleFactorX;
    	this.scaleFactorY = scaleFactorY;
    	this.shipWorld = shipWorld;
    	this.levelWorld = levelWorld;
    	this.screenWidth = screenWidth;
    	this.screenHeight = screenHeight;
        currentState = gameWorld.getCurrentState();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    	currentState = gameWorld.getCurrentState();
    	myShip = gameWorld.getShip();
    	screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        
        //compensate for the black space at the top of different screen formats
        screenY=(int) (screenY -(screenHeight*274/screenWidth-408)/2);
       
        //gameWorld.setLastScreen(screenWidth, screenHeight);
    	//System.out.println(screenX + " " + screenY);
        switch (currentState) {
		case SHIPWORLD:
			shipWorld.onClick(screenX,screenY);
			break;
		case RUNNING:
			gameWorld.onClick(screenX,screenY);
			//myShip.onClick(screenX, Gdx.graphics.getHeight() - screenY);
			break;
		case LEVELWORLD:
			levelWorld.onClick(screenX,screenY);
			break;
		case HELP:
			levelWorld.onClick(screenX,screenY);
			break;
		case SHOP:
			shipWorld.onClick(screenX,screenY);
			break;
		case TUTORIAL:
			shipWorld.onClick(screenX,screenY);
			
		default:
			break;
		}
    	
        return true; // Return true to say we handled the touch.
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
    	currentState = gameWorld.getCurrentState();
    	myShip = gameWorld.getShip();
    	screenX = scaleX(screenX);
        screenY = (int) scaleY(screenY);
        screenY=(int) (screenY -(screenHeight*274/screenWidth-408));
       // screenY=(int) (screenY*408/screenHeight);
    	switch (currentState) {
		case SHIPWORLD:
			shipWorld.onClick(screenWidth,screenHeight);
			break;
		case RUNNING:
			gameWorld.onClick(screenX,screenY);
			//myShip.onClick(screenX, Gdx.graphics.getHeight() - screenY);
			break;
		case LEVELWORLD:
			levelWorld.onClick(screenX,screenY);
			break;
		default:
			break;
		}
    	
        return true; // Return true to say we handled the touch.
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

}
