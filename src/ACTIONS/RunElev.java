package ACTIONS;

import ITEMS.Elevator;
import ITEMS.People;
import TOOLS.PrinLog;

import java.util.Comparator;
import java.util.LinkedList;

public class RunElev implements Runnable{
	private final Integer RUNINTERVAL;
    private final LinkedList<Elevator> EL;
    private final LinkedList<People> PL;
    private final PrinLog LOG;
    
    public RunElev(Integer numOfElves, Integer floors, Integer runInterval, LinkedList<People> peopleList){
        EL = new LinkedList<>();
        for(int i = 0; i < numOfElves; i++) {
            this.EL.add(new Elevator(i, floors));
        }

        RUNINTERVAL = runInterval;
        PL = peopleList;
        
        LOG = new PrinLog("Run Log",800, 400, 500, 100);
        LOG.print("Total Elevator #: " + numOfElves + ", Total level #: " + floors + ", Elevator running time per floor(seconds): " + RUNINTERVAL + ".\n\n");
    }

    @Override
    public void run() {
        try {
            while (true) {
                moveElev();
                Thread.sleep(RUNINTERVAL * 1000);
            }
        } catch (InterruptedException e) {
            e.getMessage();
        }
    }

    private void moveElev() {
        for (Elevator e : EL) {
            if (e.getStatus() == 1) {
                if (e.getUPREQ().contains(e.getNowFloor())) {
                    LOG.print("▲ Elevator #：" + e.getNO() + ", stopped at: " + e.getNowFloor() + ".\n");
                    servePeople(e);
                    e.getUPREQ().remove(e.getNowFloor());
                    LOG.print("▲ Elevator #: " + e.getNO() + ", going up: " + e.getUPREQ() + ", going down" + e.getDOWNREQ() + "。\n\n");
                }
                if (e.getNowFloor() < e.getHighest()) {
                	e.incNowFloor();
                }
                else if (!e.getDOWNREQ().isEmpty()) {
                	e.setStatus(-1);
                }
                else {
                    e.setStatus(0);
                    LOG.print("■ Elevator #: " + e.getNO() + ", idled at: " + e.getNowFloor() + ".\n\n");
                }
            } else if (e.getStatus() == -1) {
                if (e.getDOWNREQ().contains(e.getNowFloor())) {
                    LOG.print("▼ Elevator #: " + e.getNO() + ", stopped at: " + e.getNowFloor() + ".\n");
                    servePeople(e);
                    e.getDOWNREQ().remove(e.getNowFloor());
                    LOG.print("▼ Elevator #: " + e.getNO() + ", going up: " + e.getUPREQ() + ", going down: " + e.getDOWNREQ() + ".\n\n");
                }
                if (e.getNowFloor() > e.getLowest()) {
                	e.decNowFloor();
                }
                else if (!e.getUPREQ().isEmpty()) {
                	e.setStatus(1);
                }
                else {
                    e.setStatus(0);
                    LOG.print("■ Elevator #: " + e.getNO() + ", idled at: " + e.getNowFloor() + ".\n\n");
                }
            }
        }
    }
    public LinkedList<Elevator> getEL() { return EL; }
    private void servePeople(Elevator e) {
        for (People p : PL) {
            if (!p.isServed()) {
                if (p.isWaiting() && p.getNOWFLOOR().equals(e.getNowFloor()) && p.getSTATUS().equals(e.getStatus())) {
                    p.setWaiting(false);
                    p.setElevNo(e.getNO());
                    if (p.getSTATUS() == 1) {
                        if (!e.getUPREQ().contains(p.getAIMFLOOR())) {
                            e.getUPREQ().add(p.getAIMFLOOR());
                            e.getUPREQ().sort(Comparator.naturalOrder());
                        }
                    } else {
                        if (!e.getDOWNREQ().contains(p.getAIMFLOOR())) {
                            e.getDOWNREQ().add(p.getAIMFLOOR());
                            e.getDOWNREQ().sort(Comparator.reverseOrder());
                        }
                    }
                    LOG.print("→ Passenger #: " + p.getNO() + ", entered elevator: " + e.getNO() + ", moving to floor: " + p.getAIMFLOOR() + ".\n");
                }
                if (p.getAIMFLOOR().equals(e.getNowFloor()) && !p.isWaiting() && p.getElevNo().equals(e.getNO())) {
                    p.setServed(true);
                    LOG.print("← Passenger #: " + p.getNO() + ", exited from elevator: " + e.getNO() + ".\n");
                }
            }
        }
    }
}
