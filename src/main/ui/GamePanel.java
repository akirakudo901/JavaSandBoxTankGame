package ui;

/*
Class representing the JPanel that corresponds to the game components (such as tanks, bullets
and walls) displayed at gameplay. The class will be able to place each of the objects in their
appropriate positions, and to update them based on changes made to the TankGame object it is
connected to.

ACKNOWLEDGEMENT:
THIS CODE HAS BEEN EXTENSIVELY BASED ON THE "GamePanel" CLASS IN THE "B2-SpaceInvadersBase" PROJECT
PROVIDED TO US AS EXAMPLE. I APPRECIATE THE WISDOM GAINED FROM THIS PROJECT.
THE PROJECT CAN BE FOUND AS OF 2022/03/02 AT:
https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase

LAST REVISED: 03/31/2022
 */

import model.Bullet;
import model.tankgame.TankGame;
import model.TankGameObject;
import model.Wall;
import model.tanks.EnemyTank;
import model.tanks.PlayerTank;
import model.tanks.Tank;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GamePanel extends JPanel {

    //denotes the different countdown states that the timer can be in
    enum CountDownStates {
        PIC_1, PIC_2, PIC_3, PIC_GO, OVER
    }

    private static final String PIC_1_SOURCE = "./data/countDown1Transparent.png";
    private static final String PIC_2_SOURCE = "./data/countDown2Transparent.png";
    private static final String PIC_3_SOURCE = "./data/countDown3Transparent.png";
    private static final String PIC_GO_SOURCE = "./data/countDownGoTransparent.png";

    private static final int COUNTDOWN_INTERVAL = 800;

    private PlayTankGame playTankGame;
    private TankGame game;
    private List<RotatableJLabel> enemyTankGunList;
    private RotatableJLabel playerTankGun;
    private JLabel gameOverLabel;
    private JLabel gameWonLabel;
    private JLabel playAgainLabel;
    private Timer countDownTimer;
    private CountDownStates countDownState;

    private JLabel pic1Label;
    private JLabel pic2Label;
    private JLabel pic3Label;
    private JLabel picGOLabel;

    //EFFECTS: creates a new panel that will display the components included in the game
    public GamePanel(PlayTankGame playTankGame) {
        this.playTankGame = playTankGame;
        setPreferredSize(new Dimension(TankGame.WIDTH, TankGame.HEIGHT));
        setBackground(new Color(215, 206, 206));

        addGameEndLabels();

        setUp();
    }

    //MODIFIES: this
    //EFFECTS: sets this panel's main TankGame object to the one from playTankGame again
    public void setUp() {
        this.game = playTankGame.getTankGame();
        enemyTankGunList = new ArrayList<>();
        addTankGunLabels();

        setUpCountDownImages();
        addCountDownTimer();
    }

    //MODIFIES: this
    //EFFECTS: executes the initial count down that displays the images "3", "2", "1" and "GO"
    // at the set COUNTDOWN_INTERVAL interval.
    public void executeInitialCountDown() {
        countDownState = CountDownStates.PIC_3;
        showCountDownImage(pic3Label);
        this.countDownTimer.start();
    }

    public CountDownStates getCountDownState() {
        return countDownState;
    }

    //TODO UPDATE TO THE DESCRIBING STATE WHEN FINISHED WITH MOST THINGS
    @Override
    //MODIFIES: g
    //EFFECTS: draws the game and its objects on the given graphics object, and the game over
    // instruction when it is game over.
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!game.isGameOver()) {
            drawGame(g);
        } else if (game.isWon()) {
            drawGameWon();
        } else {
            drawGameOver();
        }
    }

    //MODIFIES: this
    //EFFECTS: draws both the gameOver and playAgain Label on the panel
    private void drawGameOver() {
        this.gameOverLabel.setVisible(true);
        this.playAgainLabel.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: draws both the gameWon and playAgain Label on the panel
    private void drawGameWon() {
        this.gameWonLabel.setVisible(true);
        this.playAgainLabel.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: adds all labels displayed when tank game is won or lost
    private void addGameEndLabels() {
        gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setFont(new Font("Arial", Font.PLAIN, 40));

        gameWonLabel = new JLabel("You have Won!");
        gameWonLabel.setFont(new Font("Arial", Font.PLAIN, 40));

        playAgainLabel = new JLabel("Press R to play again!");
        playAgainLabel.setFont(new Font("Arial", Font.PLAIN, 40));

        add(gameOverLabel);
        gameOverLabel.setVisible(false);

        add(gameWonLabel);
        gameOverLabel.setVisible(false);

        add(playAgainLabel);
        playAgainLabel.setVisible(false);
    }

    //MODIFIES: this
    //EFFECTS: adds a timer used to display the countdown images.
    private void addCountDownTimer() {
        this.countDownState = CountDownStates.PIC_3;
        this.countDownTimer = new Timer(COUNTDOWN_INTERVAL, ae -> countDown());
    }

    //MODIFIES: this
    //EFFECTS: displays the corresponding pictures and change the countDownstate according to the
    // current countDownState.
    public void countDown() {
        switch (countDownState) {
            case PIC_3:
                showCountDownImage(pic2Label);
                countDownState = CountDownStates.PIC_2;
                break;
            case PIC_2:
                showCountDownImage(pic1Label);
                countDownState = CountDownStates.PIC_1;
                break;
            case PIC_1:
                showCountDownImage(picGOLabel);
                countDownState = CountDownStates.PIC_GO;
                break;
            default:
                showCountDownImage(new JLabel());
                countDownState = CountDownStates.OVER;
                this.countDownTimer.stop();
                this.playTankGame.startGameTimer();
        }
    }

    //MODIFIES: this
    //EFFECTS: shows the given countdown JLabel by setting its visibility to true and
    // the visibility of the other labels to false
    private void showCountDownImage(JLabel countDownLabel) {
        List<JLabel> allLabels = new ArrayList<>();
        allLabels.add(pic1Label);
        allLabels.add(pic2Label);
        allLabels.add(pic3Label);
        allLabels.add(picGOLabel);

        allLabels.stream()
                .filter(e -> !e.equals(countDownLabel))
                .forEach(e -> e.setVisible(false));
        countDownLabel.setVisible(true);
    }

    //MODIFIES:this
    //EFFECTS: add the four countdown images of "3", "2", "1" and "GO" to the GamePanel
    // with visibility set to false
    private void setUpCountDownImages() {
        this.pic1Label = addImageLabel(PIC_1_SOURCE);
        this.pic2Label = addImageLabel(PIC_2_SOURCE);
        this.pic3Label = addImageLabel(PIC_3_SOURCE);
        this.picGOLabel = addImageLabel(PIC_GO_SOURCE);
    }

    //MODIFIES: this
    //EFFECTS: adds a JLabel with the image given as a source if it exists in
    // the "data" directory, and return it as output; otherwise, flag an IOException and
    // returns a JLabel without Icon
    private JLabel addImageLabel(String source) {
        try {
            Image iconImage = ImageIO.read(new File(source));
            Icon newIcon = new ImageIcon(iconImage);
            JLabel newLabel = new JLabel(newIcon);
            newLabel.setLocation(TankGame.WIDTH / 2 - newLabel.WIDTH / 2,
                    TankGame.HEIGHT / 2 - newLabel.HEIGHT / 2);
            newLabel.setVisible(false);

            add(newLabel);
            return newLabel;
        } catch (IOException e) {
            System.err.println("IOException thrown; failed to load a countdown picture.");
            return new JLabel();
        }
    }

    //MODIFIES: this
    //EFFECTS: adds all tank gun objects as rotatable label to this panel
    private void addTankGunLabels() {
        playerTankGun = new RotatableJLabel(this.game.getPlayerTank().getX() + Tank.WIDTH / 2,
                this.game.getPlayerTank().getY() + Tank.HEIGHT / 2,
                this.game.getPlayerTank().getGunAngle(),
                "./data/PlayerTankGun.png");

        for (EnemyTank et : this.game.getEnemyTanks()) {
            addTankGunForOneTank(et);
        }
    }

    //MODIFIES: this
    //EFFECTS: adds a tank gun object as rotatable label to this panel
    private void addTankGunForOneTank(Tank tank) {
        RotatableJLabel tankGun = new RotatableJLabel(tank.getX() + Tank.WIDTH / 2,
                tank.getY() + Tank.HEIGHT / 2, tank.getGunAngle(),
                "./data/EnemyTankGun.png");
        add(tankGun);
        enemyTankGunList.add(tankGun);
    }

    //MODIFIES: g
    //EFFECTS: draws the entire game onto the given graphics object
    private void drawGame(Graphics g) {
        drawWalls(g);
        drawPlayerTank(g);
        drawEnemyTanks(g);
        drawTankGuns(g);
        drawBulletsWithoutTankOwner(g);

        this.gameWonLabel.setVisible(false);
        this.gameOverLabel.setVisible(false);
        this.playAgainLabel.setVisible(false);
    }

    //MODIFIES: g
    //EFFECTS: draws all wall objects onto the given graphics object
    private void drawWalls(Graphics g) {
        List<Wall> walls = this.game.getWalls();
        for (Wall w : walls) {
            drawTankGameObject(g, w);
        }
    }

    //MODIFIES: g
    //EFFECTS: draws the player tank object onto the given graphics object
    private void drawPlayerTank(Graphics g) {
        PlayerTank pt = this.game.getPlayerTank();
        drawTank(g, pt);
    }

    //MODIFIES: g
    //EFFECTS: draws the enemy tank objects onto the given graphics object
    private void drawEnemyTanks(Graphics g) {
        List<EnemyTank> enemyTanks = this.game.getEnemyTanks();
        for (EnemyTank et : enemyTanks) {
            drawTank(g, et);
        }
    }

    //MODIFIES: g
    //EFFECTS: draws a tank object onto the given graphics object
    private void drawTank(Graphics g, Tank t) {
        drawTankGameObject(g, t);
        drawBullets(g, t);
    }

    //MODIFIES: g
    //EFFECTS: draws all tank gun objects onto the given graphics object
    private void drawTankGuns(Graphics g) {
        drawTankGun(g, playerTankGun, this.game.getPlayerTank());

        for (int i = 0; i < this.game.getEnemyTanks().size(); i++) {

            drawTankGun(g, enemyTankGunList.get(i), this.game.getEnemyTanks().get(i));
        }
    }

    //MODIFIES: g
    //EFFECTS: draws a tank gun object onto the given graphics object
    private void drawTankGun(Graphics g, RotatableJLabel tankGun, Tank tank) {
        tankGun.setLocation(tank.getX(), tank.getY());

        tankGun.setDegrees(tank.getGunAngle());

        tankGun.paintComponent(g);
    }

    //MODIFIES: g
    //EFFECTS: draws the bullet objects of the given tank onto the given graphics object
    private void drawBullets(Graphics g, Tank tank) {
        List<Bullet> bulletList = tank.getBullets();

        for (Bullet b : bulletList) {
            drawTankGameObject(g, b);
        }
    }

    //MODIFIES: g
    //EFFECTS: draws all bullets in the bulletsWithoutOwner list in TankGame
    private void drawBulletsWithoutTankOwner(Graphics g) {
        List<Bullet> bulletWithoutTankOwner = this.game.getBulletsWithoutTankOwner();

        for (Bullet b : bulletWithoutTankOwner) {
            drawTankGameObject(g, b);
        }
    }

    //MODIFIES: g
    //EFFECTS: draws a given TankGameObject onto the given graphics object
    private void drawTankGameObject(Graphics g, TankGameObject tgo) {
        Color originalColor = g.getColor();

        g.setColor(tgo.getColor());
        g.fillRect(tgo.getX(), tgo.getY(), tgo.getWidth(), tgo.getHeight());

        g.setColor(originalColor);
    }

    /*
    Class representing a rotatable JLabel to use for display.

    ACKNOWLEDGEMENT
    THIS CODE WAS EXTENSIVELY BASED ON THE "Rotated Icon" CLASS CODE PROVIDED BY ROB CAMICK.
    I TRULY APPRECIATE THE WISDOM GAINED FROM THIS CODE.
    THE CODE CAN BE OBTAINED FROM A LINK IN THE WEB PAGE BELOW, OR DIRECTLY FROM THE LINK BELOW.
    WEBPAGE: https://tips4java.wordpress.com/2009/04/06/rotated-icon/
    DIRECT LINK: http://www.camick.com/java/source/RotatedIcon.java
     */
    private static class RotatableJLabel extends JLabel {
        private int degrees;
        private BufferedImage image;

        //EFFECTS: creates the rotatable JLabel with given angle (in degrees) and position
        private RotatableJLabel(int posX, int posY, int degrees, String imageSource) {
            setLocation(posX, posY);
            setSize(getPreferredSize());
            setVisible(true);
            this.degrees = degrees;

            try {
                this.image = ImageIO.read(new File(imageSource));
            } catch (IOException e) {
                System.err.println("IOException thrown.");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            AffineTransform af = AffineTransform.getTranslateInstance(
                    (float) getX() + (float) Tank.WIDTH / 2 - (float) Tank.GUN_WIDTH / 2,
                    (float) getY() + (float) Tank.HEIGHT / 2 - (float) Tank.GUN_HEIGHT / 2);
            af.rotate(Math.toRadians(degrees), (float) Tank.GUN_WIDTH / 2,
                    (float) Tank.GUN_HEIGHT / 2);
            af.scale(0.4, 0.5);
            Graphics2D graphics2d = (Graphics2D) g;
            graphics2d.drawImage(this.image, af, null);
        }

        //MODIFIES: this
        //EFFECTS: sets the degree angle of this label to the given one
        private void setDegrees(int degrees) {
            this.degrees = degrees;
        }

    }

}