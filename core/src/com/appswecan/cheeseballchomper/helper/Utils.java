package com.appswecan.cheeseballchomper.helper;

import com.appswecan.cheeseballchomper.render.GameRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Varun on 04/10/2015.
 */
public class Utils {

    public static enum PUBLISH_STATUS {SUCCESSFULL,ERROR};

    public static Stack loadVolumeAssets(Skin skin){

        if(AssetLoader.volumeOffButton==null || AssetLoader.volumeOnButton==null) {
            AssetLoader.volumeOnButton = new ImageButton(skin.getDrawable("volumeon"));
            AssetLoader.volumeOffButton = new ImageButton(skin.getDrawable("volumeoff"));

            AssetLoader.volumeOnButton.addListener(volumeOnButtonListener);
            AssetLoader.volumeOffButton.addListener(volumeOffButtonListener);
        }
        Stack stack = new Stack();
        stack.add(AssetLoader.volumeOnButton);
        stack.add(AssetLoader.volumeOffButton);
        stack.setBounds(350,14,128,128);

        return stack;
    }

    public static void determineVolumeStatus()
    {
        Gdx.app.log("Volume ",""+AssetLoader.VOLUME);
        if(AssetLoader.VOLUME==1.0f)
        {
            AssetLoader.volumeOffButton.setVisible(false);
            AssetLoader.volumeOnButton.setVisible(true);

        }
        else
        {
            AssetLoader.volumeOnButton.setVisible(false);
            AssetLoader.volumeOffButton.setVisible(true);
        }
    }

    private static ClickListener volumeOffButtonListener = new ClickListener(){
        @Override
        public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                AssetLoader.VOLUME = 1.0f;
                SaveGameHelper.saveVolume();
                determineVolumeStatus();

        }
    };

    private static ClickListener volumeOnButtonListener = new ClickListener(){
        @Override
        public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                AssetLoader.VOLUME = 0.0f;
                SaveGameHelper.saveVolume();
                determineVolumeStatus();
            }
    };

    public static String determineStatus(PUBLISH_STATUS status)
    {
        if(status== Utils.PUBLISH_STATUS.SUCCESSFULL)
        {
           return "High score shared successfully";
        }
        else
        {
            return "Unable to share high score. Please try again";
        }
    }
}
