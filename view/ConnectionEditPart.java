package kmiddlePlugin.view;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import kmiddlePlugin.gefView.Policies.ConnectionDeleteEditPolicy;
import kmiddlePlugin.model.Connection;


public class ConnectionEditPart extends AbstractConnectionEditPart {
	
	public ConnectionEditPart(Connection connection) {
		setModel(connection);
	}
	
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionDeleteEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,	new ConnectionEndpointEditPolicy());
	}
	
	protected IFigure createFigure() {
		PolylineConnection conn = (PolylineConnection) super.createFigure();
		conn.setLineWidth(2);
		conn.setTargetDecoration(new PolygonDecoration());
		conn.setConnectionRouter(new ManhattanConnectionRouter());
		return conn;
	}
}