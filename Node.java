import java.util.*;

// A node of a graph for the Spring 2018 ICS 340 program

public class Node {

	String name;
	String val;  // The value of the Node
	String abbrev;  // The abbreviation for the Node
	ArrayList<Edge> outgoingEdges;  
	ArrayList<Edge> incomingEdges;
	boolean visited;
    int timeStarted;
    int timeEnded;
	
	public Node( String theAbbrev ) {
		setAbbrev( theAbbrev );
		val = null;
		name = null;
		outgoingEdges = new ArrayList<Edge>();
		incomingEdges = new ArrayList<Edge>();
		visited = false;
		timeStarted = 0;
		timeEnded = 0;
	}
	
	public String getAbbrev() {
		return abbrev;
	}
	
	public String getName() {
		return name;
	}
	
	public String getVal() {
		return val;
	}
	
	public ArrayList<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}
	
	public ArrayList<Edge> getIncomingEdges() {
		return incomingEdges;
	}
	
	public void setAbbrev( String theAbbrev ) {
		abbrev = theAbbrev;
	}
	
	public void setName( String theName ) {
		name = theName;
	}
	
	public void setVal( String theVal ) {
		val = theVal;
	}
	
	public void addOutgoingEdge( Edge e ) {
		outgoingEdges.add( e );
	}
	
	public void addIncomingEdge( Edge e ) {
		incomingEdges.add( e );
	}
	
	public void setVisited(boolean visited) {
        this.visited = visited;
    }
	
	public boolean isVisited() {
        return visited;
    }

    public int getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(int setTimeStarted) {
        this.timeStarted = setTimeStarted;
    }

    public int getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(int timeEnded) {
        this.timeEnded = timeEnded;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", name, abbrev);
    }
	
}
