package com.mygdx.panzer;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Влада on 04.12.2017.
 */

 public final class Settings {
    private Vector2 START = new Vector2(0, 0);
    private Vector2 FINISH = new Vector2(800, 600);
    private int SENS_ACT = 10; //дальность сенсора
    private int MAX_SPEED = 50;
    private double rotation = 0;


    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public int getMAX_SPEED() {
        return MAX_SPEED;

    }

    public void setMAX_SPEED(int MAX_SPEED) {
        this.MAX_SPEED = MAX_SPEED;
    }

    public Vector2 getSTART() {

        return START;
    }

    public void setSTART(Vector2 START) {
        this.START = START;
    }

    public Vector2 getFINISH() {
        return FINISH;
    }

    public void setFINISH(Vector2 FINISH) {
        this.FINISH = FINISH;
    }

    public int getSENS_ACT() {
        return SENS_ACT;
    }

    public void setSENS_ACT(int SENS_ACT) {
        this.SENS_ACT = SENS_ACT;
    }
}
