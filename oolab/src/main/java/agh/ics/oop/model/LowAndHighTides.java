package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPointsGenerator;
import static agh.ics.oop.model.MapDirection.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class LowAndHighTides extends AbstractWorldMap{

    private final HashMap<Vector2d, Water> lowTideWaterBlocks = new HashMap<>();
    private final HashMap<Vector2d, Water> highTideWaterBlocks = new HashMap<>();
    private boolean isHighTide = false;

    public LowAndHighTides(int width, int height) {
        super(width, height);
        Random random = new Random();
        int numOfWaterSources = (width*height)/14;
        RandomPointsGenerator randomPointsGenerator = new RandomPointsGenerator(width, height);
        for (int i = 0; i < numOfWaterSources; i++) {
            Vector2d waterSourcePosition = randomPointsGenerator.generate();
            lowTideWaterBlocks.put(waterSourcePosition, new Water(waterSourcePosition));
            highTideWaterBlocks.computeIfAbsent(waterSourcePosition, Water::new);
            for (int j = 0; j < 4; j++){
                if (random.nextInt(4) != 3){
                    Vector2d surroundingWaterPosition = waterSourcePosition
                            .add(MapDirection.values()[(NORTH.ordinal() + 2*j)%8].toUnitVector());
                    if (surroundingWaterPosition.precedes(getCurrentBounds().upperRight())
                            && surroundingWaterPosition.follows(getCurrentBounds().lowerLeft())) {
                        highTideWaterBlocks.computeIfAbsent(surroundingWaterPosition, Water::new);
                    }
                }
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
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || isWaterPresent(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (super.objectAt(position) == null && isWaterPresent(position)){
            return highTideWaterBlocks.get(position);
        }
        return super.objectAt(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && !isWaterPresent(position);
    }

    @Override
    public CopyOnWriteArrayList<WorldElement> getElements() {
        CopyOnWriteArrayList<WorldElement> elements = super.getElements();
        if (isHighTide){
            elements.addAll(highTideWaterBlocks.values());
        } else {
            elements.addAll(lowTideWaterBlocks.values());
        }
        return elements;
    }
}
