package CPlugin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import CPlugin.view.AreaEditPart;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;




public class Area extends Observable {
	
	private AreaEditPart myEditPart;
	public void executeCommand(Command c){
		myEditPart.executeCommand(c);
	}
	
	public void setEditPart(AreaEditPart e){
		myEditPart = e;
	}
	
//	public IFile getFile(){
//		IEditorInput input = ((DefaultEditDomain)myEditPart.getViewer().getEditDomain()).getEditorPart().getEditorInput();
//		IFile file = (input instanceof IFileEditorInput) ? ((IFileEditorInput) input).getFile() : null;
//		((IFolder)file.getParent()).getFolder("src").getFolder("")
//	}
	
	
	
	String name;
	boolean isBlackBox = false; 
	private Graph graph;
	private List<Connection> sourceConnections, targetConnections;
	Rectangle constraint;
	
	public Area(){
		constraint = new Rectangle(0, 0, 65, 35);
	}
	
	
	public Rectangle getConstraint() {
		 return constraint;
	}
	
	public void setConstraint(Rectangle constraint) {
		 this.constraint = constraint;
		 setChanged();
	}
	
	public int getX(){
		return constraint.getLocation().x;
	}
	
	public int getY(){
		return constraint.getLocation().y;
	}
	
	public void setX(int x){
		constraint.setX(x);
		setChanged();
	}
	public void setY(int y){
		constraint.setY(y);
		setChanged();
	}
	
	
	public boolean isBlackBox(){
		return isBlackBox;
	}
	
	public void setBlackBox(boolean isBlackBox){
		this.isBlackBox = isBlackBox;
	}
	
	public void setGraph(Graph g ){
		this.graph = g;
	}
	
	public Graph getGraph(){
		return graph;
	}
	
	/**
	 * Graph related methods
	 */
	public String getName() {
		 return name;
	}
	
	public void setName(String name) {
		this.name = name;
		setChanged();
	}
	
	
	public void addSourceConnection(Connection connection) {
		getSourceConnections().add(connection);
		setChanged();
	}
	
	public void addTargetConnection(Connection connection) {
		getTargetConnections().add(connection);
		setChanged();
	}
	
	public void removeSourceConnection(Connection connection) {
		getSourceConnections().remove(connection);
		setChanged();
	}
	
	public void removeTargetConnection(Connection connection) {
		getTargetConnections().remove(connection);
		setChanged();
	}
	
	
	
	public List<Connection> getSourceConnections() {
		if (sourceConnections == null)
			sourceConnections = new ArrayList<Connection>();
		return sourceConnections;
	}
	
	public List<Connection> getTargetConnections() {
		if (targetConnections == null)
			targetConnections = new ArrayList<Connection>();
		return targetConnections;
	}
	
	public void setChanged(){
		super.setChanged();
		notifyObservers();
	}
	
	
	/***
	 * Process related methods
	 */
	
	private ArrayList<Activity> process;
	
	public ArrayList<Activity> getProcess(){
		if ( process == null)
			process = new ArrayList<Activity>();
		return process;
	}
	
	public Activity addProcess(String name){
		Activity p = new Activity(name);
		getProcess().add(p);
		setChanged();
		return p;
	}
	
	public void addProccess(Activity p){
		getProcess().add(p);
		setChanged();
	}
	
	public void removeProccess(String p){
		removeProccess(getProccess(p));
		setChanged();
	}
	
	public void removeProccess(Activity p){
		getProcess().remove(p);
		setChanged();
	}
	
	public String getNextProccessName() {
		int runner = 1;
		while (true) {
			String candidate = "Proccess" + runner;
			if (getProccess(candidate) == null) 
				return candidate;
			runner ++;
		}
	}
	
	public Activity getProccess(String candidate) {
		 for (int i = 0; i < getProcess().size(); i++)
			 if (candidate.equals(((Activity)getProcess().get(i)).getName()))
				 return (Activity)getProcess().get(i);
		 return null;
	}
}