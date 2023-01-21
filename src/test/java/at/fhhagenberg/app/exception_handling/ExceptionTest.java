package at.fhhagenberg.app.exception_handling;

import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.testfx.util.WaitForAsyncUtils.waitFor;

public class ExceptionTest {
    FxRobot robot;

    @BeforeEach
    public void setup() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        robot = new FxRobot();
    }

    @Test
    void testServiceNull() throws TimeoutException {
        FxToolkit.setupApplication(ECCAppNullServiceTest.class);

        waitFor(10, TimeUnit.SECONDS, () -> robot.lookup(".dialog-pane").query().isVisible());

        var dialogPane = robot.lookup(".dialog-pane").query();
        FxAssert.verifyThat(dialogPane, NodeMatchers.isVisible());

        var okButton = robot.from(dialogPane).lookup((Text t) -> t.getText().startsWith("OK"));
        robot.clickOn(okButton.queryText());
    }
    
    @Test
    void testCannotCreateScene() throws TimeoutException {
        FxToolkit.setupApplication(ECCAppExceptionTest.class);

        waitFor(10, TimeUnit.SECONDS, () -> robot.lookup(".dialog-pane").query().isVisible());

        var dialogPane = robot.lookup(".dialog-pane").query();
        FxAssert.verifyThat(dialogPane, NodeMatchers.isVisible());
        
        var okButton = robot.from(dialogPane).lookup((Text t) -> t.getText().startsWith("OK"));
        robot.clickOn(okButton.queryText());
    }
}
