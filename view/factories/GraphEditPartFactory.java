package kmiddlePlugin.view.factories;

import kmiddlePlugin.model.Area;
import kmiddlePlugin.model.Connection;
import kmiddlePlugin.model.Graph;
import kmiddlePlugin.view.ConnectionEditPart;
import kmiddlePlugin.view.GraphEditPart;
import kmiddlePlugin.view.AreaEditPart;

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