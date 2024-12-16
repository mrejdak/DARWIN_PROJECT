package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {

    private final List<Simulation> simulationList;
    private final List<Thread> threadList = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SimulationEngine(List<Simulation> simulationList){
        this.simulationList = simulationList;
    }

    public void runSync(){
        for (Simulation simulation : simulationList) {
            simulation.run();
        }
    }

    public void runAsync(){
        for (Simulation simulation : simulationList) {
            Thread thread = new Thread(simulation);
            thread.start();
        }
    }

    public void runAsyncInThreadPool() {
        for (Simulation simulation : simulationList) {
            Thread thread = new Thread(simulation);
            threadList.add(thread);
        }
        for (Thread thread: threadList){
            executorService.submit(thread);
        }
    }

    public void awaitSimulationsEnd() throws InterruptedException {
        for (Thread thread: threadList){
            thread.join();
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }
}
