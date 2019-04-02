package CPlugin.view;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.ISelection;

import CPlugin.editors.DiagramEditor;
import CPlugin.gefView.Policies.NodeChangeXYEditPolicy;
import CPlugin.model.Area;
import CPlugin.model.Graph;

public class GraphEditPart extends AbstractGraphicalEditPart implements Observer {
	
	public GraphEditPart(Graph graph) {
		setModel(graph);
		graph.addObserver(this);
	}
	
	//Variables used for the propertyChange selection
	/*Area selectedArea = null;
	ListenerList listeners = new ListenerList(); */
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();
		layer.setLayoutManager(new FreeformLayout());
		layer.setBorder(new LineBorder(1));
		return layer;
	}
	
	
	
	protected List<Area> getModelChildren() {
		return ((Graph)getModel()).getNodes();
	}
	
	protected void createEditPolicies() {
		installEditPolicy(	
				EditPolicy.LAYOUT_ROLE,
				new NodeChangeXYEditPolicy());
	}
	
	@Override
	public void update(Observable o, Object arg) {
		List<Area> nodes = getModelChildren();
		for (int n = nodes.size() - 1; 0 <= n ;n--){
			if (nodes.get(n).countObservers() == 0){
				AreaEditPart nEP = new AreaEditPart(nodes.get(n));
				nEP.setParent(this);
			}
		}
		DiagramEditor editor = (DiagramEditor)((DefaultEditDomain) getViewer().getEditDomain()).getEditorPart();
		editor.setDirty(true);
		refresh();
	
	}
	
	public void setSelection(ISelection selection) {
		// TODO Auto-generated method stub
		getViewer().setSelection(selection);  
	}
	
	
	
	/*
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		// TODO Auto-generated method stub
		listeners.add(listener);  
	}
	@Override
	public ISelection getSelection() {
		// TODO Auto-generated method stub
		if ( selectedArea != null ) 
			return new StructuredSelection(selectedArea);
		System.out.println("selection null");
		return null;
	}
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		// TODO Auto-generated method stub
		listeners.remove(listener);  
		
	}
	@Override
	public void setSelection(final ISelection selection) {
		// TODO Auto-generated method stub
		Object[] list = listeners.getListeners();
		System.out.println("SetSelection --- Listeners count: " + list.length);
		final ISelectionProvider father = this;
		for (int i = 0; i < list.length; i++) {  
			
			final ISelectionChangedListener sel = (ISelectionChangedListener) list[i];
			
			
			Display.getCurrent().asyncExec(new Runnable() {
				   public void run() {
					   sel.selectionChanged(new SelectionChangedEvent(father, selection));
				   }
			});
		}  
	}*/
	
}