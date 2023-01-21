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
import org.testfx.service.query.EmptyNodeQueryException;

import at.fhhagenberg.service.IElevatorService;
import javafx.scene.control.Button;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeoutException;

@ExtendWith(ApplicationExtension.class)
class AppTest {

    static private final int TIMEOUT = 50000;

    FxRobot robot;

    @BeforeEach
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(TestECCApp.class);
        robot = new FxRobot();
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
    void testCosmeticMembersOfPressedInEleButtons() {
        var node = robot.lookup("#PressedInEle0_0").query();
        assertTrue(node instanceof Button);
        var button = (Button)node;
        assertTrue(button.isDisabled());
        assertEquals(1, button.opacityProperty().get());
        assertEquals(40, button.prefHeightProperty().get());
    }

    @Test 
    void testCosmeticMembersOfElevatorDirectionUpArrow() {
        var node = robot.lookup(String.format("#Arrow0_%d", IElevatorService.ELEVATOR_DIRECTION_UP)).query();
        assertTrue(node instanceof Button);
        var button = (Button)node;
        assertTrue(button.isDisabled());
        assertEquals(1, button.opacityProperty().get());
        assertEquals(270, button.rotateProperty().get());
    }

    @Test 
    void testCosmeticMembersOfElevatorDirectionDownArrow() {
        var node = robot.lookup(String.format("#Arrow0_%d", IElevatorService.ELEVATOR_DIRECTION_DOWN)).query();
        assertTrue(node instanceof Button);
        var button = (Button)node;
        assertTrue(button.isDisabled());
        assertEquals(1, button.opacityProperty().get());
        assertEquals(90, button.rotateProperty().get());
    }

    @Test 
    void testCosmeticMembersOfFloorUpArrow() {
        var node = robot.lookup(String.format("#FloorArrow0_%d", IElevatorService.ELEVATOR_DIRECTION_UP)).query();
        assertTrue(node instanceof Button);
        var button = (Button)node;
        assertTrue(button.isDisabled());
        assertEquals(1, button.opacityProperty().get());
        assertEquals(270, button.rotateProperty().get());
    }

    @Test 
    void testCosmeticMembersOfFloorDownArrow() {
        var node = robot.lookup(String.format("#FloorArrow0_%d", IElevatorService.ELEVATOR_DIRECTION_DOWN)).query();
        assertTrue(node instanceof Button);
        var button = (Button)node;
        assertTrue(button.isDisabled());
        assertEquals(1, button.opacityProperty().get());
        assertEquals(90, button.rotateProperty().get());
    }

    @Test
    void testElevatorButtonCountAndElevatorTargetButtonCountElevator0() {
        for(int i = 0; i < TestECCApp.service.getFloorNum(); i++) {
            assertNotNull(robot.lookup(String.format("#PressedInEle0_%d", i)).query());
            assertNotNull(robot.lookup(String.format("#ElevatorTarget0_%d", i)).query());
        }

        assertThrows(EmptyNodeQueryException.class, () -> 
            { robot.lookup(String.format("#PressedInEle0_%d", 
                TestECCApp.service.getFloorNum())).query(); 
            });
        assertThrows(EmptyNodeQueryException.class, () ->
            { robot.lookup(String.format("#ElevatorTarget0_%d", 
                TestECCApp.service.getFloorNum())).query();
            });
    }

    @Test
    void testElevatorDoors() {
        FxAssert.verifyThat("#Door0", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door1", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door2", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door3", LabeledMatchers.hasText("Closed"));
        
        TestECCApp.service.setDoorStatus(0, IElevatorService.ELEVATOR_DOORS_CLOSED);
        TestECCApp.service.setDoorStatus(1, IElevatorService.ELEVATOR_DOORS_CLOSING);
        TestECCApp.service.setDoorStatus(2, IElevatorService.ELEVATOR_DOORS_OPEN);
        TestECCApp.service.setDoorStatus(3, IElevatorService.ELEVATOR_DOORS_OPENING);

        int cnt = 0;
        while ((!LabeledMatchers.hasText("Closed").matches(robot.lookup("#Door0").query()) ||
                !LabeledMatchers.hasText("Closing").matches(robot.lookup("#Door1").query()) ||
                !LabeledMatchers.hasText("Open").matches(robot.lookup("#Door2").query()) ||
                !LabeledMatchers.hasText("Opening").matches(robot.lookup("#Door3").query()))&& cnt < TIMEOUT) {
            cnt++;
        }
        
        FxAssert.verifyThat("#Door0", LabeledMatchers.hasText("Closed"));
        FxAssert.verifyThat("#Door1", LabeledMatchers.hasText("Closing"));
        FxAssert.verifyThat("#Door2", LabeledMatchers.hasText("Open"));
        FxAssert.verifyThat("#Door3", LabeledMatchers.hasText("Opening"));
    }

    @Test
    void testElevatorPayload() {
        FxAssert.verifyThat("#Payload0", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Payload1", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Payload2", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Payload3", LabeledMatchers.hasText("0"));

        TestECCApp.service.setWeight(0, 10);
        TestECCApp.service.setWeight(1, 20);
        TestECCApp.service.setWeight(2, 30);
        TestECCApp.service.setWeight(3, 40);

        int cnt = 0;
        while ((!LabeledMatchers.hasText("10").matches(robot.lookup("#Payload0").query()) ||
                !LabeledMatchers.hasText("20").matches(robot.lookup("#Payload1").query()) ||
                !LabeledMatchers.hasText("30").matches(robot.lookup("#Payload2").query()) ||
                !LabeledMatchers.hasText("40").matches(robot.lookup("#Payload3").query()))&& cnt < TIMEOUT) {
            cnt++;
        }

        FxAssert.verifyThat("#Payload0", LabeledMatchers.hasText("10"));
        FxAssert.verifyThat("#Payload1", LabeledMatchers.hasText("20"));
        FxAssert.verifyThat("#Payload2", LabeledMatchers.hasText("30"));
        FxAssert.verifyThat("#Payload3", LabeledMatchers.hasText("40"));
    }

    @Test
    void testElevatorSpeed() {
        FxAssert.verifyThat("#Speed1", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Speed2", LabeledMatchers.hasText("0"));
        
        TestECCApp.service.setSpeed(1, 5);
        TestECCApp.service.setSpeed(2, 3);

        int cnt = 0;
        while ((!LabeledMatchers.hasText("5").matches(robot.lookup("#Speed1").query()) ||
            !LabeledMatchers.hasText("3").matches(robot.lookup("#Speed2").query())) && 
            cnt < TIMEOUT) {
            cnt++;
        }

        FxAssert.verifyThat("#Speed1", LabeledMatchers.hasText("5"));
        FxAssert.verifyThat("#Speed2", LabeledMatchers.hasText("3"));
    }

    @Test
    void testElevatorFloor() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";
        var style = robot.lookup("#ElevatorTarget0_0").query().getStyle();
        Assertions.assertEquals(base+"green;", style);
        style = robot.lookup("#ElevatorTarget1_0").query().getStyle();
        Assertions.assertEquals(base+"green;", style);
        style = robot.lookup("#ElevatorTarget2_0").query().getStyle();
        Assertions.assertEquals(base+"green;", style);
        style = robot.lookup("#ElevatorTarget3_0").query().getStyle();
        Assertions.assertEquals(base+"green;", style);

        TestECCApp.service.setElevatorFloor(0, 2);
        TestECCApp.service.setElevatorFloor(1, 1);
        TestECCApp.service.setElevatorFloor(2, 3);

        int cnt = 0;
        while ((!robot.lookup("#ElevatorTarget2_3").query().getStyle().equals(base+"green;") ||
                !robot.lookup("#ElevatorTarget0_2").query().getStyle().equals(base+"green;") ||
                !robot.lookup("#ElevatorTarget1_1").query().getStyle().equals(base+"green;") ||
                !robot.lookup("#ElevatorTarget3_0").query().getStyle().equals(base+"green;"))&& cnt < TIMEOUT) {
            cnt++;
        }

        assertTrue(cnt < TIMEOUT);

        style = robot.lookup("#ElevatorTarget0_2").query().getStyle();
        Assertions.assertEquals(base+"green;", style);
        style = robot.lookup("#ElevatorTarget1_1").query().getStyle();
        Assertions.assertEquals(base+"green;", style);
        style = robot.lookup("#ElevatorTarget2_3").query().getStyle();
        Assertions.assertEquals(base+"green;", style);
        style = robot.lookup("#ElevatorTarget3_0").query().getStyle();
        Assertions.assertEquals(base+"green;", style);
    }

    @Test
    void testElevatorStops() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";

        var style = robot.lookup("#PressedInEle0_3").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#PressedInEle0_4").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#PressedInEle1_0").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#PressedInEle2_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#PressedInEle3_2").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        TestECCApp.service.setElevatorButton(0, 3, true);
        TestECCApp.service.setElevatorButton(0, 4, true);
        TestECCApp.service.setElevatorButton(1, 0, true);
        TestECCApp.service.setElevatorButton(2, 1, true);
        TestECCApp.service.setElevatorButton(3, 2, true);

        int cnt = 0;
        while (!robot.lookup("#PressedInEle3_2").query().getStyle().equals(base+"yellow;") &&
            cnt < TIMEOUT) {
            cnt++;
        }

        style = robot.lookup("#PressedInEle0_0").query().getStyle();
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

        style = robot.lookup("#PressedInEle1_0").query().getStyle();
        Assertions.assertEquals(base+"yellow;", style);

        style = robot.lookup("#PressedInEle2_1").query().getStyle();
        Assertions.assertEquals(base+"yellow;", style);

        style = robot.lookup("#PressedInEle3_2").query().getStyle();
        Assertions.assertEquals(base+"yellow;", style);
    }

    @Test
    void testElevatorServiced() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";

        var style = robot.lookup("#ElevatorTarget0_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget0_2").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget0_3").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget3_4").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        TestECCApp.service.setServicesFloors(3,4,false);
        TestECCApp.service.setServicesFloors(0,1,false);
        TestECCApp.service.setServicesFloors(0,2,false);
        TestECCApp.service.setServicesFloors(0,3,false);

        int cnt = 0;
        while(!robot.lookup("#ElevatorTarget3_4").query().getStyle().equals(base+"grey;") &&
            cnt < TIMEOUT) {
            cnt++;
        }

        style = robot.lookup("#ElevatorTarget0_1").query().getStyle();
        Assertions.assertEquals(base+"grey;", style);

        style = robot.lookup("#ElevatorTarget0_2").query().getStyle();
        Assertions.assertEquals(base+"grey;", style);

        style = robot.lookup("#ElevatorTarget0_3").query().getStyle();
        Assertions.assertEquals(base+"grey;", style);

        style = robot.lookup("#ElevatorTarget3_4").query().getStyle();
        Assertions.assertEquals(base+"grey;", style);
    }

    @Test
    void testElevatorNormal() {
        final String base = "-fx-background-radius: 0;-fx-background-color: ";
        var style = robot.lookup("#ElevatorTarget0_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget1_2").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget2_3").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);

        style = robot.lookup("#ElevatorTarget3_4").query().getStyle();
        Assertions.assertEquals(base+"silver;", style);
    }

    @Test
    void testElevatorAccel() {
        FxAssert.verifyThat("#Accel1", LabeledMatchers.hasText("0"));
        FxAssert.verifyThat("#Accel2", LabeledMatchers.hasText("0"));
        
        TestECCApp.service.setAccel(1, -1);
        TestECCApp.service.setAccel(2, 3);

        int cnt = 0;
        while ((!LabeledMatchers.hasText("-1").matches(robot.lookup("#Accel1").query()) || 
            !LabeledMatchers.hasText("3").matches(robot.lookup("#Accel2").query())) && 
            cnt < TIMEOUT) {
            cnt++;
        }

        FxAssert.verifyThat("#Accel1", LabeledMatchers.hasText("-1"));
        FxAssert.verifyThat("#Accel2", LabeledMatchers.hasText("3"));
    }

    @Test
    void testFloorCalls() {
        final String base = "-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';-fx-background-color: ";

        var styleUp = robot.lookup("#FloorArrow0_0").query().getStyle();
        var styleDown = robot.lookup("#FloorArrow0_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", styleUp);
        Assertions.assertEquals(base+"silver;", styleDown);

        styleUp = robot.lookup("#FloorArrow1_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow1_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", styleUp);
        Assertions.assertEquals(base+"silver;", styleDown);

        styleUp = robot.lookup("#FloorArrow3_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow3_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", styleUp);
        Assertions.assertEquals(base+"silver;", styleDown);

        styleUp = robot.lookup("#FloorArrow4_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow4_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", styleUp);
        Assertions.assertEquals(base+"silver;", styleDown);
        
        TestECCApp.service.setFloorUp(0, true);
        TestECCApp.service.setFloorUp(1, true);
        TestECCApp.service.setFloorUp(3, true);
        TestECCApp.service.setFloorDown(0, true);
        TestECCApp.service.setFloorDown(3, true);
        TestECCApp.service.setFloorDown(4, true);

        int cnt = 0;
        while(!robot.lookup("#FloorArrow4_1").query().getStyle().equals(base+"yellow;") &&
            cnt < TIMEOUT) {
            cnt++;
        }

        styleUp = robot.lookup("#FloorArrow0_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow0_1").query().getStyle();
        Assertions.assertEquals(base+"yellow;", styleUp);
        Assertions.assertEquals(base+"yellow;", styleDown);

        styleUp = robot.lookup("#FloorArrow1_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow1_1").query().getStyle();
        Assertions.assertEquals(base+"yellow;", styleUp);
        Assertions.assertEquals(base+"silver;", styleDown);

        styleUp = robot.lookup("#FloorArrow3_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow3_1").query().getStyle();
        Assertions.assertEquals(base+"yellow;", styleUp);
        Assertions.assertEquals(base+"yellow;", styleDown);

        styleUp = robot.lookup("#FloorArrow4_0").query().getStyle();
        styleDown = robot.lookup("#FloorArrow4_1").query().getStyle();
        Assertions.assertEquals(base+"silver;", styleUp);
        Assertions.assertEquals(base+"yellow;", styleDown);
    }

    @Test
    void testManualMode(){
        var baseArrow ="-fx-shape: 'M 0 -3.5 v 7 l 4 -3.5 z';-fx-background-color: ";
        var baseButton = "-fx-background-radius: 30;-fx-border-radius: 20;-fx-border-color: lightblue;-fx-background-color: ";
        TestECCApp.service.setDoorStatus(1, IElevatorService.ELEVATOR_DOORS_OPEN);
        robot.clickOn("#Manual1");

        int cnt = 0;
        while(!robot.lookup("#ElevatorTarget1_3").query().getStyle().equals(baseButton+"silver;") &&
                cnt < TIMEOUT) {
            cnt++;
        }

        var styleArrow = robot.lookup("#Arrow1_0").query().getStyle();
        var styleButton = robot.lookup("#ElevatorTarget1_3").query().getStyle();
        Assertions.assertEquals(baseArrow+"silver;", styleArrow);
        Assertions.assertEquals(baseButton+"silver;", styleButton);

        robot.clickOn("#ElevatorTarget1_3");


        cnt = 0;
        while((!robot.lookup("#ElevatorTarget1_3").query().getStyle().equals(baseButton+"lightgreen;") ||
                !robot.lookup("#Arrow1_0").query().getStyle().equals(baseArrow+"green;")) &&
                cnt < TIMEOUT) {
            cnt++;
        }

        styleArrow = robot.lookup("#Arrow1_0").query().getStyle();
        styleButton = robot.lookup("#ElevatorTarget1_3").query().getStyle();
        Assertions.assertEquals(baseArrow+"green;", styleArrow);
        Assertions.assertEquals(baseButton+"lightgreen;", styleButton);

        TestECCApp.service.setElevatorFloor(1, 3);

        robot.clickOn("#ElevatorTarget1_0");

        cnt = 0;
        while((!robot.lookup("#ElevatorTarget1_0").query().getStyle().equals(baseButton+"lightgreen;") ||
                !robot.lookup("#Arrow1_0").query().getStyle().equals(baseArrow+"silver;") ||
                !robot.lookup("#ElevatorTarget1_3").query().getStyle().equals(baseButton+"green;")||
                !robot.lookup("#Arrow1_1").query().getStyle().equals(baseArrow+"green;"))&&
                cnt < TIMEOUT) {
            cnt++;
        }

        var styleOldArrow = robot.lookup("#Arrow1_0").query().getStyle();
        var styleOldButton = robot.lookup("#ElevatorTarget1_3").query().getStyle();
        Assertions.assertEquals(baseArrow+"silver;", styleOldArrow);
        Assertions.assertEquals(baseButton+"green;", styleOldButton);

        styleArrow = robot.lookup("#Arrow1_1").query().getStyle();
        styleButton = robot.lookup("#ElevatorTarget1_0").query().getStyle();
        Assertions.assertEquals(baseArrow+"green;", styleArrow);
        Assertions.assertEquals(baseButton+"lightgreen;", styleButton);
    }

    @Test
    void testManualModeOpacity(){
        robot.clickOn("#Manual0");

        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_0").query().getOpacity());
        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_3").query().getOpacity());

        robot.clickOn("#ElevatorTarget0_3");

        int cnt = 0;
        while((robot.lookup("#ElevatorTarget0_0").query().getOpacity() != 0.7)  &&
                cnt < TIMEOUT) {
            cnt++;
        }

        Assertions.assertEquals(0.7, robot.lookup("#ElevatorTarget0_0").query().getOpacity());
        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_3").query().getOpacity());

        robot.clickOn("#ElevatorTarget0_0");

        cnt = 0;
        while((robot.lookup("#ElevatorTarget0_0").query().getOpacity() != 1 ||
                robot.lookup("#ElevatorTarget0_3").query().getOpacity() != 0.7)  &&
                cnt < TIMEOUT) {
            cnt++;
        }

        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_0").query().getOpacity());
        Assertions.assertEquals(0.7, robot.lookup("#ElevatorTarget0_3").query().getOpacity());

        robot.clickOn("#Manual0");

        cnt = 0;
        while((robot.lookup("#ElevatorTarget0_3").query().getOpacity() != 1)  &&
                cnt < TIMEOUT) {
            cnt++;
        }

        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_0").query().getOpacity());
        Assertions.assertEquals(1, robot.lookup("#ElevatorTarget0_3").query().getOpacity());
    }

}