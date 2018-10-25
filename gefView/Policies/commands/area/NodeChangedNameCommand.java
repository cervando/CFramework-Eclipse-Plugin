package kmiddlePlugin.gefView.Policies.commands.area;

import org.eclipse.gef.commands.Command;

import kmiddlePlugin.model.Area;

public class NodeChangedNameCommand extends Command{
	
	
	private Area area;
	private String newName,oldName;
	private boolean isBlackBox;
	
	
	public NodeChangedNameCommand(Area a, String newName){
		area = a;
		isBlackBox = a.isBlackBox();
		this.newName = newName;
		oldName = a.getName();
	}

	public void execute(){
		area.setName(newName);
	}
	
	public void undo(){
		area.setName(oldName);
	}
	
	public Area getArea(){ return area; }
	public boolean isBlackBox() { return isBlackBox; }
	public String getOldName() { return oldName; }
	public String getNewName() { return newName; }
}