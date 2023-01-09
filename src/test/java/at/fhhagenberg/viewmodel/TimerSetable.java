package at.fhhagenberg.viewmodel;

import java.util.Timer;
import java.util.TimerTask;

public class TimerSetable extends Timer {

    TimerTask mTask;

    @Override
    public void schedule(TimerTask task, long delay){
        mTask = task;
    }

    public void forceUpdate(){
        mTask.run();
        try {
            mTask.wait();
        }
        catch (Exception e){
            //throw new RuntimeException(e.getMessage());
        }
    }
}
