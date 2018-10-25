package kmiddlePlugin.utils;

import java.util.List;

import kmiddlePlugin.model.Area;
import kmiddlePlugin.model.Graph;

public class ModelValidator {

	

	
	public static boolean setNodeName(Graph graph, Area area, String newName){
		List<Area> nodes =  graph.getNodes();
		
		for ( int i = 0; i < nodes.size(); i++){
			if( nodes.get(i) != area 
				&& nodes.get(i).getName().equals(newName) )
					return false;
		}
		return true;
	}
}