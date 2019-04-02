package CPlugin.gefView.Policies;

import CPlugin.gefView.Policies.commands.ConnectionCreateCommand;
import CPlugin.model.Area;
import CPlugin.model.Connection;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;


public class ConnectionCreateEditPolicy extends GraphicalNodeEditPolicy {

	protected Command getConnectionCreateCommand( CreateConnectionRequest request) {
		
		ConnectionCreateCommand result = new ConnectionCreateCommand();
		result.setSource((Area)getHost().getModel());
		result.setConnection((Connection)request.getNewObject());
		request.setStartCommand(result);
		return result;
	}
	
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		ConnectionCreateCommand result = (ConnectionCreateCommand)request.getStartCommand();
		result.setTarget((Area)getHost().getModel());
		return result;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}