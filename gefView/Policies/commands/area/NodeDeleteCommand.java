package kmiddlePlugin.gefView.Policies.commands.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kmiddlePlugin.model.Area;
import kmiddlePlugin.model.Connection;
import kmiddlePlugin.model.Graph;

import org.eclipse.gef.commands.Command;


public class NodeDeleteCommand extends Command {
	private Area node;
	private Graph graph;
	private List<Connection> connections;
	private Map<Connection, Area> connectionSources, connectionTargets;
	private boolean isBlackBox;

	public void setNode(Area node) { this.node = node;isBlackBox = node.isBlackBox(); }
	public Area getNode() { return this.node; } 
	
	public void setGraph(Graph graph) { this.graph = graph; }
	
	public void execute() {
		detachConnections();
		graph.removeNode(node);
	}
	
	
	public void undo() {
		graph.addNode(node);
		reattachConnections();
	}
	
	private void detachConnections() {
		connections = new ArrayList<Connection>();
		connectionSources = new HashMap<Connection, Area>();
		connectionTargets = new HashMap<Connection, Area>();
		connections.addAll(node.getSourceConnections());
		connections.addAll(node.getTargetConnections());
		for (int i = 0; i < connections.size(); i++) {
			Connection connection = connections.get(i);
			connectionSources.put(connection,connection.getSource());
			connectionTargets.put(connection,connection.getTarget());
			connection.setSource(null);
			connection.setTarget(null);
		}
	}
	
	
	private void reattachConnections() {
		for (int i = 0; i < connections.size(); i++) {
			Connection connection = (Connection)connections.get(i);
			connection.setSource((Area)connectionSources.get(connection));
			connection.setTarget((Area)connectionTargets.get(connection));
		}
	}
	

	public boolean isBlackBox() { return isBlackBox; }
}