package CPlugin.view.factories;

import CPlugin.model.Connection;

import org.eclipse.gef.requests.CreationFactory;

public class ConnectionFactory implements CreationFactory {
 
	public Object getNewObject() { return new Connection(); }
	public Object getObjectType() { return Connection.class; }
}