package kmiddlePlugin.listener;

import java.util.Iterator;

import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public class GraphEditorSelectionListener implements /*CommandStackListener,*/ ISelectionListener, CommandStackEventListener {
	
	private ActionRegistry actionRegistry;
	public GraphEditorSelectionListener( ActionRegistry registry) {
		this.actionRegistry = registry;
	}

	
	public void stackChanged(CommandStackEvent arg0) {
		updateActions();
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		updateActions();
	}
	
	
	private void updateActions() {
		@SuppressWarnings("rawtypes")
		Iterator iterator = actionRegistry.getActions();
		while (iterator.hasNext()) {
			Object action = iterator.next();
			if (action instanceof UpdateAction)
				((UpdateAction)action).update();
		}
	}
}