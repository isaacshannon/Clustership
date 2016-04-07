//Copyright Isaac Shannon, All Rights reserved, 2014
package com.kilobolt.Screens;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.kilobolt.GameWorld.GameRenderer;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.GameWorld.ShipWorld;
import com.kilobolt.GameWorld.LevelWorld;
import com.badlogic.gdx.InputProcessor;
import Helpers.InputHandler;

public class GameScreen implements Screen {
	private GameWorld world;
	private ShipWorld shipWorld;
	private LevelWorld levelWorld;
    private GameRenderer renderer;
    // This is the constructor, not the class declaration
    public GameScreen() {
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float gameWidth = 136;
        float gameHeight = screenHeight / (screenWidth / gameWidth);
        world = new GameWorld(screenWidth,screenHeight);
        levelWorld = new LevelWorld(world,screenWidth,screenHeight);
        shipWorld = new ShipWorld(world,levelWorld,screenWidth,screenHeight);
       
        //Gdx.input.setInputProcessor(new InputHandler(world,shipWorld,levelWorld));
        Gdx.input.setInputProcessor(new InputHandler(world,shipWorld,levelWorld, screenWidth / gameWidth, screenHeight / gameHeight,screenWidth,screenHeight));
       // renderer = new GameRenderer(world,shipWorld,levelWorld,screenWidth,screenHeight);
        renderer = new GameRenderer(world,shipWorld,levelWorld,screenWidth,gameHeight);
    }

    @Override
    public void render(float delta) {
		world.update(delta);
        renderer.render();
        //System.out.println(1/delta);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("GameScreen - resizing");
    }

    @Override
    public void show() {
        System.out.println("GameScreen - show called");
    }

    @Override
    public void hide() {
        System.out.println("GameScreen - hide called");     
    }

    @Override
    public void pause() {
        System.out.println("GameScreen - pause called");        
    }

    @Override
    public void resume() {
        System.out.println("GameScreen - resume called");       
    }

    @Override
    public void dispose() {
        // Leave blank
    }

}