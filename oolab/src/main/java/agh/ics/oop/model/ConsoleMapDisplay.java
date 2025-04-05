package agh.ics.oop.model;
//TODO: remove
public class ConsoleMapDisplay implements MapChangeListener{
    private int counter = 0;
    @Override
    public synchronized void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {
            System.out.printf("Map ID: %s%n", worldMap.getID());
            System.out.println(message);
            System.out.println(worldMap);
            System.out.printf("Update number: %d%n%n", ++counter);
        }
    }
}
