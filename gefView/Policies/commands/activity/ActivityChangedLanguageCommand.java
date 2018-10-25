package kmiddlePlugin.gefView.Policies.commands.activity;

import org.eclipse.gef.commands.Command;

import kmiddlePlugin.model.Activity;

public class ActivityChangedLanguageCommand extends Command{
	
	
	
	private Activity process;
	private int newLangage,oldLanguage;
	private int type;
	private String processName,areaName;
	
	
	
	public ActivityChangedLanguageCommand(kmiddlePlugin.model.Activity p, int newLenguage){
		process = p;
		this.newLangage = newLenguage;
		oldLanguage = p.getLanguageIndex();
		type = p.getType();
		processName = p.getName();
		areaName = p.getArea().getName();
	}

	public void execute(){
		process.setLanguage(newLangage);
		//( (DiagramEditor)((DefaultEditDomain) process.getArea().getEditPart().getViewer().getEditDomain()).getEditorPart()).setDirty(true);
	}
	
	public void undo(){
		process.setLanguage(oldLanguage);
		//( (DiagramEditor)((DefaultEditDomain) process.getArea().getEditPart().getViewer().getEditDomain()).getEditorPart()).setDirty(true);
	}
	
	public Activity getProcess(){ return process; }
	public String getOldLanguage(){ return process.getLanguageList()[oldLanguage]; }
	public String getNewLanguage(){ return process.getLanguageList()[newLangage]; }
	public int getType(){ return type; }
	public String getProcessName(){return processName;}
	public String getAreaName(){return areaName;}
}
