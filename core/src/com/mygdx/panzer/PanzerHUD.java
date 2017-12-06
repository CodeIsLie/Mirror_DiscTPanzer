package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Влада on 05.12.2017.
 */

public class PanzerHUD {

    private PanzerProject game;
    private Stage stage;
    private Batch batch;
    private Viewport vp;
    private Camera camera;
    private TextField startCoords;
    private TextField finishCoords;
    private Image toMenu;
    private Image startButton;

    private Texture startButtonTexture;
    private Texture pauseButtonTexture;
    private Texture menuButtonTexture;


    public PanzerHUD(PanzerProject game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.position.set(Settings.WORLD_WIDTH / 2, Settings.WORLD_HEIGHT / 2, 0);
        camera.update();

        vp = new FitViewport(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT, camera);
        vp.apply(true);
        batch = new SpriteBatch();
        this.stage = new Stage(vp, batch);

        startButtonTexture = new Texture("startButton.png");
        pauseButtonTexture = new Texture("pauseButton.png");
        menuButtonTexture = new Texture("toMenuButton.png");

        startButton = new Image(startButtonTexture);
        startButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switchToOpposite();
                return true;
            }
        });

        toMenu = new Image(menuButtonTexture);
        //    toMenu.addListener(new InputListener() {
        //        @Override
        //        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        //            switchToOpposite();
        //            return true;
        //        }
        //    });

        Table table = new Table();
        table.setPosition(0, 0);
        table.setSize(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT);

        table.add().padRight(Settings.WORLD_WIDTH - 200);
        table.add(toMenu);
        table.row();
        table.add().padBottom(Settings.WORLD_HEIGHT - 150);
        table.row();
        table.add().padRight(Settings.WORLD_WIDTH - 200);
        table.add(startButton);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);


    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    private void switchToOpposite() {
        if (game.getProcessState() == ProcessScreen.ProcessState.PAUSE) {
            game.setProcessState(ProcessScreen.ProcessState.RUN);
            startButton.setDrawable(new TextureRegionDrawable(new TextureRegion(pauseButtonTexture)));
            startButton.setSize(pauseButtonTexture.getWidth(), pauseButtonTexture.getHeight());
        } else if (game.getProcessState() == ProcessScreen.ProcessState.RUN) {
            game.setProcessState(ProcessScreen.ProcessState.PAUSE);
            startButton.setDrawable(new TextureRegionDrawable(new TextureRegion(startButtonTexture)));
            startButton.setSize(pauseButtonTexture.getWidth(), pauseButtonTexture.getHeight());
        }
    }

    public void dispose()
    {
        startButtonTexture.dispose();
        pauseButtonTexture.dispose();
        menuButtonTexture.dispose();
    }
}
