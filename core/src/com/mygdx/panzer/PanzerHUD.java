package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Влада on 05.12.2017.
 */

public class PanzerHUD {

    private PanzerProject game;
    private Stage stage;
    private Label startButton;
    private Batch batch;
    private Viewport vp;
    private Camera camera;
    private TextField startCoords;
    private TextField finishCoords;

    public PanzerHUD(PanzerProject game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.position.set(Settings.WORLD_WIDTH / 2, Settings.WORLD_HEIGHT / 2, 0);
        camera.update();

        vp = new FitViewport(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT, camera);
        vp.apply(true);
        batch = new SpriteBatch();
        this.stage = new Stage(vp, batch);
        Table table = new Table();
        startButton = new Label("Start", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        startButton.setFontScale(3);

        startButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switchToOpposite();
                return true;
            }
        });

        table.add(startButton);
        table.setPosition(Settings.WORLD_WIDTH - 150, 0);
        table.setSize(100,100);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);


    }

    public void render(float delta)
    {
        stage.act(delta);
        stage.draw();
    }

    private void switchToOpposite() {
        if (game.getProcessState() == ProcessScreen.ProcessState.PAUSE) {
            game.setProcessState(ProcessScreen.ProcessState.RUN);
            startButton.setText("Pause");
        } else if (game.getProcessState() == ProcessScreen.ProcessState.RUN) {
            game.setProcessState(ProcessScreen.ProcessState.PAUSE);
            startButton.setText("Start");

        }
    }
}
