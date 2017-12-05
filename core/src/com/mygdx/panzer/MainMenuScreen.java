package com.mygdx.panzer;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by Влада on 03.12.2017.
 */

public class MainMenuScreen extends ScreenAdapter {

    final PanzerProject process;
    OrthographicCamera camera;

    public MainMenuScreen(final PanzerProject panz)
    {
        process = panz;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        Label label = new Label("", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
