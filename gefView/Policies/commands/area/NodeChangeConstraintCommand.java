package CPlugin.gefView.Policies.commands.area;

import CPlugin.model.Area;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;


/**
 * This class execute a simple change in the graph, 
 * also is a row in the changes history 
 * @author armando-cinvestav
 *
 */
public class NodeChangeConstraintCommand extends Command {
	
	
	private Rectangle newConstraint;
	private Rectangle oldConstraint;
	private Area node;
	private String nodeName;
	
	public void execute() {
		if (oldConstraint == null)
			oldConstraint = new Rectangle(node.getConstraint());
		node.setConstraint(newConstraint);
	}
	
	public void undo() {node.setConstraint(oldConstraint);}
	
	public void setNewConstraint(Rectangle newConstraint) {
		this.newConstraint = newConstraint;
	}
	public Rectangle getNewConstraint(){return newConstraint; }
	
	public void setNode(Area node) { this.node = node; nodeName = node.getName(); }
	public Area getNode() { return this.node; }
	public String getAreaName(){ return nodeName; }
	
}