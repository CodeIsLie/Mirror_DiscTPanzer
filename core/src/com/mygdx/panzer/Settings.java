package com.mygdx.panzer;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Влада on 04.12.2017.
 */

 public final class Settings {
    private static Vector2 startPos = new Vector2(0, 0);
    private static Vector2 finishPos = new Vector2(800, 600);
    private static int sensorRange = 100;
    private static int maxSpeed = 50;
    private static int startAngle = 0;
    public static final int WORLD_WIDTH = 2048;
    public static final int WORLD_HEIGHT = 1152;
    private static TiledMap map;


    static public TiledMap getMap() {return map;}

    static public void setMap(TiledMap newMap) {map = newMap;}

    static public int getStartAngle() {
        return startAngle;
    }

    static public void setStartAngle(int newRotation) {
        startAngle = newRotation;
    }

    static public int getMaxSpeed() {
        return maxSpeed;
    }

    static public void setMaxSpeed(int newMaxSpeed) {
        maxSpeed = newMaxSpeed;
    }

    static public Vector2 getStartPos() {
        return startPos;
    }

    static public void setStartPos(Vector2 newStart) {
        startPos.x = newStart.x;
        startPos.y = newStart.y;
    }

    static public Vector2 getFinishPos() {
        return finishPos;
    }

    static public void setFinishPos(Vector2 newFinish) {
        finishPos = newFinish;
    }

    static public int getSensorRange() {
        return sensorRange;
    }

    static public void setSensorRange(int newSensAct) {
        sensorRange = newSensAct;
    }
}
