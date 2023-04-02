package persistence;

//LAST REVISED: 03/31/2022

import model.Bullet;
import model.tankgame.TankGame;
import model.Wall;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    TankGame testTankGame;
    PlayerTank playerTank;
    EnemyTank enemyTank1;
    EnemyTank enemyTank2;
    List<EnemyTank> enemyTankList;
    Wall wall1;
    Wall wall2;
    List<Wall> wallList;
    Bullet bullet1;
    Bullet bullet2;
    List<Bullet> bulletList1;
    List<Bullet> bulletList2;

    @BeforeEach
    void runBeforeEach() {
        playerTank = new PlayerTank(200, 300, 55);
        enemyTank1 = new EnemyTank(300, 200, 89);
        enemyTank2 = new EnemyTank(98, 188, 310);
        wall1 = new Wall(80, 120);
        wall2 = new Wall(360, 160);
        bullet1 = new Bullet(200, 90, 30, -10);
        bullet2 = new Bullet(60, 180, -5, 19);
        wallList = new ArrayList<>();
        enemyTankList = new ArrayList<>();
        bulletList1 = new ArrayList<>();
        bulletList1.add(bullet1);
        bulletList2 = new ArrayList<>();
        bulletList2.add(bullet2);
    }

    @Test
    void testWriterIOExceptionThrown() {
        //setup
        testTankGame = new TankGame("close to empty", playerTank, enemyTankList, wallList);
        //execute & check
        try {
            JsonWriter writer = new JsonWriter("./data/\0illegalcharacter.json");
            writer.open();
            writer.write(testTankGame);
            writer.close();
            fail("Expected IOException not thrown.");
        } catch (IOException e) {
            //expected exception thrown
        }
    }

    @Test
    void testWriterTankGameCloseToEmpty() {
        //setup
        testTankGame = new TankGame("close to empty", playerTank, enemyTankList, wallList);
        //execute & check
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterTankGameCloseToEmpty.json");
            writer.open();
            writer.write(testTankGame);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterTankGameCloseToEmpty.json");
            TankGame readTankGame = reader.read();
            assertEquals(testTankGame.getName(), readTankGame.getName()); //name
            assertTrue(testTankGame.getPlayerTank().hasIdenticalProperties( //playerTank
                    readTankGame.getPlayerTank()));
            for (int i = 0; i < testTankGame.getEnemyTanks().size(); i++) { //enemyTanks
                assertTrue(testTankGame.getEnemyTanks().get(i).hasIdenticalProperties(
                        readTankGame.getEnemyTanks().get(i)));
            }
            for (int j = 0; j < testTankGame.getWalls().size(); j++) { //walls
                assertTrue(testTankGame.getWalls().get(j).hasIdenticalProperties(
                        readTankGame.getWalls().get(j)));
            }
        } catch (IOException e) {
            fail("Unexpected IOException thrown.");
        }
    }

    @Test
    void testWriterGeneralTankGame() {
        //setup
        playerTank = new PlayerTank(200, 300, 55, bulletList1);
        enemyTank1 = new EnemyTank(300, 200, 89, bulletList2);
        enemyTankList.add(enemyTank1);
        enemyTankList.add(enemyTank2);
        wallList.add(wall1);
        wallList.add(wall2);
        testTankGame = new TankGame("general", playerTank, enemyTankList, wallList);
        //execute & check
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralTankGame.json");
            writer.open();
            writer.write(testTankGame);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralTankGame.json");
            TankGame readTankGame = reader.read();
            assertEquals(testTankGame.getName(), readTankGame.getName()); //name
            assertTrue(testTankGame.getPlayerTank().hasIdenticalProperties( //playerTank
                    readTankGame.getPlayerTank()));
            for (int i = 0; i < testTankGame.getEnemyTanks().size(); i++) { //enemyTanks
                assertTrue(testTankGame.getEnemyTanks().get(i).hasIdenticalProperties(
                        readTankGame.getEnemyTanks().get(i)));
            }
            for (int j = 0; j < testTankGame.getWalls().size(); j++) { //walls
                assertTrue(testTankGame.getWalls().get(j).hasIdenticalProperties(
                        readTankGame.getWalls().get(j)));
            }
        } catch (IOException e) {
            fail("Unexpected IOException thrown.");
        }
    }

}
