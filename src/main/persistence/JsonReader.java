package persistence;

/*
This class implements a JSON reader object which reads information from a save file of the game
to return a TankGame corresponding to the information read.

ACKNOWLEDGEMENT:
THIS CLASS CODE WAS CREATED BASED ON THE "JsonSerializationDemo" FILE PROVIDED AS AN EXAMPLE.
I TRULY APPRECIATE THE WISDOM GAINED FROM THE ORIGINAL CODE.
    CODES WERE TAKEN FROM THE "JsonReader" CLASS IN:
https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

LAST REVISED: 03/31/2022
 */

import model.Bullet;
import model.tankgame.TankGame;
import model.Wall;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import model.tanks.Tank;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JsonReader {
    private final String source;

    //EFFECTS: creates a Json reader object reading information from the given source
    public JsonReader(String source) {
        this.source = source;
    }

    //EFFECTS: reads the file and returns a TankGame object matching the description
    // in the read JSON file; or throws an IOException if the file isn't found, or
    // its path is invalid
    public TankGame read() throws IOException {
        //convert the given JSON file into String form
        String jsonDataInString = readFileAsString(this.source);
        //cast the String data back into JSON form
        JSONObject jsonObjectInJson = new JSONObject(jsonDataInString);
        //parse the JSON object to create a TankGame object to be returned
        return parseJsonContent(jsonObjectInJson);
    }

    //EFFECTS: reads the source file and return it as a string; or throws an IOException
    // if the file isn't found, or its path is invalid
    private String readFileAsString(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    //EFFECTS: returns the TankGame that matches the given JSON Object data
    private TankGame parseJsonContent(JSONObject data) {
        String name = data.getString("name");
        PlayerTank playerTank = jsonObjectToPlayerTank(data.getJSONObject("playerTank"));
        List<EnemyTank> enemyTanks = jsonArrayToEnemyTankList(data.getJSONArray("enemyTanks"));
        List<Wall> walls = jsonArrayToWallList(data.getJSONArray("walls"));

        return new TankGame(name, playerTank, enemyTanks, walls);
    }

    //EFFECTS: returns the PlayerTank object that matches the given JSON Array data
    private PlayerTank jsonObjectToPlayerTank(JSONObject playerTankJsonObject) {
        Tank baseTank = jsonObjectToTank(playerTankJsonObject);
        return new PlayerTank(baseTank.getX(), baseTank.getY(),
                baseTank.getGunAngle(), baseTank.getBullets());
    }

    //EFFECTS: returns the PlayerTank object that matches the given JSON Array data
    private EnemyTank jsonObjectToEnemyTank(JSONObject enemyTankJsonObject) {
        Tank baseTank = jsonObjectToTank(enemyTankJsonObject);
        return new EnemyTank(baseTank.getX(), baseTank.getY(),
                baseTank.getGunAngle(), baseTank.getBullets());
    }

    //EFFECTS: returns the list of EnemyTank objects that matches the given JSON Array data
    private List<EnemyTank> jsonArrayToEnemyTankList(JSONArray enemyTanksArray) {
        List<EnemyTank> enemyTankList = new ArrayList<>();
        for (int i = 0; i < enemyTanksArray.length(); i++) {
            EnemyTank individualEnemyTank = jsonObjectToEnemyTank(enemyTanksArray.getJSONObject(i));
            enemyTankList.add(individualEnemyTank);
        }
        return enemyTankList;
    }

    //EFFECTS: returns the Tank object that matches the given JSON Array data
    private Tank jsonObjectToTank(JSONObject tankJsonObject) {
        List<Bullet> bulletList = new ArrayList<>();
        int tankPosX = tankJsonObject.getInt("posX");
        int tankPosY = tankJsonObject.getInt("posY");
        int tankGunAngle = tankJsonObject.getInt("gunAngle");
        JSONArray bulletJsonArray = tankJsonObject.getJSONArray("bullets");
        for (int i = 0; i < bulletJsonArray.length(); i++) {
            JSONObject individualJsonBullet = bulletJsonArray.getJSONObject(i);
            int bulletPosX = individualJsonBullet.getInt("posX");
            int bulletPosY = individualJsonBullet.getInt("posY");
            int bulletVelX = individualJsonBullet.getInt("velX");
            int bulletVelY = individualJsonBullet.getInt("velY");
            bulletList.add(new Bullet(bulletPosX, bulletPosY, bulletVelX, bulletVelY));
        }
        return new Tank(tankPosX, tankPosY, tankGunAngle, bulletList);
    }

    //EFFECTS: returns the list of Wall objects that matches the given JSON Array data
    private List<Wall> jsonArrayToWallList(JSONArray wallArray) {
        List<Wall> wallList = new ArrayList<>();
        for (int i = 0; i < wallArray.length(); i++) {
            JSONObject individualJsonWall = wallArray.getJSONObject(i);
            int wallPosX = individualJsonWall.getInt("posX");
            int wallPosY = individualJsonWall.getInt("posY");
            wallList.add(new Wall(wallPosX, wallPosY));
        }
        return wallList;
    }

}
