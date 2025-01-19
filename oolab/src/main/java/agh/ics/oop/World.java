package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.SimulationParameters;

import java.util.ArrayList;
import java.util.List;


public class World {

    public static void main(String[] input) {

            SimulationParameters simulationParameters = new SimulationParameters(
                    10,
                    10,
                    "Earth",
                    4,
                    2,
                    2,
                    3,
                    10,
                    5,
                    3,
                    1,
                    3,
                    "Standard",
                    7);

            Earth earth = new Earth(simulationParameters.mapWidth(),simulationParameters.mapHeight());
            AbstractWorldMap waterMap = new LowAndHighTides(simulationParameters.mapWidth(),simulationParameters.mapHeight());

            earth.addObserver(new ConsoleMapDisplay());
            waterMap.addObserver(new ConsoleMapDisplay());

            Simulation simulation = new Simulation(earth, simulationParameters);
            Simulation simulation2 = new Simulation(waterMap, simulationParameters);
            simulation.run();
//            simulation2.run();

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
