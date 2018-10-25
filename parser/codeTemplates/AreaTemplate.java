package kmiddlePlugin.parser.codeTemplates;

public class AreaTemplate {
	public static String sourceCode = 
"package %s;" +
"\n" +
"\nimport config.AreaNames;" +
"\nimport kmiddle2.nodes.areas.Area;" +
"\nimport kmiddle2.nodes.activities.ActConf;" +
"\n" +
"\npublic class %s extends Area{" +
"\n	" +
"\n	public %s(){" +
"\n		this.ID = AreaNames.%s;" + 
"\n		this.namer = AreaNames.class;" +
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