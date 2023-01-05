package at.fhhagenberg.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import at.fhhagenberg.model.Floor;
import at.fhhagenberg.viewmodels.FloorViewModel;

class FloorViewModelTest {
    @Test
    void testWantUp() {
        Floor model = new Floor(0);
        FloorViewModel viewModel = new FloorViewModel(model);
        
        model.setWantUp(false);
        viewModel.update();
        assertEquals("", viewModel.getWantUpStrProp().get());
        assertFalse(viewModel.getWantUp());
        assertFalse(viewModel.getWantUpProp().get());

        model.setWantUp(true);
        viewModel.update();
        assertEquals("Up", viewModel.getWantUpStrProp().get());
        assertTrue(viewModel.getWantUp());
        assertTrue(viewModel.getWantUpProp().get());
    }
    
    @Test
    void testWantDown() {
        Floor model = new Floor(0);
        FloorViewModel viewModel = new FloorViewModel(model);
        
        model.setWantDown(false);
        viewModel.update();
        assertEquals("", viewModel.getWantDownStrProp().get());
        assertFalse(viewModel.getWantDown());
        assertFalse(viewModel.getWantDownProp().get());

        model.setWantDown(true);
        viewModel.update();
        assertEquals("Down", viewModel.getWantDownStrProp().get());
        assertTrue(viewModel.getWantDown());
        assertTrue(viewModel.getWantDownProp().get());
    }
}
