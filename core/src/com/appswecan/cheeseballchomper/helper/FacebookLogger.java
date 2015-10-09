package com.appswecan.cheeseballchomper.helper;

import com.appswecan.cheeseballchomper.render.GameRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;

import java.util.ArrayList;
import java.util.List;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphResult;
import de.tomgrill.gdxfacebook.core.GDXFacebookLoginResult;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;

/**
 * Created by supple on 6/10/2015.
 */
public class FacebookLogger {

    public static void postToFacebook(final Menu menu)
    {
        GDXFacebookConfig config = new GDXFacebookConfig();
        config.APP_ID = "694373700695185";

        List<String> permissions = new ArrayList<String>();
        permissions.add("publish_actions");

        final GDXFacebook facebook = GDXFacebookSystem.install(config);

        facebook.loginWithPublishPermissions(permissions, new GDXFacebookCallback<GDXFacebookLoginResult>() {
            @Override
            public void onSuccess(GDXFacebookLoginResult result) {
                // Login successful
                GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me/feed").useCurrentAccessToken();
                request.setMethod(Net.HttpMethods.POST);
                request.putField("message", "My High Score on Cheeseball Chomper is : " + SaveGameHelper.loadHighScore() + ". What's yours?");
                request.putField("link", "http://www.cheeseballchomper.com");
                request.putField("picture","http://www.cheeseballchomper.com/images/facebooklogo.png");
                request.putField("caption", "Download Cheeseball Chomper for free here");//. The ultimate addictive game for all ages this season.");

                facebook.newGraphRequest(request, new GDXFacebookCallback<GDXFacebookGraphResult>() {

                    @Override
                    public void onSuccess(GDXFacebookGraphResult result) {
                        // Success
                        menu.displayFacebookPublishStatus(Utils.PUBLISH_STATUS.SUCCESSFULL);
                    }

                    @Override
                    public void onError(GDXFacebookError error) {
                        menu.displayFacebookPublishStatus(Utils.PUBLISH_STATUS.ERROR);
                    }

                    @Override
                    public void onFail(Throwable t) {
                        menu.displayFacebookPublishStatus(Utils.PUBLISH_STATUS.ERROR);
                    }

                    @Override
                    public void onCancel() {
                        // Cancel
                    }

                });

            }

            @Override
            public void onError(GDXFacebookError error) {
                // Error handling
            }

            @Override
            public void onCancel() {
                // When the user cancels the login process
            }

            @Override
            public void onFail(Throwable t) {
                // When the login fails
            }
        });
    }


}
