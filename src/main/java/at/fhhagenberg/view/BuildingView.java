package at.fhhagenberg.view;

import at.fhhagenberg.viewmodels.BuildingViewModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class that views a building
 */
public class BuildingView {
    private final HBox mView;
    private final BuildingViewModel mViewModel;

    private static final int PADDING = 20;

    /**
     * creates all elevators
     * @return all elevators in a HBox
     */
    private HBox createElevatorLayout(){
        var elevatorLayout = new HBox();
        var addElevators = elevatorLayout.getChildren();
        for (var elevator : mViewModel.getElevatorViewModels()) {
            addElevators.add((new ElevatorView(elevator)).getLayout());
        }
        return elevatorLayout;
    }

    /**
     * creates all floors
     * @return all floors in a VBox
     */
    private VBox createFloorLayout(){
        var floorLayout = new VBox();
        var addFloors = floorLayout.getChildren();
        var lbl = new Label("Buttons on floors");
        addFloors.add(lbl);
        for (var floor : mViewModel.getFloorViewModels()) {
            addFloors.add((new FloorView(floor)).getLayout());
        }
        return floorLayout;
    }

    /**
     * Constructor of BuildingView
     * @param viewModel view model with the data of the building to be viewed
     */
    public BuildingView(BuildingViewModel viewModel) {
        mViewModel = viewModel;
        mView = new HBox(PADDING);
        mView.setPadding(new Insets(PADDING));
        mView.getChildren().add(createElevatorLayout());
        mView.getChildren().add(createFloorLayout());
    }

    /**
     * Getter for the layout
     * @return layout
     */
    public HBox getLayout() {
        return mView;
    }
}
