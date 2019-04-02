package CPlugin.parser.codeTemplates;

public class InitTemplate {

	public static String init =
					
"package config;" +
"\n" +
"\nimport cFramework.nodes.service.Igniter;" +
"\n" +
"\npublic class Init extends Igniter{" + 
"\n" +
"\n	public Init(){" +
"\n		String [] areas = {" +
"\n		};" +
"\n		" +
"\n		setAreas(areas);" +
"\n		run();" +
"\n	}" +
"\n	" +
"\n	public static void main(String[] args){" +
"\n		new Init();" +
"\n	}" +
"\n}";

}
