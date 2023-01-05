package at.fhhagenberg.view;

import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.viewmodels.ElevatorViewModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

    private final int padding = 10;
    private final String ElevatorBorderStyle = "-fx-border-color: blue;-fx-border-insets: 3;-fx-border-width: 2;";
    private final String ArrowStyle = "-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';";
    private final String ElevatorFloorStyle = "-fx-background-radius: 0;";
    private final String ElevatorNormalColor = "-fx-background-color: silver;";
    private final String ElevatorTargetColor = "-fx-background-color: lightgreen;";
    private final String ElevatorCurrentPosColor = "-fx-background-color: green;";
    private final String ElevatorButtonPressedColor = "-fx-background-color: yellow;";
    private final String ElevatorNoServiceColor = "-fx-background-color: grey;";
    private final String ElevatorArrowActiveColor = "-fx-background-color: green;";
    private final String ElevatorArrowInactiveColor = "-fx-background-color: silver;";
    private final int mFloorHeightPx = 40;
    private final int mElevatorNr;

    private VBox createInfoText(){
        var infoText = new VBox();

        var payload = new HBox();
        Label payloadLbl = new Label();
        payloadLbl.setId(String.format("Payload%d", mElevatorNr));
        payloadLbl.textProperty().bind(mViewModel.getPayloadProp().asString());
        payload.getChildren().add(new Label("Payload: "));
        payload.getChildren().add(payloadLbl);
        payload.getChildren().add(new Label("mass"));
        infoText.getChildren().add(payload);

        var speed = new HBox();
        Label speedLbl = new Label();
        speedLbl.setId(String.format("Speed%d", mElevatorNr));
        speedLbl.textProperty().bind(mViewModel.getSpeedProp().asString());
        speed.getChildren().add(new Label("Speed: "));
        speed.getChildren().add(speedLbl);
        speed.getChildren().add(new Label("x/s"));
        infoText.getChildren().add(speed);

        var accel = new HBox();
        Label accelLbl = new Label();
        accelLbl.setId(String.format("Accel%d", mElevatorNr));
        accelLbl.textProperty().bind(mViewModel.getAccelProp().asString());
        accel.getChildren().add(new Label("Accel: "));
        accel.getChildren().add(accelLbl);
        accel.getChildren().add(new Label("x/s^2"));
        infoText.getChildren().add(accel);

        var door = new HBox();
        Label doorLbl = new Label();
        doorLbl.setId(String.format("Door%d", mElevatorNr));
        doorLbl.textProperty().bind(mViewModel.getDoorStatusStringProp());
        door.getChildren().add(new Label("Door: "));
        door.getChildren().add(doorLbl);
        infoText.getChildren().add(door);

        return infoText;
    }

    private VBox createElevatorGraphic(String identifierBase){
        var elevatorGraphic = new VBox(0);
        for(int i = mViewModel.getNrFloors()-1; i >= 0 ; --i){
            var btn = new Button("Floor " + i);
            btn.setId(String.format(identifierBase + "%d_%d", mElevatorNr, i));
            btn.setDisable(true);
            btn.setOpacity(1);
            btn.setPrefHeight(mFloorHeightPx);
            elevatorGraphic.getChildren().add(btn);
        }
        return elevatorGraphic;
    }

    private VBox createPressedInElevatorGraphic(){
        var buttonPressedGraphic = createElevatorGraphic("BtnInElevator");
        var floors = buttonPressedGraphic.getChildren();
        for(int i = mViewModel.getNrFloors()-1; i >= 0 ; --i){
            var floor = i;
            floors.get(floor).styleProperty().bind(Bindings.createStringBinding(()->{
                if(mViewModel.getStopsProp().get().contains(floor)){
                    return ElevatorFloorStyle + ElevatorButtonPressedColor;
                }
                else{
                    return ElevatorFloorStyle + ElevatorNormalColor;
                }
            }));
        }
        return buttonPressedGraphic;
    }

    private VBox createElevatorTargetGraphic(){
        var elevatorTarget = createElevatorGraphic("ElevatorTarget");
        var floors = elevatorTarget.getChildren();
        for(int i = mViewModel.getNrFloors()-1; i >= 0 ; --i){
            var floor = i;
            floors.get(floor).styleProperty().bind(Bindings.createStringBinding(()->{
                if(mViewModel.getNearestFloorProp().get() == floor){
                    return ElevatorFloorStyle + ElevatorCurrentPosColor;
                }
                else if(mViewModel.getTargetProp().get() == floor){
                    return ElevatorFloorStyle + ElevatorTargetColor;
                }
                else{
                    return ElevatorFloorStyle + ElevatorNormalColor;
                }
            }));
        }
        return elevatorTarget;
    }

    private Button createArrow(int direction, int rotation){
        var arrow = new Button();
        arrow.setId(String.format("Arrow%d",direction));
        arrow.setDisable(true);
        arrow.setOpacity(1);
        arrow.styleProperty().bind(Bindings.createStringBinding(()-> {
            if(mViewModel.getDirectionProp().get() == direction) {
                return ArrowStyle + ElevatorArrowActiveColor;
            }
            else{
                return ArrowStyle + ElevatorArrowInactiveColor;
            }
        }));
        arrow.setRotate(rotation);
        return arrow;
    }

    private HBox createArrows(){
        var arrows = new HBox();
        var arrUp = createArrow(IElevatorService.ELEVATOR_DIRECTION_UP,270);
        var arrDown = createArrow(IElevatorService.ELEVATOR_DIRECTION_DOWN, 90);
        arrows.getChildren().addAll(arrUp, arrDown);
        arrows.setAlignment(Pos.CENTER);
        return arrows;
    }

    private VBox createGraphics(){
        var graphics = new VBox();
        graphics.getChildren().add(createArrows());

        var elevatorGraphics = new HBox(10);
        elevatorGraphics.getChildren().add(createElevatorTargetGraphic());
        elevatorGraphics.getChildren().add(createPressedInElevatorGraphic());

        graphics.getChildren().add(elevatorGraphics);

        return graphics;
    }

    private HBox createInformation(){
        var information = new HBox();
        var graphics = createGraphics();
        var infoText = createInfoText();
        HBox.setMargin(infoText,new Insets(2*padding, 0, 0, padding));
        information.getChildren().add(graphics);
        information.getChildren().add(infoText);

        return information;
    }

    private HBox createElevatorNameHeader(){
        var header = new HBox();
        Label headerLbl = new Label(Integer.toString(mElevatorNr));
        headerLbl.setId(String.format("Header%d", mElevatorNr));
        header.getChildren().add(new Label("Elevator "));
        header.getChildren().add(headerLbl);
        return header;
    }

    private ToggleButton createManualModeButton(){
        var button = new ToggleButton("Manual Mode");
        button.setId(String.format("Manual%d", mElevatorNr));
        mViewModel.getManualProp().bind(button.selectedProperty());
        return button;
    }

    /**
     * Constructor of ElevatorView
     * @param viewModel view model of the elevator to be viewed
     */
    public ElevatorView(ElevatorViewModel viewModel) {
        mViewModel = viewModel;
        mView = new VBox();
        mElevatorNr = mViewModel.getElevatorNr();

        mView.setStyle(ElevatorBorderStyle);

        mView.setPadding(new Insets(padding,padding,padding,padding));
        mView.getChildren().addAll(
                createElevatorNameHeader(),
                createInformation(),
                createManualModeButton());
    }
    
    /**
     * Getter for the layout
     * @return layout
     */
    public VBox getLayout() {
        return mView;
    }
}
