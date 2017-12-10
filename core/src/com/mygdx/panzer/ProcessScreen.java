package com.mygdx.panzer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Влада on 03.12.2017.
 */

public class ProcessScreen extends ScreenAdapter {

    public enum ProcessState {
        RUN,
        PAUSE
    }

    private PanzerProject game;
    private SpriteBatch batch;
    private TiledMap map1;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private FitViewport viewport;

    private PanzerHUD hud;

    private static final int PIXELS_PER_TILE = 32;
    private static final float HALF = 0.5f;

    public Array<Rectangle> rectPhysObjects = new Array<>();
    public Array<Ellipse> ellipsePhysObjects = new Array<>();

    public Panzer panzer;
    public ProcessScreen(PanzerProject game) {
        this.game = game;
        game.setProcessState(ProcessState.PAUSE);
    }

    @Override
    public void show() {
        panzer = new Panzer();

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.position.set(Settings.WORLD_WIDTH / 2, Settings.WORLD_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(Settings.WORLD_WIDTH, Settings.WORLD_HEIGHT, camera);
        viewport.apply(true);

        batch = new SpriteBatch();
        map1 = Settings.getMap();
        mapRenderer = new OrthogonalTiledMapRenderer(map1, batch);
        mapRenderer.setView(camera);
        buildPhysicalBodies();

        hud = new PanzerHUD(game, camera, viewport, batch);
	}

    @Override
    public void render(float delta) {
        switch (game.getProcessState()) {
            case RUN:
                panzer.updatePosition(delta);
                break;
            case PAUSE:
                panzer.reset();
                break;
            default:
                break;
        }
        clearScreen();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        mapRenderer.render();
        //drawDebug();
        panzer.draw(batch);
        hud.render(delta);
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
        for (Ellipse el : ellipsePhysObjects)
        {
            Rectangle rec = new Rectangle(el.x, el.y, el.width, el.height);
            rectPhysObjects.add(rec);
        }
    }


    @Override
    public void dispose () {
        batch.dispose();
        panzer.dispose();
        map1.dispose();
        hud.dispose();
    }
}

