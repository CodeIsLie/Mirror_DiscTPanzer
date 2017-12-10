package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
/**
 * Created by Влада on 05.12.2017.
 */

public class PanzerHUD {
    private enum conditions {NOTHING, START, FINISH};
    private conditions condition = conditions.NOTHING;

    private PanzerProject game;
    private Stage stage;
    private Batch batch;
    private Viewport vp;
    private Camera camera;

    private Image toMenu;
    private Image playButton;
    private Image editStartButton;
    private Image editFinishButton;

    private Texture playButtonTexture;
    private Texture stopButtonTexture;
    private Texture menuButtonTexture;
    private Texture editStartButtonTexture;
    private Texture editFinishButtonTexture;
    private Texture finishTexture;

    public PanzerHUD(PanzerProject game, OrthographicCamera camera, FitViewport vp, Batch batch) {

        this.game = game;
        this.camera = camera;
        this.vp = vp;
        this.batch = batch;
        this.stage = new Stage(vp, batch);

        playButtonTexture = new Texture("playButton.png");
        stopButtonTexture = new Texture("stopButton.png");
        menuButtonTexture = new Texture("toMenuButton.png");
        editFinishButtonTexture = new Texture("FinishButton.png");
        editStartButtonTexture = new Texture("StartButton.png");
        finishTexture = new Texture("finish.png");

        playButton = new Image(playButtonTexture);
        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("play : clicked");
                switchToOpposite();
                return true;
            }
        });

        toMenu = new Image(menuButtonTexture);
    /*    toMenu.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }
            });*/

        editStartButton = new Image(editStartButtonTexture);
        editStartButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("editstart : clicked");
                    switch (condition)
                    {
                        case NOTHING:
                            condition = conditions.START;
                        default:
                            break;
                    }
                    return true;
                }
            });

        editFinishButton = new Image(editFinishButtonTexture);
        editFinishButton.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("editfinish : clicked");
                    switch (condition)
                    {
                        case NOTHING:
                            condition = conditions.FINISH;
                        default:
                            break;
                    }
                    return true;
                }
            });

        Table table = new Table();
        table.setPosition(0, 0);
        table.setSize(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT);

        table.add().padRight(Settings.WORLD_WIDTH - 500);
        table.add(editStartButton); table.add().padRight(32);
        table.add(editFinishButton); table.add().padRight(32);
        table.add(toMenu);
        table.row();
        table.add().padBottom(Settings.WORLD_HEIGHT - 150);
        table.row();
        table.add().padRight(Settings.WORLD_WIDTH - 500);
        table.add().padRight(playButtonTexture.getWidth()); table.add().padRight(32);
        table.add().padRight(playButtonTexture.getWidth()); table.add().padRight(32);
        table.add(playButton);

        stage.addActor(table);
       InputMultiplexer multiplexer = new InputMultiplexer();
       multiplexer.addProcessor(stage);
       multiplexer.addProcessor(new InputAdapter(){
            public boolean touchDown(int x,int y,int pointer,int button){
                Vector2 realCoord = new Vector2(x, y);
                vp.unproject(realCoord);
                System.out.println("adapter : touched");
                if (!inPolygons(realCoord.x,realCoord.y)){
                switch (condition) {
                    case START:
                        System.out.println("adapter : start at " + x + " " + y);
                        Settings.setStartPos(realCoord);
                        break;
                    case FINISH:
                        System.out.println("adapter : finish at " + x + " " + y);
                        Settings.setFinishPos(realCoord);
                        break;
                    case NOTHING:
                        System.out.println("adapter : nothing");
                        break;
                }}
                condition = conditions.NOTHING;
                return true;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    private boolean inPolygons(float x, float y)
    {
        Rectangle panz = game.proc.panzer.panzerSprite.getBoundingRectangle();
        Rectangle newpanz = new Rectangle(x - panz.getWidth() / 2, y - panz.getHeight() / 2, panz.getWidth(), panz.getHeight());
        for (Rectangle r: game.proc.rectPhysObjects) {
            if (Intersector.overlaps(r, newpanz))
                return true;
        }
        return false;
    }
    
    public void render(float delta) {
        Vector2 finishpos = Settings.getFinishPos();
        batch.begin();
        batch.draw(finishTexture, finishpos.x - finishTexture.getWidth() / 2, finishpos.y - finishTexture.getHeight() / 2);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    private void switchToOpposite() {
        if (game.getProcessState() == ProcessScreen.ProcessState.PAUSE) {
            editStartButton.setVisible(false);
            editFinishButton.setVisible(false);
            game.setProcessState(ProcessScreen.ProcessState.RUN);
            playButton.setDrawable(new TextureRegionDrawable(new TextureRegion(stopButtonTexture)));
            playButton.setSize(stopButtonTexture.getWidth(), stopButtonTexture.getHeight());
        } else if (game.getProcessState() == ProcessScreen.ProcessState.RUN) {
            editStartButton.setVisible(true);
            editFinishButton.setVisible(true);
            game.setProcessState(ProcessScreen.ProcessState.PAUSE);
            playButton.setDrawable(new TextureRegionDrawable(new TextureRegion(playButtonTexture)));
            playButton.setSize(stopButtonTexture.getWidth(), stopButtonTexture.getHeight());
        }
    }

    public void dispose()
    {
        playButtonTexture.dispose();
        stopButtonTexture.dispose();
        menuButtonTexture.dispose();
        editStartButtonTexture.dispose();
        editFinishButtonTexture.dispose();
    }
}
