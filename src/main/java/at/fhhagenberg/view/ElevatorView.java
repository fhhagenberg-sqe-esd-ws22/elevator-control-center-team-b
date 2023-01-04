package at.fhhagenberg.view;

import at.fhhagenberg.logic.BusinesLogic;
import at.fhhagenberg.viewmodels.ElevatorViewModel;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ElevatorView {
    private final ElevatorViewModel mViewModel;
    private final VBox mView;

    public ElevatorView(ElevatorViewModel viewModel) {
        mViewModel = viewModel;
        mView = new VBox(4);

        var header = new HBox();
        header.getChildren().add(new Label(String.format("Elevator %d", mViewModel.getElevatorNr())));
        mView.getChildren().add(header);
        
        var payload = new HBox();
        Label payloadLbl = new Label();
        payloadLbl.textProperty().bind(mViewModel.getPayloadProp().asString());
        payload.getChildren().add(new Label("Payload: "));
        payload.getChildren().add(payloadLbl);
        mView.getChildren().add(payload);

        var speed = new HBox();
        Label speedLbl = new Label();
        speedLbl.textProperty().bind(mViewModel.getSpeedProp().asString());
        speed.getChildren().add(new Label("Speed: "));
        speed.getChildren().add(speedLbl);
        mView.getChildren().add(speed);

        var accel = new HBox();
        Label accelLbl = new Label();
        accelLbl.textProperty().bind(mViewModel.getAccelProp().asString());
        accel.getChildren().add(new Label("Accel: "));
        accel.getChildren().add(accelLbl);
        mView.getChildren().add(accel);

        var target = new HBox();
        Label targetLbl = new Label();
        targetLbl.textProperty().bind(mViewModel.getTargetProp().asString());
        target.getChildren().add(new Label("Next stop: "));
        target.getChildren().add(targetLbl);
        mView.getChildren().add(target);

        var door = new HBox();
        Label doorLbl = new Label();
        doorLbl.textProperty().bind(mViewModel.getDoorStatusStringProp());
        door.getChildren().add(new Label("Next stop: "));
        door.getChildren().add(doorLbl);
        mView.getChildren().add(door);

        var nearest = new HBox();
        Label nearestLbl = new Label();
        nearestLbl.textProperty().bind(mViewModel.getNearestFloorProp().asString());
        nearest.getChildren().add(new Label("Nearest floor: "));
        nearest.getChildren().add(nearestLbl);
        mView.getChildren().add(nearest);

        var dir = new HBox();
        Label dirLbl = new Label();
        dirLbl.textProperty().bind(mViewModel.getDirectionStringProp());
        dir.getChildren().add(new Label("Direction: "));
        dir.getChildren().add(dirLbl);
        mView.getChildren().add(dir);

        var stops = new HBox();
        Label stopsLbl = new Label();
        stopsLbl.textProperty().bind(mViewModel.getStopsProp());
        stops.getChildren().add(new Label("Stops: "));
        stops.getChildren().add(stopsLbl);
        mView.getChildren().add(stops);

        var button = new ToggleButton("Manual Mode");
        mViewModel.getManualProp().bind(button.selectedProperty());
        mView.getChildren().add(button);
    }
    
    public VBox GetView() {
        return mView;
    }
}
