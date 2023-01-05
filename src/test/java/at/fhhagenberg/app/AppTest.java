package at.fhhagenberg.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.ButtonMatchers;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class AppTest {
    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     *
     * @param stage - Will be injected by the test runner.
     */
    @Start
    public void start(Stage stage) {
        var app = new TestECCApp();
        app.start(stage);
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testElevatorHeaders(FxRobot robot) {
        FxAssert.verifyThat("#Header0", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Header1", LabeledMatchers.hasText("1"));
        FxAssert.verifyThat("#Header2", LabeledMatchers.hasText("2"));
        FxAssert.verifyThat("#Header3", LabeledMatchers.hasText("3"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testElevatorDoors(FxRobot robot) {
        FxAssert.verifyThat("#Door0", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door1", LabeledMatchers.hasText("Closing"));
        FxAssert.verifyThat("#Door2", LabeledMatchers.hasText("Open"));
        FxAssert.verifyThat("#Door3", LabeledMatchers.hasText("Opening"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testElevatorPayload(FxRobot robot) {
        FxAssert.verifyThat("#Payload0", LabeledMatchers.hasText("10"));
        FxAssert.verifyThat("#Payload1", LabeledMatchers.hasText("20"));
        FxAssert.verifyThat("#Payload2", LabeledMatchers.hasText("30"));
        FxAssert.verifyThat("#Payload3", LabeledMatchers.hasText("40"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testElevatorSpeed(FxRobot robot) {
        FxAssert.verifyThat("#Speed0", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Speed1", LabeledMatchers.hasText("5"));
        FxAssert.verifyThat("#Speed2", LabeledMatchers.hasText("3"));
        FxAssert.verifyThat("#Speed3", LabeledMatchers.hasText("0"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testElevatorNearest(FxRobot robot) {
        FxAssert.verifyThat("#Nearest0", LabeledMatchers.hasText("2"));
        FxAssert.verifyThat("#Nearest1", LabeledMatchers.hasText("1"));
        FxAssert.verifyThat("#Nearest2", LabeledMatchers.hasText("3"));
        FxAssert.verifyThat("#Nearest3", LabeledMatchers.hasText("0"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testElevatorDirection(FxRobot robot) {
        FxAssert.verifyThat("#Dir0", LabeledMatchers.hasText("Uncommited"));
        FxAssert.verifyThat("#Dir1", LabeledMatchers.hasText("Up"));
        FxAssert.verifyThat("#Dir2", LabeledMatchers.hasText("Down"));
        FxAssert.verifyThat("#Dir3", LabeledMatchers.hasText("Uncommited"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testElevatorStops(FxRobot robot) {
        FxAssert.verifyThat("#Stops0", LabeledMatchers.hasText("43"));
        FxAssert.verifyThat("#Stops1", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Stops2", LabeledMatchers.hasText("543210"));
        FxAssert.verifyThat("#Stops3", LabeledMatchers.hasText("41"));
    }

    /**
     * @param robot - Will be injected by the test runner.
     */
    @Test
    void testFloorCalls(FxRobot robot) {
        FxAssert.verifyThat("#Up0", LabeledMatchers.hasText("Up"));
        FxAssert.verifyThat("#Up1", LabeledMatchers.hasText("Up"));
        FxAssert.verifyThat("#Up2", LabeledMatchers.hasText(""));
        FxAssert.verifyThat("#Up3", LabeledMatchers.hasText("Up"));
        FxAssert.verifyThat("#Up4", LabeledMatchers.hasText(""));
        FxAssert.verifyThat("#Up5", LabeledMatchers.hasText(""));
        FxAssert.verifyThat("#Down0", LabeledMatchers.hasText("Down"));
        FxAssert.verifyThat("#Down1", LabeledMatchers.hasText(""));
        FxAssert.verifyThat("#Down2", LabeledMatchers.hasText(""));
        FxAssert.verifyThat("#Down3", LabeledMatchers.hasText("Down"));
        FxAssert.verifyThat("#Down4", LabeledMatchers.hasText("Down"));
        FxAssert.verifyThat("#Down5", LabeledMatchers.hasText(""));
    }
}