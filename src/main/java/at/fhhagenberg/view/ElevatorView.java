package at.fhhagenberg.view;

import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.viewmodels.ElevatorViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
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
    private final String ElevatorPressableStyle = "-fx-background-radius: 30;-fx-border-radius: 20;-fx-border-color: lightblue;";
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

    /**
     * creates one line of elevator information
     * @param identifierBase String to create a unique ID for the information
     * @param label what the information should be labeled
     * @param unit what the unit of the information should be (empty String if it doesn't need a unit)
     * @param binding property/binding of the information value
     * @return a line of information in a HBox
     */
    private HBox createInfoLine(String identifierBase, String label, String unit, StringExpression binding){
        var infoLine = new HBox();
        Label payloadLbl = new Label();
        payloadLbl.setId(String.format(identifierBase + "%d", mElevatorNr));
        payloadLbl.textProperty().bind(binding);
        infoLine.getChildren().add(new Label(label + ": "));
        infoLine.getChildren().add(payloadLbl);
        infoLine.getChildren().add(new Label(unit));
        return infoLine;
    }

    /**
     * creates the info text of an elevator
     * @return the entire info text for an elevator (payload, acceleration, speed, door status) in a VBox
     */
    private VBox createInfoText(){
        var infoText = new VBox();

        infoText.getChildren().addAll(
                createInfoLine("Payload", "Payload", "mass", mViewModel.getPayloadProp().asString()),
                createInfoLine("Speed", "Speed", "x/y", mViewModel.getSpeedProp().asString()),
                createInfoLine("Accel", "Accel", "x/y^2", mViewModel.getAccelProp().asString()),
                createInfoLine("Door", "Door", "", mViewModel.getDoorStatusStringProp()));

        return infoText;
    }

    /**
     * creates one button representing an elevator floor
     * @param identifierBase String to create a unique ID for the floor
     * @param i floor number
     * @return button representing an elevator floor
     */
    private Button createElevatorFloor(String identifierBase, int i){
        var btn = new Button("Floor " + i);
        btn.setId(String.format(identifierBase + "%d_%d", mElevatorNr, i));
        btn.setDisable(true);
        btn.setOpacity(1);
        btn.setPrefHeight(mFloorHeightPx);
        return btn;
    }

    /**
     * creates a graphic for all the buttons pressed inside an elevator
     * @return graphic for all the buttons pressed inside an elevator in a VBox
     */
    private VBox createPressedInElevatorGraphic(){
        var buttonPressedGraphic = new VBox(0);

        var lbl = new Label("Pressed:");
        buttonPressedGraphic.getChildren().add(lbl);

        var floors = buttonPressedGraphic.getChildren();
        for(int i = mViewModel.getNrFloors()-1; i >= 0 ; --i){
            var floor = i;
            var btn = createElevatorFloor("PressedInEle", floor);
            btn.styleProperty().bind(Bindings.createStringBinding(()->{
                if(mViewModel.getStopsProp().get().contains(floor)){
                    return ElevatorFloorStyle + ElevatorButtonPressedColor;
                }
                else{
                    return ElevatorFloorStyle + ElevatorNormalColor;
                }
            }, mViewModel.getStopsProp()));
            buttonPressedGraphic.getChildren().add(btn);
        }
        return buttonPressedGraphic;
    }

    /**
     * creates a graphic for the current position and target of the elevator; in manual mode the buttons can be pressed
     * to set new targets
     * @return graphic for the current position and target of the elevator in a VBox
     */
    private VBox createElevatorTargetGraphic(){
        var elevatorTarget = new VBox(0);

        var lbl = new Label("Status:");
        elevatorTarget.getChildren().add(lbl);

        for(int i = mViewModel.getNrFloors()-1; i >= 0 ; --i){
            var floor = i;
            var btn = createElevatorFloor("ElevatorTarget", floor);
            btn.disableProperty().bind(mViewModel.getManualProp().not());
            btn.styleProperty().bind(Bindings.createStringBinding(()->{
                var baseStyle = ElevatorFloorStyle;
                if(mViewModel.getManualProp().get()){
                    baseStyle = ElevatorPressableStyle;
                }
                if(mViewModel.getNearestFloorProp().get() == floor){
                    return baseStyle + ElevatorCurrentPosColor;
                }
                else if(mViewModel.getTargetProp().get() == floor){
                    return baseStyle + ElevatorTargetColor;
                }
                else if(mViewModel.getServicedProp().get().contains(floor)){
                    return baseStyle + ElevatorNormalColor;
                }
                else{
                    return baseStyle + ElevatorNoServiceColor;
                }
            }, mViewModel.getManualProp(), mViewModel.getNearestFloorProp(), 
            mViewModel.getTargetProp(), mViewModel.getServicedProp()));
            elevatorTarget.getChildren().add(btn);
        }
        return elevatorTarget;
    }

    /**
     * creates an arrow
     * @param direction does the arrow go up or down
     * @param rotation rotation from a right pointing arrow in degrees (90 for down, 270 for up)
     * @return an arrow as a disabled button
     */
    private Button createArrow(int direction, int rotation){
        var arrow = new Button();
        arrow.setId(String.format("Arrow%d_%d",mElevatorNr, direction));
        arrow.setDisable(true);
        arrow.setOpacity(1);
        arrow.styleProperty().bind(Bindings.createStringBinding(()-> {
            if(mViewModel.getDirectionProp().get() == direction) {
                return ArrowStyle + ElevatorArrowActiveColor;
            }
            else{
                return ArrowStyle + ElevatorArrowInactiveColor;
            }
        }, mViewModel.getDirectionProp()));
        arrow.setRotate(rotation);
        return arrow;
    }

    /**
     * creates arrows for up and down
     * @return arrows for up and down in a HBox
     */
    private HBox createArrows(){
        var arrows = new HBox();
        var arrUp = createArrow(IElevatorService.ELEVATOR_DIRECTION_UP,270);
        var arrDown = createArrow(IElevatorService.ELEVATOR_DIRECTION_DOWN, 90);
        arrows.getChildren().addAll(arrUp, arrDown);
        arrows.setAlignment(Pos.CENTER);
        return arrows;
    }

    /**
     * creates graphics for the up/down arrows and the elevator status and pressed buttons
     * @return all graphics the elevator contains in a VBox
     */
    private VBox createGraphics(){
        var graphics = new VBox();
        graphics.getChildren().add(createArrows());

        var elevatorGraphics = new HBox(10);
        elevatorGraphics.getChildren().add(createElevatorTargetGraphic());
        elevatorGraphics.getChildren().add(createPressedInElevatorGraphic());

        graphics.getChildren().add(elevatorGraphics);

        return graphics;
    }

    /**
     * creates all information (including graphics) that describes an elevator
     * @return all information of an elevator in a HBox
     */
    private HBox createInformation(){
        var information = new HBox();
        var graphics = createGraphics();
        var infoText = createInfoText();
        HBox.setMargin(infoText,new Insets(padding));
        information.getChildren().add(graphics);
        information.getChildren().add(infoText);

        return information;
    }

    /**
     * creates the name for an elevator
     * @return name of the elevator in a HBox
     */
    private HBox createElevatorNameHeader(){
        var header = new HBox();
        Label headerLbl = new Label(Integer.toString(mElevatorNr));
        headerLbl.setId(String.format("Header%d", mElevatorNr));
        header.getChildren().add(new Label("Elevator "));
        header.getChildren().add(headerLbl);
        return header;
    }


    /**
     * creates a button to activate the manual mode
     * @return ToggleButton for enabling the manual mode in a HBox
     */
    private HBox createManualModeButton(){
        var btnBox = new HBox();
        var button = new ToggleButton("Manual Mode");
        button.setId(String.format("Manual%d", mElevatorNr));
        mViewModel.getManualProp().bind(button.selectedProperty());
        btnBox.getChildren().add(button);
        btnBox.setPadding(new Insets(padding));
        return btnBox;
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

        mView.setPadding(new Insets(padding));
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
