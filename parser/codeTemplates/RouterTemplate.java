package CPlugin.parser.codeTemplates;

public class RouterTemplate {
	public static String sourceCode = 
"package %s;" +
"\n" +
"\nimport java.util.HashMap;" +
"\n" +
"\nimport config.Names;" +
"\nimport cFramework.nodes.routers.Router;" +
"\nimport cFramework.nodes.processes.ProcessConfiguration;" + 
"\nimport cFramework.communications.spikes.SpikeMerger;" + 
"\nimport cFramework.communications.spikes.SpikeRouter;" +
"\n" +
"\npublic class %s extends Router{" +
"\n	" +
"\n	public %s(){" +
"\n		this.ID = Names.%s;" + 
"\n		this.namer = Names.class;" +
"\n		" +
"\n		//Copy and paste next block to add a route" + 
"\n		/*AddRoute(new SpikeRouter(" +
"\n				new int[]{ Names.X, Names.Y}," + 
"\n				new int[]{ Names.Z }" +
"\n				,new SpikeMerger() {" +
"\n					public byte[] merge(HashMap<Long, byte[]> spikes) {" +
"\n						byte[] elRetorno = null;" + 
"\n						return elRetorno;" +
"\n					}" +
"\n				}));*/" +
"\n	}" +
"\n	" +
"\n	public void init(){" +
"\n	" +
"\n	}" +
"\n	" +
"\n	public void receive(long nodeID, byte[] data) {" +
"\n" +
"\n }" +
"\n}";
}