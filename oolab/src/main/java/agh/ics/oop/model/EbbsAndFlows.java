package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPointsGenerator;
import static agh.ics.oop.model.MapDirection.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EbbsAndFlows extends AbstractWorldMap{

    private final HashMap<Vector2d, Water> ebbsWaterBlocks = new HashMap<>();
    private final HashMap<Vector2d, Water> flowsWaterBlocks = new HashMap<>();
    // TODO add ebbs and flows

    public EbbsAndFlows(int width, int height, int numOfWaterSources) {
        super(width, height);
        RandomPointsGenerator randomPointsGenerator = new RandomPointsGenerator(numOfWaterSources);
        for (int i = 0; i < numOfWaterSources; i++) {
            Vector2d waterSourcePosition = randomPointsGenerator.generate();
            ebbsWaterBlocks.put(waterSourcePosition, new Water(waterSourcePosition));
            for (int j = 0; j < 4; j++){
                Vector2d surroundingWaterPosition = waterSourcePosition
                        .add(MapDirection.values()[NORTH.ordinal() + 2*i].toUnitVector());
                flowsWaterBlocks.computeIfAbsent(surroundingWaterPosition, Water::new);
            }
        }
    }



}
