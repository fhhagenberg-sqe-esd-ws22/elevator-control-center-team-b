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
import javafx.scene.layout.VBox;

/**
 * View for a floor
 */
public class FloorView {
    private final FloorViewModel mViewModel;
    private final HBox mView;

    private final String ArrowStyle = "-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';";
    private final String FloorStyle = "-fx-background-radius: 0;-fx-border-color: black;-fx-background-color: silver;";
    private final String ButtonPressedColor = "-fx-background-color: yellow;";
    private final String ButtonInactiveColor = "-fx-background-color: silver;";
    private final int Height = 40;
    private final int Width = 60;
    private final int Padding = 10;

    /**
     * creates an arrow for going up/down
     * @param direction does the arrow go up or down
     * @param rotation rotation from a right pointing arrow in degrees (90 for down, 270 for up)
     * @param binding property that the arrow is bound to
     * @return disabled button that depicts an arrow
     */
    private Button createArrow(int direction, int rotation, SimpleBooleanProperty binding){
        var arrow = new Button();
        arrow.setId(String.format("FloorArrow%d",direction));
        arrow.setDisable(true);
        arrow.setOpacity(1);
        arrow.styleProperty().bind(Bindings.createStringBinding(()-> {
            if(binding.get()) {
                return ArrowStyle + ButtonPressedColor;
            }
            else{
                return ArrowStyle + ButtonInactiveColor;
            }
        }));
        arrow.setRotate(rotation);
        return arrow;
    }

    /**
     * creates the graphic for a single floor
     * @return graphic for a single floor in an HBox
     */
    private HBox createFloorGraphic(){
        var floorGraphic = new HBox();
        floorGraphic.setStyle(FloorStyle);
        var arrUp = createArrow(IElevatorService.ELEVATOR_DIRECTION_UP,270, mViewModel.getWantUpProp());
        var arrDown = createArrow(IElevatorService.ELEVATOR_DIRECTION_DOWN, 90, mViewModel.getWantDownProp());
        floorGraphic.getChildren().addAll(arrUp, arrDown);
        floorGraphic.setAlignment(Pos.CENTER);
        floorGraphic.setPrefSize(Width, Height);
        return floorGraphic;
    }

    /**
     * creates a label for a floor
     * @return floor number as a label
     */
    private Label createFloorLabel(){
        var lbl = new Label(Integer.toString(mViewModel.getFloorNumber()));
        lbl.setPadding(new Insets(Padding));
        return lbl;
    }


    /**
     * Constructor of FloorView
     * @param viewModel view model of the floor to be viewed
     */
    public FloorView(FloorViewModel viewModel) {
        mViewModel = viewModel;
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
