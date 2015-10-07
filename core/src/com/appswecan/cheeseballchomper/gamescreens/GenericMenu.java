package com.appswecan.cheeseballchomper.gamescreens;

import com.appswecan.cheeseballchomper.helper.FacebookLogger;
import com.appswecan.cheeseballchomper.helper.Menu;
import com.appswecan.cheeseballchomper.helper.Utils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

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

}
