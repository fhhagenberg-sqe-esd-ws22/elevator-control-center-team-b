/**
 * Author: Philipp Holzer
 * Mat.-Nr.: s2110567016
 */
package at.fhhagenberg.logic;

import at.fhhagenberg.service.IElevatorService;
import at.fhhagenberg.updater.IUpdater;
import at.fhhagenberg.viewmodels.BuildingViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppControllerTest {

    @Mock
    ScheduledExecutorService executor;

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

    AppController controller;

    @BeforeEach
    void setup() {
        controller = new AppController(service, updater, logic, vm, executor, showErrorCb, showInfoCb);
    }

    @Test
    void testObjectCreationFails() {
        assertThrows(LogicException.class, () -> new AppController(null, updater, logic, vm, executor, showErrorCb, showInfoCb));
        assertThrows(LogicException.class, () -> new AppController(service, null, logic, vm, executor, showErrorCb, showInfoCb));
        assertThrows(LogicException.class, () -> new AppController(service, updater, null, vm, executor, showErrorCb, showInfoCb));
        assertThrows(LogicException.class, () -> new AppController(service, updater, logic, null, executor, showErrorCb, showInfoCb));
        assertThrows(LogicException.class, () -> new AppController(service, updater, logic, vm, null, showErrorCb, showInfoCb));
        assertThrows(LogicException.class, () -> new AppController(service, updater, logic, vm, executor, null, showInfoCb));
        assertThrows(LogicException.class, () -> new AppController(service, updater, logic, vm, executor, showErrorCb, null));
    }

    @Test
    void testStart() {
        controller.start();
        verify(executor).scheduleAtFixedRate(any(), longThat(integer -> integer == AppController.UPDATE_INTERVAL_MS), longThat(integer -> integer == AppController.UPDATE_INTERVAL_MS), any(TimeUnit.class));
    }

    @Test
    void testStop() throws InterruptedException {
        when(executor.awaitTermination(longThat(l -> l == AppController.TERMINATION_TIMEOUT_MS), any(TimeUnit.class))).thenReturn(true);
        controller.stop();
        verify(executor, times(1)).shutdown();
    }

    @Test
    void testStopAwaitTerminationFails() throws InterruptedException {
        when(executor.awaitTermination(anyLong(), any())).thenReturn(false);
        controller.stop();
        verify(executor, times(1)).shutdown();
        verify(executor, times(1)).shutdownNow();
    }

    @Test
    void testStopAwaitTerminateThrowsInterruptedException() throws InterruptedException {
        when(executor.awaitTermination(anyLong(), any())).thenThrow(InterruptedException.class);
        controller.stop();
        verify(executor, times(1)).shutdown();
        verify(executor, times(1)).shutdownNow();
    }
}
