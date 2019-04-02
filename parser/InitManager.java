package CPlugin.parser;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public class InitManager extends Manager {

	private IFile initFile;

	public InitManager(IProject project) {
		super(project);
		
		//Creates Init file
		initFile = config.getFile("Init.java");
		if ( !initFile.exists()){
			
		    try{
		    	saveFile(initFile, KMiddleCodeFactory.newInitFile());
			} catch (Exception e) {
				System.out.println(this.getClass() + ":: Error creating init file");
				System.out.println(e.toString());
			}
		}
	}
	
	
	
	public boolean addArea(String area){
		String pack = getPackageName(area);
		String className = getClassName(area);
		return fileReplaceAll(initFile, 
				"String[ \\t]*\\[\\][ \\t]*areas[ \\t]*=[ \\t]*\\{",
				"String[] areas = \t{ \n\t\t\t\t" + className + ".class.getName(),")
				&&
				fileReplaceAll(initFile, 
						"\\nimport cFramework.nodes.service.Igniter;",
						"\nimport " + pack + "."+ className  +  ";\nimport cFramework.nodes.service.Igniter;");
	}
	
	public boolean setNameArea(String oldAreaName, String newAreaName){
		return fileReplaceAll(initFile,
				"[ \\t]*" + Pattern.quote(getClassName(oldAreaName)) + ".class.getName",
				"\t\t\t\t" + getClassName(newAreaName) + ".class.getName")
				&&
				fileReplaceAll(initFile, 
						"import " + Pattern.quote(getPackageName(oldAreaName) + "." + getClassName(oldAreaName)),
						"import " + getPackageName(newAreaName) + "." + getClassName(newAreaName));
	}
	
	public boolean deleteArea(String area){
		String pack = getPackageName(area);
		String className = getClassName(area);
		return fileReplaceAll(initFile,
						"import " + Pattern.quote(pack + "."+ className)  +  ";\n",
						"") && 
				fileReplaceAll(initFile,
						"[ \\t]*" + Pattern.quote(className) + ".*\\n",
						"");
	}
}