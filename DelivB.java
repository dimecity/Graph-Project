import java.io.*;
import java.util.*;

// Class DelivB does the work for deliverable DelivB of the Prog340

public class DelivB {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	private int counter = 1;
	
	public DelivB( File in, Graph gr ) {
		inputFile = in;
		g = gr;
		
		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring( 0, inputFileName.length()-4 ); // Strip off ".txt"
		String outputFileName = baseFileName.concat( "_out.txt" );
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
		
		
		//Code goes here
		//
		//		
		//
	    Node startNode = new Node(null); 	   				//Find the starting node 
	    
	    for (int i = 0; i < g.getNodeList().size(); i++) {
	    	if (g.getNodeList().get(i).getVal().equalsIgnoreCase("S")) {		    	//startNode starts with letter s 	
	    		startNode =  g.getNodeList().get(i);
	    	}
	    }
	    
	    System.out.println("Node\t\tStart Time\tEnd Time");
		output.println("Node\t\tStart Time\tEnd Time");	
		DepthFirstSearch(startNode);		//Call DFS		
		System.out.println(printNode()); 	//Print result
		output.println(printNode());		
		
		System.out.println("Edge\t\tType");
		output.println("Edge\t\tType");
		FindTreeType(g); 					//Call FindTreeType
		System.out.println(printTree());	//Print result
		output.println(printTree());
		
		output.flush();		
	}
	
	public void DepthFirstSearch(Node startNode) {			//DFS function using recursive
		startNode.setVisited(true); 						//Mark the node as visited
		startNode.setTimeStarted(counter);					//Set start
		counter++;											//Increment counter every time DFS is called 
		for (int i = 0; i < startNode.getOutgoingEdges().size(); i++) {		//Traverse throughout the node's neighbor
			Node nextNode = startNode.getOutgoingEdges().get(i).getHead();
			if ( !nextNode.isVisited() ) {					//If node is not visited then call DFS on it
				startNode.getOutgoingEdges().get(i).setEdgeClass("T");
				DepthFirstSearch(nextNode);
			}
		}		
		startNode.setTimeEnded(counter);				//When there is no neighbor around node, set ended time
        counter++;										//Increment counter for the next DFS call
	}
	
	public void FindTreeType(Graph gr) {
		for (int i = 0; i < gr.getEdgeList().size(); i++) {							// Find edge type by comparing time started and ended 
	         Node head = gr.getEdgeList().get(i).getHead();							
	         Node tail = gr.getEdgeList().get(i).getTail();						
	         if (gr.getEdgeList().get(i).getEdgeClass() != "T") {					//If it is not a tree type then start the condition
	        	 if (head.getTimeStarted() > tail.getTimeStarted() && head.getTimeEnded() < tail.getTimeEnded()) {	//If head node started after tail node but ended before tail node 
	        		 gr.getEdgeList().get(i).setEdgeClass("F");														//then tail->head is forward
	        	 }
	        	 if (head.getTimeStarted() < tail.getTimeStarted() && head.getTimeEnded() > tail.getTimeEnded()) {	//Reversely from the previous condition
	        		 gr.getEdgeList().get(i).setEdgeClass("B");														// we have tail->head as back
	        	 }
	        	 if (head.getTimeStarted() <= tail.getTimeStarted() && head.getTimeEnded() <= tail.getTimeEnded()) {	//If head node started before tail node, and head node ended before tail node then tail->head is cross 
	        		 gr.getEdgeList().get(i).setEdgeClass("B");															//Basically head was found before tail. Both tail->head or head->tail is still cross
	        	 }
	         }
		}	 
	}
	
	public String printTree() {			//Print type of tree
		StringBuilder edgeResult = new StringBuilder();
		String currentOutput = new String();
		
        for (int i = 0; i < g.getEdgeList().size(); i++) {	
            Edge edge = g.getEdgeList().get(i);
            currentOutput = String.format("%s-%s\t\t%s\n", edge.getTail().getAbbrev(), edge.getHead().getAbbrev(),edge.getEdgeClass());
            edgeResult.append(currentOutput);	//Add result to the string
        }
        return edgeResult.toString();
	}
	
	public String printNode() {			//Print start and end time of DFS search
        Collections.sort(g.getNodeList(), new Comparator<Node>() {		//Sort the nodeList by starting time in ascending order
            @Override
            public int compare(Node node1, Node node2) {
                return node1.getTimeStarted() - node2.getTimeStarted();
            }});
        
        StringBuilder dfsResult = new StringBuilder();
        String currentOutput = new String();
        
        for (int i = 0; i < g.getNodeList().size(); i++) {		
            Node currentNode = g.getNodeList().get(i);
            currentOutput = String.format("%-16s%-16d%-16d\n", currentNode.getName(), currentNode.getTimeStarted(), currentNode.getTimeEnded());
            dfsResult.append(currentOutput);		//Add result the the string 
        }        
        return dfsResult.toString();
    }
}

