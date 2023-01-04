package at.fhhagenberg.view;

import at.fhhagenberg.viewmodels.ElevatorViewModel;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * View of an elevator
 */
public class ElevatorView {
    private final ElevatorViewModel mViewModel;
    private final VBox mView;

    /**
     * Constructor of ElevatorView
     * @param viewModel view model of the elevator to be viewed
     */
    public ElevatorView(ElevatorViewModel viewModel) {
        mViewModel = viewModel;
        mView = new VBox(4);
        int elevatorNr = mViewModel.getElevatorNr();

        var header = new HBox();
        Label headerLbl = new Label(Integer.toString(elevatorNr));
        headerLbl.setId(String.format("Header%d", elevatorNr));
        header.getChildren().add(new Label("Elevator "));
        header.getChildren().add(headerLbl);
        mView.getChildren().add(header);
        
        var payload = new HBox();
        Label payloadLbl = new Label();
        payloadLbl.setId(String.format("Payload%d", elevatorNr));
        payloadLbl.textProperty().bind(mViewModel.getPayloadProp().asString());
        payload.getChildren().add(new Label("Payload: "));
        payload.getChildren().add(payloadLbl);
        mView.getChildren().add(payload);

        var speed = new HBox();
        Label speedLbl = new Label();
        speedLbl.setId(String.format("Speed%d", elevatorNr));
        speedLbl.textProperty().bind(mViewModel.getSpeedProp().asString());
        speed.getChildren().add(new Label("Speed: "));
        speed.getChildren().add(speedLbl);
        mView.getChildren().add(speed);

        var accel = new HBox();
        Label accelLbl = new Label();
        accelLbl.setId(String.format("Accel%d", elevatorNr));
        accelLbl.textProperty().bind(mViewModel.getAccelProp().asString());
        accel.getChildren().add(new Label("Accel: "));
        accel.getChildren().add(accelLbl);
        mView.getChildren().add(accel);

        var target = new HBox();
        Label targetLbl = new Label();
        targetLbl.setId(String.format("Target%d", elevatorNr));
        targetLbl.textProperty().bind(mViewModel.getTargetProp().asString());
        target.getChildren().add(new Label("Next stop: "));
        target.getChildren().add(targetLbl);
        mView.getChildren().add(target);

        var door = new HBox();
        Label doorLbl = new Label();
        doorLbl.setId(String.format("Door%d", elevatorNr));
        doorLbl.textProperty().bind(mViewModel.getDoorStatusStringProp());
        door.getChildren().add(new Label("Next stop: "));
        door.getChildren().add(doorLbl);
        mView.getChildren().add(door);

        var nearest = new HBox();
        Label nearestLbl = new Label();
        nearestLbl.setId(String.format("Nearest%d", elevatorNr));
        nearestLbl.textProperty().bind(mViewModel.getNearestFloorProp().asString());
        nearest.getChildren().add(new Label("Nearest floor: "));
        nearest.getChildren().add(nearestLbl);
        mView.getChildren().add(nearest);

        var dir = new HBox();
        Label dirLbl = new Label();
        dirLbl.setId(String.format("Dir%d", elevatorNr));
        dirLbl.textProperty().bind(mViewModel.getDirectionStringProp());
        dir.getChildren().add(new Label("Direction: "));
        dir.getChildren().add(dirLbl);
        mView.getChildren().add(dir);

        var stops = new HBox();
        Label stopsLbl = new Label();
        stopsLbl.setId(String.format("Stops%d", elevatorNr));
        stopsLbl.textProperty().bind(mViewModel.getStopsProp());
        stops.getChildren().add(new Label("Stops: "));
        stops.getChildren().add(stopsLbl);
        mView.getChildren().add(stops);

        var button = new ToggleButton("Manual Mode");
        button.setId(String.format("Manual%d", elevatorNr));
        mViewModel.getManualProp().bind(button.selectedProperty());
        mView.getChildren().add(button);
    }
    
    /**
     * Getter for the layout
     * @return layout
     */
    public VBox getLayout() {
        return mView;
    }
}
