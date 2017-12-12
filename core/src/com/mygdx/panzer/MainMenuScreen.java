package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by Влада on 03.12.2017.
 */

public class MainMenuScreen extends ScreenAdapter {
    final int WIDTH = 600;
    final int HEIGHT = 600;
    private PanzerProject process;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private FitViewport viewport;
    private Stage stage;
    private Image applyButton;
    private Texture applyButtonTexture;

    public MainMenuScreen(final PanzerProject panz)
    {
        this.process = panz;
        camera = new OrthographicCamera();
        camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        viewport.apply(true);
        batch = new SpriteBatch();
        this.stage = new Stage(viewport, batch);

        applyButtonTexture = new Texture("ApplyButton.png");
        applyButton = new Image(applyButtonTexture);
        applyButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                process.setProcess();
                return true;
            }
        });

        Table table = new Table();
        table.setPosition(0, 0);
        table.setSize(WIDTH, HEIGHT);
        table.add(applyButton);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
            viewport.update(width, height);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        applyButtonTexture.dispose();
    }
}
