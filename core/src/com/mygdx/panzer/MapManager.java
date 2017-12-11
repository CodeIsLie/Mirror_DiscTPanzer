package com.mygdx.panzer;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class MapManager  {

    public static final String DEBUG_MAP_PATH = "debug.tmx";

    private static MapManager instance = new MapManager();

    private Panzer panzer;

    public static MapManager getInstance() {
        return instance;
    }

    public Map map;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Panzer getPanzer() {
        return panzer;
    }

    public void setPanzer(Panzer panzer) {
        this.panzer = panzer;
    }
}
