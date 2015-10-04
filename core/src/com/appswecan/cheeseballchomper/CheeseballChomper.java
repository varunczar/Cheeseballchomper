package com.appswecan.cheeseballchomper;

import com.appswecan.cheeseballchomper.gamescreens.MainMenu;
import com.appswecan.cheeseballchomper.render.GameRenderer;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class CheeseballChomper extends Game{

	private GameRenderer gameRenderer;

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	private SpriteBatch batch;

	@Override
	public void create () {

		batch = new SpriteBatch();
		Gdx.input.setCatchBackKey(true);
		//this.setScreen(new GameRenderer(480,800,this));
		MainMenu mainMenu = new MainMenu(480,800,this);
		this.gameRenderer = mainMenu.getGameRenderer();
		this.setScreen(mainMenu);
		//gameRenderer = new GameRenderer(480,800,this);

	}



	@Override
	public void render() {
		//gameRenderer.render(1.0f);
		super.render();
	}



	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();

		//gameRenderer.dispose();

	}

	@Override
	public void resize(int width, int height)
	{
		super.resize(width,height);
		//gameRenderer.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		Gdx.app.log("Paused", "Paused");

	}

	@Override
	public void resume() {
		Gdx.app.log("Resume", "Resume");
		GameRenderer.gameState= GameRenderer.GameState.PAUSE;
	}


}
