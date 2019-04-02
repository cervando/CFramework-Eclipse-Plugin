package CPlugin.view.factories;

import CPlugin.model.Area;
import CPlugin.model.Connection;
import CPlugin.model.Graph;
import CPlugin.view.ConnectionEditPart;
import CPlugin.view.GraphEditPart;
import CPlugin.view.AreaEditPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class GraphEditPartFactory implements EditPartFactory {
	
	
	
	public EditPart createEditPart( EditPart context, Object model) {	
		
		if (model instanceof Graph) {
			return new GraphEditPart((Graph)model);
			
		} else if (model instanceof Area) {
			return new AreaEditPart((Area)model);
			
		} else if (model instanceof Connection){
			 return new ConnectionEditPart((Connection)model);
		
		} else {
			return null;
		}
	}
}