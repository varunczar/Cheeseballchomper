package com.appswecan.cheeseballchomper.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by Varun on 02/10/2015.
 */
public class SaveGameHelper {

    public static class JsonWorld {
           public int highScore;
        public float volume;
    }


    public static void saveHighScore(int highScore) {
        JsonWorld jWorld = new JsonWorld();

        jWorld.highScore = highScore;
        Json json = new Json();
        writeFile("game.sav", json.toJson(jWorld));
    }

    public static void saveVolume() {
        JsonWorld jWorld = new JsonWorld();
        jWorld.volume = AssetLoader.VOLUME;
        Gdx.app.log("Saving volume : ",""+jWorld.volume);
        Json json = new Json();
        writeFile("config.sav", json.toJson(jWorld));
    }

    public static int loadHighScore() {
        String save = readFile("game.sav");
        if (!save.isEmpty()) {
            int highScore = 0;

            Json json = new Json();
            JsonWorld jWorld = json.fromJson(JsonWorld.class, save);

            highScore = jWorld.highScore;
            return highScore;
        }
        return 0;
    }

    public static float loadVolume() {
        String save = readFile("config.sav");
        if (!save.isEmpty()) {

            Json json = new Json();
            JsonWorld jWorld = json.fromJson(JsonWorld.class, save);
            Gdx.app.log("Loading volume : ",""+jWorld.volume);
            return jWorld.volume;
        }
        Gdx.app.log("Default volume : ",""+AssetLoader.VOLUME);
        return AssetLoader.VOLUME;
    }

    public static void writeFile(String fileName, String s) {
        FileHandle file = Gdx.files.local(fileName);
        file.writeString(com.badlogic.gdx.utils.Base64Coder.encodeString(s), false);
    }

    public static String readFile(String fileName) {
        FileHandle file = Gdx.files.local(fileName);
        if (file != null && file.exists()) {
            String s = file.readString();
            if (!s.isEmpty()) {
                return com.badlogic.gdx.utils.Base64Coder.decodeString(s);
            }
        }
        return "";
    }
}