package at.fhhagenberg.view;

import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.viewmodels.ElevatorViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private static final int PADDING = 10;
    private static final String ELEVATOR_BORDER_STYLE = "-fx-border-color: blue;-fx-border-insets: 3;-fx-border-width: 2;";
    private static final String ARROW_STYLE = "-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';";
    private static final String ELEVATOR_PRESSABLE_STYLE = "-fx-background-radius: 30;-fx-border-radius: 20;-fx-border-color: lightblue;";
    private static final String ELEVATOR_FLOOR_STYLE = "-fx-background-radius: 0;";
    private static final String ELEVATOR_NORMAL_COLOR = "-fx-background-color: silver;";
    private static final String ELEVATOR_TARGET_COLOR = "-fx-background-color: lightgreen;";
    private static final String ELEVATOR_CURRENT_POS_COLOR = "-fx-background-color: green;";
    private static final String ELEVATOR_BUTTON_PRESSED_COLOR = "-fx-background-color: yellow;";
    private static final String ELEVATOR_NO_SERVICE_COLOR = "-fx-background-color: grey;";
    private static final String ELEVATOR_ARROW_ACTIVE_COLOR = "-fx-background-color: green;";
    private static final String ELEVATOR_ARROW_INACTIVE_COLOR = "-fx-background-color: silver;";
    private static final int FLOOR_HEIGHT_PX = 40;
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
        payloadLbl.setId(String.format("%s%d", identifierBase, mElevatorNr));
        payloadLbl.textProperty().bind(binding);
        infoLine.getChildren().add(new Label(String.format("%s: ", label)));
        infoLine.getChildren().add(payloadLbl);
        infoLine.getChildren().add(new Label(" "));
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
                createInfoLine("Payload", "Payload", "kg", mViewModel.getPayloadProp().asString()),
                createInfoLine("Speed", "Speed", "ft/s", mViewModel.getSpeedProp().asString()),
                createInfoLine("Accel", "Accel", "ft/s^2", mViewModel.getAccelProp().asString()),
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
        btn.setId(String.format("%s%d_%d", identifierBase, mElevatorNr, i));
        btn.setDisable(true);
        btn.setOpacity(1);
        btn.setPrefHeight(FLOOR_HEIGHT_PX);
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

        for(int i = mViewModel.getNrFloors()-1; i >= 0 ; --i){
            var floor = i;
            var btn = createElevatorFloor("PressedInEle", floor);
            btn.styleProperty().bind(Bindings.createStringBinding(()->{
                if(mViewModel.getStopsProp().get().contains(floor)){
                    return ELEVATOR_FLOOR_STYLE + ELEVATOR_BUTTON_PRESSED_COLOR;
                }
                else{
                    return ELEVATOR_FLOOR_STYLE + ELEVATOR_NORMAL_COLOR;
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
                var baseStyle = ELEVATOR_FLOOR_STYLE;
                if(mViewModel.getManualProp().get()){
                    baseStyle = ELEVATOR_PRESSABLE_STYLE;
                }
                else{
                    // if we are not in manual mode, set all buttons to normal
                    for(var elevator : elevatorTarget.getChildren()){
                        elevator.setOpacity(1);
                    }
                }
                if(mViewModel.getFloorProp().get() == floor){
                    return baseStyle + ELEVATOR_CURRENT_POS_COLOR;
                }
                else if(mViewModel.getTargetProp().get() == floor){
                    return baseStyle + ELEVATOR_TARGET_COLOR;
                }
                else if(mViewModel.getServicedProp().get().contains(floor)){
                    return baseStyle + ELEVATOR_NORMAL_COLOR;
                }
                else{
                    return baseStyle + ELEVATOR_NO_SERVICE_COLOR;
                }
            }, mViewModel.getManualProp(), mViewModel.getFloorProp(), 
            mViewModel.getTargetProp(), mViewModel.getServicedProp()));
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    mViewModel.getManualFloorProp().setValue(floor);
                    // grey out other buttons
                    for(var elevator : elevatorTarget.getChildren()){
                        if(elevator == btn){
                            elevator.setOpacity(1);
                        }
                        else{
                            elevator.setOpacity(0.7);
                        }
                    }
                }
            });
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
                return ARROW_STYLE + ELEVATOR_ARROW_ACTIVE_COLOR;
            }
            else{
                return ARROW_STYLE + ELEVATOR_ARROW_INACTIVE_COLOR;
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
        HBox.setMargin(infoText,new Insets(PADDING));
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
        btnBox.setPadding(new Insets(PADDING));
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

        mView.setStyle(ELEVATOR_BORDER_STYLE);

        mView.setPadding(new Insets(PADDING));
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
