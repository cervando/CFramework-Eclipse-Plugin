package kmiddlePlugin.gefView.Policies.commands.area;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.PropertySheetPage;

import kmiddlePlugin.model.Area;

public class NodeChangedXCommand extends Command{
	
	
	private Area area;
	private String areaName;
	private int newX,oldX;
	private int y, w, h;
	private PropertySheetPage page;
	
	
	
	public NodeChangedXCommand(Area a, int newX, PropertySheetPage page){
		area = a;
		areaName = a.getName();
		this.newX = newX;
		oldX = a.getX();
		this.page = page;
		y = a.getY();
		w = a.getConstraint().width;
		h = a.getConstraint().height;
	}

	public void execute(){
		area.setX(newX);
		if ( page != null )
			page.refresh();
	}
	
	public void undo(){
		area.setX(oldX);
	}
	
	
	public int getNewX(){ return newX; }
	public int getY(){ return y; }
	public int getHeigh(){ return h; }
	public int getWidth(){ return w; }
	public String getAreaName(){ return areaName; }
}
