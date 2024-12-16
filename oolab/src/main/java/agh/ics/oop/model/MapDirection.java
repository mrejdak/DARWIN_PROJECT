package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    private static final Vector2d[] vectorList = {
            new Vector2d(0, 1), new Vector2d(1, 0),
            new Vector2d(0, -1), new Vector2d(-1, 0)
    };

    @Override
    public String toString() {
        return switch(this) {
            case NORTH -> "North";
            case EAST -> "East";
            case SOUTH -> "South";
            case WEST -> "West";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> vectorList[0];
            case EAST -> vectorList[1];
            case SOUTH -> vectorList[2];
            case WEST -> vectorList[3];
        };
    }
}
