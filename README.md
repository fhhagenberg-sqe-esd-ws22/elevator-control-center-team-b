# Description Graphical User Interface
## Basic structure

The GUI consists of two semantically different elements: the elevators and the floors. Every elevator of the given building is visualized with the information corresponding to that individual elevator. On the right-hand side of the elevators is the visualization of the floors.

## Elevators

Every elevator can be identified by a label on the top, which shows the number, or ID, of that elevator. Beneath that number, the current direction of the elevator is visualized. A green arrow means that the elevator is traveling in that direction. If both arrows are gray, the elevator is waiting for a new target and direction. Beneath the direction, there are two columns, whichs' elements are labelled by the floor they are representing. The left of the two columns can have 4 different colours:

1. Silver: Default color of the floor
1. Green: The elevator is currently at this floor
1. Light Green: This floor is the current target of the elevator
1. Gray: The floor is currently not serviced by this elevator
1. Light Gray: The elevator is currently in manual mode and currently cannot target this floor

The right column indicates where guests of the elevator want to get off the elevator. A yellow colour indicates that a guest wants to get off the elevator at that floor.
Beneatch the columns is a button that toggles the manual mode of the elevator. If the manual mode is active, the elements of the left column have rounded edges. On right-hand side of the column is some additional information about the elevator.

## Floors

The floors depict the buttons at every floor, guests can press to call an elevator. There is a button to call the elevator to go upwards and a button to call the elevator to go downwards. This information is visualized via a column. Every box of that column represents a floor. To the left of the box is a number, which indicates the represented floor.
When the upwards-button is pressed, a yellow arrow pointing upwards is displayed. When the downwards-button is pressed, a yellow arrow pointing downwards is displayed.

# How to start the application

In order to start the application, the elevator simulator must be started. After the simulator is running, the application can be started and it will immediatelly display the information it receives from the simulator. If the simulator cannot be reached by the application, it will shut down after displaying this information. If a connection problem arises during operation, an error will be shown to the user.

# Automatic mode algorithm

With every update interval, new targets for the elevators are set. The application processes elevator 0 first and proceeds elevators with a stepsize of one. Firstly the application checks if there is a requested stop or a call in the direction the elevator is currently travelling in. If at least one is found, the elevator targets the closest one. If no floor is found, the same process is repeated for the other direction. Once an elevator is moving, no new target will be set until the elevator arrived at it's target floor.

# Operation Manual for manual  mode

In order to activate manual mode, the "Manual Mode"-Button needs to be pressed. Manual mode can be set for every individual elevator. Once the manual mode is active, which is indicated by the rounded edges of the boxes of the left column, the buttons of the left column can be pressed in order to set the corresponding floor as the next target. However, this is only possible, if the floor is serviced by the elevator. If this is not the case, the button of that floor will have a gray colour. After a serviced floor, different from the floor where the elevator is currently at, is pressed, the elevator will set it as a target. Until that floor is reached, the target cannot be changed.

# Graphical User Interface with JavaFx

### Prerequisites

- [x] Java 11 SDK (e.g. Oracle or OpenJDK).
- [x] Maven 3. (If you use an IDE like Eclipse or IntelliJ, Maven is **already included** :sunglasses:.)
	- see http://maven.apache.org/install.html
- [x] An IDE or code editor of your choice.

> Confirm your installation with `mvn -v` in a new shell. The result should be similar to:

```
$ mvn -v
Apache Maven 3.6.2 (40f52333136460af0dc0d7232c0dc0bcf0d9e117; 2019-08-27T17:06:16+02:00)
Maven home: C:\Users\winterer\.tools\apache-maven-3.6.2
Java version: 11, vendor: Oracle Corporation, runtime: C:\Program Files\Java\jdk-11
Default locale: en_GB, platform encoding: Cp1252
OS name: "windows 10", version: "10.0", arch: "amd64", family: "windows"
```

### Instructions

This maven project is already set up for JavaFx based GUI applications. It also contains a small example application - `App.java`.

1. Import this git repository into your favourite IDE.

1. Make sure, you can run the sample application without errors.
	- Either run it in your IDE
	- Via command line, run it with `mvn clean javafx:run`.

You can build your project with maven with

```
mvn clean package
```

The resulting archive (`.jar` file) is in the `target` directory.
