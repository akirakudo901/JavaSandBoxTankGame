package model.tankgame;

/*
Represents a handler object for the movement of TankGameObject objects, working as a part of tank game

LAST REVISED: 03/31/2022
 */

import model.Bullet;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import model.tanks.Tank;

import java.util.List;

public class MovementHandler {
    private PlayerTank playerTank;
    private List<EnemyTank> enemyTanks;
    private CollisionHandler collisionHandler;
    private List<Bullet> bulletsWithoutTankOwner;

    //EFFECTS: creates a new movement handler which handles movements for the given TankGameObject
    public MovementHandler(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
        this.playerTank = collisionHandler.getPlayerTank();
        this.enemyTanks = collisionHandler.getEnemyTanks();
        this.bulletsWithoutTankOwner = collisionHandler.getBulletsWithoutTankOwner();
    }

    //TODO
    //MODIFIES: this and tanks
    //EFFECTS: moves all the enemy tanks according to AIs, rotate their tank guns
    // according to input, and then deals with wall collision
    // FOR SIMPLICITY, ENEMY TANKS WON'T DO ANYTHING IN PHASE 1
    public void moveAllEnemyTanks() {
        this.enemyTanks.forEach(this::moveEnemyTank);
    }

    //MODIFIES: this and bullets
    //EFFECTS: moves all bullets by 1 tick and then deals with wall collision; removes all bullets with
    // bounce count equal to the max bounce number
    public void moveAllBullets() {
        //for player tank
        moveBulletsForATank(this.playerTank);

        //for enemy tanks
        this.enemyTanks.forEach(this::moveBulletsForATank);

        //for bullets without owner
        this.bulletsWithoutTankOwner.forEach(this::moveBulletInGame);
        this.bulletsWithoutTankOwner.removeIf(b -> b.getBounceCount() >= TankGame.MAX_BOUNCE_COUNT);
    }

    //MODIFIES: this and tank
    //EFFECTS: moves the player's tank according to input, rotates its tank gun
    // according to input, and then deals with wall collision
    public void movePlayerTank(String playerInput) {
        moveATank(playerInput, playerTank);
    }

    public PlayerTank getPlayerTank() {
        return playerTank;
    }

    public List<EnemyTank> getEnemyTanks() {
        return enemyTanks;
    }

    public CollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    // FOR SIMPLICITY, ENEMY TANKS WON'T DO ANYTHING IN PHASE 1
    //TODO
    //MODIFIES: this and tank
    //EFFECTS: moves one enemy tank according to input, rotate its tank gun according to input
    // and then deals with wall collision
    // FOR SIMPLICITY, ENEMY TANKS WON'T DO ANYTHING IN PHASE 1
    private void moveEnemyTank(Tank enemyTank) {
        //TEMPORARY FOR FUN
        moveATank(TankGame.FIRE, enemyTank);
        moveATank(TankGame.ANTI_CLOCKWISE, enemyTank);
    }

    //TODO
    //MODIFIES: this and tank
    //EFFECTS: moves the given tank according to input, rotates its tank gun according to
    // input, and then deals with wall collision
    private void moveATank(String input, Tank movedTank) {
        int[] newPosition;
        int velX = 0;
        int velY = 0;
        moveATankGun(input, movedTank);
        //move movedTank around
        if (input.equals(TankGame.RIGHT)) {
            velX += Tank.SPEED;
        }
        if (input.equals(TankGame.LEFT)) {
            velX -= Tank.SPEED;
        }
        if (input.equals(TankGame.UP)) {
            velY -= Tank.SPEED;
        }
        if (input.equals(TankGame.DOWN)) {
            velY += Tank.SPEED;
        }
        newPosition = this.collisionHandler.getPushedBackPositionIfCollidingOrAdvance(velX, velY, movedTank);
        movedTank.setLocation(newPosition[0], newPosition[1]);
        /* fixme: HANDLE FIRE BULLET RIGHT BEFORE MOVING TANKS IN THE TANKGAME ITSELF
        //fire bullet
        if (input.equals(TankGame.FIRE)) {
            fireBullet(movedTank);
        }

         */
    }

    //MODIFIES: this and tank
    //EFFECTS: moves this tank's gun according to input
    private void moveATankGun(String input, Tank movedTank) {
        switch (input) {
            case TankGame.CLOCKWISE:
                movedTank.rotateGunClockWise();
                break;
            case TankGame.ANTI_CLOCKWISE:
                movedTank.rotateGunAntiClockWise();
                break;
        }
    }

    //MODIFIES: tank and this
    //EFFECTS: moves every bullet for the given tank, and remove every bullet that have exceeded
    // bounce-count
    private void moveBulletsForATank(Tank t) {
        t.getBullets().forEach(this::moveBulletInGame);
        t.getBullets().removeIf(b -> b.getBounceCount() >= TankGame.MAX_BOUNCE_COUNT);
    }

    //MODIFIES: this and bullet
    //EFFECTS: moves a bullet by 1 tick and then deals with wall collision; if the bullet collides
    // with a wall, it turns around and keeps going, and its bounce count is increased by 1
    private void moveBulletInGame(Bullet bullet) {
        if (collisionHandler.isCollidingWithAWallInGame(bullet.getVelX(), bullet.getVelY(), bullet)) {
            collisionHandler.dealWithBulletWallCollision(bullet);
        } else {
            bullet.moveBullet(); //just advance the bullet
        }
    }

}
