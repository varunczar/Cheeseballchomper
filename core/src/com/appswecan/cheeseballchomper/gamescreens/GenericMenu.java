package com.appswecan.cheeseballchomper.gamescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by supple on 7/10/2015.
 */
public abstract class GenericMenu {


    protected ClickListener exitButtonListener = new ClickListener(){
        @Override
        public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
            Gdx.app.exit();

        }
    };



}
