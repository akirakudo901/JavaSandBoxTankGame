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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonReaderTest {
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
        playerTank = new PlayerTank(210, 74, 81);
        enemyTank1 = new EnemyTank(130, 69, 215);
        enemyTank2 = new EnemyTank(188, 98, 170);
        wall1 = new Wall(160, 200);
        wall2 = new Wall(160, 360);
        bullet1 = new Bullet(50, 108, 20, -2);
        bullet2 = new Bullet(200, 234, -88, 13);
        wallList = new ArrayList<>();
        enemyTankList = new ArrayList<>();
        bulletList1 = new ArrayList<>();
        bulletList1.add(bullet1);
        bulletList2 = new ArrayList<>();
        bulletList2.add(bullet2);
    }

    @Test
    void testReaderIOExceptionThrown() {
        //setup
        String source = "./data/nonExistentFile";
        testTankGame = new TankGame("supposed to be failing", playerTank, enemyTankList, wallList);
        //execute & check
        try {
            JsonReader reader = new JsonReader(source);
            reader.read();
            fail("Expected IOException not thrown.");
        } catch (IOException e) {
            //expected exception thrown
        }
    }

    @Test
    void testReaderTankGameCloseToEmpty() {
        //setup
        String source = "./data/testReaderTankGameCloseToEmpty.json";
        testTankGame = new TankGame("reader close to empty", playerTank, enemyTankList, wallList);
        writeReaderTankGameToFile(source, testTankGame);
        //execute & check
        try {
            JsonReader reader = new JsonReader(source);
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
    void testReaderGeneralTankGame() {
        //setup
        String source = "./data/testReaderGeneralTankGame.json";
        playerTank = new PlayerTank(210, 74, 81, bulletList1);
        enemyTank1 = new EnemyTank(130, 69, 215, bulletList2);
        enemyTankList.add(enemyTank1);
        enemyTankList.add(enemyTank2);
        wallList.add(wall1);
        wallList.add(wall2);
        testTankGame = new TankGame("reader general", playerTank, enemyTankList, wallList);
        writeReaderTankGameToFile(source, testTankGame);
        //execute & check
        try {
            JsonReader reader = new JsonReader(source);
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

    //MODIFIES: destination file
    //EFFECTS: writes the given tankGame to the destination file; or thrown error if the
    // destination file cannot be opened for writing
    private void writeReaderTankGameToFile(String destination, TankGame tankGame) {
        JsonWriter writer = new JsonWriter(destination);
        try{
            writer.open();
            writer.write(tankGame);
            writer.close();
        } catch (IOException e) {
            fail("Unexpected IOException called from inside \"writerReaderTankGameToFile\" method.");
        }
    }

}
