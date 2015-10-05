package com.appswecan.cheeseballchomper.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import java.awt.Rectangle;

/**
 * Created by Varun on 03/10/2015.
 */
public class AssetLoader {

    public static Texture cheeseballImage;
    public static Texture creatureOpenEyed;
    public static Texture creatureCloseEyed;
    public static Texture monsterOne;
    public static Texture monsterTwo;
    public static Texture monsterThree;
    public static Texture cloudImage;
    public static Sound dropSound;
    public static Sound gameOverSound;
    public static Animation blinkAnimation;
    public static TextureRegion[] blinkFrames;
    public static BitmapFont font;
    public static BitmapFont menuFont;
    private static FreeTypeFontGenerator generator;
    private static FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    public static TextureAtlas textureAtlas;
    public static float VOLUME = 1.0f;

    public static ImageButton volumeOnButton;
    public static ImageButton volumeOffButton;


    public static void load()
    {
        // load the images for the droplet and the bucket, 64x64 pixels each
        cheeseballImage = new Texture(Gdx.files.internal("cheeseball.png"));
        creatureOpenEyed = new Texture(Gdx.files.internal("alienopeneyed.png"));
        creatureCloseEyed = new Texture(Gdx.files.internal("aliencloseeyed.png"));
        monsterOne = new Texture(Gdx.files.internal("enemy.png"));
        monsterTwo = new Texture(Gdx.files.internal("monster2.png"));
        monsterThree = new Texture(Gdx.files.internal("monster3.png"));
        cloudImage = new Texture(Gdx.files.internal("cloud.png"));

        dropSound = Gdx.audio.newSound(Gdx.files.internal("funny_bite.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("beep_pop_sound.mp3"));


        blinkFrames = new TextureRegion[20];

        for(int i=0;i<19;i++)
        {
            blinkFrames[i]=new TextureRegion(creatureOpenEyed);
        }
        blinkFrames[19]=new TextureRegion(creatureCloseEyed);

        blinkAnimation = new Animation(0.2f,blinkFrames);

        VOLUME = SaveGameHelper.loadVolume();

        loadFonts();


    }

    public static void loadFonts()
    {
        if(generator==null) {
            generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo-Black.otf"));
            parameter = new FreeTypeFontParameter();
        }
        parameter.size = 24;
        parameter.color= Color.valueOf("FFFFFF");
        parameter.borderColor=Color.valueOf("191970");
        parameter.borderWidth=1;
        font = generator.generateFont(parameter); // font size 12 pixels
    }

    public static void loadMenuAssets()
    {

       textureAtlas = new TextureAtlas("ui/ui.pack");

    }

    public static void loadMenuFonts()
    {
        if(generator==null) {
            generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Exo-Black.otf"));
            parameter = new FreeTypeFontParameter();
        }
        parameter.size = 36;
        parameter.color= Color.valueOf("FFFFFF");
        parameter.borderColor=Color.valueOf("000000");
        parameter.borderWidth=2;

        menuFont = generator.generateFont(parameter); // font size 12 pixels
    }

    public static void disposeFonts()
    {
        font.dispose();

    }

    public static void disposeMenuFonts()
    {
        menuFont.dispose();

    }

    public static void disposeMenuAssets()
    {
        textureAtlas.dispose();

    }

    public static void dispose()
    {
        cheeseballImage.dispose();
        creatureOpenEyed.dispose();
        creatureCloseEyed.dispose();
        monsterOne.dispose();
        monsterTwo.dispose();
        monsterThree.dispose();
        cloudImage.dispose();
        dropSound.dispose();
        disposeFonts();
        gameOverSound.dispose();
    }
}
