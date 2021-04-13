import java.util.Random;
import java.util.List;
import java.util.Iterator;

/**
 * The new animal addition to the simulation... Bears!
 * In this simulator, the bears are made to only eat the foxes, not the rabbits.
 * Also, the probability of a bear being created is extremely low, as
 * they could be a serious threat to the foxes if much more were populated.
 * The color that is assigned to the bears is magenta.
 *
 * @author Ben Suarez
 * @version 2021.04.12 v2.0
 */
public class Bear extends Animal
{
    private static final int MAX_LITTER_SIZE = 3;
    private static final Random rand = Randomizer.getRandom();
    private static final int FOX_FOOD_VALUE = 7;
    private static final int MAX_AGE = 200;
    private static final double BREEDING_PROBABILITY = 0.06;
    private static final int BREEDING_AGE = 17;
    private int age;
    private int foodLevel;

    /**
     * Constructor for objects of class Bear
     */
    public Bear(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(FOX_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = FOX_FOOD_VALUE;
        }
    }

    /**
     * This is what the bears mostly do: they hunt for
     * foxes. In the process, they might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newBears A list to return newly born foxes.
     */
    public void act(List<Animal> newBears)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newBears);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Increase the age. This could result in the bear's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Makes this bear more hungry. This could result in the bear's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for foxes adjacent to the current location.
     * Only the first live fox is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this bear is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBears A list to return newly born bears.
     */
    private void giveBirth(List<Animal> newBears)
    {
        // New bears are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Bear young = new Bear(false, field, loc);
            newBears.add(young);
        }
    }
    
    /**
     * Sets the age for bears.
     */
    public void setAge(int age)
    {
        this.age = age;
    }
    
    /**
     * Calls for the age of the bears
     * @return the age value.
     */
    public int getAge()
    {
        return age;
    }
    
    /**
     * Gets the age in which the bears are allowed to breed
     * @return the starting age for bear breeding.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Calls for the maximum age that the bears are able to live up to
     * @return the maximum age number for bears.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Gets the maximum number of bear births allowed at once
     * @return the max litter (birth) size.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Calls for the probability of the bears being able to breed
     * @return the breeding probability for bears.
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
}
