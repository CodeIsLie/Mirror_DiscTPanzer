package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Влада on 05.12.2017.
 */

public class Panzer {


    private static final int SENSOR_COUNT = 3;

    private Vector2 position = new Vector2(0,0);
    // В градусах
    private float angle = 0;
    private Polygon physBody;
    public Sprite panzerSprite;
    private Texture panzerImage;
    private Vector2 panzerSize;
    private Array<Sensor> sensors = new Array<>();

    private RuleSet ruleSet;

    public Panzer(float startAngle) {
        this.angle = startAngle;
        panzerImage = new Texture(Gdx.files.internal("panzer.png"));
        panzerSize = new Vector2(panzerImage.getWidth(), panzerImage.getHeight());
        panzerSprite = new Sprite(panzerImage);
        int deltaX = panzerImage.getWidth() / 2;
        int deltaY = panzerImage.getHeight() / 2;
        Settings.setFinishPos(new Vector2(Settings.WORLD_WIDTH - deltaX, Settings.WORLD_HEIGHT - deltaY));
        //Settings.setStartPos(new Vector2(deltaX, deltaY));
        //TODO: убрать, если начальный поворот не задается
        physBody = new Polygon();
        panzerSprite.setRotation(startAngle);
        Rectangle p = panzerSprite.getBoundingRectangle();
        Settings.setStartPos(new Vector2(p.getWidth() / 2, p.getHeight() / 2));
        for (int i = 0, sensorAngle = 45; i < SENSOR_COUNT; ++i, sensorAngle-=45) {
            Sensor sensor = new Sensor(Settings.getSensorRange() + (int)panzerSize.x / 2, sensorAngle);
            sensor.setDebugTag("SENSOR" + i);
            sensors.add(sensor);
        }
        float[] vertices = { 0, 0, 0, panzerSize.y, panzerSize.x, panzerSize.y, panzerSize.x, 0};
        physBody.setVertices(vertices);
        physBody.setPosition(Settings.getStartPos().x - panzerSize.x/2, Settings.getStartPos().y - panzerSize.y/2);
        physBody.setOrigin(panzerSize.x / 2,panzerSize.y / 2);
        physBody.setRotation(startAngle);
        ruleSet = new RuleSet(RuleSet.getRules(), this);
    }

    //TODO: удалить выход за границы экрана
    private void checkForOutOfBounds() {
        if (position.x >= Settings.WORLD_WIDTH) {
            position.x = 0;
        }
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public void updatePosition(float delta) {
        // Скорость - условные единицы в секунду
        Array<Float> powers = ruleSet.apply(sensors);
        float leftMove = powers.get(0);
        float rightMove = powers.get(1);

        //System.out.println("left power is " + leftMove);
        //System.out.println("right power is " + rightMove);


        //System.out.println("левое значение " + leftMove + " правое значение " + rightMove);

        // Пустим танк по кругу!
        calculateMotion(leftMove * (delta / 1),rightMove * (delta / 1));
        for (Sensor sensor: sensors) {
            sensor.update(delta);
        }
        checkForOutOfBounds();
        angle = (angle + 360) % 360;
        //System.out.println("current pos: " + position.x + " " + position.y);
    }

    // Поворачивает и передвигает танк, основываясь на передвижении (не мощности) левой и правой гусеницы.
    private void calculateMotion(float leftMovement, float rightMovement) {
        float weightDiff = leftMovement - rightMovement;
        if (weightDiff == 0) {
            moveStraight(leftMovement);
            return;
        }
        boolean isRightRotation = weightDiff > 0;
        float sectorLength = Math.abs(weightDiff);
        float straightMovement = Math.min(leftMovement, rightMovement);
        moveStraight(straightMovement);
        double rotationAngle = (180 * sectorLength) / (panzerSize.y * Math.PI);
        angle = isRightRotation ? angle - (float)rotationAngle : angle + (float)rotationAngle;
        physBody.setOrigin(panzerSize.x / 2,panzerSize.y / 2);
        physBody.setRotation(angle);
        float movementRemainder = (float) Math.sin(rotationAngle * (Math.PI / 180)) * (panzerSize.y / 2);
        moveStraight(movementRemainder);
    }

    private void moveStraight(float distance){
        // Переводим в радианы
        double curr_angle = angle * (Math.PI / 180);
        double x = distance * Math.cos(curr_angle);
        double y = distance * Math.sin(curr_angle);
        Vector2 new_position = new Vector2((float)(x + position.x), (float)y + (position.y));
        position.x = new_position.x;
        position.y = new_position.y;
        physBody.setPosition(position.x - panzerSize.x/2, position.y - panzerSize.y/2);
    }
    public void reset() {
        angle = Settings.getStartAngle();
        setPosition((int)Settings.getStartPos().x, (int)Settings.getStartPos().y);
        physBody.setPosition(Settings.getStartPos().x - panzerSize.x/2, Settings.getStartPos().y - panzerSize.y/2);
        physBody.setOrigin(panzerSize.x / 2,panzerSize.y / 2);
        physBody.setRotation(angle);
    }


    public void draw(Batch batch){
        panzerSprite.setRotation(angle);
        panzerSprite.setCenter(position.x, position.y);
        batch.begin();
        panzerSprite.draw(batch);
        batch.end();
    }

    public void dispose() {
        panzerImage.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public Array<Sensor> getSensors() {
        return sensors;
    }

    public Polygon getPhysBody() {
        return physBody;
    }

    public Vector2 getPanzerSize() {
        return panzerSize;
    }
}