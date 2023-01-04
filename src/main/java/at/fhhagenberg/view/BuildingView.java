package at.fhhagenberg.view;

import java.util.Vector;

import at.fhhagenberg.viewmodels.BuildingViewModel;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Class that views a building
 */
public class BuildingView {
    private final HBox mView;
    private final BuildingViewModel mViewModel;
    private final Vector<ElevatorView> mElevators;
    private final Vector<FloorView> mFloors;

    /**
     * Constructor of BuildingView
     * @param viewModel view model with the data of the building to be viewed
     */
    public BuildingView(BuildingViewModel viewModel) {
        mViewModel = viewModel;
        mElevators = new Vector<>();
        mFloors = new Vector<>();
        var elevatorLayout = new HBox(10);
        var floorLayout = new VBox(2);

        for (var elevator : mViewModel.getElevatorViewModels()) {
            mElevators.add(new ElevatorView(elevator));
        }

        for (var elevator : mElevators) {
            elevatorLayout.getChildren().add(elevator.GetView());
        }

        for (var floor : mViewModel.getFloorViewModels()) {
            mFloors.add(new FloorView(floor));
        }

        for (var floor : mFloors) {
            floorLayout.getChildren().add(0, floor.GetView());
        }

        mView = new HBox(20);
        mView.getChildren().add(elevatorLayout);
        mView.getChildren().add(floorLayout);
    }

    /**
     * Getter for the layout
     * @return layout
     */
    public HBox GetLayout() {
        return mView;
    }
}
