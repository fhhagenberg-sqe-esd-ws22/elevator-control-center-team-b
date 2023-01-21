package at.fhhagenberg.app.exception_handling;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;
import javafx.scene.Node;
import javafx.scene.text.Text;

public class NullServiceTest {
    
    FxRobot robot;

    @BeforeEach
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(ECCAppNullServiceTest.class);
        robot = new FxRobot();
    }
    
    @Test
    void testServiceNull() {
        Node dialogPane = robot.lookup(".dialog-pane").query();
        var contentQuery = robot.from(dialogPane).lookup(
            (Text t) -> t.getText().startsWith("The service could not be created"));
        assertNotNull(contentQuery.query());
        FxAssert.verifyThat(dialogPane, NodeMatchers.isVisible());
        var okButton = robot.from(dialogPane).lookup((Text t) -> t.getText().startsWith("OK"));
        robot.clickOn(okButton.queryText());
    }
}