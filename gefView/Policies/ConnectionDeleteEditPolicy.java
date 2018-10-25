package kmiddlePlugin.gefView.Policies;

import kmiddlePlugin.gefView.Policies.commands.ConnectionDeleteCommand;
import kmiddlePlugin.model.Connection;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

public class ConnectionDeleteEditPolicy extends org.eclipse.gef.editpolicies.ConnectionEditPolicy {
	
	protected Command getDeleteCommand(GroupRequest request) {
		ConnectionDeleteCommand result = new ConnectionDeleteCommand();
		result.setConnection((Connection)getHost().getModel());
		return result;
	}
}