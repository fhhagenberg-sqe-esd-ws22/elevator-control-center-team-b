package at.fhhagenberg.view;

import at.fhhagenberg.viewmodels.FloorViewModel;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FloorView {
    private final FloorViewModel mViewModel;
    private final HBox mView;

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

    public HBox GetView() {
        return mView;
    }
}
