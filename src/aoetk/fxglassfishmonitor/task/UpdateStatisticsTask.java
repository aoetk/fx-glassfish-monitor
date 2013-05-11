package aoetk.fxglassfishmonitor.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import aoetk.fxglassfishmonitor.model.Statistic;
import aoetk.fxglassfishmonitor.serviceclient.ConnectFailedException;

/**
 *
 * @author aoetk
 */
public class UpdateStatisticsTask implements Runnable {

    private List<Statistic> targets = Collections.synchronizedList(new ArrayList<Statistic>());

    @Override
    public void run() {
        for (Statistic statistic : targets) {
            try {
                statistic.update();
            } catch (Exception ex) {
                Logger.getLogger(UpdateStatisticsTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addStatistic(Statistic statistic) {
        targets.add(statistic);
    }

}
