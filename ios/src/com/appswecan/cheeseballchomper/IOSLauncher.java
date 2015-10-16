package com.appswecan.cheeseballchomper;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSPropertyList;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.pods.google.mobileads.GADAdSize;
import org.robovm.pods.google.mobileads.GADBannerView;
import org.robovm.pods.google.mobileads.GADRequest;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.appswecan.cheeseballchomper.CheeseballChomper;

public class IOSLauncher extends IOSApplication.Delegate {

    private UIViewController rootViewController;
    private GADBannerView bannerView;

    private static final String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new CheeseballChomper(null), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }


    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        // I know kinda weird. But keep it this way for now.
        boolean finished = super.didFinishLaunching(application, launchOptions);
        this.createAd();
        return finished;
    }

    /**
     * Creates a new admob ad
     */
    private void createAd() {
        rootViewController = UIApplication.getSharedApplication().getKeyWindow().getRootViewController();
        bannerView = new GADBannerView(GADAdSize.Banner());
        CGSize screenSize = UIScreen.getMainScreen().getBounds().getSize();
        CGSize adSize = bannerView.getBounds().getSize();

        //Portrait bottom of screen banner

        double adWidth = adSize.getWidth();
        double adHeight = adSize.getHeight();
        double screenHeight = screenSize.getHeight();
        double screenWidth = screenSize.getWidth();
        float bannerWidth = (float) screenWidth;
        float bannerHeight = (float) (bannerWidth / adWidth * adHeight);
        double adX = (screenWidth / 2) - (adWidth / 2);
        double adY = screenHeight - bannerHeight;

        //Landscape bottom screen banner
        /*
        double adWidth = adSize.getWidth();
        double adHeight = adSize.getHeight();
        double screenHeight = screenSize.getHeight();
        double screenWidth = screenSize.getWidth();
        float bannerWidth = (float) screenWidth/2;
        float bannerHeight = (float) ((float) screenHeight/10.0);
        double adX = (screenWidth / 2) - (adWidth / 2);
        double adY = 0;
        */

        bannerView.setFrame(new CGRect(adX, adY, bannerWidth, bannerHeight));


        bannerView.setAdUnitID(BANNER_AD_UNIT_ID);
        rootViewController.getView().addSubview(bannerView);

        bannerView.setRootViewController(rootViewController);

        GADRequest request = new GADRequest();

        bannerView.loadRequest(request);

        this.showAds(true);
    }

    public void showAds(boolean show) {
        if (bannerView != null) {
            bannerView.setHidden(!show);
        }
    }


}