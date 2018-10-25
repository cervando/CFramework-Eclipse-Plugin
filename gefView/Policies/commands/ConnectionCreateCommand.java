package kmiddlePlugin.gefView.Policies.commands;

import java.util.List;

import kmiddlePlugin.model.Area;
import kmiddlePlugin.model.Connection;

import org.eclipse.gef.commands.Command;
	

public class ConnectionCreateCommand extends Command {
	private Area source;
	private Area target;
	private Connection connection;
	private String sourceName, targetName;
	
	
	public boolean canExecute() {
		//Check if the connection already exist
		if ( source != null && target != null ){
			List<Connection> cons = source.getSourceConnections();
			for ( int i = 0; i < cons.size(); i++){
				if ( cons.get(i).getTarget() == target )
					return false;
			}
			return true;
		}
		return false;
	}
	
	public void execute() {
		connection.setSource(source);
		connection.setTarget(target);
		
	}
	
	public void undo() {
		connection.setSource(null);
		connection.setTarget(null);
	}
	
	
	public void setSource(Area source) { 
		this.source = source;	
		if ( source != null ) sourceName = source.getName();
	}
	public void setTarget(Area target) { 
		this.target = target;
		if ( target != null ) targetName = target.getName();
	}
	public Area getSource() { return source; }
	public Area getTarget() { return target; }
	
	public String getSourceName(){ return sourceName; }
	public String getTargetName(){ return targetName; }
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}