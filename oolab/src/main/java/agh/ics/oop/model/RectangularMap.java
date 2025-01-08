package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

public class RectangularMap extends AbstractWorldMap {

    public RectangularMap(int width, int height) {
        super(width, height);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && position.follows(bounds.lowerLeft()) && position.precedes(bounds.upperRight());
    }
}
