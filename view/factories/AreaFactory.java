package kmiddlePlugin.view.factories;

import kmiddlePlugin.model.Area;

import org.eclipse.gef.requests.CreationFactory;

public class AreaFactory implements CreationFactory {
	public Object getNewObject() { return new Area(); }
	public Object getObjectType() { return Area.class; }
}