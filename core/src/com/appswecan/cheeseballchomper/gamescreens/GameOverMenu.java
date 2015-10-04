package com.appswecan.cheeseballchomper.gamescreens;

import com.appswecan.cheeseballchomper.CheeseballChomper;
import com.appswecan.cheeseballchomper.helper.AssetLoader;
import com.appswecan.cheeseballchomper.helper.SaveGameHelper;
import com.appswecan.cheeseballchomper.helper.Utils;
import com.appswecan.cheeseballchomper.render.GameRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Varun on 04/10/2015.
 */
public class GameOverMenu implements Screen {

    final CheeseballChomper cheeseballChomper;
    final GameRenderer gameRenderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    private Stage stage;
    private TextureAtlas textureAtlas;
    private Skin skin;
    private Table table;
    private TextButton buttonRetry, buttonExit;
    private Label highScore;
    private Label gameOver;
    private Stack stack;

    public GameOverMenu(int gameWidth, int gameHeight, final CheeseballChomper cheeseballChomper,final GameRenderer gameRenderer) {
        this.cheeseballChomper = cheeseballChomper;
        this.gameRenderer = gameRenderer;
        camera = new OrthographicCamera();
        batch = this.cheeseballChomper.getBatch();
        viewport = new FitViewport(gameWidth, gameHeight, camera);
        stage = new Stage(viewport, batch);
        initAssets();
    }

    private void initAssets() {
        AssetLoader.loadMenuFonts();
        AssetLoader.loadMenuAssets();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.49f, 0.75f, 0.93f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

    }

    @Override
    public void show() {

       Gdx.input.setInputProcessor(stage);

        textureAtlas = AssetLoader.textureAtlas;
        skin = new Skin(textureAtlas);

        table = new Table(skin);
        table.setBounds(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("buttonup");
        textButtonStyle.down = skin.getDrawable("buttondown");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = AssetLoader.menuFont;

        buttonRetry = new TextButton("RETRY", textButtonStyle);
        buttonRetry.addListener(retryButtonListener);
        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonExit.addListener(exitButtonListener);


        Label.LabelStyle highScoreLabelstyle = new Label.LabelStyle();
        highScoreLabelstyle.font = AssetLoader.menuFont;
        highScore = new Label("HIGH SCORE : "+ SaveGameHelper.loadHighScore(),highScoreLabelstyle);
        highScore.setFontScale(0.7f);
        highScore.setAlignment(Align.center);

        gameOver = new Label("GAME OVER",highScoreLabelstyle);
        gameOver.setFontScale(2f);
        gameOver.setAlignment(Align.center);

        table.add(gameOver).width(250).pad(20);
        table.row();
        table.add(buttonRetry).width(250).pad(20);
        table.row();
        table.add(buttonExit).width(250).pad(20);
        table.row();
        table.add(highScore).width(250).pad(20);

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

    private ClickListener retryButtonListener = new ClickListener()
    {
        @Override
        public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {

            table.setTouchable(Touchable.disabled);
            stack.setTouchable(Touchable.disabled);
            playGame();
        }

    };

    private void playGame()
    {
       // dispose();

        cheeseballChomper.setScreen(new GameRenderer(480, 800, this.cheeseballChomper));
    }

    @Override
    public void resize(int width, int height)
    {
        Gdx.app.log("Resize", "tt");
       stage.getViewport().update(width, height, true);

    }

    @Override
    public void dispose()
    {
        AssetLoader.disposeMenuFonts();
        AssetLoader.disposeMenuAssets();
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
    public void hide()
    {

    }
}
