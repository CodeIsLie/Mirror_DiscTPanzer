package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Set;

/**
 * Created by Влада on 05.12.2017.
 */

public class Panzer {
    public Texture panzerImage;
    private static final float MOVE_TIME = 0.01F;
    private static final int PANZER_MOVEMENT = 1;
    private float timer = MOVE_TIME;
    private Vector2 position = new Vector2(0,0);
    private int deltaX = 0;
    private int deltaY = 0;
    public Sprite panzerSprite;

    public Panzer()
    {
        panzerImage = new Texture(Gdx.files.internal("panzer.png"));
        panzerSprite = new Sprite(panzerImage);
        deltaX = panzerImage.getWidth() / 2;
        deltaY = panzerImage.getHeight() / 2;
        Settings.setFinishPos(new Vector2(Settings.WORLD_WIDTH - deltaX, Settings.WORLD_HEIGHT - deltaY));
        //Settings.setStartPos(new Vector2(deltaX, deltaY));
        //TODO: убрать, если начальный поворот не задается
        panzerSprite.setRotation(Settings.getRotation());
        Rectangle p = panzerSprite.getBoundingRectangle();
        Settings.setStartPos(new Vector2(p.getWidth() / 2, p.getHeight() / 2));
    }

    //TODO: удалить выход за границы экрана
    private void checkForOutOfBounds() {
        if (position.x >= Settings.WORLD_WIDTH) {
            position.x = 0;
        }
    }

    public void setPosition(int x, int y)
    {
       position.x = x;
       position.y = y;
    }

    public void updatePosition(float delta) {
        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;
            position.x += PANZER_MOVEMENT;
        }
        checkForOutOfBounds();
        Settings.setRotation((Settings.getRotation() + 1) % 361);
        panzerSprite.setRotation(Settings.getRotation());
        System.out.println("current pos: " + position.x + " " + position.y);
    }

    public void reset()
    {
        Settings.setRotation(0);
        setPosition((int)Settings.getStartPos().x, (int)Settings.getStartPos().y);
    }


    public void draw(Batch batch){
        panzerSprite.setRotation(Settings.getRotation());
        panzerSprite.setCenter(position.x, position.y);
        batch.begin();
        panzerSprite.draw(batch);
        batch.end();
    }

    public void dispose()
    {
        panzerImage.dispose();
    }

}
