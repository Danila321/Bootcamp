package com.mygdx.game.utility;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Vector2> points;

    public Path(TiledMap tiledMap) {
        points = new ArrayList<>();
        MapObjects objects = tiledMap.getLayers().get("road").getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObj = (RectangleMapObject) object;
                float x = rectObj.getRectangle().x;
                float y = rectObj.getRectangle().y;
                points.add(new Vector2(x, y));
            }
        }
    }

    public Vector2 getPoint(int index) {
        return points.get(index);
    }

    public int getLength() {
        return points.size();
    }
}
