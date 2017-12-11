package com.mygdx.panzer;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

public class Sensor {

    private String debugTag;
    private ShapeRenderer debugRenderer = new ShapeRenderer();
    private Vector2 sensorBegin = new Vector2();
    private Vector2 sensorEnd = new Vector2();
    private MapManager mapManager = MapManager.getInstance();

    public enum Seeing {
        OBJECT,
        NOTHING
    }

    private int maxRange;
    private int range;
    private float angle;
    private Vector2 position = new Vector2();
    private Seeing seeing = Seeing.NOTHING;

    public Sensor(int maxRange, float angle) {
        this.maxRange = maxRange;
        this.angle = angle;
    }

    public void update(float delta) {
        Panzer panzer = mapManager.getPanzer();
        angle = panzer.getAngle();
        // Переводим в радианы
        angle *= Math.PI / 180;
        position = panzer.getPosition();
        double x = maxRange * Math.cos(angle);
        double y = maxRange * Math.sin(angle);
        sensorBegin = new Vector2(position);
        //TODO: Добавить в класс танка размер, а то это дичь какая то

        sensorEnd = new Vector2((float)(x + sensorBegin.x), (float)y + (sensorBegin.y));
        Array<Rectangle> rectObjects = mapManager.getMap().getRectPhysObjects();
        float minRange = Float.MAX_VALUE;
        for (Rectangle rectangle: rectObjects) {
            Vector2 p1 = new Vector2(rectangle.x, rectangle.y);
            Vector2 p2 = new Vector2(rectangle.x, rectangle.y + rectangle.height);
            Vector2 p3 = new Vector2(rectangle.x + rectangle.width, rectangle.y + rectangle.height);
            Vector2 p4 = new Vector2(rectangle.x + rectangle.width, rectangle.y);
            float currentRange = calculateRange(sensorBegin, sensorEnd, p1, p2);
            if (currentRange < minRange) {
                minRange =  currentRange;
            }
            currentRange = calculateRange(sensorBegin, sensorEnd, p2, p3);
            if (currentRange < minRange) {
                minRange =  currentRange;
            }
            currentRange = calculateRange(sensorBegin, sensorEnd, p3, p4);
            if (currentRange < minRange) {
                minRange =  currentRange;
            }
            currentRange = calculateRange(sensorBegin, sensorEnd, p1, p4);
            if (currentRange < minRange) {
                minRange =  currentRange;
            }
        }
        if (minRange < maxRange) {
            seeing = Seeing.OBJECT;
            System.out.println("Sensor " + debugTag + " just found object in " + (minRange - panzer.panzerSprite.getWidth() / 2) + " pixels!");
        } else {
            seeing = Seeing.NOTHING;
            System.out.println("Sensor found nothing...");
        }
        range = (int)minRange;
        range -= panzer.panzerSprite.getWidth() / 2;
    }

    private float calculateRange(Vector2 p1, Vector2 p2, Vector2 p3, Vector2 p4) {
        float range = Float.MAX_VALUE;
        Vector2 intersection = new Vector2();
        if (Intersector.intersectLines(p1, p2, p3, p4, intersection)) {
            if (    Intersector.pointLineSide(intersection, p1, p2) == 0 &&
                    Intersector.pointLineSide(intersection, p3, p4) == 0 &&
                    (intersection.x - p1.x)*(intersection.x - p2.x) <= 0 &&
                    (intersection.x - p3.x)*(intersection.x - p4.x) <= 0 &&
                    (intersection.y - p1.y)*(intersection.y - p2.y) <= 0 &&
                    (intersection.y - p3.y)*(intersection.y - p4.y) <= 0) {
                range = (float) Math.sqrt
                        (
                                (intersection.x - p1.x) * (intersection.x - p1.x) +
                                        (intersection.y - p1.y) * (intersection.y - p1.y)
                        );
            }
        }
        return range;
    }

    public void setDebugTag(String debugTag) {
        this.debugTag = debugTag;
    }

    public Vector2 getSensorBegin() {
        return sensorBegin;
    }

    public Vector2 getSensorEnd() {
        return sensorEnd;
    }
}
