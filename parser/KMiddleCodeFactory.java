package CPlugin.parser;

import CPlugin.parser.codeTemplates.RouterTemplate;
import CPlugin.parser.codeTemplates.InitTemplate;
import CPlugin.parser.codeTemplates.ProcessTemplate;

public class KMiddleCodeFactory {

	public static String newArea( String pack, String className){
		String code = String.format(RouterTemplate.sourceCode, new Object[]{pack,className,className,className});
		return code;
	}

	public static String newProcess(String pack, String areaName, String activityName, String language){
		String code = ""; 
		if ( language.equals("JAVA"))
			code = String.format(ProcessTemplate.javaSourceCode, new Object[]{pack, areaName,activityName,areaName,activityName,areaName,activityName});
		else if ( language.equals("PYTHON")){
			code = String.format(ProcessTemplate.pythonSourceCode, new Object[]{areaName,activityName, areaName,activityName});
		}
		return code;
	}

	public static String addProcessLanguage(String areaName, String activity){
		return addProcessLanguage(areaName, activity, "JAVA");
	}
	
	public static String addProcessLanguage(String areaName, String activity, String language){
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
	
	
	public static String newNames(){
		return "package config;\n\nimport cFramework.util.IDHelper;\npublic class Names {\n}";
	}
	
	
	public static String newInitFile(){
		return InitTemplate.init;
	}
}