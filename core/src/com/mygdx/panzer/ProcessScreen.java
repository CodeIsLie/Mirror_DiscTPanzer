package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Влада on 03.12.2017.
 */

public class ProcessScreen extends ScreenAdapter {

    private SpriteBatch batch;
    private Texture panzerImage;
    private TiledMap map1;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private Panzer game;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;

    private static final float MOVE_TIME = 1F;
    private static final int SNAKE_MOVEMENT = 32;
    private static final int WORLD_WIDTH = 2048;
    private static final int WORLD_HEIGHT = 1152;
    private static final int PIXELS_PER_TILE = 32;
    private static final float HALF = 0.5f;

    private float timer = MOVE_TIME;
    private int snakeX = 0, snakeY = 0;
    private Array<Rectangle> rectPhysObjects = new Array<>();
    private Array<Ellipse> ellipsePhysObjects = new Array<>();

    public ProcessScreen(Panzer game) {
        this.game = game;
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);
        batch = new SpriteBatch();
        map1 = new TmxMapLoader().load("map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map1, batch);
        mapRenderer.setView(camera);
        panzerImage = new Texture(Gdx.files.internal("panzer.png"));
        buildPhysicalBodies();
	}

    private void checkForOutOfBounds() {
        if (snakeX >= Gdx.graphics.getWidth()) {
            snakeX = 0;
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw();
        drawDebug();
    }

    private void update(float delta) {
        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;
            snakeX += SNAKE_MOVEMENT;
        }
        checkForOutOfBounds();
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        mapRenderer.render();
        batch.begin();
        batch.draw(panzerImage, snakeX, snakeY);
        batch.end();
    }

    //рендерим прямоугольники физических обьектов
    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Rectangle rectangle: rectPhysObjects) {
            shapeRenderer.rect(rectangle.x, rectangle.y,
                    rectangle.width, rectangle.height);
        }
        for (Ellipse ellipse: ellipsePhysObjects) {
            shapeRenderer.ellipse(ellipse.x, ellipse.y,
                    ellipse.width, ellipse.height);
        }
        shapeRenderer.end();
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
	public void dispose () {
		batch.dispose();
		panzerImage.dispose();
		map1.dispose();
	}


	//TODO: вынеси в отдельный класс какой-нибудь, типа MapBuilder или что-то такое,
    //TODO: пишу просто как пример того, как экспоритровать обьекты из TiledMap
    private void buildPhysicalBodies() {
        MapObjects objects = map1.getLayers().get("rocks").getObjects();
        for (MapObject object : objects) {
            //Считаем, что камень - прямоугольник (так оно и есть, но если там будут не прямоугольники, будет ошибка
            RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleMapObject.getRectangle();
            rectPhysObjects.add(rectangle);
        }

        objects = map1.getLayers().get("trees").getObjects();
        for (MapObject object : objects) {
            //Считаем, что дерево - эллипс (так оно и есть, но если там будут не эллипсы, будет ошибка
            EllipseMapObject ellipseMapObject = (EllipseMapObject) object;
            Ellipse ellipse = ellipseMapObject.getEllipse();
            ellipsePhysObjects.add(ellipse);
        }
    }

}

