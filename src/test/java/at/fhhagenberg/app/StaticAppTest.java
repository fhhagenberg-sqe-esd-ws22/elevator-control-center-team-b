package at.fhhagenberg.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.matcher.control.LabeledMatchers;

import java.util.concurrent.TimeoutException;

@ExtendWith(ApplicationExtension.class)
class StaticAppTest {

    FxRobot robot;

    @BeforeEach
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(TestECCApp.class);
        robot = new FxRobot();
    }

    @Test
    void testElevatorHeaders() {
        FxAssert.verifyThat("#Header0", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Header1", LabeledMatchers.hasText("1"));
        FxAssert.verifyThat("#Header2", LabeledMatchers.hasText("2"));
        FxAssert.verifyThat("#Header3", LabeledMatchers.hasText("3"));
    }

    @Test
    void testElevatorDoors() {
        FxAssert.verifyThat("#Door0", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door1", LabeledMatchers.hasText("Closing"));
        FxAssert.verifyThat("#Door2", LabeledMatchers.hasText("Open"));
        FxAssert.verifyThat("#Door3", LabeledMatchers.hasText("Opening"));
    }

    @Test
    void testElevatorPayload() {
        FxAssert.verifyThat("#Payload0", LabeledMatchers.hasText("10"));
        FxAssert.verifyThat("#Payload1", LabeledMatchers.hasText("20"));
        FxAssert.verifyThat("#Payload2", LabeledMatchers.hasText("30"));
        FxAssert.verifyThat("#Payload3", LabeledMatchers.hasText("40"));
    }

    @Test
    void testElevatorSpeed() {
        FxAssert.verifyThat("#Speed0", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Speed1", LabeledMatchers.hasText("5"));
        FxAssert.verifyThat("#Speed2", LabeledMatchers.hasText("3"));
        FxAssert.verifyThat("#Speed3", LabeledMatchers.hasText("0"));
    }

    @Test
    void testElevatorNearest() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";
        var style = robot.lookup("#ElevatorTarget0_2").query().getStyle();
        Assertions.assertEquals(base+"green;", style);

        style = robot.lookup("#ElevatorTarget1_1").query().getStyle();
        Assertions.assertEquals(base+"green;", style);

        style = robot.lookup("#ElevatorTarget2_3").query().getStyle();
        Assertions.assertEquals(base+"green;", style);

        style = robot.lookup("#ElevatorTarget3_0").query().getStyle();
        Assertions.assertEquals(base+"green;", style);
    }

    @Test
    void testElevatorTarget() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";
        var style = robot.lookup("#ElevatorTarget0_0").query().getStyle();
        Assertions.assertEquals(base+"lightgreen;", style);

        style = robot.lookup("#ElevatorTarget1_3").query().getStyle();
        Assertions.assertEquals(base+"lightgreen;", style);

        style = robot.lookup("#ElevatorTarget2_2").query().getStyle();
        Assertions.assertEquals(base+"lightgreen;", style);
    }

    @Test
    void testElevatorDirection() {
        final String base = "-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';-fx-background-color: ";

        var styleUp = robot.lookup("#Arrow0_0").query().getStyle();
        var styleDown = robot.lookup("#Arrow0_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", styleUp);
        Assertions.assertEquals(base+"silver;", styleDown);

        styleUp = robot.lookup("#Arrow1_0").query().getStyle();
        styleDown = robot.lookup("#Arrow1_1").query().getStyle();
        Assertions.assertEquals(base+"green;", styleUp);
        Assertions.assertEquals(base+"silver;", styleDown);

        styleUp = robot.lookup("#Arrow2_0").query().getStyle();
        styleDown = robot.lookup("#Arrow2_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", styleUp);
        Assertions.assertEquals(base+"green;", styleDown);
    }

    @Test
    void testElevatorStops() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";
        var style = robot.lookup("#PressedInEle0_0").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#PressedInEle0_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#PressedInEle0_2").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#PressedInEle0_3").query().getStyle();
        Assertions.assertEquals(base+"yellow;", style);

        style = robot.lookup("#PressedInEle0_4").query().getStyle();
        Assertions.assertEquals(base+"yellow;", style);

        style = robot.lookup("#PressedInEle0_5").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);
    }

    @Test
    void testElevatorServiced() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";
        var style = robot.lookup("#ElevatorTarget0_1").query().getStyle();
        Assertions.assertEquals(base+"grey;", style);

        style = robot.lookup("#ElevatorTarget0_3").query().getStyle();
        Assertions.assertEquals(base+"grey;", style);

        style = robot.lookup("#ElevatorTarget3_4").query().getStyle();
        Assertions.assertEquals(base+"grey;", style);
    }

    @Test
    void testElevatorNormal() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";
        var style = robot.lookup("#ElevatorTarget0_4").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget0_5").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget1_0").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget1_2").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget1_4").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget1_5").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);
    }

    @Test
    void testFloorCalls() {

        final String base = "-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';-fx-background-color: ";

        var styleUp = robot.lookup("#FloorArrow0_0").query().getStyle();
        var styleDown = robot.lookup("#FloorArrow0_1").query().getStyle();
        Assertions.assertEquals(base+"yellow;", styleUp);
        Assertions.assertEquals(base+"yellow;", styleDown);

        styleUp = robot.lookup("#FloorArrow1_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow1_1").query().getStyle();
        Assertions.assertEquals(base+"yellow;", styleUp);
        Assertions.assertEquals(base+"silver;", styleDown);

        styleUp = robot.lookup("#FloorArrow2_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow2_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", styleUp);
        Assertions.assertEquals(base+"silver;", styleDown);
    }
}