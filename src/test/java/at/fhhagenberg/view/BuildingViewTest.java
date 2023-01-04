package at.fhhagenberg.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import at.fhhagenberg.logic.BusinesLogic;
import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.Floor;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.updater.BuildingUpdater;
import at.fhhagenberg.updater.ElevatorUpdater;
import at.fhhagenberg.updater.FloorUpdater;
import at.fhhagenberg.viewmodels.BuildingViewModel;

@Disabled
class BuildingViewTest {
    @Test
    void test() {
        var service = new MockElevatorService(2, 2, 10);
        
        ModelFactory factory = new ModelFactory(service);
        Building model = factory.createBuilding();
        BusinesLogic logic = new BusinesLogic(model);
        
        List<FloorUpdater> floorUpdaters = new ArrayList<>();
        List<ElevatorUpdater> elevatorUpdaters = new ArrayList<>();

        for (Elevator elevator : model.getElevators()) {
            elevatorUpdaters.add(new ElevatorUpdater(service, elevator));
        }

        for (Floor floor : model.getFloors()) {
            floorUpdaters.add(new FloorUpdater(service, floor));
        }

        BuildingUpdater updater = new BuildingUpdater(service, elevatorUpdaters, floorUpdaters, model);
        BuildingViewModel viewModel = new BuildingViewModel(updater, model, logic);
        BuildingView view = new BuildingView(viewModel);

        assertEquals(2, view.getLayout().getChildren().size());
    }    
}
