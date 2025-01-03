package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.RandomPointsGenerator;

import java.util.*;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;

public class GrassField extends AbstractWorldMap {

    private final Map<Vector2d, Grass> grassBlocks = new HashMap<>();


    public GrassField(int numberOfGrassFiles){
        super(10,10);
        RandomPointsGenerator randomPointsGenerator = new RandomPointsGenerator(numberOfGrassFiles);
        for (int i = 0; i < numberOfGrassFiles; i++){
            Vector2d randomGrassPosition = randomPointsGenerator.generate();
            grassBlocks.put(randomGrassPosition, new Grass(randomGrassPosition));
        }

    }


    @Override
    public WorldElement objectAt(Vector2d position) {
        WorldElement temp = super.objectAt(position);
        if (temp == null) {
            return this.grassBlocks.get(position);
        }
        return temp;
    }

    @Override
    public Collection<WorldElement> getElements() {
        Collection<WorldElement> elements = super.getElements();
        elements.addAll(grassBlocks.values());
        return elements;
    }

    @Override
    public Boundary getCurrentBounds() {
        Vector2d mapUpperRight = new Vector2d((int) NEGATIVE_INFINITY, (int) NEGATIVE_INFINITY);
        Vector2d mapLowerLeft = new Vector2d((int) POSITIVE_INFINITY, (int) POSITIVE_INFINITY);

        for (Vector2d position : animals.keySet()) {

            mapLowerLeft = mapLowerLeft.lowerLeft(position);
            mapUpperRight = mapUpperRight.upperRight(position);
        }

        for (Vector2d position : grassBlocks.keySet()) {

            mapLowerLeft = mapLowerLeft.lowerLeft(position);
            mapUpperRight = mapUpperRight.upperRight(position);
        }

        if (mapUpperRight.getX() > (int) NEGATIVE_INFINITY) {
            return new Boundary(mapLowerLeft, mapUpperRight);
        }
        else {
            Vector2d middleOfMap = new Vector2d(0, 0);
            return new Boundary(new Vector2d(0, 0), middleOfMap);
        }
    }
}
