package com.appswecan.cheeseballchomper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;



public class CheeseballChomper extends ApplicationAdapter implements ApplicationListener{
	private Texture dropImage;
	private Texture creatureOpenEyed;
	private Texture creatureCloseEyed;
	private Texture monster;
	private Texture cloudImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	private Array<Rectangle> clouds;
	private Array<Rectangle> enemies;
	private long lastDropTime;
	private long lastCloudTime;
	private long lastEnemyTime;

	private int dropScore = 0;

	private float gameWidth = 480;
	private float stateTime;

	private Animation blinkAnimation;

	private TextureRegion[] blinkFrames;
	private TextureRegion currentFrame;

	public enum GameState {
		PAUSE, DEAD, RUNNING
	}

	private GameState gameState;

	@Override
	public void create () {

		camera = new OrthographicCamera();

		gameState = GameState.RUNNING;

		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		float gameHeight = screenHeight / (screenWidth / gameWidth);

		//camera.setToOrtho(false, 480, 800);
		camera.setToOrtho(false, gameWidth, gameHeight);
		batch = new SpriteBatch();

		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("cheeseball.png"));
		creatureOpenEyed = new Texture(Gdx.files.internal("alienopeneyed.png"));
		creatureCloseEyed = new Texture(Gdx.files.internal("aliencloseeyed.png"));
		monster = new Texture(Gdx.files.internal("enemy.png"));
		cloudImage = new Texture(Gdx.files.internal("cloud.png"));

		blinkFrames = new TextureRegion[20];

		for(int i=0;i<19;i++)
		{
			blinkFrames[i]=new TextureRegion(creatureOpenEyed);
		}
		blinkFrames[19]=new TextureRegion(creatureCloseEyed);

		blinkAnimation = new Animation(0.2f,blinkFrames);



		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		//rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background music immediately
		//rainMusic.setLooping(true);
		//rainMusic.play();

		raindrops = new Array<Rectangle>();
		clouds = new Array<Rectangle>();
		enemies = new Array<Rectangle>();
		spawnRaindrop();
		spawnEnemy();

		bucket = new Rectangle();
		bucket.y = 64;
		bucket.x = gameWidth/2-64/2;
		bucket.width = 64;
		bucket.height = 64;
	}

	@Override
	public void render() {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0.49f, 0.75f, 0.93f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		batch.begin();


		for (Rectangle cloud : clouds) {
			batch.draw(cloudImage, cloud.x, cloud.y);
		}

		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = blinkAnimation.getKeyFrame(stateTime, true);

		//batch.draw(creatureOpenEyed, bucket.x, bucket.y);
		if (gameState == GameState.RUNNING) {
			batch.draw(currentFrame, bucket.x, bucket.y);
		}
		else
		{
			batch.draw(creatureOpenEyed, bucket.x, bucket.y);
		}

		for (Rectangle raindrop : raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}

		for (Rectangle enemy : enemies) {
			batch.draw(monster, enemy.x, enemy.y);
		}


		batch.end();

		if (gameState == GameState.RUNNING) {
			// process user input
			if (Gdx.input.isTouched()) {
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				bucket.x = touchPos.x - 64 / 2;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
				bucket.x -= 200 * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
				bucket.x += 200 * Gdx.graphics.getDeltaTime();

			// make sure the bucket stays within the screen bounds
			if (bucket.x < 0) bucket.x = 0;
			if (bucket.x > gameWidth - 64) bucket.x = gameWidth - 64;

			// check if we need to create a new raindrop
			if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

			if (TimeUtils.nanoTime() - lastCloudTime > 1001000001) spawnClouds();

			if (TimeUtils.nanoTime() - lastEnemyTime > 2001000001) spawnEnemy();

			// move the raindrops, remove any that are beneath the bottom edge of
			// the screen or that hit the bucket. In the later case we play back
			// a sound effect as well.
			Iterator<Rectangle> iter = raindrops.iterator();
			while (iter.hasNext()) {
				Rectangle raindrop = iter.next();
				raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
				if (raindrop.y + 64 < 0) iter.remove();
				if (raindrop.overlaps(bucket)) {
					//dropSound.play();
					iter.remove();
				}
			}

			iter = clouds.iterator();
			while (iter.hasNext()) {
				Rectangle cloud = iter.next();
				cloud.y -= 110 * Gdx.graphics.getDeltaTime();
				if (cloud.y + 64 < 0) iter.remove();

			}

			iter = enemies.iterator();
			while (iter.hasNext()) {
				Rectangle enemy = iter.next();
				enemy.y -= 130 * Gdx.graphics.getDeltaTime();
				if (enemy.y + 64 < 0) iter.remove();
				Gdx.app.log("MyTag", ""+enemy.x);

				//if(enemy.y>64&&enemy.overlaps(bucket)){
				if (collides(enemy, bucket)) {
					Gdx.app.log("enemy.x", ""+enemy.x);
					Gdx.app.log("bucket.x", ""+bucket.x);
					Gdx.app.log("bucket.width", ""+bucket.width);
					Gdx.app.log("enemy.width", ""+enemy.width);
					Gdx.app.log("enemy.y", ""+enemy.y);
					Gdx.app.log("bucket.y", ""+bucket.y);
					Gdx.app.log("bucket.height", ""+bucket.height);
					Gdx.app.log("enemy.height", ""+enemy.height);
					gameState=GameState.DEAD;
				}

			}
		}
	}

	private boolean collides(Rectangle enemy, Rectangle alien)
	{
		//return (enemy.y>0)&&(enemy.x < bucket.x + (bucket.width/2) && enemy.x + (enemy.width/2) > bucket.x &&enemy.y < bucket.y + bucket.height && enemy.y + enemy.height > bucket.y);
		return (enemy.y>64)&&(enemy.x < bucket.x + (bucket.width) && enemy.x + (enemy.width) > bucket.x &&enemy.y+5 < bucket.y + (bucket.height) && enemy.y + (enemy.height) > bucket.y);
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, gameWidth - 64);
		raindrop.y = 800;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	private void spawnClouds() {
		Rectangle cloud = new Rectangle();
		cloud.x = MathUtils.random(0, gameWidth - 64);
		cloud.y = 900;
		cloud.width = 128;
		cloud.height = 128;
		clouds.add(cloud);

		lastCloudTime = TimeUtils.nanoTime();
	}

	private void spawnEnemy() {
		Rectangle monster = new Rectangle();
		monster.x = MathUtils.random(0, gameWidth - 64);
		monster.y = 800;
		monster.width = 55;
		monster.height = 55;
		if(enemies.size<2) {
			enemies.add(monster);
		}

		lastEnemyTime = TimeUtils.nanoTime();
	}


	private void addDropScore()
	{
		dropScore++;
		System.out.println(dropScore);
	}

	@Override
	public void dispose() {
		dropImage.dispose();
		creatureOpenEyed.dispose();
		dropSound.dispose();
		cloudImage.dispose();
	//	rainMusic.dispose();
		batch.dispose();
	}

}
