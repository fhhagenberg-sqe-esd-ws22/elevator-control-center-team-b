package at.fhhagenberg.viewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        model.setWantUp(true);
        viewModel.update();
        assertEquals("Up", viewModel.getWantUpStrProp().get());
    }    
}
