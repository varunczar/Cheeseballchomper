package com.appswecan.cheeseballchomper.helper;

import com.appswecan.cheeseballchomper.gameobjects.Level;
import com.badlogic.gdx.Gdx;

/**
 * Created by Varun on 03/10/2015.
 */
public class LevelDeterminer {


    int cheeseballSpeedSeed=340;
    int monsterOneSpeedSeed=180;
    int monsterTwoSpeedSeed=180;
    int monsterThreeSpeedSeed=190;
    int cloudSpeedSeed=110;

    public void setLevelInformation(int levelNumber, Level level)
    {
        level.setLevelNumber(levelNumber);
        level.setCheeseballSpeed(cheeseballSpeedSeed + (20 * levelNumber));
        level.setMonsterOneSpeed(monsterOneSpeedSeed + (10 * levelNumber));
        level.setMonsterTwoSpeed(monsterTwoSpeedSeed + (2 * levelNumber));
        level.setMonsterThreeSpeed(monsterThreeSpeedSeed + (20 * levelNumber));
        level.setCloudSpeed(cloudSpeedSeed + (20 * levelNumber));
        Gdx.app.log("Level ", level.toString());
    }

    public int determineLevelNumber(int numberOfCheeseballsConsumed)
    {
        if(numberOfCheeseballsConsumed>=10) {
            return numberOfCheeseballsConsumed / 10;
        }
        return 1;
    }

}
