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

    private VBox createInfoText(){
        var infoText = new VBox();

        infoText.getChildren().addAll(
                createInfoLine("Payload", "Payload", "mass", mViewModel.getPayloadProp().asString()),
                createInfoLine("Speed", "Speed", "x/y", mViewModel.getSpeedProp().asString()),
                createInfoLine("Accel", "Accel", "x/y^2", mViewModel.getAccelProp().asString()),
                createInfoLine("Door", "Door", "", mViewModel.getDoorStatusStringProp()));

        return infoText;
    }

    private Button createElevatorFloor(String identifierBase, int i){
        var btn = new Button("Floor " + i);
        btn.setId(String.format(identifierBase + "%d_%d", mElevatorNr, i));
        btn.setDisable(true);
        btn.setOpacity(1);
        btn.setPrefHeight(mFloorHeightPx);
        return btn;
    }

    private VBox createPressedInElevatorGraphic(){
        var buttonPressedGraphic = new VBox(0);
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
            }));
            buttonPressedGraphic.getChildren().add(btn);
        }
        return buttonPressedGraphic;
    }

    private VBox createElevatorTargetGraphic(){
        var elevatorTarget = new VBox(0);
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
            }, mViewModel.getManualProp(), mViewModel.getNearestFloorProp()));
            elevatorTarget.getChildren().add(btn);
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
        HBox.setMargin(infoText,new Insets(padding, padding, padding, padding));
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
