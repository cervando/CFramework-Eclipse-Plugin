package kmiddlePlugin.parser.codeTemplates;

public class AreaTemplate {
	public static String sourceCode = 
"package %s;" +
"\n" +
"\nimport config.AreaNames;" +
"\nimport kmiddle2.nodes.areas.Area;" +
"\nimport kmiddle2.nodes.activities.ActConf;" + 
"\nimport kmiddle2.communications.spikes.SpikeMerger;" + 
"\nimport kmiddle2.communications.spikes.SpikeRouter;" +
"\n" +
"\npublic class %s extends Area{" +
"\n	" +
"\n	public %s(){" +
"\n		this.ID = AreaNames.%s;" + 
"\n		this.namer = AreaNames.class;" +
"\n		" + 
"\n		AddRoute(new SpikeRouter(" +
"\n				new int[]{ AreaNames.X, AreaNames.Y}," + 
"\n				new int[]{ AreaNames.Z }" +
"\n				,new SpikeMerger() {" +
"\n					public byte[] merge(HashMap<Integer, byte[]> spikes) {" +
"\n						byte[] elRetorno = null;" + 
"\n						return elRetorno;" +
"\n					}" +
"\n				}));" +
"\n	}" +
"\n	" +
"\n	public void init(){" +
"\n	" +
"\n	}" +
"\n	" +
"\n	public void receive(int nodeID, byte[] data) {" +
"\n" +
"\n }" +
"\n}";
}