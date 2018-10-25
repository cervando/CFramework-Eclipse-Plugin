package kmiddlePlugin.model;



public class Connection {

	
	private Area source, target;
	public Area getSource() { return source; }
	
	
	
	public void setSource(Area source) {
		if (this.source != null)
			this.source.removeSourceConnection(this);
		this.source = source;
		if ( source != null)
			source.addSourceConnection(this);
	}
	public Area getTarget() { return target; }
	public void setTarget(Area target) {
		if (this.target != null)
			this.target.removeTargetConnection(this);
		this.target = target;
		if ( target != null)
			target.addTargetConnection(this);
	}
}