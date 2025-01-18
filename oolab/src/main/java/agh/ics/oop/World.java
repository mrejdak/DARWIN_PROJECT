package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.List;


public class World {

    public static void main(String[] input) {

            Earth earth = new Earth(10,11);
            AbstractWorldMap waterMap = new LowAndHighTides(10, 10, 2);
            List<Vector2d> position = List.of(new Vector2d(0,0));

            earth.addObserver(new ConsoleMapDisplay());
            waterMap.addObserver(new ConsoleMapDisplay());

            Simulation simulation = new Simulation(position, earth, 1, 2, 8, 10, 15, 8, 4, 6);
            Simulation simulation2 = new Simulation(position, waterMap, 1, 2, 8, 10, 15, 8, 4, 6);
//            simulation.run();
            simulation2.run();

//        try {
//            List<Simulation> simulationList = new ArrayList<>();
//
//            List<MoveDirection> directions = OptionsParser.parseOptions(input);
//            List<Vector2d> positions = List.of(new Vector2d(3, 4), new Vector2d(2,2));
//[2, 5, 2, 2, 7, 0, 3, 2]

//            for (int i = 0; i < 10; i++) {
//                GrassField grassMap = new GrassField(10);
//                RectangularMap rectMap = new RectangularMap(5, 5);
//
//                grassMap.addObserver(new ConsoleMapDisplay());
//                rectMap.addObserver(new ConsoleMapDisplay());
//
//                Simulation simulation1 = new Simulation(positions, directions, grassMap);
//                Simulation simulation2 = new Simulation(positions, directions, rectMap);
//                simulationList.add(simulation1);
//                simulationList.add(simulation2);
//            }

//            SimulationEngine simulationEngine = new SimulationEngine(simulationList);
//
//            simulationEngine.runAsyncInThreadPool();
//            simulationEngine.awaitSimulationsEnd();
//
//        }
//        catch (IllegalArgumentException e) {
//            e.printStackTrace(System.err);
//        }
//        catch (InterruptedException e2) {
//            System.out.printf("Interrupted: %s%n", e2.getMessage());
//        }

    }
}
