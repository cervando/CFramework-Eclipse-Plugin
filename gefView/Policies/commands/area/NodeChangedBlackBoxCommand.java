package kmiddlePlugin.gefView.Policies.commands.area;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.PropertySheetPage;

import kmiddlePlugin.model.Area;

public class NodeChangedBlackBoxCommand extends Command{
	
	
	private Area area;
	private String name;
	private boolean newValue, oldvalue;
	private PropertySheetPage page;
	
	
	
	public NodeChangedBlackBoxCommand(Area a, boolean isBlackbox, PropertySheetPage page){
		area = a;
		name = area.getName();
		this.page = page;
		newValue = isBlackbox;
		oldvalue = area.isBlackBox();
	}

	public void execute(){
		area.setBlackBox(newValue);
	}
	
	public void undo(){
		area.setBlackBox(oldvalue);
	}
	

	public boolean isBlackBox(){ return newValue; }
	public String getName() { return name; }
}
