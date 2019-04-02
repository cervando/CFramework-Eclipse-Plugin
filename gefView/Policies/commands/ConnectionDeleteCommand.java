package CPlugin.gefView.Policies.commands;

import CPlugin.model.Area;
import CPlugin.model.Connection;

import org.eclipse.gef.commands.Command;


public class ConnectionDeleteCommand extends Command {
	private Area source, target;
	private String sourceName, targetName;
	private Connection connection;
	
	
	public void setConnection(Connection connection) {
		this.connection = connection;
		if ( connection.getSource() != null )
			sourceName = connection.getSource().getName();
		
		if ( connection.getTarget() != null )
			targetName = connection.getTarget().getName();
	}
	
	public void execute() {
		//Esto se hace con el objetivo de guardar el source para undo
		if (source == null) 
			source = connection.getSource();
		
		if (target == null) 
			target = connection.getTarget();
		
		//Erasing reference to this conection fron the NODES
		connection.setSource(null);
		connection.setTarget(null);
	}
	
	public void undo() {
		connection.setSource(source);
		connection.setTarget(target);
	}
	
	public Area getSource(){	return source;	}
	public Area getTarget(){	return target;	}
	
	public String getSourceName(){ return sourceName; }
	public String getTargetName(){ return targetName; }
	
	
}