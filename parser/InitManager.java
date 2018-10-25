package kmiddlePlugin.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
			byte[] code = KMiddleCodeFactory.newInitFile().getBytes();
			InputStream source = new ByteArrayInputStream(code);
		    try{
		    	work = new MyProgressMonitor();
		    	initFile.create(source, true, work);
		    	while( !work.isDone() );
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
						"\\nimport kmiddle2.nodes.service.Igniter;",
						"\nimport " + pack + "."+ className  +  ";\nimport kmiddle2.nodes.service.Igniter;");
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