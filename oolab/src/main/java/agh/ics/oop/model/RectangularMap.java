package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

public class RectangularMap extends AbstractWorldMap {

    private final Vector2d mapLowerLeft = new Vector2d(0, 0);
    private final Vector2d mapUpperRight;
    private final Boundary bounds;

    public RectangularMap(int width, int height) {
        mapUpperRight = new Vector2d(width-1, height-1);
        bounds = new Boundary(mapLowerLeft, this.mapUpperRight);
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && position.follows(mapLowerLeft) && position.precedes(mapUpperRight);
    }


    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(bounds.lowerLeft(), bounds.upperRight());
    }
}
