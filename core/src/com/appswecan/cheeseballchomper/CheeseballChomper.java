package com.appswecan.cheeseballchomper;

import com.appswecan.cheeseballchomper.helper.AssetLoader;
import com.appswecan.cheeseballchomper.render.GameRenderer;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class CheeseballChomper extends Game{

	private Music rainMusic;

	private GameRenderer gameRenderer;



	@Override
	public void create () {


		gameRenderer = new GameRenderer(480,800);

	}

	@Override
	public void render() {
		gameRenderer.render();

	}



	@Override
	public void dispose() {
		super.dispose();
		gameRenderer.dispose();
	//	rainMusic.dispose();

	}

	@Override
	public void resize(int width, int height)
	{
		gameRenderer.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		Gdx.app.log("Paused", "Paused");

	}

	@Override
	public void resume() {
		Gdx.app.log("Resume", "Resume");

	}

}
