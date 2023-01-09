package at.fhhagenberg.view;

import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.viewmodels.FloorViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * View for a floor
 */
public class FloorView {
    private final FloorViewModel mViewModel;
    private final HBox mView;

    private static final String ARROW_STYLE = "-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';";
    private static final String FLOOR_STYLE = "-fx-background-radius: 0;-fx-border-color: black;-fx-background-color: silver;";
    private static final String BUTTON_PRESSED_COLOR = "-fx-background-color: yellow;";
    private static final String BUTTON_INACTIVE_COLOR = "-fx-background-color: silver;";
    private static final int HEIGHT = 40;
    private static final int WIDTH = 60;
    private static final int PADDING = 10;
    private int mFloorNumber;

    /**
     * creates an arrow for going up/down
     * @param direction does the arrow go up or down
     * @param rotation rotation from a right pointing arrow in degrees (90 for down, 270 for up)
     * @param binding property that the arrow is bound to
     * @return disabled button that depicts an arrow
     */
    private Button createArrow(int direction, int rotation, SimpleBooleanProperty binding){
        var arrow = new Button();
        arrow.setId(String.format("FloorArrow%d_%d",mFloorNumber,direction));
        arrow.setDisable(true);
        arrow.setOpacity(1);
        arrow.styleProperty().bind(Bindings.createStringBinding(()-> {
            if(binding.get()) {
                return ARROW_STYLE + BUTTON_PRESSED_COLOR;
            }
            else{
                return ARROW_STYLE + BUTTON_INACTIVE_COLOR;
            }
        }, binding));
        arrow.setRotate(rotation);
        return arrow;
    }

    /**
     * creates the graphic for a single floor
     * @return graphic for a single floor in an HBox
     */
    private HBox createFloorGraphic(){
        var floorGraphic = new HBox();
        floorGraphic.setStyle(FLOOR_STYLE);
        var arrUp = createArrow(IElevatorService.ELEVATOR_DIRECTION_UP,270, mViewModel.getWantUpProp());
        var arrDown = createArrow(IElevatorService.ELEVATOR_DIRECTION_DOWN, 90, mViewModel.getWantDownProp());
        floorGraphic.getChildren().addAll(arrUp, arrDown);
        floorGraphic.setAlignment(Pos.CENTER);
        floorGraphic.setPrefSize(WIDTH, HEIGHT);
        return floorGraphic;
    }

    /**
     * creates a label for a floor
     * @return floor number as a label
     */
    private Label createFloorLabel(){
        var lbl = new Label(Integer.toString(mFloorNumber));
        lbl.setPadding(new Insets(PADDING));
        return lbl;
    }


    /**
     * Constructor of FloorView
     * @param viewModel view model of the floor to be viewed
     */
    public FloorView(FloorViewModel viewModel) {
        mViewModel = viewModel;
        mFloorNumber = mViewModel.getFloorNumber();
        mView = new HBox();

        mView.setAlignment(Pos.CENTER_LEFT);
        mView.getChildren().addAll(createFloorLabel(), createFloorGraphic());
    }

    /**
     * Getter for the layout
     * @return layout
     */
    public HBox getLayout() {
        return mView;
    }
}
