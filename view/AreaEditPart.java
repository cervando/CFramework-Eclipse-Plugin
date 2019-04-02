package CPlugin.view;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.StructuredSelection;

import CPlugin.editors.DiagramEditor;
import CPlugin.gefView.Policies.ConnectionCreateEditPolicy;
import CPlugin.gefView.Policies.NodeDeleteEditPolicy;
import CPlugin.model.Area;
import CPlugin.model.Connection;
import CPlugin.model.Graph;
import CPlugin.view.Figures.AreaFigure;


public class AreaEditPart extends AbstractGraphicalEditPart implements Observer, NodeEditPart {
	
	IFigure figure = new AreaFigure();
	
	public AreaEditPart(Area node) {
		node.setEditPart(this);
		setModel(node); 
	}
	
	protected IFigure createFigure() { return figure; }
	
	public Area getArea(){
		return (Area)getModel();
	}
	
	public Graph getGraph(){
		return ((Area)getModel()).getGraph();
	}
	
	
	public void setDirty(boolean dirty){
		( (DiagramEditor)((DefaultEditDomain) getViewer().getEditDomain()).getEditorPart()).setDirty(dirty);
	}
	
	public boolean isDirty(){
		return ((DefaultEditDomain) getViewer().getEditDomain()).getEditorPart().isDirty();
	}
	
	public void executeCommand(Command c){
		getViewer().getEditDomain().getCommandStack().execute(c);
	}
	
	@Override
	public void performRequest(Request req) {
	    if(req.getType() == RequestConstants.REQ_OPEN) {
	        //System.out.println("requested double-click."); 
	        //IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			//page.openEditor(getModel(), "kmiddle.GraphEditor");
	    }
	}
	
	
	
	
	
//	public void setBackgroundColor ( Color color ){
//		figure.setBackgroundColor(color);
//	}
	
	protected void createEditPolicies() {
		installEditPolicy(
				EditPolicy.GRAPHICAL_NODE_ROLE,
				new ConnectionCreateEditPolicy());
		
		installEditPolicy(
				EditPolicy.COMPONENT_ROLE,
				new NodeDeleteEditPolicy());
	}
	
	public void refreshVisuals() {
		//Init Variables
		AreaFigure figure = (AreaFigure)getFigure();
		Area node = (Area)getModel();
		
		figure.getLabel().setText(node.getName());
		Rectangle r = new Rectangle(node.getConstraint());
		GraphEditPart parent = (GraphEditPart)getParent();
		parent.setLayoutConstraint(this, figure, r);

		if ( null != getViewer().getEditDomain() ) {
			DiagramEditor editor = (DiagramEditor)((DefaultEditDomain) getViewer().getEditDomain()).getEditorPart();
			editor.setDirty(true);
		}
	}


	/**
	 * Activate the BLACK MAGIC for select, move and resize
	 */
	public void activate() {
		if (!isActive()) 
			((Area)getModel()).addObserver(this);
		
		((GraphEditPart)getParent()).setSelection(new StructuredSelection(this));
		
		
		super.activate();
	}

	

	/**
	 * deactivate the BLACK MAGIC for select, move and resize
	 */
	public void deactivate() {
		if (isActive()) 
			((Area)getModel()).deleteObserver(this);
		super.deactivate();
	}
	
	public void update(Observable arg0, Object arg1) {
		refreshVisuals();
		addConnectionEtitParts();
		refreshSourceConnections();
		refreshTargetConnections();
	}
	
	public void addConnectionEtitParts(){
		List<Connection> con = getModelSourceConnections();
		for ( int c = 0; c < con.size(); c++){
			if ( con.get(c).getTarget() == null ){
				ConnectionEditPart cEP = new ConnectionEditPart(con.get(c));
				cEP.setParent(this.getParent());				
			}
		}
	}
	
	protected List<Connection> getModelSourceConnections() {
		return ((Area)getModel()).getSourceConnections();
	}
	
	protected List<Connection> getModelTargetConnections() {
		return ((Area)getModel()).getTargetConnections();
	}
	
	public ConnectionAnchor getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart connection) {
		return ((AreaFigure)getFigure()).getConnectionAnchor();
	}

	public ConnectionAnchor getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart connection) {
		return ((AreaFigure)getFigure()).getConnectionAnchor();
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return ((AreaFigure)getFigure()).getConnectionAnchor();
	}
	
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return ((AreaFigure)getFigure()).getConnectionAnchor();
	}

}