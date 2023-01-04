package at.fhhagenberg.view;

import at.fhhagenberg.viewmodels.FloorViewModel;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * View for a floor
 */
public class FloorView {
    private final FloorViewModel mViewModel;
    private final HBox mView;

    /**
     * Constructor of FloorView
     * @param viewModel view model of the elevator to be viewed
     */
    public FloorView(FloorViewModel viewModel) {
        mViewModel = viewModel;
        mView = new HBox(10);

        var lblUp = new Label();
        lblUp.textProperty().bind(mViewModel.getWantUpStrProp());

        var lblDown = new Label();
        lblDown.textProperty().bind(mViewModel.getWantDownStrProp());

        mView.getChildren().add(lblUp);
        mView.getChildren().add(lblDown);
    }

    /**
     * Getter for the layout
     * @return layout
     */
    public HBox GetView() {
        return mView;
    }
}
