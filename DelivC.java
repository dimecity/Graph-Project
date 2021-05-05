import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// Class DelivC does the work for deliverable DelivC of the Prog340
/*Given a set of cities, you want to find the shortest route that visits every city and ends up back at the original starting city.
For the purposes of this problem, every city will be directly reachable from every other city*/

public class DelivC {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	int shortestDistance;
	
	public DelivC( File in, Graph gr ) {
		inputFile = in;
		g = gr;
		
		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring( 0, inputFileName.length()-4 ); // Strip off ".txt"
		String outputFileName = baseFileName.concat( "V.txt" );
		outputFile = new File( outputFileName );
		if ( outputFile.exists() ) {    // For retests
			outputFile.delete();
		}
		
		try {
			output = new PrintWriter(outputFile);			
		}
		catch (Exception x ) { 
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}
		////////////////////////////////////////////////////////////////////Code goes here
		StringBuilder result = new StringBuilder();				
		StringBuilder verbose = new StringBuilder();
		ArrayList<Node> newList = g.getNodeList();				//This is the list of nodes we get from to graph to work with
		ArrayList<String> resultNodes = new ArrayList<>();		//This list will contain the result's nodes that we visited
		ArrayList<Integer> keepTrack = new ArrayList<>();		//This list will contain the result's distance that we visited to make sure we won't print a path twice
		String ss = new String();
		
		
		verbose.append("Verbose results:\n");
		result.append("Summary result:\n");
		
		Tour currTour = CreateTour(newList, keepTrack, resultNodes, verbose);					//Start the first tour	
		
		result.append(currTour);								//Add it to the result
		this.shortestDistance = currTour.getDistance();			//Update the total distance			
		
				
		RandomRestart(result, newList, keepTrack, resultNodes, verbose);				//Start finding other tours
		
		
		for (int j = resultNodes.get(resultNodes.size() - 1).length() - 2; j >= 0; j--) 	//This part is to strip of the distance from the result 
			if (!Character.isDigit(resultNodes.get(resultNodes.size() - 1).charAt(j))) {	//and only eturn the name
				ss = resultNodes.get(resultNodes.size() - 1).substring(0, j);				//Ex: "CHKMPC 3516" will return "CHKMPC"
				break;						
		}					

		System.out.println(result);
		System.out.printf("Shortest path found was %s with distance %d at %dth step out of %d steps.", ss, shortestDistance, keepTrack.indexOf(shortestDistance) + 1, keepTrack.size());
        output.println(verbose);
        output.printf("Shortest path found was %s with distance %d at %dth step out of %d steps.", ss, shortestDistance, keepTrack.indexOf(shortestDistance) + 1, keepTrack.size());
        output.flush();
	}	
	
	
	public class Tour { 						//Tour class with total distance and String of cities + distance we visit
		private String cities;
		private int distance;
		 
		public Tour(String cities, int distance) {
			this.cities = cities;
			this.distance = distance;
		 }
		 
		public int getDistance() {
			return distance;
		}
		 
		public String getCities() {
			return cities;
		}
		@Override
		public String toString() {
			return this.cities;
		}
	}
	
	public Tour CreateTour(ArrayList<Node> myTour, ArrayList<Integer> keepTrack, ArrayList<String> finalNodes, StringBuilder verbose) {	//This function is to build a path by adding the node to the result string, and adding the distance together
		StringBuilder currPath = new StringBuilder();
		int totalDist = 0;
		
		for (int i = 0; i < myTour.size() - 1; i++) {		//This part of codes appends every cities we visit until last-1 element but it only appends the tail
			Node tail = myTour.get(i);						//ex: Minneapolis to Kansas it only appends Minneapolis, not Kansas
			Node head = myTour.get(i + 1);
			currPath.append(tail.getAbbrev());		
			
			Edge currEdge = FindEdge(head, tail);			//Find the edge between 2 specific nodes and add it to total distance			
			totalDist += currEdge.getDist();				
		}		
		
		Node lastTail = myTour.get(myTour.size() - 1);	//appends the head of the last iteration (which is the tail now) because in the last iteration we only appended the tail, not the head
        Node lastHead = myTour.get(0);					//appends the first node we started (which means we completed the cycle)        
        currPath.append(lastTail.getAbbrev());
        currPath.append(lastHead.getAbbrev());        
        
        Edge currEdge = FindEdge(lastHead, lastTail); 	//Find the edge between 2 specific nodes and add it to total distance
        totalDist += currEdge.getDist();    	
        
        String result = String.format("%s %d\n", currPath, totalDist);        
       
        verbose.append(result);
        keepTrack.add(totalDist);   
              
        
        return new Tour(result, totalDist);	
	}
	
	
	public void RandomRestart(StringBuilder result, ArrayList<Node> myTour, ArrayList<Integer> keepTrack, ArrayList<String> finalNodes, StringBuilder verbose) {	//This function is to find better tour
		long factorial = 1;										//This part will calculate how many paths can we take, which is factorial times of the list node - 1
		int num = myTour.size() - 1;							// -1 because we don't need to calculate the starting node 
        
		for(int i = 1; i <= num; ++i)        
            factorial *= i;
		
		long attemps = (myTour.size() > 11) ? 5000000 : factorial;	//if input size is too big then takes 5 mil instead
		
		ArrayList<Node> tempTour = myTour;		
		
		while (attemps > 0) {
			ArrayList<Node> randomTour = RandomNodes(tempTour);	//This line and the next line are meant to keep randomizing and updating the nodes everytime
			tempTour = randomTour;
			
			Tour newTour = CreateTour(randomTour, keepTrack, finalNodes, verbose); //Find a new tour from the nodes that were just randomized
			
			if (newTour.getDistance() < shortestDistance) {	//If new your is better the update the distance and add it to the result 
				result.append(newTour);
				shortestDistance = newTour.getDistance();	
				finalNodes.add(newTour.getCities());						
			} 
				attemps--;			
		}
		
	}
	
	public ArrayList<Node> RandomNodes (ArrayList<Node> myTour) {
		ArrayList<Node> copiedNodes = myTour;	//This is the nodes that we'll modify	
		ArrayList<Node> newNodes = new ArrayList<>();	//This is the nodes that we'll return as a randomized list
		
		newNodes.add(copiedNodes.remove(0)); //Take the starting city before randomize the list because we only randomize every city except that starting one
		
		while (!copiedNodes.isEmpty()) {	//Each time we iterate, we remove a random node, add that random node to newNodes, and decrease the size of copiedNodes by remove()
            newNodes.add(copiedNodes.remove(new Random().nextInt(copiedNodes.size())));	
        }
        return newNodes;	//Return a new list of nodes that is completely randomized
	}
	
	public Edge FindEdge(Node head, Node tail){
        Edge foundEdge = null;
        for (int i = 0; i < g.edgeList.size() ; i++) {		//Traverse through every edge in the graph, and check if its head and tail match with what we want to find
            Edge currentEdge = g.edgeList.get(i);
            if (currentEdge.getHead().getName().equals(head.getName()) && currentEdge.getTail().getName().equals(tail.getName())) {                
                    foundEdge = currentEdge;
                    break;
                }
            }        
        return foundEdge;
    }	
} 



