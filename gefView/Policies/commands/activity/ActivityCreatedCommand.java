package CPlugin.gefView.Policies.commands.activity;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.PropertySheetPage;

import CPlugin.model.Activity;
import CPlugin.model.Area;
import CPlugin.view.AreaEditPart;

public class ActivityCreatedCommand extends Command{
	
	
	
	private AreaEditPart editor;
	private PropertySheetPage page;
	
	private Area area;
	private Activity process;
	private String areaName, processName;
	
	
	public ActivityCreatedCommand(Area a, PropertySheetPage page){
		area = a;
		areaName = a.getName();
		this.page = page;
	}


	public void execute(){
		this.processName = area.getNextProccessName();
		area.addProcess(processName);
		process = area.getProccess(processName);
		process.setArea(area);
		//if ( editor != null ) editor.setDirty(true);
		if ( page !=  null) page.refresh();
	}
	
	public void undo(){
		area.removeProccess(process);
		if ( editor != null ) editor.setDirty(true);
		if ( page !=  null) page.refresh();
	}
	
	
	public Area getArea(){ return area; }
	public String getAreaName(){ return areaName; }
	public String getProcessName(){return processName;}
	public Activity getProccess() {return process; }
}
