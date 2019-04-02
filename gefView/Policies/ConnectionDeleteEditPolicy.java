package CPlugin.gefView.Policies;

import CPlugin.gefView.Policies.commands.ConnectionDeleteCommand;
import CPlugin.model.Connection;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

public class ConnectionDeleteEditPolicy extends org.eclipse.gef.editpolicies.ConnectionEditPolicy {
	
	protected Command getDeleteCommand(GroupRequest request) {
		ConnectionDeleteCommand result = new ConnectionDeleteCommand();
		result.setConnection((Connection)getHost().getModel());
		return result;
	}
}