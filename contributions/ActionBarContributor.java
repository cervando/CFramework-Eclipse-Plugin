package kmiddlePlugin.contributions;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;

import kmiddlePlugin.editors.DiagramEditor;


/**
 * This class is in charge of LINK the change in the plugin editor to the UNDO/REDO actions in eclipse
 * @author armando-cinvestav
 *
 */
public class ActionBarContributor extends EditorActionBarContributor {
	
	
	public void setActiveEditor(IEditorPart targetEditor) {
		IActionBars actionBars = getActionBars();
		if (actionBars == null) 
			return;
		
		String undoId = ActionFactory.UNDO.getId();
		String redoId = ActionFactory.REDO.getId();
		String deleteId = ActionFactory.DELETE.getId();
		
		if ( targetEditor instanceof DiagramEditor ){
			ActionRegistry actionRegistry = ((DiagramEditor)targetEditor).getActionRegistry();
			actionBars.setGlobalActionHandler(undoId, actionRegistry.getAction(undoId));
			actionBars.setGlobalActionHandler(redoId, actionRegistry.getAction(redoId));
			actionBars.setGlobalActionHandler(deleteId, actionRegistry.getAction(deleteId));
			actionBars.updateActionBars();
		}else
			System.out.println("Error: El IEditorPart no es GraphEditor");
	}
}