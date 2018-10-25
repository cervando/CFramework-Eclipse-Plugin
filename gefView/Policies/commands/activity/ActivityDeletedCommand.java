package kmiddlePlugin.gefView.Policies.commands.activity;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.PropertySheetPage;

import kmiddlePlugin.model.Activity;
import kmiddlePlugin.model.Area;

public class ActivityDeletedCommand extends Command{
	
	
	private Area area;
	private Activity process;
	private PropertySheetPage page;
	private String areaName;
	
	
	
	public ActivityDeletedCommand(Area a, Activity p, PropertySheetPage page){
		area = a;
		areaName = a.getName();
		process = p;
		this.page = page;
	}

	public void execute(){
		area.removeProccess(process);
		if ( page !=  null)
			page.refresh();
	}
	
	public void undo(){
		area.addProccess(process);
		if ( page !=  null)
			page.refresh();	
	}
	
	public Area getArea(){ return area; }
	public String getAreaName(){return areaName; }
	public Activity getProccess() {return process; }
}
