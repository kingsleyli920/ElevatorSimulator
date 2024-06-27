package ACTIONS;

import ITEMS.People;
import TOOLS.PrinLog;

import java.io.IOException;
import java.io.PipedWriter;
import java.util.LinkedList;

public class SimuPeop implements Runnable{
	
	private final Integer PEOPLE;
	private final Integer MININTERVAL;
	private final Integer MAXINTERVAL;
    private final Integer FLOORS;
    private final LinkedList<People> PL;
    private final PipedWriter WRITER;
    private final PrinLog LOG;

    public SimuPeop(Integer People, Integer MinInterval, Integer MaxInterval, Integer floors, PipedWriter writer) {
    	PEOPLE = People;
    	MININTERVAL = MinInterval;
    	MAXINTERVAL = MaxInterval;
    	FLOORS = floors;
        PL = new LinkedList<>();
        WRITER = writer;
        LOG = new PrinLog("Passenger Log",600, 400, 100, 100);
    }

    @Override
    public void run() {
        try {
            LOG.print("At least " + MININTERVAL + "s & at most " + MAXINTERVAL + "s for a passenger to arrive, total # of passenger: " + PEOPLE + ".\n\n");
            for (int i = 0; i < PEOPLE; i++) {
                Thread.sleep((long)(Math.random() * (MAXINTERVAL * 1000 - 1000) + MININTERVAL * 1000));
                People p = new People(i, FLOORS);
                PL.addLast(p);
                if (p.getSTATUS() == 1)  LOG.print("Passenger #: " + i + ", current at floor: " + p.getNOWFLOOR() + ", going up.\n");
                else LOG.print("Passenger #: " + i + ", current at floor: " + p.getNOWFLOOR() + ", going down.\n");
                WRITER.write(1);
            }
            WRITER.close();
        } catch (InterruptedException | IOException e) {
            e.getMessage();
        }
    }
    
    public LinkedList<People> getPL() { return PL; }
}
