import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * Name: Janine Bariuan
 * EID: jrb6727
 * Project 3: Dynamic Programming
 * Spring 2019, Dr. Julien
 */


public class Program3 {

    DamageCalculator calculator;
    PlanetPathScenario planetScenario;

    public Program3() {
        this.calculator = null;
        this.planetScenario = null;
        
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 2.
     */
    public void initialize(PlanetPathScenario ps) {
        this.planetScenario = ps;
    }

    /*
     * This method is used in lieu of a required constructor signature to initialize
     * your Program3. After calling a default (no-parameter) constructor, we
     * will use this method to initialize your Program3 for Part 1.
     */
    public void initialize(DamageCalculator dc) {
        this.calculator = dc;
    }
   
    /*
     * Edge class
     */
    protected class edgeFlight {
    	
    	int endPlanet;
    	int optValue;
    	
    	public edgeFlight(int end, int value) {
    		this.endPlanet = end;
    		this.optValue = value;
    		
    	}
    	
    }

    /*
     * This method returns an integer that is the minimum amount of time necessary to travel
     * from the start planet to the end planet in the PlanetPathScenario given the total
     * amout of fuel that Thanos has. If a path is not possible given the amount of fuel, return -1.
     */
     //TODO: Complete this method
     public int computeMinimumTime() {
        int start = planetScenario.getStartPlanet(); //start planet
        int end = planetScenario.getEndPlanet();	// end planet
        int totalFuel = planetScenario.getTotalFuel();
        
        int totalPlanets = planetScenario.getNumPlanets();
        
        SpaceFlight[][] allFlights = planetScenario.getAllFlights();
       
        
        
        //initialize 2D array of flight times, with planet 3 having no thing 
        int[][] flightTimes = new int[totalPlanets][totalFuel+1];
        flightTimes[end][0] = Integer.MAX_VALUE;
        ArrayList<Integer> neighbors = new ArrayList<Integer>();
        
        //initializes zero fuel
        for(int i = 0; i < totalPlanets; i++) {
        	
        	if(i == end) {
        		flightTimes[end][0] = 0;
        	}
        	else {
        		flightTimes[i][0] = Integer.MAX_VALUE;
        	}
        
        }  
        
        for(int i = 0; i < totalFuel + 1; i++) {
        	flightTimes[end][i] = 0;
        }
        
        //at every planet, only care about source destination
        //make array 
        for(int fuel = 1; fuel < totalFuel+1; fuel++){
        	for(int planet = 0; planet < totalPlanets; planet++) {
    		       		   
    		   //look at every neighbor and see if enough fuel
    		   for(int neighbor = 0; neighbor < allFlights[planet].length; neighbor++) {
    			   
    			   
    			   SpaceFlight currentFlight = allFlights[planet][neighbor];
    			   int destinPlanet = currentFlight.getDestination();
    			   int flightFuel = currentFlight.getFuel();
    			       			   
    			   int totalFlightTime = 0; 
    			   
    			   if(planet != end) {
    			   
	    			   //if no fuel available, path not possible
	    			   if(fuel < flightFuel) {
	    				   neighbors.add(Integer.MAX_VALUE);
	    			   }
	    			   //if fuel left, add total times
	    			   else {
	    				   
	    				  //if previous time was also infinity, keep as infinity
	    				  if((fuel-flightFuel < 0) || (flightTimes[destinPlanet][fuel-flightFuel] == Integer.MAX_VALUE) ) {
	       				   	neighbors.add(Integer.MAX_VALUE);
	    				  }
	    				  //else add the opt flight times if able to reach end planet
	    				  else {
	    					  totalFlightTime = currentFlight.getTime() + flightTimes[destinPlanet][fuel-flightFuel];
	    					  neighbors.add(totalFlightTime);
	    				  }
	    				  
	    			   }
    			   }
    			   else {
    				   neighbors.add(0);
    			   }
    			   
    			   
    		   }
    		   
    		   //gets the minimum for that opt spot
    		   int min = neighbors.get(0);
    		   for(int i = 1; i < neighbors.size(); i++) {
    			   
    			   int current = neighbors.get(i);
    			   if(current < min) {
    				   min = current;
    			   }
    		   }
    		   
    		   flightTimes[planet][fuel] = min;
    		   neighbors.clear();
    		   
    	   }
       }
        
        return flightTimes[start][totalFuel];
     }
     
     
     
    /*
     * This method returns an integer that is the maximum possible damage that can be dealt
     * given a certain amount of time.
     */
    //TODO: Complete this function
    public int computeDamage() {

        int totalTime = calculator.getTotalTime();
        int numAttacks = calculator.getNumAttacks();
        
        int[][] maxDamages = new int[numAttacks][totalTime+1];
        ArrayList<Integer> currentDamages = new ArrayList<Integer>();
        
        //initialize no damages at time 0
        for(int i = 0; i < numAttacks; i++) {
        	maxDamages[i][0] = 0;
        }
        
        for(int i = 0; i < totalTime+1; i++) {
        	maxDamages[0][i] = 0;
        }
        
        //find maxes
        for(int attack = 0; attack < numAttacks; attack++) {
        	for(int time = 1; time < totalTime +1 ; time++) {
        		
        		
        		//calculates the damages 
        		for(int timeIter = 0; timeIter <= time ; timeIter++) {
        			int damageCalc = calculator.calculateDamage(attack, timeIter);
        			
        			if(attack > 0) {
        			int prevMax = maxDamages[attack-1][time-timeIter];
        			int damage =  damageCalc + prevMax;
        			currentDamages.add(damage);
        			}
        			else {
        				currentDamages.add(damageCalc);
        			}
        		}
        		
        		//get the max damage out of all of them
        		int max = currentDamages.get(0);
        		for(int i = 1; i < currentDamages.size(); i++) {
        			if(currentDamages.get(i) > max) {
        				max = currentDamages.get(i);
        			}
        		}
        		
        		//clear the array
        		maxDamages[attack][time] = max;
        		currentDamages.clear();
        		
        		
        	}
        }
        
        return maxDamages[numAttacks-1][totalTime];
    }

}


