package com.mygdx.panzer;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

public class Map {

    private TiledMap tiledMap;

    private Array<Polygon> polPhysObject = new Array<>();

    public Map(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        buildPhysicalBodies();
    }

    public Array<Polygon> getPolygonPhysObjects() {
        return polPhysObject;
    }

    private void buildPhysicalBodies() {
        MapObjects objects = tiledMap.getLayers().get("obstacles").getObjects();
        for (MapObject object : objects) {
            PolygonMapObject polygonMapObject = (PolygonMapObject) object;
            Polygon polygon = polygonMapObject.getPolygon();
            polPhysObject.add(polygon);
        }

    }

    public void dispose() {
        tiledMap.dispose();
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
