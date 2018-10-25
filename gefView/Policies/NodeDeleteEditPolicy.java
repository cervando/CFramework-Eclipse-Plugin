package kmiddlePlugin.gefView.Policies;

import kmiddlePlugin.gefView.Policies.commands.area.NodeDeleteCommand;
import kmiddlePlugin.model.Area;
import kmiddlePlugin.model.Graph;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;




public class NodeDeleteEditPolicy extends ComponentEditPolicy {
	
	
	protected Command createDeleteCommand(GroupRequest request) {
		NodeDeleteCommand deleteCommand = new NodeDeleteCommand();
		deleteCommand.setGraph((Graph)getHost().getParent().getModel());
		deleteCommand.setNode((Area)getHost().getModel());
		return deleteCommand;
	}
}