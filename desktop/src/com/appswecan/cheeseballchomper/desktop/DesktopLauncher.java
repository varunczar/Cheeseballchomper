package com.appswecan.cheeseballchomper.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.appswecan.cheeseballchomper.CheeseballChomper;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drop";
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new CheeseballChomper(), config);
	}
}
