package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPointsGenerator;
import static agh.ics.oop.model.MapDirection.*;

import java.util.HashMap;

public class LowAndHighTides extends AbstractWorldMap{

    private final HashMap<Vector2d, Water> lowTideWaterBlocks = new HashMap<>();
    private final HashMap<Vector2d, Water> highTideWaterBlocks = new HashMap<>();
    private boolean isHighTide = false;

    public LowAndHighTides(int width, int height, int numOfWaterSources) {
        super(width, height);
        RandomPointsGenerator randomPointsGenerator = new RandomPointsGenerator(numOfWaterSources);
        for (int i = 0; i < numOfWaterSources; i++) {
            Vector2d waterSourcePosition = randomPointsGenerator.generate();
            lowTideWaterBlocks.put(waterSourcePosition, new Water(waterSourcePosition));
            highTideWaterBlocks.computeIfAbsent(waterSourcePosition, Water::new);
            for (int j = 0; j < 4; j++){
                Vector2d surroundingWaterPosition = waterSourcePosition
                        .add(MapDirection.values()[NORTH.ordinal() + 2*i].toUnitVector());
                highTideWaterBlocks.computeIfAbsent(surroundingWaterPosition, Water::new);
            }
        }
    }

    @Override
    public void changeTide(){
        isHighTide = !isHighTide;
    }

    @Override
    public boolean isWaterPresent(Vector2d position) {
        if (isHighTide){
            return highTideWaterBlocks.get(position) != null;
        }
        else {
            return lowTideWaterBlocks.get(position) != null;
        }
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        if (isHighTide){
            return super.canMoveTo(position) && highTideWaterBlocks.get(position) == null;
        }
        return super.canMoveTo(position) && lowTideWaterBlocks.get(position) == null;
    }
}
