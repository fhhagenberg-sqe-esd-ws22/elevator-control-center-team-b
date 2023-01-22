# Installation and Setup
## Requirements
For the use of this software please check that you have JRE 1.8.0 or newer.

The elevator simulator https://github.com/winterer/elevator/releases/tag/v0.1.2 is required to simulate use of a real elevator service.

## Setup
Download the jar file from the release page.

Start the elevator simulator and start a simulation.

Run the jar file by double clicking on it.

# Description and Usage of the Graphical User Interface
## Basic structure

The GUI consists of two semantically different elements: the elevators and the floors. Every elevator of the given building is visualized with the information corresponding to that individual elevator. On the right-hand side of the elevators is the visualization of the floors.

## Elevators

Every elevator can be identified by a label on the top, which shows the number, or ID, of that elevator. Beneath that number, the current direction of the elevator is visualized. A green arrow means that the elevator is traveling in that direction. If both arrows are gray, the elevator is currently not moving in any direction. Beneath the direction, there are two columns, whichs elements are labelled by the floor they are representing. The left of the two columns can have 4 different colours:

1. Silver: Default color of the floor
1. Green: The elevator is currently at this floor
1. Light Green: This floor is the current target of the elevator
1. Gray: The floor is currently not serviced by this elevator

The right column indicates where the passengers in the elevator want to get off. A yellow colour indicates that the button corresponding to this floor has been pressed inside the elevator.
Beneatch the columns is a button that toggles the manual mode of the elevator. If the manual mode is active, the elements of the left column have rounded edges.

On right-hand side of the column is some additional information about the elevator, like its current speed, acceleration, load and the status of its doors.

## Floors

The floor-section on the right side of the user interface depict the buttons that can be pressed by users on every floor to call an elevator. There is a button to call the elevator to go upwards and a button to call the elevator to go downwards. This information is visualized via this column. Every box of that column represents a floor. To the left of the box is a number, which indicates the represented floor.
When the upwards-button is pressed, a yellow arrow pointing upwards is displayed. When the downwards-button is pressed, a yellow arrow pointing downwards is displayed.

# How to start the application

In order to start the application, the elevator simulator must be started. After the simulator is running, the application can be started and it will immediatelly display the information it receives from the simulator. If the simulator cannot be reached by the application, it will shut down after displaying an error message. If a connection problem arises during operation, an error will be shown to the user.

# Automatic mode algorithm

With every update interval, new targets for the elevators are set. Elevator 0 has the highest priority, if it is currently moving towards a target elevator 1 will be used instead, and so forth for all elevators in the system. An elevator does have a direction in which it is going. If any stops are requested (inside or outside the elevator) in that direction it will go to the nearest stop. If no stops are available in this direction, the elevator will change its direction and look for stops in that direction. Once a target stop is set for an elevator it cannot be changed and it will be the next stop of the elevator.

# Operation Manual for manual  mode

In order to activate manual mode, the "Manual Mode"-Button needs to be pressed. Manual mode can be set for every individual elevator. Once the manual mode is active, which is indicated by the rounded edges of the boxes of the left column, the buttons of the left column can be pressed in order to set the corresponding floor as the next target. If the elevator has a target it is currently moving towards when manual mode is activated, it will still go to the original target first before the operator can manually set target. If a floor is selected in manual mode it is highlighted by the user interface. As soon as the systems registers the highlighted button as the next target it becomes light green. If the selected floor is not yet registered as target when deactivating manual mode, it will not be set as the next target of the elevator.
If manual mode is deactivated, the automatic mode algorithm will resume the operation of the elevator.

