package agh.ics.oop.model;

import java.util.Arrays;
import java.util.Random;

public class Genes {
    private final Random random = new Random();
    private final int[] genes;

    //Constructor for initial animals
    public Genes(int numberOfGenes){
        genes = new int[numberOfGenes];
        pickRandomGenes(numberOfGenes);
    }

    //Constructor for children
    public Genes(int numberOfGenes, Animal strongerParent, Animal weakerParent, String simulationVariant, int amountOfMutations){
        genes = new int[numberOfGenes];
        calculateGenes(strongerParent, weakerParent, numberOfGenes);
        mutation(simulationVariant, amountOfMutations); //amount of mutations is hardcoded for now TODO: add variable to constructor
    }

    private void calculateGenes(Animal strongerParent, Animal weakerParent, int numberOfGenes){
        //Initializing function values
        int[] strongerParentsSequence = strongerParent.getGenes().getGenesSequence();
        int[] weakerParentsSequence = weakerParent.getGenes().getGenesSequence();

        int strongerParentsEnergy = strongerParent.getEnergyLevel();
        int weakerParentsEnergy = weakerParent.getEnergyLevel();

        int side = random.nextInt(0,2); // 0 - left side, 1 - right side

        //Calculating index at which parents' arrays are split and
        int[] leftSide;
        int[] rightSide;

        int splitIndex = findSplitIndex(strongerParentsEnergy, weakerParentsEnergy, numberOfGenes, side);

        if(side == 0){
            leftSide = Arrays.copyOfRange(strongerParentsSequence, 0, splitIndex);
            rightSide = Arrays.copyOfRange(weakerParentsSequence, splitIndex, numberOfGenes);

        }else{
            leftSide = Arrays.copyOfRange(weakerParentsSequence, 0, splitIndex);
            rightSide  = Arrays.copyOfRange(strongerParentsSequence, splitIndex, numberOfGenes);
        }

        //filling main array
        int index = 0;

        for (int gene : leftSide) {
            genes[index] = gene;
            index++;
        }

        for (int gene: rightSide){
            genes[index] = gene;
            index++;
        }

    }

    private int findSplitIndex(int higherEnergy, int lowerEnergy, int numberOfGenes, int side){

        double numerator = side == 0 ? higherEnergy : lowerEnergy;
        int denominator = higherEnergy + lowerEnergy;

        return side == 0 ?
                (int) Math.ceil((numerator/denominator) * numberOfGenes) :
                (int) Math.floor((numerator/denominator) * numberOfGenes);
    }

    private void mutation(String simulationVariant, int amountOfMutations){
        for(int i = 0; i < amountOfMutations; i++){
            if(simulationVariant.equals("Standard")){ //Basic variant
                oneGeneMutation();
            }else{                      //Additional variant "[2] podmianka"
                double mutationChoice = random.nextDouble();
                if(mutationChoice > 0.5){
                    oneGeneMutation();
                }
                else{
                    twoGenesSwap();
                }
            }
        }
    }

    private void oneGeneMutation(){
        int randomIndex = random.nextInt(0, genes.length);
        int randomPossibleIndex = random.nextInt(0, 7);

        //Making sure that we don't swap the randomly chosen gene with itself
        int[] possibleGenes = new int[7];
        int index = 0;

        for(int i = 0; i < 8; i++){
            if(i != genes[randomIndex]) {
                possibleGenes[index] = i;
                index++;
            }
        }

        genes[randomIndex] = possibleGenes[randomPossibleIndex];
    }

    private void twoGenesSwap(){
        int firstRandomIndex = random.nextInt(0, genes.length);

        //Making sure first and second randomly chosen indexes are not the same
        int[] possibleIndexes = new int[genes.length - 1];

        int index = 0;
        for(int i = 0; i < genes.length; i++){
            if(i != firstRandomIndex) {
                possibleIndexes[index] = i;
                index++;
            }
        }

        int secondRandomIndex = possibleIndexes[random.nextInt(0, genes.length - 1)];

        //Swapping the genes
        int temp = genes[firstRandomIndex];
        genes[firstRandomIndex] = genes[secondRandomIndex];
        genes[secondRandomIndex] = temp;
    }

    private void pickRandomGenes(int numberOfGenes){
        for(int i = 0; i < numberOfGenes; i++){
            genes[i] = random.nextInt(0,8);
        }
    }

    public int[] getGenesSequence(){
        return Arrays.copyOf(genes, genes.length);
    }

    public int getGeneAtIndex(int index){
        return genes[index];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genes genes1 = (Genes) o;
        return Arrays.equals(genes, genes1.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }
}
