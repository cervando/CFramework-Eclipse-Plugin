package kmiddlePlugin.gefView.Policies.commands.activity;

import kmiddlePlugin.model.Activity;

import org.eclipse.gef.commands.Command;

public class ActivityChangedNameCommand extends Command{
	
	
	private Activity process;
	private String newName,oldName, language,areaName;
	
	
	
	public ActivityChangedNameCommand(kmiddlePlugin.model.Activity p, String newName){
		process = p;
		areaName = p.getArea().getName();
		this.newName = newName;
		oldName = p.getName();
		language = p.getLanguage();
	}

	public void execute(){
		process.setName(newName);
		//( (DiagramEditor)((DefaultEditDomain) process.getArea().getEditPart().getViewer().getEditDomain()).getEditorPart()).setDirty(true);
	}
	
	public void undo(){
		process.setName(oldName);
		//( (DiagramEditor)((DefaultEditDomain) process.getArea().getEditPart().getViewer().getEditDomain()).getEditorPart()).setDirty(true);
	}
	
	public Activity getProcess(){ return process; }
	public String getOldName(){ return oldName; }
	public String getNewName(){ return newName; }
	public String getLanguage(){ return language; }
	public String getAreaName(){return areaName; }
	
}
