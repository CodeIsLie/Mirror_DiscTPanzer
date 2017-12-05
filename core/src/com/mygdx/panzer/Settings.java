package com.mygdx.panzer;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Влада on 04.12.2017.
 */

 public final class Settings {
    private static Vector2 START = new Vector2(0, 0);
    private static Vector2 FINISH = new Vector2(800, 600);
    private static int SENS_ACT = 10; //дальность сенсора
    private static int MAX_SPEED = 50;
    private static double  rotation = 0;
    public static final int WORLD_WIDTH = 2048;
    public static final int WORLD_HEIGHT = 1152;
    private static TiledMap map;


    static public TiledMap getMap() {return map;}

    static public void setMap(TiledMap newmap) {map = newmap;}

    static public double getRotation() {
        return rotation;
    }

    static public void setRotation(double newrotation) {
        rotation = newrotation;
    }

    static public int getMAX_SPEED() {
        return MAX_SPEED;
    }

    static public void setMAX_SPEED(int newMAX_SPEED) {
        MAX_SPEED = newMAX_SPEED;
    }

    static public Vector2 getSTART() {
        return START;
    }

    static public void setSTART(Vector2 newSTART) {
        START = newSTART;
    }

    static public Vector2 getFINISH() {
        return FINISH;
    }

    static public void setFINISH(Vector2 newFINISH) {
        FINISH = newFINISH;
    }

    static public int getSENS_ACT() {
        return SENS_ACT;
    }

    static public void setSENS_ACT(int newSENS_ACT) {
        SENS_ACT = newSENS_ACT;
    }
}
