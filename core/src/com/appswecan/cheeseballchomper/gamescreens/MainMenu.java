package com.appswecan.cheeseballchomper.gamescreens;

import com.appswecan.cheeseballchomper.CheeseballChomper;
import com.appswecan.cheeseballchomper.helper.AssetLoader;
import com.appswecan.cheeseballchomper.helper.FacebookLogger;
import com.appswecan.cheeseballchomper.helper.Menu;
import com.appswecan.cheeseballchomper.helper.SaveGameHelper;
import com.appswecan.cheeseballchomper.helper.Utils;
import com.appswecan.cheeseballchomper.render.GameRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.event.InputEvent;

/**
 * Created by Varun on 04/10/2015.
 */
public class MainMenu extends GenericMenu implements Screen, Menu {

    final CheeseballChomper cheeseballChomper;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;

    private Stage stage;
    private TextureAtlas textureAtlas;
    private Skin skin;
    private Table table;
    private TextButton buttonPlay, buttonExit;
    private ImageButton facebookButton;
    private Label heading;

    private Stack stack;
    TextureRegion textureRegion;


    public GameRenderer getGameRenderer() {
        return gameRenderer;
    }

    public void setGameRenderer(GameRenderer gameRenderer) {
        this.gameRenderer = gameRenderer;
    }

    private GameRenderer gameRenderer;

    public MainMenu(int gameWidth, int gameHeight, final CheeseballChomper cheeseballChomper) {
        this.cheeseballChomper = cheeseballChomper;
        camera = new OrthographicCamera();
        batch = this.cheeseballChomper.getBatch();
        viewport = new FitViewport(gameWidth, gameHeight, camera);
        stage = new Stage(viewport, batch);
        initAssets();
    }

    private void initAssets() {
        AssetLoader.load();
        AssetLoader.loadMenuFonts();
        AssetLoader.loadMenuAssets();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.49f, 0.75f, 0.93f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        if(textureRegion!=null)
        {
            batch.draw(textureRegion,10,520);
        }
        if(message !=null)
        {
            message.setText(messageText);
        }
        batch.end();
        stage.act(delta);
        if(!isActive())
        {
            messageText="";
        }
        stage.draw();


    }

    @Override
    public void show() {

       Gdx.input.setInputProcessor(stage);

        textureAtlas = AssetLoader.textureAtlas;
        skin = new Skin(textureAtlas);

        table = new Table(skin);
        table.setBounds(0, -80, viewport.getWorldWidth(), viewport.getWorldHeight());

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("buttonup");
        textButtonStyle.down = skin.getDrawable("buttondown");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = AssetLoader.menuFont;

        ImageButtonStyle imageButtonStyle = new ImageButtonStyle();
        imageButtonStyle.up = skin.getDrawable("facebook");
        imageButtonStyle.down = skin.getDrawable("facebook");
        imageButtonStyle.pressedOffsetX = 1;
        imageButtonStyle.pressedOffsetY = -1;

        buttonPlay = new TextButton("PLAY", textButtonStyle);
        buttonPlay.addListener(playButtonListener);
        buttonExit = new TextButton("EXIT", textButtonStyle);
        buttonExit.addListener(exitButtonListener);

        textureRegion = skin.get("ccgamelogo", TextureRegion.class);

        facebookButton = new ImageButton(imageButtonStyle);
        facebookButton.addListener(facebookPostButtonListener);

        LabelStyle highScoreLabelstyle = new LabelStyle();
        highScoreLabelstyle.font = AssetLoader.menuFont;
        heading = new Label("HIGH SCORE : "+ SaveGameHelper.loadHighScore(),highScoreLabelstyle);
        heading.setFontScale(0.7f);
        heading.setAlignment(Align.center);

        message = new Label("",highScoreLabelstyle);
        message.setFontScale(0.5f);
        message.setAlignment(Align.center);



        table.row();
        table.add(buttonPlay).width(250).pad(20);
        table.row();
        table.add(buttonExit).width(250).pad(20);
        table.row();
        table.add(heading).pad(20);
        table.row();
        table.add(facebookButton).pad(5);
        table.row();
        table.add(message).width(250).pad(20);

        stage.addActor(table);
        stack = Utils.loadVolumeAssets(skin);

        stage.addActor(stack);

        Utils.determineVolumeStatus();


    }



    private ClickListener playButtonListener = new ClickListener()
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
        this.gameRenderer = new GameRenderer(480, 800, this.cheeseballChomper);
        cheeseballChomper.setScreen(gameRenderer);
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
        AssetLoader.dispose();
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
