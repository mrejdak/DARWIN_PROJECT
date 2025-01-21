package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class RandomPointsGenerator implements Iterable<Vector2d>{

    private final int width;
    private final int height;
    private final int[][] allPoints;
    private final Iterator<Vector2d> generator;


    public RandomPointsGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        this.allPoints = generateAllPoints();
        this.generator = iterator();
    }

    private int[][] generateAllPoints() {
        int[][] allPoints = new int[this.width*this.height][2];
        for (int i = 0; i < this.width*this.height; i++) {
            allPoints[i][0] = i % this.width;
            allPoints[i][1] = i / this.width;
        }
        System.out.println(Arrays.deepToString(allPoints));
        return allPoints;
    }

    public Vector2d generate(){
        return generator.hasNext() ? generator.next() : null;
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return new PointsIterator(allPoints, this.width*this.height-1);
    }
}


class PointsIterator implements Iterator<Vector2d>{

    private int index;
    private final int[][] allPoints;
    Random random = new Random();

    public PointsIterator(int[][] allPoints, int index){
        this.index = index;
        this.allPoints = allPoints;
    }

    @Override
    public boolean hasNext() {
        return (index > 0);
    }

    @Override
    public Vector2d next() {
        int randomIndex = random.nextInt(0, index);
        Vector2d randomPoint = new Vector2d(allPoints[randomIndex][0], allPoints[randomIndex][1]);
        int tempX = allPoints[randomIndex][0];
        int tempY = allPoints[randomIndex][1];
        allPoints[randomIndex][0] = allPoints[index][0];
        allPoints[randomIndex][1] = allPoints[index][1];
        allPoints[index][0] = tempX;
        allPoints[index][1] = tempY;
        index -= 1;
        return randomPoint;
    }
}