package com.mygdx.panzer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.panzer.Panzer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Panzar";
		cfg.width = 800;
		cfg.height = 600;
		new LwjglApplication(new Panzer(), cfg);
	}
}
