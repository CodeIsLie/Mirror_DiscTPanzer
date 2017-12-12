package com.mygdx.panzer;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    private Array<Rectangle> rectPhysObjects = new Array<>();
    private Array<Ellipse> ellipsePhysObjects = new Array<>();
    private Array<Polygon> polPhysObject = new Array<>();

    public Map(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        buildPhysicalBodies();
    }

    public Array<Rectangle> getRectPhysObjects() {
        return rectPhysObjects;
    }

    public Array<Ellipse> getEllipsePhysObjects() {
        return ellipsePhysObjects;
    }
    public Array<Polygon> getPolygonPhysObjects() {
        return polPhysObject;
    }

    private void buildPhysicalBodies() {
        MapObjects objects = tiledMap.getLayers().get("rocks").getObjects();
        for (MapObject object : objects) {
            //Считаем, что камень - прямоугольник (так оно и есть, но если там будут не прямоугольники, будет ошибка
            RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleMapObject.getRectangle();
            rectPhysObjects.add(rectangle);
        }

        objects = tiledMap.getLayers().get("trees").getObjects();
        /*for (MapObject object : objects) {
            //Считаем, что дерево - эллипс (так оно и есть, но если там будут не эллипсы, будет ошибка
            EllipseMapObject ellipseMapObject = (EllipseMapObject) object;
            Ellipse ellipse = ellipseMapObject.getEllipse();
            ellipsePhysObjects.add(ellipse);
        }
        for (Ellipse el : ellipsePhysObjects)
        {
            Rectangle rec = new Rectangle(el.x, el.y, el.width, el.height);
            rectPhysObjects.add(rec);
        }*/

        for (MapObject object : objects) {
            //Считаем, что дерево - polygon
            PolygonMapObject polygoneMapObject = (PolygonMapObject) object;
            Polygon polygon = polygoneMapObject.getPolygon();
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
