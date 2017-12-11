package com.mygdx.panzer;

import com.badlogic.gdx.utils.Array;

public abstract class Rule {

    public abstract void apply(Array<Sensor> sensors);
}
