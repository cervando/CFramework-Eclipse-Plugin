package kmiddlePlugin.gefView.Policies.commands.activity;

import kmiddlePlugin.model.Activity;

import org.eclipse.gef.commands.Command;

public class ActivityTypeChangedCommand extends Command{
	
	private Activity process;
	private int newVal, oldVal;
	private String areaName, processName, lenguage;
	
	
	
	public ActivityTypeChangedCommand(kmiddlePlugin.model.Activity p, int newVal){
		process = p;
		this.newVal = newVal;
		oldVal = p.getType();
		areaName = p.getArea().getName();
		processName = p.getName();
		lenguage = p.getLanguage();
	}

	public void execute(){
		process.setType(newVal);
		//( (DiagramEditor)((DefaultEditDomain) process.getArea().getEditPart().getViewer().getEditDomain()).getEditorPart()).setDirty(true);
	}
	
	public void undo(){
		process.setType(oldVal);
		//( (DiagramEditor)((DefaultEditDomain) process.getArea().getEditPart().getViewer().getEditDomain()).getEditorPart()).setDirty(true);
	}
	
	//public Process getProcess(){ return process; }
	public int getOldValue(){ return oldVal; }
	public int getNewValue(){ return newVal; }
	public String getAreaName(){ return areaName;}
	public String getProcessName(){ return processName;}
	public String getLanguage() { return lenguage; }
	
}
