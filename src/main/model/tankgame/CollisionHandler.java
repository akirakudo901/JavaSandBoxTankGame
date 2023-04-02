package model.tankgame;

/*
Represents a handler for collision between tank game objects, working as a part of the TankGame object

LAST REVISED: 03/31/2022
 */

import model.Bullet;
import model.TankGameObject;
import model.Wall;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import model.tanks.Tank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollisionHandler {
    private PlayerTank playerTank;
    private List<EnemyTank> enemyTanks;
    private List<Wall> walls;
    private List<Bullet> bulletsWithoutTankOwner;

    //EFFECTS: creates a new collision handler object which handles the given objects' collision
    public CollisionHandler(PlayerTank playerTank, List<EnemyTank> enemyTanks, List<Wall> walls,
                            List<Bullet> bulletsWithoutTankOwner) {
        this.playerTank = playerTank;
        this.enemyTanks = enemyTanks;
        this.walls = walls;
        this.bulletsWithoutTankOwner = bulletsWithoutTankOwner;
    }

    //EFFECTS: returns the coordinate that the given object would be if it collides and is pushed
    // back; or the position it would be by advancing, if the object is not colliding
    public int[] getPushedBackPositionIfCollidingOrAdvance(int velX, int velY, TankGameObject tankGameObject) {
        if (isCollidingWithAWallInGame(velX, velY, tankGameObject)) {
            Wall collidingWall = getFirstCollidingWall(velX, velY, tankGameObject);
            int newPosX = collidingWall.coordinateXIfObjectCollidesAndIsPushedBack(velX,
                    velY, tankGameObject);
            int newPosY = collidingWall.coordinateYIfObjectCollidesAndIsPushedBack(velX,
                    velY, tankGameObject);
            return new int[]{newPosX, newPosY};
        } else {
            return new int[]{tankGameObject.getX() + velX, tankGameObject.getY() + velY};
        }
    }

    //EFFECTS: returns true if the given object would collide with any wall in the game, given
    // its current x and y velocity
    public boolean isCollidingWithAWallInGame(int velX, int velY, TankGameObject tankGameObject) {
        boolean isCollidingWithAWall = false;
        for (Wall w : this.walls) {
            if (w.isCollidingWithWall(velX, velY, tankGameObject)) {
                isCollidingWithAWall = true;
                break;
            }
        }
        return isCollidingWithAWall;
    }

    //REQUIRES: given bullet has to be colliding to a wall after moving at its speed
    //MODIFIES: bullet
    //EFFECTS: sets the given bullet's position to that if the bullet advances by its speed,
    // and it bounces off the wall
    public void dealWithBulletWallCollision(Bullet bullet) {
        Wall collidingWall = getFirstCollidingWall(bullet.getVelX(), bullet.getVelY(), bullet);
        //get the new position
        int[] newPositions = getBulletBouncedBackPosition(bullet, collidingWall);
        //increase the count
        bullet.increaseBounceCount();
        //flip the speed
        flipSpeed(bullet, collidingWall);
        //set new coordinate
        bullet.setLocation(newPositions[0], newPositions[1]);
    }

    //MODIFIES: this
    //EFFECTS: check for collision between every bullet and other bullets and tanks,
    // and remove those colliding to each other
    public void dealWithCollisionForBullets() {
        //deal with bullet collision between bullets within every tank
        dealWithBulletCollisionForBulletsInOneTank(playerTank);
        this.enemyTanks.forEach(this::dealWithBulletCollisionForBulletsInOneTank);
        //deal with bullet collision between different tanks
        this.enemyTanks.forEach(et -> dealWithBulletCollisionForTwoTanks(playerTank, et));
        for (int i = 0; i < enemyTanks.size(); i++) {
            for (int j = i + 1; j < enemyTanks.size(); j++) {
                Tank enemyTank1 = enemyTanks.get(i);
                Tank enemyTank2 = enemyTanks.get(j);
                dealWithBulletCollisionForTwoTanks(enemyTank1, enemyTank2);
            }
        }
        //deal with collision of bullets between bullets without owner
        dealWithBulletCollisionForBulletsWithoutTankOwnerAndOneTank(playerTank);
        this.enemyTanks.forEach(this::dealWithBulletCollisionForBulletsWithoutTankOwnerAndOneTank);
    }

    //MODIFIES: this and enemy tanks in enemyTanks
    //EFFECTS: checks for collision between bullets and every enemy tank, removing the colliding bullet
    // & tank and moving the bullets from the colliding tank to the bullets without tank owner list.
    // Returns true if there is no more tank in the game, false otherwise.
    public boolean dealWithCollisionBetweenEnemyTanksAndBullets() {
        List<Tank> enemyTankToBeRemoved = new ArrayList<>();

        for (Tank etForCollision : enemyTanks) {
            //adding "null" object when the collision is not happening //fixme
            enemyTankToBeRemoved.add(dealWithCollisionBetweenATankAndTankBullets(etForCollision, playerTank));
            for (Tank etForBullets : enemyTanks) {
                //adding "null" object when the collision is not happening //fixme
                enemyTankToBeRemoved.add(dealWithCollisionBetweenATankAndTankBullets(etForCollision, etForBullets));
            }
        }

        for (Tank etToBeRemoved : enemyTankToBeRemoved) {
            if (etToBeRemoved != null) {
                this.bulletsWithoutTankOwner.addAll(etToBeRemoved.getBullets());
            }
        }
        this.enemyTanks.removeAll(enemyTankToBeRemoved);

        return this.enemyTanks.isEmpty();
    }

    //MODIFIES: this and playerTank
    //EFFECTS: checks for collision between bullets and the player tank, removing the colliding bullet
    // & player tank if colliding, moving its bullets to the list of bullets without tank owner
    // Returns true if the player tank is removed, or false if it is not.
    public boolean dealWithCollisionBetweenPlayerTankAndBullets() { //fixme
        boolean removePlayerTank = (dealWithCollisionBetweenATankAndTankBullets(playerTank, playerTank) != null);
        if (!removePlayerTank) {
            for (Tank et : enemyTanks) { //fixme
                removePlayerTank = (dealWithCollisionBetweenATankAndTankBullets(playerTank, et) != null);
                if (removePlayerTank) {
                    break;
                }
            }
        }
        if (removePlayerTank) {
            this.bulletsWithoutTankOwner.addAll(this.playerTank.getBullets());
            this.playerTank = null;
            return true;
        } else {
            return false;
        }
    }

    public PlayerTank getPlayerTank() {
        return playerTank;
    }

    public List<EnemyTank> getEnemyTanks() {
        return enemyTanks;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Bullet> getBulletsWithoutTankOwner() {
        return bulletsWithoutTankOwner;
    }

    //REQUIRES: the object needs to be colliding with at least one wall
    //EFFECTS: returns the first wall to which the given object would collide, given its current
    // x and y velocity; or throws a RunTimeException if no such wall exist
    private Wall getFirstCollidingWall(int velX, int velY, TankGameObject tankGameObject) {
        for (Wall w : this.walls) {
            if (w.isCollidingWithWall(velX, velY, tankGameObject)) {
                return w;
            }
        }
        throw new RuntimeException();
    }

    //HELPER FUNCTION for moveBulletInGame(): set the new bullet position
    //REQUIRES: bullet needs to be colliding with the given wall
    //EFFECTS: returns the bullet's position when it would have bounced back
    private int[] getBulletBouncedBackPosition(Bullet bullet, Wall collidingWall) {
        int[] temporalPos = getPushedBackPositionIfCollidingOrAdvance(bullet.getVelX(), bullet.getVelY(), bullet);
        int temporalPosX = temporalPos[0];
        int temporalPosY = temporalPos[1];
        int newPosX = temporalPosX;
        int newPosY = temporalPosY;
        //if the bullet is colliding with a wall to the sides
        if (collidingWall.isCollidingWithLeftWall(bullet.getVelX(), bullet.getVelY(), bullet)
                || collidingWall.isCollidingWithRightWall(bullet.getVelX(), bullet.getVelY(), bullet)) {
            newPosX = bullet.getX() + (temporalPosX - bullet.getX()) * 2 - bullet.getVelX();
            //otherwise, (the bullet is naturally colliding with a wall at the top or bottom)
        } else {
            newPosY = bullet.getY() + (temporalPosY - bullet.getY()) * 2 - bullet.getVelY();
        }
        return new int[]{newPosX, newPosY};
    }

    // HELPER FUNCTION for moveBulletInGame(): flip the speeds
    //REQUIRES: bullet has to collide with a wall
    //MODIFIES: bullet
    //EFFECTS: updates the speed of the bullet according to which wall it has collided, flipping
    // the appropriate speeds; or does nothing if the bullet is not colliding with any wall
    private void flipSpeed(Bullet bullet, Wall collidingWall) {
        if (collidingWall.isCollidingWithLeftWall(bullet.getVelX(), bullet.getVelY(), bullet)
                || collidingWall.isCollidingWithRightWall(bullet.getVelX(), bullet.getVelY(), bullet)) {
            bullet.setBulletVelocity(-bullet.getVelX(), bullet.getVelY());
        }
        //collision to the Top or Bottom wall
        if (collidingWall.isCollidingWithTopWall(bullet.getVelX(), bullet.getVelY(), bullet)
                || collidingWall.isCollidingWithBottomWall(bullet.getVelX(), bullet.getVelY(), bullet)) {
            bullet.setBulletVelocity(bullet.getVelX(), -bullet.getVelY());
        }
    }

    //MODIFIES: tank
    //EFFECTS: check for collision between every bullet for the given tank, and
    // remove those colliding to each other
    private void dealWithBulletCollisionForBulletsInOneTank(Tank tank) {
        List<Bullet> bulletToBeRemovedFromTank = new ArrayList<>();

        for (int i = 0; i < tank.getBullets().size(); i++) {
            for (int j = i + 1; j < tank.getBullets().size(); j++) {
                Bullet tankBullet1 = tank.getBullets().get(i);
                Bullet tankBullet2 = tank.getBullets().get(j);
                if (tankBullet1.isCollidingWith(tankBullet2)) {
                    bulletToBeRemovedFromTank.add(tankBullet1);
                    bulletToBeRemovedFromTank.add(tankBullet2);
                }
            }
        }
        tank.getBullets().removeAll(bulletToBeRemovedFromTank);
    }

    //MODIFIES: tank
    //EFFECTS: check for collision between every bullet for the given tank and the bullets
    // from the list of bullets without owner, and remove the ones that collided to each other
    private void dealWithBulletCollisionForBulletsWithoutTankOwnerAndOneTank(Tank tank) {
        List<Bullet> bulletToBeRemovedFromTank = new ArrayList<>();
        List<Bullet> bulletToBeRemovedWithoutTankOwner = new ArrayList<>();

        for (Bullet bulletFromTank : tank.getBullets()) {
            for (Bullet bulletWithoutOwner : this.bulletsWithoutTankOwner) {
                if (bulletFromTank.isCollidingWith(bulletWithoutOwner)) {
                    bulletToBeRemovedFromTank.add(bulletFromTank);
                    bulletToBeRemovedWithoutTankOwner.add(bulletWithoutOwner);
                }
            }
        }
        tank.getBullets().removeAll(bulletToBeRemovedFromTank);
        this.bulletsWithoutTankOwner.removeAll(bulletToBeRemovedWithoutTankOwner);
    }

    //MODIFIES: tank1, tank2
    //EFFECTS: check for collision between every bullet for the two given tanks, and
    // remove those colliding to each other; if the playerTank is removed, it is replaced
    // with null
    private void dealWithBulletCollisionForTwoTanks(Tank tank1, Tank tank2) {
        List<Bullet> bulletToBeRemovedFromTank1 = new ArrayList<>();
        List<Bullet> bulletToBeRemovedFromTank2 = new ArrayList<>();

        for (Bullet tank1Bullet : tank1.getBullets()) {
            for (Bullet tank2Bullet : tank2.getBullets()) {
                if (tank1Bullet.isCollidingWith(tank2Bullet)) {
                    bulletToBeRemovedFromTank1.add(tank1Bullet);
                    bulletToBeRemovedFromTank2.add(tank2Bullet);
                }
            }
        }
        tank1.getBullets().removeAll(bulletToBeRemovedFromTank1);
        tank2.getBullets().removeAll(bulletToBeRemovedFromTank2);
    }

    /* fixme original without iterator
    //MODIFIES: this
    //EFFECTS: checks for collision between bullets in the tankForBulletsToCheck and tankForCollision, and
    // removes every colliding bullet, and finally return the tankForCollision if it collided with any bullet
    private Tank dealWithCollisionBetweenATankAndTankBullets(Tank tankForCollision, Tank tankForBulletsToCheck) {
        List<Bullet> bulletsToBeRemoved = new ArrayList<>();
        boolean haveToRemoveTankForCollision = false;
        for (Bullet b : tankForBulletsToCheck.getBullets()) {
            if (b.isCollidingWith(tankForCollision)) {
                bulletsToBeRemoved.add(b);
                haveToRemoveTankForCollision = true;
            }
        }
        if (haveToRemoveTankForCollision) {
            tankForBulletsToCheck.getBullets().removeAll(bulletsToBeRemoved);
            return tankForCollision;
        } else {
            return null;
        }
    }
    */

    /* fixme with iterator
    //MODIFIES: this
    //EFFECTS: checks for collision between bullets in the tankForBulletsToCheck and tankForCollision, and
    // removes every colliding bullet, and finally return the tankForCollision if it collided with any bullet
    private Tank dealWithCollisionBetweenATankAndTankBullets(Tank tankForCollision, Tank tankForBulletsToCheck) {
        Iterator<Bullet> iteratorForBullets = tankForBulletsToCheck.getBullets().iterator();
        boolean haveToRemoveTankForCollision = false;
        while (iteratorForBullets.hasNext()) {
            Bullet b = iteratorForBullets.next();
            if (b.isCollidingWith(tankForCollision)) {
                iteratorForBullets.remove();
                haveToRemoveTankForCollision = true;
            }
        }
        if (haveToRemoveTankForCollision) {
            return tankForCollision;
        } else {
            return null;
        }
    }
     */

    //MODIFIES: this
    //EFFECTS: checks for collision between bullets in the tankForBulletsToCheck and tankForCollision, and
    // removes every colliding bullet, and finally return the tankForCollision if it collided with any bullet
    private Tank dealWithCollisionBetweenATankAndTankBullets(Tank tankForCollision, Tank tankForBulletsToCheck) {
        Iterator<Bullet> iteratorForBullets = tankForBulletsToCheck.getBullets().iterator();
        boolean haveToRemoveTankForCollision = false;
        while (iteratorForBullets.hasNext()) {
            Bullet b = iteratorForBullets.next();
            if (b.isCollidingWith(tankForCollision)) {
                iteratorForBullets.remove();
                haveToRemoveTankForCollision = true;
            }
        }
        if (haveToRemoveTankForCollision) {
            return tankForCollision;
        } else {
            return null;
        }
    }

}
