package at.fhhagenberg.app.exception_handling;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.service.query.EmptyNodeQueryException;

import javafx.scene.Node;
import javafx.scene.text.Text;

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
        
        int cnt = 0;
        Node dialogPane = null;
        // todo @Kargl check timeout
        do
        {
            try {
                dialogPane = robot.lookup(".dialog-pane").query();
            } catch (EmptyNodeQueryException ex) {}
            cnt = cnt + 1;
        } while (dialogPane == null && cnt < 1000000);
        
        var contentQuery = robot.from(dialogPane).lookup(
            (Text t) -> t.getText().startsWith("The service could not be created"));
        
        FxAssert.verifyThat(dialogPane, NodeMatchers.isVisible());
        assertNotNull(contentQuery.query());

        var okButton = robot.from(dialogPane).lookup((Text t) -> t.getText().startsWith("OK"));
        robot.clickOn(okButton.queryText());
    }
    
    @Test
    void testCannotCreateScene() throws TimeoutException {
        FxToolkit.setupApplication(ECCAppExceptionTest.class);

        int cnt = 0;
        Node dialogPane = robot.lookup(".dialog-pane").query();
        // todo @Kargl check timeout
        do
        {
            try {
                dialogPane = robot.lookup(".dialog-pane").query();
            } catch (EmptyNodeQueryException ex) {}
            cnt = cnt + 1;
        } while (dialogPane == null && cnt < 1000000);

        var contentQuery = robot.from(dialogPane).lookup(
            (Text t) -> t.getText().startsWith("The app could not be started"));
        FxAssert.verifyThat(dialogPane, NodeMatchers.isVisible());
        assertNotNull(contentQuery.query());
        
        var okButton = robot.from(dialogPane).lookup((Text t) -> t.getText().startsWith("OK"));
        robot.clickOn(okButton.queryText());
    }
}
