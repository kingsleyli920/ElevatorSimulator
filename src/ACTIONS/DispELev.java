package ACTIONS;

import ITEMS.Elevator;
import ITEMS.People;
import TOOLS.PrinLog;

import java.io.IOException;
import java.io.PipedReader;
import java.util.Comparator;
import java.util.LinkedList;

public class DispELev implements Runnable {
    private final LinkedList<People> PL;
    private final LinkedList<Elevator> EL;
    private final PipedReader READER;
    private final PrinLog LOG;

    public DispELev(LinkedList<People> peopleList, LinkedList<Elevator> elevatorList, PipedReader reader) {
        PL = peopleList;
        EL = elevatorList;
        READER = reader;
        LOG = new PrinLog("Scheduling Log",500,400,1050,100);
    }

    @Override
    public void run() {
        try {
            while (READER.read() != -1) {
                chooseElev(PL.getLast());
            }
            READER.close();
            for (int i = 0; i < EL.size(); i++) {
            	if (EL.get(i).getStatus() != 0) i -= 1;
            }
            LOG.print("●●●●Path Statistics●●●●\n");
            int totalSteps = 0;
            for (Elevator e : EL) {
            	totalSteps += e.getSteps();
            	LOG.print("Elevator #: " + e.getNO() + ", Total path: " + e.getSteps() + ".\n");
            }
            LOG.print("Total path: " + totalSteps + ", Average path: " + totalSteps/EL.size() + "。\n");
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void chooseElev(People p) {
        int min = 80;
        int dist = 80;
        Elevator elev = null;

        LOG.print("◆◆◆◆Decision making begins◆◆◆◆\n");
        
        for (Elevator e : EL) {
            if (e.getStatus() == 0) {
                dist = Math.abs(e.getNowFloor() - p.getNOWFLOOR());
            } else if (e.getStatus() == 1) {
                if (p.getSTATUS() == 1) {
                    if (p.getNOWFLOOR() >= e.getNowFloor()) {
                        dist = Math.abs(e.getNowFloor() - p.getNOWFLOOR());
                    } else {
                        dist = Math.abs(e.getHighest() - e.getNowFloor()) + Math.abs(e.getHighest() - e.getLowest()) + Math.abs(e.getLowest() - p.getNOWFLOOR());
                    }
                } else if (p.getSTATUS() == -1) {
                    if (p.getNOWFLOOR() >= e.getHighest()) {
                        dist = Math.abs(e.getNowFloor() - p.getNOWFLOOR());
                    } else {
                        dist = Math.abs(e.getHighest() - e.getNowFloor()) + Math.abs(e.getHighest() - p.getNOWFLOOR());
                    }
                }
            } else {
                if (p.getSTATUS() == -1) {
                    if (p.getNOWFLOOR() <= e.getNowFloor()) {
                        dist = Math.abs(e.getNowFloor() - p.getNOWFLOOR());
                    } else {
                        dist = Math.abs(e.getLowest() - e.getNowFloor()) + Math.abs(e.getHighest() - e.getLowest()) + Math.abs(e.getHighest() - p.getNOWFLOOR());
                    }
                } else if (p.getSTATUS() == 1) {
                    if (p.getNOWFLOOR() <= e.getLowest()) {
                        dist = Math.abs(e.getNowFloor() - p.getNOWFLOOR());
                    } else {
                        dist = Math.abs(e.getLowest() - e.getNowFloor()) + Math.abs(e.getLowest() - p.getNOWFLOOR());
                    }
                }
            }
            LOG.print("Elevator #: " + e.getNO() + ", current floor #: " + e.getNowFloor() + ", path: " + dist + ".\n");
            if (dist < min) {
                min = dist;
                elev = e;
            }
        }

        if (p.getSTATUS() == 1) {
            if (!elev.getUPREQ().contains(p.getNOWFLOOR())) {
                elev.getUPREQ().add(p.getNOWFLOOR());
                elev.getUPREQ().sort(Comparator.naturalOrder());
            }
        } else if (p.getSTATUS() == -1) {
            if (!elev.getDOWNREQ().contains(p.getNOWFLOOR())) {
                elev.getDOWNREQ().add(p.getNOWFLOOR());
                elev.getDOWNREQ().sort(Comparator.reverseOrder());
            }
        }

        if (elev.getStatus() == 0){
            if (elev.getNowFloor() < p.getNOWFLOOR()) elev.setStatus(1);
            else if (elev.getNowFloor() > p.getNOWFLOOR()) elev.setStatus(-1);
            else elev.setStatus(p.getSTATUS());
        }

        LOG.print("Client #: " + p.getNO() + ". Elevator #: " + elev.getNO() + ".\n");
        LOG.print("◆◆◆◆Decision-making end◆◆◆◆\n\n");
    }
}
