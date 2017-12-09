package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Влада on 05.12.2017.
 */

public class Panzer {
    private Texture panzerImage;
    private static final float MOVE_TIME = 0.01F;
    private static final int PANZER_MOVEMENT = 1;
    private float timer = MOVE_TIME;
    private Vector2 position = new Vector2(0,0);

    public Panzer()
    {
        panzerImage = new Texture(Gdx.files.internal("panzer.png"));
    }

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
    }

    public void reset()
    {
        position = Settings.getSTART();
    }


    public void draw(Batch batch){
        batch.begin();
        batch.draw(panzerImage, position.x, position.y);
        batch.end();
    }

    public void dispose()
    {
        panzerImage.dispose();
    }

}
