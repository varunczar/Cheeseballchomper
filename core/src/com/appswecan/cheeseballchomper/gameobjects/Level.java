package com.appswecan.cheeseballchomper.gameobjects;

/**
 * Created by Varun on 03/10/2015.
 */
public class Level {

    private int levelNumber;
    private int cheeseballSpeed;
    private int monsterOneSpeed;



    private int monsterTwoSpeed;
    private int monsterThreeSpeed;
    private int cloudSpeed;

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getCheeseballSpeed() {
        return cheeseballSpeed;
    }

    public void setCheeseballSpeed(int cheeseballSpeed) {
        this.cheeseballSpeed = cheeseballSpeed;
    }

    public int getMonsterOneSpeed() {
        return monsterOneSpeed;
    }

    public void setMonsterOneSpeed(int monsterOneSpeed) {
        this.monsterOneSpeed = monsterOneSpeed;
    }

    public int getCloudSpeed() {
        return cloudSpeed;
    }

    public void setCloudSpeed(int cloudSpeed) {
        this.cloudSpeed = cloudSpeed;
    }


    public int getMonsterTwoSpeed() {
        return monsterTwoSpeed;
    }

    public void setMonsterTwoSpeed(int monsterTwoSpeed) {
        this.monsterTwoSpeed = monsterTwoSpeed;
    }

    public int getMonsterThreeSpeed() {
        return monsterThreeSpeed;
    }

    public void setMonsterThreeSpeed(int monsterThreeSpeed) {
        this.monsterThreeSpeed = monsterThreeSpeed;
    }
}
