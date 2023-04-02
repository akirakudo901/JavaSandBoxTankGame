# Sandbox Tank Game
LAST REVISED: 2022/03/31
## Overview
This project aims to create a **sandbox tank game**, where the player
controls a **toy tank** within a map with the goal to **defeat all tanks**
in the same map.
The concept is "***A tank game reminiscent of childhood***".
## What will the application do?
The application currently consists of a **tank game** with **a single map** and
**multiple enemy tanks to beat**. The player will control its own **player tank**
and have to defeat all enemy tanks to win.
## Who will use it?
Anyone who wants to play a simple tank game could enjoy this project!
## Why of interest to me?
There was a very similar video game in my childhood that I really enjoyed,
and I wanted to extend it by adding more tank and ammunition types.
I am also hoping to possibly use the end product to train an
AI, to extend my understanding on game AIs and AI in general.
## User stories

***Below are the user stories I at least covered in Phase 1***

The project (user side or game side) would like to:
- add an arbitrary number of "Bullet" objects in the "TankGame"
  object (in **TankGame** class)
- rotate the tank gun to the right (in **TankGame** class)
- rotate the tank gun to the left (in **TankGame** class)
- stop the game from running (in **PlayTankGame** class)
  
***Below are the user stories I at least covered in Phase 2***

- The project would like to:
- save a game's current state including bullets, tanks and walls
- load a game given a specific state of bullets, tanks and walls
  
***Below are the content I at least covered in Phase 3***

- "Add multiple Xs to a Y": the GamePanel object in PlayTankGame
  displays all bullet objects added newly to the TankGame object
  via Tank objects.
- "Add two events related to Xs and Y":
1. The player can press the "space" key to "fire a bullet" in
   the game, which adds a Bullet object to the TankGame via the
   Tank object.
2. The player can press the "RESET THE GAME" button in the Menu
   Panel to reset the list of Xs in Y to the state saved in the
   "tankGameInitialGameFile.json" file in the data directory

- "Load and save the state of the application": the MenuPanel object
  in PlayTankGame holds two buttons each for loading/saving data
  to/from the file "tankGameSaveFile.json" in the "data" directory.

- "Graphic component" : Each time a new game is
  started, the GamePanel will show a countdown realized using images.

***Phase 4: Task 2 Event Log example*** 

Tue Mar 29 09:32:16 PDT 2022 \
Game initialized from source ./data/tankGameInitialGameFile.json \
Tue Mar 29 09:32:19 PDT 2022 \
Player tank fired a new bullet. \
Tue Mar 29 09:32:19 PDT 2022 \
Player tank fired a new bullet. \
Tue Mar 29 09:32:23 PDT 2022 \
Player tank fired a new bullet. \
Tue Mar 29 09:32:25 PDT 2022 \
Player tank fired a new bullet. \
Tue Mar 29 09:32:30 PDT 2022 \
Game initialized from source ./data/tankGameInitialGameFile.json \
Tue Mar 29 09:32:34 PDT 2022 \
Player tank fired a new bullet. \
Tue Mar 29 09:32:35 PDT 2022 \
Game initialized from source ./data/tankGameInitialGameFile.json \

***Phase 4: Task 3***

Possible refactoring:
- CollisionHandler, TankGame and MovementHandler all depend on 
Wall, EnemyTank, PlayerTank and Bullet which are meant to be 
exactly the same objects, since MovementHandler and 
CollisionHandler work as part of the TankGame. This could be 
refactored so that only one of the classes (probably TankGame) 
contain the objects as fields, and the other parts could always 
fetch the objects as needed from that class.
- TankGameControlThroughKeyBindings and PlayTankGame both rely on
the same TankGame to function properly, but this is only implicitly
set by each of the classes holding the same TankGame as their field, 
and thus having to reset the field everytime the TankGame is updated.
This could be avoided by making one of the class reliant on the specific
TankGame object, and the other class always obtaining the game from the
other class.
- GamePanel and MenuPanel are meant to work as parts of the PlayTankGame
object, however the relationship is not a unidirectional association
which would be more suitable in this case, but a bidirectional one which
may be unnecessarily increasing coupling. This could be avoided by 
refactoring the code in a way that only PlayTankGame rely on the other
two Panels and not the other way, which would decrease coupling.

***Below are the user stories I want to ultimately all cover***

As part of the app play, one would want to:
- move the tank around.
- fire an arbitrary number of new bullets with a set maximum
  number, adding them to the game
- change the direction of the tank gun
  The app itself needs to:
- add new enemy tank to the game map
- fire new enemy bullets with a set maximum number, adding them
  to the map
- move the enemy tanks
- remove bullets that have hit a wall more than n times from the game
- *bounce the bullets around in a specific way*
- check for collision between walls and tanks
- remove bullets that hit each other
- remove tanks that were hit by a bullet
- check for collision between tanks and tanks
- save the current high score and number of game played
  and many more.
## Acknowledgement
- Although I directly did not use any code, I used the
**B02-SpaceInvadersBase project** (private as part of CPSC210
course contents) extensively as model for my GamePanel and 
PlayTankGame class. The code can be found below:    
https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase

- The **Teller App** code provided as an example was also extensively
used as model to create my PlayTankGameConsole class. The code can be
found below:
https://github.students.cs.ubc.ca/CPSC210/TellerApp

- The classes "JsonReader", "JsonWriter" and "Writable" were based
extensively, and codes were taken from the classes with the respective 
name in:
https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

- The classes "EventLog" and "Event" were entirely taken from the 
corresponding classes in the **AlarmSystem** project, which can be found
below:
https://github.students.cs.ubc.ca/CPSC210/AlarmSystem

- The class "TankGameControlThroughKeyBindings" was extensively based on
the **"MotionWithKeyBindings"** class originally created by Rob Camick. 

The code can be found as of 2022/03/31 at the bottom of the following page:
https://tips4java.wordpress.com/2013/06/09/motion-using-the-keyboard/

- The "RotatableJLabel" inner class within the GamePanel class was extensively
based on the "Rotated Icon" class provided by Rob Camick.

The code can be obtained as of 2022/03/31 from a link in the web page below, 
or directly from the link below.

Webpage: https://tips4java.wordpress.com/2009/04/06/rotated-icon/
Direct link: http://www.camick.com/java/source/RotatedIcon.java

I truly appreciate all the wisdom I gained from those codes!
