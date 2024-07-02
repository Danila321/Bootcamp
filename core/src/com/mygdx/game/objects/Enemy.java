package com.mygdx.game.objects;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utility.GameResourses;
import com.mygdx.game.GameSettings;

public class Enemy extends objects.GameObject {

    public Enemy(String texturePath,World world){
        super(texturePath,0,0,50,50, GameSettings.ENEMY_BIT, world);
    }
}
