package com.mygdx.panzer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2D;

public class PanzerProject extends Game {

    private ProcessScreen.ProcessState processState;
    public ProcessScreen proc;

	@Override
	public void create () {
		Box2D.init();
		Settings.setMap(new TmxMapLoader().load("map1.tmx"));
		proc = new ProcessScreen(this);
		setScreen(proc);
	}

    public ProcessScreen.ProcessState getProcessState() {
        return processState;
    }

    public void setProcessState(ProcessScreen.ProcessState processState) {
        this.processState = processState;
    }
}
