package com.mygdx.panzer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Panzer extends ApplicationAdapter {
	OrthographicCamera camera;
    SpriteBatch batch;
	private Texture bckgr;
	private Texture treeImage;
	private Texture rockImage;
	private Texture panzerImage;
	Rectangle panzer;

	@Override
	public void create () {
		batch = new SpriteBatch();
		bckgr = new Texture(Gdx.files.internal("map1.png"));
		treeImage = new Texture(Gdx.files.internal("tree.png"));
		rockImage = new Texture(Gdx.files.internal("rock.png"));
		panzerImage = new Texture(Gdx.files.internal("panzer.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        //in render?
        panzer = new Rectangle();
        panzer.x = 800/2 - 64/ 2;
        panzer.y = 600/2 - 64/2;
        panzer.width = panzer.height = 64;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(69, 69, 69, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bckgr, 0,0,1920,1080);
        batch.draw(panzerImage, panzer.x, panzer.y);
        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		bckgr.dispose();
		treeImage.dispose();
		rockImage.dispose();
		panzerImage.dispose();
	}
}
