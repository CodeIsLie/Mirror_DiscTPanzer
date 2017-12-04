package com.mygdx.panzer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2D;

public class Panzer extends Game {

	private ProcessScreen proc = new ProcessScreen(this);

	@Override
	public void create () {
		Box2D.init();
		setScreen(proc);
	}
}
