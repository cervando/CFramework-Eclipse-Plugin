package CPlugin.gefView.Policies.commands.area;

import CPlugin.model.Area;
import CPlugin.model.Graph;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

public class NodeCreateCommand extends Command {

	private static final Dimension INITIAL_NODE_DIMENSION = new Dimension(65, 35);
	
	public Area node;
	private Rectangle constraint;
	private Graph graph;
	private String areaName;
	
	/**
	 * Ejecuta el commandoes 
	 * WARNING Also is called has [REDO] 
	 */
	public void execute() {
		setNodeConstraint();
		setNodeName();
		/*
		NodeEditPart nEP = new NodeEditPart(node);
		nEP.setParent(parent);
		*/
		//System.out.println("------------  \nCreateNodeCommand::Execute");
		graph.addNode(node);
	}
	
	
	/**
	 * Se ejecuta al desazer
	 */
	public void undo() {
		//System.out.println("------------  \nCreateNodeCommand::undo");
		graph.removeNode(node);
	}
	
	
	
	
	
	
	
	
	/***
	 * Estos metodos son llamados por los EditPolicy para generar el objeto Command
	 * 
	 */
	
	
	private void setNodeName() {
		node.setName(graph.getNextNodeName());
		areaName = node.getName();
	}
	
	private void setNodeConstraint() {
		if (constraint !=null)
			node.setConstraint(constraint);
	}	
	
	public void setNode(Area node) {this.node = node;}
	public Area getNode() { return this.node; } 
	public String getAreaName(){ return areaName; }
	
	public void setLocation(Point location) {
		this.constraint = new Rectangle(location, INITIAL_NODE_DIMENSION);
	}
	
	public void setParent(Graph parent) {
		this.graph = parent;
	}
}