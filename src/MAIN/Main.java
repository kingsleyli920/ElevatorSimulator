package MAIN;

import ACTIONS.*;
import ITEMS.InitItem;
import TOOLS.InitFrame;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class Main {
    public static void main(String[] args) throws IOException {

        InitFrame initFrame = InitFrame.getINSTANCE();
        initFrame.initFrame();
        while (!initFrame.isConfirmed());
        InitItem initItem = initFrame.getInitItem();

        PipedWriter w = new PipedWriter();
        SimuPeop sp = new SimuPeop(initItem.getPeople(), initItem.getMinInteral(), initItem.getMaxInterval(), initItem.getFloors(), w);
        new Thread(sp).start();

        RunElev re = new RunElev(initItem.getElevators(), initItem.getFloors(), initItem.getRunInterval(), sp.getPL());
        new Thread(re).start();

        PipedReader r = new PipedReader();
        w.connect(r);
        DispELev de = new DispELev(sp.getPL(), re.getEL(), r);
        new Thread(de).start();
    }
}