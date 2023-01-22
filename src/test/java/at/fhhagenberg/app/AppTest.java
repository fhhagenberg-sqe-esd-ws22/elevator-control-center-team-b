package at.fhhagenberg.app;

import at.fhhagenberg.logic.AppController;
import at.fhhagenberg.logic.BusinessLogic;
import at.fhhagenberg.mock_observable.MockElevatorService;
import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.updater.IUpdater;
import at.fhhagenberg.updater.UpdaterException;
import at.fhhagenberg.viewmodels.BuildingViewModel;
import javafx.scene.control.Button;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.service.query.EmptyNodeQueryException;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

@ExtendWith(ApplicationExtension.class)
@ExtendWith(MockitoExtension.class)
class AppTest {

    MockElevatorService mock;
    FxRobot robot;

    @Mock
    BuildingViewModel vm;

    @Mock
    IUpdater updater;

    @Mock
    BusinessLogic logic;

    @Mock
    Consumer<String> showErrorCb;

    @Mock
    Consumer<String> showInfoCb;

    @Mock
    IElevatorService service;

    @BeforeEach
    public void setup() throws TimeoutException {
        mock = new MockElevatorService(4, 6, 10);
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(() -> new TestECCApp(mock));
        robot = new FxRobot();
    }

    @Test
    void testUpdateMechanism() {
        ScheduledExecutorService realExecutor = Executors.newSingleThreadScheduledExecutor();
        AppController controller = new AppController(service, updater, logic, vm, realExecutor, showErrorCb, showInfoCb);

        controller.start();
        try {
            WaitForAsyncUtils.waitFor(500, TimeUnit.MILLISECONDS, () -> false);
        } catch (TimeoutException ignored) {
        }
        controller.stop();

        verify(updater).update();
        verify(vm).update();
        verify(logic).setNextTargets();
    }

    @Test
    void testUpdateMechanismFailedDueToUpdater() throws TimeoutException {
        ScheduledExecutorService realExecutor = Executors.newSingleThreadScheduledExecutor();
        AtomicBoolean displayedError = new AtomicBoolean(false);
        AppController controller = new AppController(service, updater, logic, vm, realExecutor, (String s) -> { displayedError.set(true); }, showInfoCb);
        doThrow(UpdaterException.class).when(updater).update();
        when(service.connect()).thenReturn(false);

        controller.start();
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, displayedError::get);
        controller.stop();

        verify(service, times(AppController.DISPLAY_MESSAGE_FAILURE_CNT)).connect();
    }

    @Test
    void testUpdateMechanismFailedDueToViewModel() throws TimeoutException {
        ScheduledExecutorService realExecutor = Executors.newSingleThreadScheduledExecutor();
        AtomicBoolean displayedError = new AtomicBoolean(false);
        AppController controller = new AppController(service, updater, logic, vm, realExecutor, (String s) -> { displayedError.set(true); }, showInfoCb);
        doThrow(UpdaterException.class).when(vm).update();
        when(service.connect()).thenReturn(false);

        controller.start();
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, displayedError::get);
        controller.stop();

        verify(service, times(AppController.DISPLAY_MESSAGE_FAILURE_CNT)).connect();
    }

    @Test
    void testUpdateMechanismFailedDueToLogic() throws TimeoutException {
        ScheduledExecutorService realExecutor = Executors.newSingleThreadScheduledExecutor();
        AtomicBoolean displayedError = new AtomicBoolean(false);
        AppController controller = new AppController(service, updater, logic, vm, realExecutor, (String s) -> { displayedError.set(true); }, showInfoCb);
        doThrow(UpdaterException.class).when(logic).setNextTargets();
        when(service.connect()).thenReturn(false);

        controller.start();
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, displayedError::get);
        controller.stop();

        verify(service, times(AppController.DISPLAY_MESSAGE_FAILURE_CNT)).connect();
    }

    @Test
    void testReconnect() throws TimeoutException {
        ScheduledExecutorService realExecutor = Executors.newSingleThreadScheduledExecutor();
        AtomicBoolean displayedError = new AtomicBoolean(false);
        AtomicBoolean reconnected = new AtomicBoolean(false);
        AppController controller = new AppController(service, updater, logic, vm, realExecutor, (String s) -> displayedError.set(true), (String s) -> reconnected.set(true));
        doThrow(UpdaterException.class).when(updater).update();
        when(service.connect()).thenReturn(false);

        controller.start();
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, displayedError::get);
        when(service.connect()).thenReturn(true);
        try {
            WaitForAsyncUtils.waitFor(500, TimeUnit.MILLISECONDS, () -> false);
        }
        catch(TimeoutException ignored) {}
        Mockito.reset(updater);
        WaitForAsyncUtils.waitFor(5, TimeUnit.SECONDS, reconnected::get);
        controller.stop();

        verify(service, times(AppController.DISPLAY_MESSAGE_FAILURE_CNT + 1)).connect();
    }

    @Test
    void testElevatorHeaders() {
        // headers are static - they are not influenced by the service
        FxAssert.verifyThat("#Header0", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Header1", LabeledMatchers.hasText("1"));
        FxAssert.verifyThat("#Header2", LabeledMatchers.hasText("2"));
        FxAssert.verifyThat("#Header3", LabeledMatchers.hasText("3"));
    }

    @Test
    void testPressableMembersOfPressedInEleButtons() {
        var node = robot.lookup("#PressedInEle0_0").query();
        assertTrue(node instanceof Button);
        var button = (Button) node;
        assertTrue(button.isDisabled());
    }

    @Test
    void testPressableMembersOfElevatorDirectionUpArrow() {
        var node = robot.lookup(String.format("#Arrow0_%d", IElevatorService.ELEVATOR_DIRECTION_UP)).query();
        assertTrue(node instanceof Button);
        var button = (Button) node;
        assertTrue(button.isDisabled());
    }

    @Test
    void testPressableMembersOfElevatorDirectionDownArrow() {
        var node = robot.lookup(String.format("#Arrow0_%d", IElevatorService.ELEVATOR_DIRECTION_DOWN)).query();
        assertTrue(node instanceof Button);
        var button = (Button) node;
        assertTrue(button.isDisabled());
    }

    @Test
    void testPressableMembersOfFloorUpArrow() {
        var node = robot.lookup(String.format("#FloorArrow0_%d", IElevatorService.ELEVATOR_DIRECTION_UP)).query();
        assertTrue(node instanceof Button);
        var button = (Button) node;
        assertTrue(button.isDisabled());
    }

    @Test
    void testPressableMembersOfFloorDownArrow() {
        var node = robot.lookup(String.format("#FloorArrow0_%d", IElevatorService.ELEVATOR_DIRECTION_DOWN)).query();
        assertTrue(node instanceof Button);
        var button = (Button) node;
        assertTrue(button.isDisabled());
    }

    @Test
    void testElevatorButtonCountAndElevatorTargetButtonCountElevator0() {
        for (int i = 0; i < mock.getFloorNum(); i++) {
            assertNotNull(robot.lookup(String.format("#PressedInEle0_%d", i)).query());
            assertNotNull(robot.lookup(String.format("#ElevatorTarget0_%d", i)).query());
        }

        assertThrows(EmptyNodeQueryException.class, () ->
                robot.lookup(String.format("#PressedInEle0_%d",
                        mock.getFloorNum())).query());
        assertThrows(EmptyNodeQueryException.class, () ->
                robot.lookup(String.format("#ElevatorTarget0_%d",
                        mock.getFloorNum())).query());
    }

    @Test
    void testElevatorDoors() throws TimeoutException {
        FxAssert.verifyThat("#Door0", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door1", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door2", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door3", LabeledMatchers.hasText("Closed"));

        mock.setDoorStatus(0, IElevatorService.ELEVATOR_DOORS_CLOSED);
        mock.setDoorStatus(1, IElevatorService.ELEVATOR_DOORS_CLOSING);
        mock.setDoorStatus(2, IElevatorService.ELEVATOR_DOORS_OPEN);
        mock.setDoorStatus(3, IElevatorService.ELEVATOR_DOORS_OPENING);

        waitFor(1, TimeUnit.SECONDS, () -> LabeledMatchers.hasText("Opening").matches(robot.lookup("#Door3").query()));


        FxAssert.verifyThat("#Door0", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door1", LabeledMatchers.hasText("Closing"));
        FxAssert.verifyThat("#Door2", LabeledMatchers.hasText("Open"));
        FxAssert.verifyThat("#Door3", LabeledMatchers.hasText("Opening"));
    }

    @Test
    void testElevatorPayload() throws TimeoutException {
        FxAssert.verifyThat("#Payload0", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Payload1", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Payload2", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Payload3", LabeledMatchers.hasText("0"));

        mock.setWeight(0, 10);
        mock.setWeight(1, 20);
        mock.setWeight(2, 30);
        mock.setWeight(3, 40);

        waitFor(1, TimeUnit.SECONDS, () -> LabeledMatchers.hasText("40").matches(robot.lookup("#Payload3").query()));

        FxAssert.verifyThat("#Payload0", LabeledMatchers.hasText("10"));
        FxAssert.verifyThat("#Payload1", LabeledMatchers.hasText("20"));
        FxAssert.verifyThat("#Payload2", LabeledMatchers.hasText("30"));
        FxAssert.verifyThat("#Payload3", LabeledMatchers.hasText("40"));
    }

    @Test
    void testElevatorSpeed() throws TimeoutException {
        FxAssert.verifyThat("#Speed1", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Speed2", LabeledMatchers.hasText("0"));

        mock.setSpeed(1, 5);
        mock.setSpeed(2, 3);

        waitFor(1, TimeUnit.SECONDS, () -> LabeledMatchers.hasText("3").matches(robot.lookup("#Speed2").query()));

        FxAssert.verifyThat("#Speed1", LabeledMatchers.hasText("5"));
        FxAssert.verifyThat("#Speed2", LabeledMatchers.hasText("3"));
    }

    @Test
    void testElevatorFloor() throws TimeoutException {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";
        var style = robot.lookup("#ElevatorTarget0_0").query().getStyle();
        Assertions.assertEquals(base + "green;", style);
        style = robot.lookup("#ElevatorTarget1_0").query().getStyle();
        Assertions.assertEquals(base + "green;", style);
        style = robot.lookup("#ElevatorTarget2_0").query().getStyle();
        Assertions.assertEquals(base + "green;", style);
        style = robot.lookup("#ElevatorTarget3_0").query().getStyle();
        Assertions.assertEquals(base + "green;", style);

        mock.setElevatorFloor(0, 2);
        mock.setElevatorFloor(1, 1);
        mock.setElevatorFloor(2, 3);

        waitFor(1, TimeUnit.SECONDS, () -> robot.lookup("#ElevatorTarget0_2").query().getStyle().equals(base + "green;"));

        style = robot.lookup("#ElevatorTarget0_2").query().getStyle();
        Assertions.assertEquals(base + "green;", style);
        style = robot.lookup("#ElevatorTarget1_1").query().getStyle();
        Assertions.assertEquals(base + "green;", style);
        style = robot.lookup("#ElevatorTarget2_3").query().getStyle();
        Assertions.assertEquals(base + "green;", style);
        style = robot.lookup("#ElevatorTarget3_0").query().getStyle();
        Assertions.assertEquals(base + "green;", style);
    }

    @Test
    void testElevatorStops() throws TimeoutException {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";

        var style = robot.lookup("#PressedInEle0_3").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#PressedInEle0_4").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#PressedInEle1_0").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#PressedInEle2_1").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#PressedInEle3_2").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        mock.setElevatorButton(0, 3, true);
        mock.setElevatorButton(0, 4, true);
        mock.setElevatorButton(1, 0, true);
        mock.setElevatorButton(2, 1, true);
        mock.setElevatorButton(3, 2, true);

        waitFor(1, TimeUnit.SECONDS, () -> robot.lookup("#PressedInEle3_2").query().getStyle().equals(base + "yellow;"));

        style = robot.lookup("#PressedInEle0_0").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#PressedInEle0_1").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#PressedInEle0_2").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#PressedInEle0_3").query().getStyle();
        Assertions.assertEquals(base + "yellow;", style);

        style = robot.lookup("#PressedInEle0_4").query().getStyle();
        Assertions.assertEquals(base + "yellow;", style);

        style = robot.lookup("#PressedInEle0_5").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#PressedInEle1_0").query().getStyle();
        Assertions.assertEquals(base + "yellow;", style);

        style = robot.lookup("#PressedInEle2_1").query().getStyle();
        Assertions.assertEquals(base + "yellow;", style);

        style = robot.lookup("#PressedInEle3_2").query().getStyle();
        Assertions.assertEquals(base + "yellow;", style);
    }

    @Test
    void testElevatorServiced() throws TimeoutException {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";

        var style = robot.lookup("#ElevatorTarget0_1").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#ElevatorTarget0_2").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#ElevatorTarget0_3").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#ElevatorTarget3_4").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        mock.setServicesFloors(3, 4, false);
        mock.setServicesFloors(0, 1, false);
        mock.setServicesFloors(0, 2, false);
        mock.setServicesFloors(0, 3, false);

        waitFor(1, TimeUnit.SECONDS, () -> robot.lookup("#ElevatorTarget3_4").query().getStyle().equals(base + "grey;"));

        style = robot.lookup("#ElevatorTarget0_1").query().getStyle();
        Assertions.assertEquals(base + "grey;", style);

        style = robot.lookup("#ElevatorTarget0_2").query().getStyle();
        Assertions.assertEquals(base + "grey;", style);

        style = robot.lookup("#ElevatorTarget0_3").query().getStyle();
        Assertions.assertEquals(base + "grey;", style);

        style = robot.lookup("#ElevatorTarget3_4").query().getStyle();
        Assertions.assertEquals(base + "grey;", style);
    }

    @Test
    void testElevatorNormal() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";
        var style = robot.lookup("#ElevatorTarget0_1").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#ElevatorTarget1_2").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#ElevatorTarget2_3").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);

        style = robot.lookup("#ElevatorTarget3_4").query().getStyle();
        Assertions.assertEquals(base + "silver;", style);
    }

    @Test
    void testElevatorAccel() throws TimeoutException {
        FxAssert.verifyThat("#Accel1", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Accel2", LabeledMatchers.hasText("0"));

        mock.setAccel(1, -1);
        mock.setAccel(2, 3);

        waitFor(1, TimeUnit.SECONDS, () -> LabeledMatchers.hasText("3").matches(robot.lookup("#Accel2").query()));

        FxAssert.verifyThat("#Accel1", LabeledMatchers.hasText("-1"));
        FxAssert.verifyThat("#Accel2", LabeledMatchers.hasText("3"));
    }

    @Test
    void testFloorCalls() throws TimeoutException {
        final String base = "-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';-fx-background-color: ";

        var styleUp = robot.lookup("#FloorArrow0_0").query().getStyle();
        var styleDown = robot.lookup("#FloorArrow0_1").query().getStyle();
        Assertions.assertEquals(base + "silver;", styleUp);
        Assertions.assertEquals(base + "silver;", styleDown);

        styleUp = robot.lookup("#FloorArrow1_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow1_1").query().getStyle();
        Assertions.assertEquals(base + "silver;", styleUp);
        Assertions.assertEquals(base + "silver;", styleDown);

        styleUp = robot.lookup("#FloorArrow3_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow3_1").query().getStyle();
        Assertions.assertEquals(base + "silver;", styleUp);
        Assertions.assertEquals(base + "silver;", styleDown);

        styleUp = robot.lookup("#FloorArrow4_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow4_1").query().getStyle();
        Assertions.assertEquals(base + "silver;", styleUp);
        Assertions.assertEquals(base + "silver;", styleDown);

        mock.setFloorUp(0, true);
        mock.setFloorUp(1, true);
        mock.setFloorUp(3, true);
        mock.setFloorDown(0, true);
        mock.setFloorDown(3, true);
        mock.setFloorDown(4, true);

        waitFor(1, TimeUnit.SECONDS, () -> robot.lookup("#FloorArrow4_1").query().getStyle().equals(base + "yellow;"));

        styleUp = robot.lookup("#FloorArrow0_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow0_1").query().getStyle();
        Assertions.assertEquals(base + "yellow;", styleUp);
        Assertions.assertEquals(base + "yellow;", styleDown);

        styleUp = robot.lookup("#FloorArrow1_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow1_1").query().getStyle();
        Assertions.assertEquals(base + "yellow;", styleUp);
        Assertions.assertEquals(base + "silver;", styleDown);

        styleUp = robot.lookup("#FloorArrow3_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow3_1").query().getStyle();
        Assertions.assertEquals(base + "yellow;", styleUp);
        Assertions.assertEquals(base + "yellow;", styleDown);

        styleUp = robot.lookup("#FloorArrow4_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow4_1").query().getStyle();
        Assertions.assertEquals(base + "silver;", styleUp);
        Assertions.assertEquals(base + "yellow;", styleDown);
    }

    @Test
    void testManualMode() throws TimeoutException {
        var baseArrow = "-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';-fx-background-color: ";
        var baseButton = "-fx-background-radius: 30;-fx-border-radius: 20;-fx-border-color: lightblue;-fx-background-color: ";
        mock.setDoorStatus(1, IElevatorService.ELEVATOR_DOORS_OPEN);
        robot.clickOn("#Manual1");

        waitFor(1, TimeUnit.SECONDS, () -> robot.lookup("#ElevatorTarget1_3").query().getStyle().equals(baseButton + "silver;"));

        var styleArrow = robot.lookup("#Arrow1_0").query().getStyle();
        var styleButton = robot.lookup("#ElevatorTarget1_3").query().getStyle();
        Assertions.assertEquals(baseArrow + "silver;", styleArrow);
        Assertions.assertEquals(baseButton + "silver;", styleButton);

        robot.clickOn("#ElevatorTarget1_3");

        waitFor(1, TimeUnit.SECONDS, () -> robot.lookup("#ElevatorTarget1_3").query().getStyle().equals(baseButton + "lightgreen;") &&
                robot.lookup("#Arrow1_0").query().getStyle().equals(baseArrow + "green;"));

        styleArrow = robot.lookup("#Arrow1_0").query().getStyle();
        styleButton = robot.lookup("#ElevatorTarget1_3").query().getStyle();
        Assertions.assertEquals(baseArrow + "green;", styleArrow);
        Assertions.assertEquals(baseButton + "lightgreen;", styleButton);

        mock.setElevatorFloor(1, 3);

        robot.clickOn("#ElevatorTarget1_0");

        waitFor(1, TimeUnit.SECONDS, () -> robot.lookup("#ElevatorTarget1_3").query().getStyle().equals(baseButton + "green;") &&
                robot.lookup("#Arrow1_1").query().getStyle().equals(baseArrow + "green;"));

        var styleOldArrow = robot.lookup("#Arrow1_0").query().getStyle();
        var styleOldButton = robot.lookup("#ElevatorTarget1_3").query().getStyle();
        Assertions.assertEquals(baseArrow + "silver;", styleOldArrow);
        Assertions.assertEquals(baseButton + "green;", styleOldButton);

        styleArrow = robot.lookup("#Arrow1_1").query().getStyle();
        styleButton = robot.lookup("#ElevatorTarget1_0").query().getStyle();
        Assertions.assertEquals(baseArrow + "green;", styleArrow);
        Assertions.assertEquals(baseButton + "lightgreen;", styleButton);
    }

    @Test
    void testManualModeOpacity() throws TimeoutException {
        robot.clickOn("#Manual0");

        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_0").query().getOpacity());
        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_3").query().getOpacity());

        robot.clickOn("#ElevatorTarget0_3");

        waitFor(1, TimeUnit.SECONDS, () -> (robot.lookup("#ElevatorTarget0_0").query().getOpacity() == 0.7));

        Assertions.assertEquals(0.7, robot.lookup("#ElevatorTarget0_0").query().getOpacity());
        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_3").query().getOpacity());

        robot.clickOn("#ElevatorTarget0_0");

        waitFor(1, TimeUnit.SECONDS, () -> robot.lookup("#ElevatorTarget0_3").query().getOpacity() == 0.7);

        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_0").query().getOpacity());
        Assertions.assertEquals(0.7, robot.lookup("#ElevatorTarget0_3").query().getOpacity());

        robot.clickOn("#Manual0");

        waitFor(1, TimeUnit.SECONDS, () -> robot.lookup("#ElevatorTarget0_3").query().getOpacity() == 1);

        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_0").query().getOpacity());
        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_3").query().getOpacity());
    }

}