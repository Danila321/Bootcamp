package com.mygdx.game.utility;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.objects.GameObject;

public class ContactManager {
    World world;

    public ContactManager(World world) {
        this.world = world;

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                //начало контакта
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();

                int cDef = fixA.getFilterData().categoryBits;
                int cDef2 = fixB.getFilterData().categoryBits;

                if (cDef == GameSettings.BASE_BULLET_BIT && cDef2 == GameSettings.ENEMY_BIT ||
                cDef == GameSettings.ENEMY_BIT && cDef2 == GameSettings.BASE_BULLET_BIT ||
                        cDef == GameSettings.BASE_BULLET_BIT && cDef2 == GameSettings.BASE_BULLET_BIT) {
                    ((GameObject) fixA.getUserData()).hit(GameSettings.BASE_BULLET_DAMAGE);
                    ((GameObject) fixB.getUserData()).hit(GameSettings.BASE_BULLET_DAMAGE);
                }
                if (cDef == GameSettings.BASE_BULLET_DAMAGE && cDef2 == GameSettings.BASE_TOWER_BIT) {
                    ((GameObject) fixA.getUserData()).hit(GameSettings.BASE_BULLET_DAMAGE);
                }
                if (cDef == GameSettings.BASE_TOWER_BIT && cDef2 == GameSettings.BASE_BULLET_DAMAGE) {
                    ((GameObject) fixB.getUserData()).hit(GameSettings.BASE_BULLET_DAMAGE);
                }
            }

            @Override
            public void endContact(Contact contact) {
                //завершение контакта
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                //перед вычислением всех контактоа
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                // после вычислений контактов
            }
        });
    }
}
