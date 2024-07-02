package com.mygdx.game.Objects;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameResources;
import com.mygdx.game.GameSettings;

public class Enemy extends GameObject{

    public Enemy(String texturePath,World world){
        super(texturePath,0,0,50,50, GameSettings.ENEMY_BIT, world);
    }
}
