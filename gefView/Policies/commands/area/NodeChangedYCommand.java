package kmiddlePlugin.gefView.Policies.commands.area;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.PropertySheetPage;

import kmiddlePlugin.model.Area;

public class NodeChangedYCommand extends Command{
	
	
	private Area area;
	private String areaName;
	private int newY,oldY;
	private int x, w, h;
	private PropertySheetPage page;
	
	
	
	public NodeChangedYCommand(Area a, int newY, PropertySheetPage page){
		area = a;
		areaName = a.getName();
		this.newY = newY;
		oldY = a.getY();
		this.page = page;
		x = a.getX();
		w = a.getConstraint().width;
		h = a.getConstraint().height;
	}

	public void execute(){
		area.setY(newY);
		if ( page != null)
			page.refresh();
	}
	
	public void undo(){
		area.setY(oldY);
	}
	

	public int getNewY(){ return newY; }
	public int getX(){ return x; }
	public int getHeigh(){ return h; }
	public int getWidth(){ return w; }
	public String getAreaName(){ return areaName; }
}
