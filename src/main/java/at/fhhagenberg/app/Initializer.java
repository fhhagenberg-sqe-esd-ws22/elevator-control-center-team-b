package at.fhhagenberg.app;

import java.util.ArrayList;
import java.util.List;

import at.fhhagenberg.logic.BusinesLogic;
import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.Elevator;
import at.fhhagenberg.model.Floor;
import at.fhhagenberg.model.ModelFactory;
import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.updater.BuildingUpdater;
import at.fhhagenberg.updater.ElevatorUpdater;
import at.fhhagenberg.updater.FloorUpdater;
import at.fhhagenberg.view.BuildingView;
import at.fhhagenberg.viewmodels.BuildingViewModel;
import javafx.scene.Parent;

/**
 * Class responsible for initializing the individual components of the application
 */
public class Initializer {
    private final Parent root;

    /**
     * Creates all objects required for the application
     * @param service
     */
    public Initializer(IElevatorService service) {
        ModelFactory factory = new ModelFactory(service);
        Building building = factory.createBuilding();
        List<FloorUpdater> floorUpdaters = new ArrayList<>();
        List<ElevatorUpdater> elevatorUpdaters = new ArrayList<>();
        
        for (Elevator elevator : building.getElevators()) {
            elevatorUpdaters.add(new ElevatorUpdater(service, elevator));
        }

        for (Floor floor : building.getFloors()) {
            floorUpdaters.add(new FloorUpdater(service, floor));
        }

        BuildingUpdater updater = new BuildingUpdater(service, elevatorUpdaters, floorUpdaters, building);

        BusinesLogic logic = new BusinesLogic(building);
        BuildingViewModel buildingViewModel = new BuildingViewModel(updater, building, logic);
        BuildingView buildingView = new BuildingView(buildingViewModel);

        root = buildingView.getLayout();
    }

    /**
     * Getter for the root layout
     * @return layout of the building
     */
    public Parent getRoot() {
        return root;
    }
}
