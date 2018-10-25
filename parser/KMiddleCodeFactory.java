package kmiddlePlugin.parser;

import kmiddlePlugin.parser.codeTemplates.AreaTemplate;
import kmiddlePlugin.parser.codeTemplates.InitTemplate;
import kmiddlePlugin.parser.codeTemplates.ActivityTemplate;

public class KMiddleCodeFactory {

	public static String newArea( String pack, String className){
		String code = String.format(AreaTemplate.sourceCode, new Object[]{pack,className,className,className});
		return code;
	}

	public static String newProcess(String pack, String areaName, String activityName, String language){
		String code = ""; 
		if ( language.equals("JAVA"))
			code = String.format(ActivityTemplate.javaSourceCode, new Object[]{pack, areaName,activityName,areaName,activityName,areaName,activityName});
		else if ( language.equals("PYTHON")){
			code = String.format(ActivityTemplate.pythonSourceCode, new Object[]{areaName,activityName, areaName,activityName});
		}
		return code;
	}

	public static String addActivityType(String areaName, String activity){
		return addActivityType(areaName, activity, "JAVA");
	}
	
	public static String addActivityType(String areaName, String activity, String language){
		String ret = "";
		if (language.equals("JAVA")){
			ret = ret + "addProcess(" + areaName + "_" + activity + ".class);";
		}/*else if ( language.equals("Python")){
			ret = ret + "\"" + areaName + "_" + activity + "\", Languages.PYTHON";
		}
			
		if ( mapped )
			return ret + ", new NodeConfiguration(NodeConfiguration.BEHAVIOR_MAPPED) );";*/
		return ret;
	}
	
	
	public static String newAreaNames(){
		return "package config;\n\nimport kmiddle2.util.IDHelper;\npublic class AreaNames {\n}";
	}
	
	
	public static String newInitFile(){
		return InitTemplate.init;
	}
}