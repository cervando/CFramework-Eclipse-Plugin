package CPlugin.gefView.Policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import CPlugin.gefView.Policies.commands.area.NodeChangeConstraintCommand;
import CPlugin.gefView.Policies.commands.area.NodeCreateCommand;
import CPlugin.model.Area;
import CPlugin.model.Graph;


/**
 * I THINK: This class is incharge of valid if is legal to do a change a node in the graph (move, resize) 
 * @author armando-cinvestav
 *
 */
public class NodeChangeXYEditPolicy extends XYLayoutEditPolicy {
 
	
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		NodeChangeConstraintCommand changeConstraintCommand = new NodeChangeConstraintCommand();
		changeConstraintCommand.setNode((Area)child.getModel());
		changeConstraintCommand.setNewConstraint((Rectangle)constraint);
		
		return changeConstraintCommand;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		
		if (request.getNewObjectType().equals(Area.class)) {
			//System.out.println("Entro a comando crear");
			NodeCreateCommand result = new NodeCreateCommand();
			result.setLocation(request.getLocation());
			result.setNode((Area)request.getNewObject());
			result.setParent((Graph)getHost().getModel());
			return result;
		}
		return null;
	}
	

}