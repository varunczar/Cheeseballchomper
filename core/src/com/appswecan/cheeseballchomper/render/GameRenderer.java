package com.appswecan.cheeseballchomper.render;

import com.appswecan.cheeseballchomper.CheeseballChomper;
import com.appswecan.cheeseballchomper.gameobjects.Cheeseball;
import com.appswecan.cheeseballchomper.gameobjects.Cloud;
import com.appswecan.cheeseballchomper.gameobjects.Hero;
import com.appswecan.cheeseballchomper.gamescreens.GameOverMenu;
import com.appswecan.cheeseballchomper.helper.AssetLoader;
import com.appswecan.cheeseballchomper.helper.LevelDeterminer;
import com.appswecan.cheeseballchomper.gameobjects.Level;
import com.appswecan.cheeseballchomper.helper.SaveGameHelper;
import com.appswecan.cheeseballchomper.helper.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

/**
 * Created by Varun on 03/10/2015.
 */
public class GameRenderer implements Screen,InputProcessor{

    private OrthographicCamera camera;

    private SpriteBatch batch;

    private GameOverMenu gameOverMenu;

    private Hero hero;
    private Array<Cheeseball> cheeseballs;
    private Array<Cloud> clouds;
    private Array<Rectangle> monstersOne;
    private Array<Rectangle> monstersTwo;
    private Array<Rectangle> monstersThree;

    private long lastDropTime;
    private long lastCloudTime;
    final CheeseballChomper cheeseballChomper;
    private long lastMonsterOneTime;
    private long lastMonsterTwoTime;
    private long lastMonsterThreeTime;

    private int dropScore = 0;
    private Level currentLevel;
    private float gameWidth = 480;
    private float stateTime;
    private int currentLevelNumber;

    private Viewport viewport;

    private TextureRegion currentFrame;

    private int numberOfCheeseBallsConsumed=0;
    private LevelDeterminer levelDeterminer;

    private TextureAtlas textureAtlas;
    private Skin skin;
    private Table table;
    private TextButton buttonResume, buttonRetry, buttonExit;
    private Label highScoreLabel;
    private Stack stack;


    private Stage stage;

    public static enum GameState {
        PAUSE, DEAD, RUNNING
    }

    public static GameState gameState;
    private int highScore;

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public GameRenderer(int gameWidth, int gameHeight,final CheeseballChomper cheeseballChomper) {

        camera = new OrthographicCamera();
        viewport = new FitViewport(gameWidth, gameHeight, camera);
        this.batch = cheeseballChomper.getBatch();
        this.cheeseballChomper = cheeseballChomper;
        this.gameOverMenu = new GameOverMenu(gameWidth,gameHeight,cheeseballChomper,this);

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);
        //initAssets();
        initGameObjects();
    }

   /* private void initAssets()
    {
        AssetLoader.load();
    }*/

    @Override
    public void render(float delta)
    {
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

        renderClouds();

        renderHero();

        renderCheeseballs();

        renderMonsters();

        displayStats();

        batch.end();



        if (gameState == GameState.RUNNING) {
            table.setTouchable(Touchable.disabled);
            stack.setTouchable(Touchable.disabled);
            runGameLoop();
        }
        else if (gameState == GameState.PAUSE) {
            table.setTouchable(Touchable.enabled);
            stack.setTouchable(Touchable.enabled);
            table.setVisible(true);
            stack.setVisible(true);
            stage.act(delta);
            stage.draw();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            gameState=GameState.PAUSE;
        }
    }

    private void runGameLoop() {
        processHeroMovement();

        spawnGameObjectsIfNeeded();

        processCheeseBalls();

        processClouds();

        processMonsterOne();

        processMonsterTwo();

        processMonsterThree();
    }

    private void processMonsterOne()
    {
        Iterator<Rectangle> monsterOneIterator = monstersOne.iterator();
        while (monsterOneIterator.hasNext()) {
            Rectangle enemy = monsterOneIterator.next();

            enemy.y -= currentLevel.getMonsterOneSpeed() * Gdx.graphics.getDeltaTime();
            if (enemy.y + 64 < 0) monsterOneIterator.remove();

            if (collides(enemy, hero)) {
                handleDeath();
            }

        }
    }

    private void processMonsterTwo()
    {
        Iterator<Rectangle> monsterTwoIterator = monstersTwo.iterator();
        while (monsterTwoIterator.hasNext()) {
            Rectangle enemy = monsterTwoIterator.next();
            enemy.y -= currentLevel.getMonsterTwoSpeed() * Gdx.graphics.getDeltaTime();
            if (enemy.y + 64 < 0) monsterTwoIterator.remove();
            if (collides(enemy, hero)) {
                handleDeath();
            }

        }
    }

    private void processMonsterThree()
    {
        Iterator<Rectangle> monsterThreeIterator = monstersThree.iterator();
        while (monsterThreeIterator.hasNext()) {
            Rectangle enemy = monsterThreeIterator.next();
            enemy.y -= currentLevel.getMonsterThreeSpeed() * Gdx.graphics.getDeltaTime();
            if (enemy.y + 64 < 0) monsterThreeIterator.remove();
            if (collides(enemy, hero)) {
                handleDeath();
            }

        }
    }

    private void processClouds()
    {
        Iterator<Cloud> cloudsIterator = clouds.iterator();
        while (cloudsIterator.hasNext()) {
            Rectangle cloud = cloudsIterator.next();
            cloud.y -= currentLevel.getCloudSpeed() * Gdx.graphics.getDeltaTime();
            if (cloud.y + 64 < 0) cloudsIterator.remove();

        }
    }

    private void processCheeseBalls()
    {
        Iterator<Cheeseball> iter = cheeseballs.iterator();
        while (iter.hasNext()) {
            Cheeseball cheeseball = iter.next();
            cheeseball.y -= currentLevel.getCheeseballSpeed() * Gdx.graphics.getDeltaTime();
            if (cheeseball.y + 64 < 0) iter.remove();
            if (cheeseball.overlaps(hero)) {
                AssetLoader.dropSound.play(AssetLoader.VOLUME);
                addDropScore();
                iter.remove();
            }
        }
    }

    private void spawnGameObjectsIfNeeded() {
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnCheeseBalls();

        if (TimeUtils.nanoTime() - lastCloudTime > 1001000001) spawnClouds();

        if (TimeUtils.nanoTime() - lastMonsterOneTime > 1001000001) spawnMonsterOne();

        if (TimeUtils.nanoTime() - lastMonsterTwoTime > 2001000001) spawnMonsterTwo();

        if (TimeUtils.nanoTime() - lastMonsterThreeTime > 2001000001) spawnMonsterThree();
    }

    private void processHeroMovement() {

        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            hero.x = touchPos.x - 64 / 2;

        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            hero.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            hero.x += 200 * Gdx.graphics.getDeltaTime();

        // make sure the bucket stays within the screen bounds
        if (hero.x < 0) hero.x = 0;
        if (hero.x > gameWidth - 64) hero.x = gameWidth - 64;
    }

    private void renderMonsters() {
        for (Rectangle enemy : monstersOne) {
            batch.draw(AssetLoader.monsterOne, enemy.x, enemy.y);
        }

        for (Rectangle enemy : monstersTwo) {
            batch.draw(AssetLoader.monsterTwo, enemy.x, enemy.y);
        }

        for (Rectangle enemy : monstersThree) {
            batch.draw(AssetLoader.monsterThree, enemy.x, enemy.y);
        }
    }

    private void displayStats() {
        AssetLoader.font.draw(batch, "LEVEL " + currentLevelNumber, 30, 780);
        AssetLoader.font.draw(batch, String.format("%07d", dropScore), 185, 780);
        AssetLoader.font.draw(batch, String.format("%07d", highScore), 340, 780);
    }

    private void renderCheeseballs() {
        for (Cheeseball cheeseball : cheeseballs) {
            batch.draw(AssetLoader.cheeseballImage, cheeseball.x, cheeseball.y);
        }
    }

    private void renderHero() {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = AssetLoader.blinkAnimation.getKeyFrame(stateTime, true);

        //batch.draw(creatureOpenEyed, bucket.x, bucket.y);
        if (gameState == GameState.RUNNING) {
            batch.draw(currentFrame, hero.x, hero.y);
        }
        else
        {
            batch.draw(AssetLoader.creatureOpenEyed, hero.x, hero.y);
        }
    }

    private void renderClouds() {
        for (Rectangle cloud : clouds) {
            batch.draw(AssetLoader.cloudImage, cloud.x, cloud.y);
        }
    }

    private void determineAndSetLevel(int numberOfCheeseBalls)
    {
        int calculatedLevelNumber = levelDeterminer.determineLevelNumber(numberOfCheeseBalls);
        if(calculatedLevelNumber!=currentLevelNumber)
        {
            currentLevelNumber=calculatedLevelNumber;
            levelDeterminer.setLevelInformation(currentLevelNumber,currentLevel);
        }
    }

    private boolean collides(Rectangle enemy, Rectangle hero)
    {
        return (enemy.y>32)&&(enemy.x < hero.x + (hero.width) && enemy.x + (enemy.width) > hero.x &&enemy.y+5 < hero.y + (hero.height) && enemy.y + (enemy.height) > hero.y);
    }

    private void spawnCheeseBalls() {

        Cheeseball cheeseball = new Cheeseball(MathUtils.random(0, gameWidth - 64));
        cheeseballs.add(cheeseball);
        lastDropTime = TimeUtils.nanoTime();
    }

    private void spawnClouds() {
        Cloud cloud = new Cloud(MathUtils.random(0, gameWidth - 64));
        clouds.add(cloud);
        lastCloudTime = TimeUtils.nanoTime();
    }

    private void spawnMonsterOne() {
        Rectangle monster = new Rectangle();
        //monster.x = MathUtils.random(0, gameWidth - 64);
        monster.x=hero.x;
        monster.y = 800;
        monster.width = 55;
        monster.height = 55;
        if(monstersOne.size<2) {
            monstersOne.add(monster);
        }

        lastMonsterOneTime = TimeUtils.nanoTime();
    }

    private void spawnMonsterTwo() {

        if(currentLevelNumber>10) {
            Rectangle monster = new Rectangle();
            monster.x = MathUtils.random(0, gameWidth - 64);
            monster.y = 800;
            monster.width = 55;
            monster.height = 55;
            if (monstersTwo.size < 2) {
                monstersTwo.add(monster);
            }

            lastMonsterTwoTime = TimeUtils.nanoTime();
        }
    }

    private void spawnMonsterThree() {
        if(currentLevelNumber>20) {
            Rectangle monster = new Rectangle();
            monster.x = MathUtils.random(0, gameWidth - 64);
            monster.y = 800;
            monster.width = 55;
            monster.height = 55;
            if (monstersThree.size < 2) {
                monstersThree.add(monster);
            }

            lastMonsterThreeTime = TimeUtils.nanoTime();
        }
    }

    public void calculateHighScore(int currentScore)
    {
        if(currentScore>highScore)
        {
            highScore=currentScore;
            com.appswecan.cheeseballchomper.helper.SaveGameHelper.saveHighScore(highScore);
        }
    }

    private void addDropScore()
    {
        dropScore++;
        numberOfCheeseBallsConsumed++;
        calculateHighScore(dropScore);
        determineAndSetLevel(numberOfCheeseBallsConsumed);
    }

    private void initGameObjects()
    {
        currentLevel = new Level();
        initGameState();
        hero = new Hero(gameWidth/2-64/2);
        dropScore=0;
        highScore = com.appswecan.cheeseballchomper.helper.SaveGameHelper.loadHighScore();
        cheeseballs = new Array<Cheeseball>();
        clouds = new Array<Cloud>();
        monstersOne = new Array<Rectangle>();
        monstersTwo = new Array<Rectangle>();
        monstersThree = new Array<Rectangle>();
        spawnCheeseBalls();
        spawnMonsterOne();

    }

    public void initGameState()
    {
        gameState = GameState.RUNNING;
        levelDeterminer = new LevelDeterminer();
        currentLevelNumber=1;
        levelDeterminer.setLevelInformation(1, currentLevel);
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose()
    {
        AssetLoader.dispose();
    }

    @Override
    public void resume()
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(stage);

        textureAtlas = AssetLoader.textureAtlas;
        skin = new Skin(textureAtlas);

        table = new Table(skin);
        table.setBounds(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("buttonup");
        textButtonStyle.down = skin.getDrawable("buttondown");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = AssetLoader.menuFont;

        buttonResume = new TextButton("RESUME", textButtonStyle);
        buttonResume.addListener(resumeButtonListener);
        buttonRetry = new TextButton("RESTART", textButtonStyle);
        buttonRetry.addListener(restartButtonListener);
        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonExit.addListener(exitButtonListener);


        Label.LabelStyle highScoreLabelstyle = new Label.LabelStyle();
        highScoreLabelstyle.font = AssetLoader.menuFont;
        highScoreLabel = new Label("HIGH SCORE : "+ SaveGameHelper.loadHighScore(),highScoreLabelstyle);
        highScoreLabel.setFontScale(0.7f);
        highScoreLabel.setAlignment(Align.center);


        table.add(buttonResume).width(250).pad(20);
        table.row();
        table.add(buttonRetry).width(250).pad(20);
        table.row();
        table.add(buttonExit).width(250).pad(20);
        table.row();
        table.add(highScoreLabel).width(250).pad(20);

        stage.addActor(table);

        stack= Utils.loadVolumeAssets(skin);
        stage.addActor(stack);
    }

    private ClickListener exitButtonListener = new ClickListener(){
        @Override
        public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
            Gdx.app.exit();
            /*AssetLoader.disposeMenuFonts();
            AssetLoader.disposeMenuAssets();*/
        }
    };

    private ClickListener resumeButtonListener = new ClickListener()
    {
        @Override
        public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {

            table.setTouchable(Touchable.disabled);
            stack.setTouchable(Touchable.disabled);
            table.setVisible(false);
            stack.setVisible(false);
            gameState=GameState.RUNNING;
          //  playGame();
        }

    };

    private ClickListener restartButtonListener = new ClickListener()
    {
        @Override
        public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {

            table.setTouchable(Touchable.disabled);
            stack.setTouchable(Touchable.disabled);
            table.setVisible(false);
            stack.setVisible(false);
            initGameObjects();
            gameState=GameState.RUNNING;
            //  playGame();
        }

    };


    @Override
    public void hide()
    {

    }


    private void handleDeath()
    {
        gameState=GameState.DEAD;
        AssetLoader.gameOverSound.play(AssetLoader.VOLUME);
        this.cheeseballChomper.setScreen(gameOverMenu);

    }

    /** Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyDown (int keycode)
    {
        Gdx.app.log("Back Button ","Pressed");
        if(keycode == Input.Keys.BACK){
            // Optional back button handling (e.g. ask for confirmation)
           Gdx.app.log("Back Button ","Pressed");
                Gdx.app.exit();
        }
        return false;

    }

    /** Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    public boolean keyUp (int keycode)
    {
        return false;
    }


    /** Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed */
    public boolean keyTyped (char character) {
        return false;
    }


    /** Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        return false;
    }


    /** Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        return false;
    }


    /** Called when a finger or the mouse was dragged.
     * @param pointer the pointer for the event.
     * @return whether the input was processed */
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        return false;
    }


    /** Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     * @return whether the input was processed */
    public boolean mouseMoved (int screenX, int screenY) {
        return false;
    }


    /** Called when the mouse wheel was scrolled. Will not be called on iOS.
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed. */
    public boolean scrolled (int amount)
    {
        return false;
    }


}
