package CPlugin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class Graph extends Observable {
	
	private List<Area> nodes;
	
	public List<Area> getNodes() {
		if (nodes == null) 
			nodes = new ArrayList<Area>();
	 	return nodes;
	}
	
	public void setNode(List<Area> areas){
		this.nodes = areas;
		for (Area area : nodes) {
			area.setGraph(this);
		}
		setChanged();
		notifyObservers();
	}
	
	
	public void addNode(Area node) {
		getNodes().add(node);
		node.setGraph(this);
		setChanged();
		notifyObservers();
	}
	
	public void removeNode(Area node) {
		getNodes().remove(node);
		setChanged();
		notifyObservers();
	}
	
	public String getNextNodeName() {
		int runner = 1;
		while (true) {
			String candidate = "Node" + runner;
			if (getNodeByName(candidate) == null) 
				return candidate;
			runner ++;
		}
	}
	
	private Area getNodeByName(String candidate) {
		 for (int i = 0; i < getNodes().size(); i++)
			 if (candidate.equals(((Area)getNodes().get(i)).getName()))
				 return (Area)getNodes().get(i);
		 return null;
	}
}