package com.appswecan.cheeseballchomper.gamescreens;

import com.appswecan.cheeseballchomper.helper.FacebookLogger;
import com.appswecan.cheeseballchomper.helper.Menu;
import com.appswecan.cheeseballchomper.helper.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by supple on 7/10/2015.
 */
public abstract class GenericMenu implements Menu{

    protected Label message;
    protected String messageText = "";
    private static final int DURATION = 5000;
    private long activatedAt = Long.MAX_VALUE;


    public void displayFacebookPublishStatus(Utils.PUBLISH_STATUS status)
    {
        messageText = Utils.determineStatus(status);
        activatedAt = System.currentTimeMillis();
        Gdx.app.log("Posted ",""+status);
    }

    public void postToFacebook()
    {
        FacebookLogger.postToFacebook(this);
    }


    public boolean isActive() {
        return (System.currentTimeMillis() - activatedAt) <= DURATION;
    }

    protected ClickListener exitButtonListener = new ClickListener(){
        @Override
        public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
            Gdx.app.exit();

        }
    };

    protected ClickListener facebookPostButtonListener = new ClickListener(){
        @Override
        public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
            messageText="Sharing High score. Please Wait";
            activatedAt = System.currentTimeMillis();
            postToFacebook();

        }
    };

}
