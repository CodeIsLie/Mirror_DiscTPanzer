package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Влада on 03.12.2017.
 */

public class ProcessScreen extends ScreenAdapter{
    SpriteBatch batch;
    private Texture panzerImage;
    private TiledMap map1;
    private TiledMapRenderer renderer;
    OrthographicCamera camera;

    private static final float MOVE_TIME = 1F;
    private float timer = MOVE_TIME;
    private static final int SNAKE_MOVEMENT = 32;
    private int snakeX = 0, snakeY = 0;

    @Override
    public void show (){
		batch = new SpriteBatch();
		panzerImage = new Texture(Gdx.files.internal("panzer.png"));

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
        camera.setToOrtho(false, 30, 20);
        map1 = new TmxMapLoader().load("map1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map1, 1/64f);
	}

    private void checkForOutOfBounds() {
        if (snakeX >= Gdx.graphics.getWidth()) {
            snakeX = 0;
        }
    }

	@Override
	public void render (float delta) {

        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;
            snakeX += SNAKE_MOVEMENT;
        }

        camera.update();
        //batch.setProjectionMatrix(camera.combined);
        renderer.setView(camera);
        renderer.render();
        batch.begin();
        batch.draw(panzerImage, snakeX, snakeY);
        batch.end();
        checkForOutOfBounds();
    }

	@Override
	public void dispose () {
		batch.dispose();
//		bckgr.dispose();
//		treeImage.dispose();
//		rockImage.dispose();
		panzerImage.dispose();
		map1.dispose();
	}
}

