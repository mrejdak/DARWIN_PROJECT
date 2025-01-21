package agh.ics.oop.model;


public class Earth extends AbstractWorldMap{

    public Earth(int width, int height){
        super(width, height);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.followsVertically(bounds.lowerLeft()) && position.precedesVertically(bounds.upperRight());
    }
}
