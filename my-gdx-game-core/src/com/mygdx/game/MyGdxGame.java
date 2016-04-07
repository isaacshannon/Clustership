//Copyright Isaac Shannon, All Rights reserved, 2014
package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.kilobolt.Screens.GameScreen;
import Helpers.AssetLoader;
import Helpers.LevelLoader;;

public class MyGdxGame extends Game {

    @Override
    public void create() {
        System.out.println("Game Created!");
        AssetLoader.load();
        LevelLoader.load();
        setScreen(new GameScreen());
    }

}

