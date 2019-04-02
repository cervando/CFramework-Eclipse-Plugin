package CPlugin.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import CPlugin.utils.Constants;

public class SourceCodeManager extends Manager{


	public SourceCodeManager(IProject project) {
    	super(project);
	}

	public boolean addArea(String areaName) {
		String className = getClassName(areaName);
		String packageName = getPackageName(areaName);
		IFolder pack = src.getFolder(packageName);
		if ( !pack.exists()){
			work = new MyProgressMonitor();
			try {
				pack.create(IResource.NONE, true, work);
			} catch (CoreException e) {
				System.out.println("Error creating Area Folder");
				return false;
			}
			while( !work.isDone() );
		}
		IFile areaClass = pack.getFile(className  + ".java");
		if ( !areaClass.exists() ){
			work = new MyProgressMonitor();
			String content = KMiddleCodeFactory.newArea(packageName, className);
			InputStream source = new ByteArrayInputStream(content.getBytes());
			try {
				areaClass.create(source, true, work);
			} catch (CoreException e) {
				System.out.println("Error creating Area File");
				return false;
			}
			while( !work.isDone() );
		}
		return true;
	}

	public boolean deleteArea(String area) {
		IFolder pack = getPackage(area);
		if ( !pack.exists() )
			return true;
		try {
			work = new MyProgressMonitor();
			pack.delete(false, work);
			while( !work.isDone() );
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean setAreaName(String oldAreaName, String newAreaName) {
		IFolder pack = getPackage(oldAreaName);
		try {
			work = new MyProgressMonitor();
			pack.move(getPackage(newAreaName).getFullPath(), true, work);
			while( !work.isDone() );
			pack = getPackage(newAreaName);
			
			IResource resources[] = pack.members();
			for ( IResource res: resources ){
				if( res.getType() == IResource.FILE ){
					String newName = res.getName().replaceAll(
														Pattern.quote( getClassName(oldAreaName) ),
														getClassName(newAreaName));
					work = new MyProgressMonitor();
					((IFile)res).move(pack.getFile(newName).getFullPath(), true, work);
					while( !work.isDone() );
					res = pack.getFile(newName);
					fileReplaceAll((IFile)res, 
							"\\W" + Pattern.quote( getClassName(oldAreaName) ) + "[\\W_]",
							Pattern.quote( getClassName(oldAreaName) ), 
							getClassName(newAreaName) );
					fileReplaceAll((IFile)res, 
							"package[ \\t]" + Pattern.quote( getPackageName(oldAreaName) ) + "[ \\t]*;" , 
							"package " + getPackageName(newAreaName) + ";" );
				}
			}
			return true;
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	//------------------------------ Section for Process Code handle ---------------
	/**
	 * 
	 * @param areaName
	 * @param activityName
	 * @return
	 */
	public boolean addActivity(String areaName, String activityName) {
		String packageName = getPackageName(areaName);
		String areaClassName = getClassName(areaName);
		String actClassName = areaClassName + "_" + getClassName(activityName);
		IFolder pack = src.getFolder(packageName);
		if ( !pack.exists()){
			work = new MyProgressMonitor();
			try {
				pack.create(IResource.NONE, true, work);
			} catch (CoreException e) {
				e.printStackTrace();
				return false;
			}
			while( !work.isDone() );
		}
		IFile actClass = pack.getFile(actClassName  + ".java");
		if ( !actClass.exists() ){
			work = new MyProgressMonitor();
			String content = KMiddleCodeFactory.newProcess(packageName, areaClassName, getClassName(activityName), "JAVA");
			InputStream source = new ByteArrayInputStream(content.getBytes());
			try {
				actClass.create(source, true, work);
			} catch (CoreException e) {
				e.printStackTrace();
				return false;
			}
			while( !work.isDone() );
		}
		
		IFile areaClass =  getAreaFile(areaName);
		fileReplaceAll(areaClass, 
							"this.namer = AreaNames.class;",
							"this.namer = AreaNames.class;\n\t\t"+
							KMiddleCodeFactory.addProcessLanguage(areaName, activityName));		
		return true;
	}
	
	public boolean deleteProcess(String areaName, String activityName, String lenguage) {
		try {
			work = new MyProgressMonitor();
			getActivityFile(areaName, activityName).delete(true, work);
			while( !work.isDone() );
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		IFile areaClass = getAreaFile(areaName);
		fileReplaceAll(areaClass, 
							"[ \\t]*addProcess\\W*\\(\\W*" + Pattern.quote(getClassName(areaName)+"_"+activityName) + ".*\\)[ \\t]*;[ \\t]*\\n",
							"");		
		return true;		
	}
	
	public boolean setActivityName(String aName, String actOldName, String actNewName, String lenguage){
		String actOldClassName = getClassName(aName) + "_" + getClassName(actOldName);
		String actNewClassName = getClassName(aName) + "_" + getClassName(actNewName);	
		IFile file;
		//Change Activity sourceCode and name
		try {
			file = getActivityFile(aName, actOldName);
			if ( file.exists() ){
				fileReplaceAll(file, 
						"\\W" + Pattern.quote( getClassName(actOldClassName) ) + "\\W",
						Pattern.quote( getClassName(actOldClassName) ), 
						actNewClassName );
				work = new MyProgressMonitor();
				file.move( getPackage(aName).getFile(actNewClassName + "." + file.getFileExtension()).getFullPath() , true, work);
				while( !work.isDone() );
			}
			
		} catch ( CoreException e) {
			e.printStackTrace();
			return false;
		}
		
		file = getAreaFile(aName);
		fileReplaceAll(file, 
							"\\W" + Pattern.quote( actOldClassName ) + "\\W",
							Pattern.quote( actOldClassName ),
							actNewClassName );		
		return true;			
	}
	
	
	
	public boolean setActivityType(String areaName, String activityName, int type) {
		
		IFile area = getAreaFile( areaName );
		String actClassName = getClassName(areaName) + "_" + getClassName(activityName);
		
		if ( area.exists() ){
			
			//Add , to segment elements if not exist
			fileReplaceAll( area ,
					"[ \\t]*addProcess\\(\\W*" + Pattern.quote(actClassName) + ".class[ \\t]*,*",
					"\t\taddProcess(" + actClassName + ".class, ");
			fileReplaceAll( area ,
					"addProcess\\(\"" + Pattern.quote(actClassName) + "\"[ \\t]*,*",
					"\t\taddProcess(\"" + actClassName + "\", ");
			
			//Delete previos configuration if exist
			fileReplaceAll( area , 
					"[ \\t]*addProcess\\(\\W*\"?"+ Pattern.quote(actClassName) +"\"?(\\.class)?[,\\w .\\|\\n\\t]*\\)\\W*;[ \\t]*\\n",
					"ActConf.TYPE_\\w*",
					"");
			
			//Add new configuration
			String typeString = "";
			if ( type == Constants.TYPE_SINGLETON )
				typeString = "SINGLETON";
			else if ( type == Constants.TYPE_PARALLEL )
				typeString = "PARALLEL";
			fileReplaceAll( area ,
					"[ \\t]*addProcess\\(\\W*\"?"+ Pattern.quote(actClassName) +"\"?(\\.class)?[,\\w .\\|\\n\\t]*\\)\\W*;[ \\t]*\\n",
					", ",
					", ActConf.TYPE_" + typeString);
			
			//Insert "|" between arguments if needed
			fileReplaceAll( area ,
					"[ \\t]*addProcess\\(\\W*\"?"+ Pattern.quote(actClassName) +"\"?(\\.class)?[,\\w .\\|\\n\\t]*\\)\\W*;[ \\t]*\\n",
					"[\\| \n\t]*ActConf",
					" | ActConf");
			
			//Delete the "|" inserted after the ","
			fileReplaceAll( area ,
					"[ \\t]*addProcess\\(\\W*\"?"+ Pattern.quote(actClassName) +"\"?(\\.class)?[,\\w .\\|\\n\\t]*\\)\\W*;[ \\t]*\\n",
					", \\| ",
					", ");
		}
		return true;
	}
	
	public boolean setActivityLanguage(String areaName, String processName, String oldLanguage, String newLanguage) {
		/*
		ICompilationUnit area = getClass(areaName, getClassName(areaName) );
		String actClassName = getClassName(areaName) + "_" + getClassName(processName);
		
		
		//Get the file
		String filePath = getProcessFileName( getClassName(areaName), getClassName(processName) , oldLanguage);
		IFile processFile = src.getFolder(areaName).getFile( filePath );
		
		//Get file contents
		String fileCode = readAllContente(processFile);
		
		//Default code of the smallNode
		String code = KMiddleCodeFactory.newProcess("",areaName, processName, oldLanguage);
		
		//Compairs if the file has the default code
		if ( !fileCode.equals(code)) {
			System.out.println("Error, " + filePath  + " file has been modified");
			return false;
		}
		
		//get new default code
		code = KMiddleCodeFactory.newProcess("",areaName, processName, newLanguage);
		
		
		
		
		
		
		
		if ( area.getResource().getType() == IResource.FILE ){
			//Add , to segment elements if not exist
			fileReplaceAll((IFile)area.getResource(),
					"[ \\t]*addProcess\\(\\W*" + Pattern.quote(actClassName) + ".class[ \\t]*,*",
					"\t\taddProcess(" + actClassName + ".class, ");
			fileReplaceAll((IFile)area.getResource(),
					"addProcess(\"" + Pattern.quote(actClassName) + "\"[ \\t]*,*",
					"\t\taddProcess(\"" + actClassName + "\", ");
			
			//Delete previos configuration if exist
			fileReplaceInLine((IFile)area.getResource(),
					"[ \\t]*addProcess\\(\\W*\"?"+ Pattern.quote(actClassName) +"\"?(\\.class)?[,\\w .|\\n\\t]*\\)\\W*;[ \\t]*\\n",
					"ActConf.TYPE_\\w*",
					"");
			
			//Insert "|" between arguments if needed
			fileReplaceInLine((IFile)area.getResource(),
					"[ \\t]*addProcess\\(\\W*\"?"+ Pattern.quote(actClassName) +"\"?(\\.class)?[,\\w .|\\n\\t]*\\)\\W*;[ \\t]*\\n",
					"[| \n\t]*ActConf",
					" | ActConf");
			
			//Delete the "|" inserted after the ","
			fileReplaceInLine((IFile)area.getResource(),
					"[ \\t]*addProcess\\(\\W*\"?"+ Pattern.quote(actClassName) +"\"?(\\.class)?[,\\w .|\\n\\t]*\\)\\W*;[ \\t]*\\n",
					", | ",
					", ");
		}
		return true;
		
		areaName = areaName.replace(" ", "");
		processName = processName.replace(" ", "");		
		
		//Get the file
		String filePath = getProcessFileName(areaName, processName , oldLanguage);
		IFile processFile = src.getFolder(areaName).getFile( filePath );
		
		//Get file contents
		String fileCode = readAllContente(processFile);
		
		//Default code of the smallNode
		String code = KMiddleCodeFactory.newProcess("",areaName, processName, oldLanguage);
		
		//Compairs if the file has the default code
		if ( !fileCode.equals(code)) {
			System.out.println("Error, " + filePath  + " file has been modified");
			return false;
		}
		
		//get new default code
		code = KMiddleCodeFactory.newProcess("",areaName, processName, newLanguage);
		try {
			//Delete old file
			processFile.delete(true, null);
			
			//Creates the new file
			filePath = getProcessFileName(areaName, processName , newLanguage);
			InputStream contentInputS =  new ByteArrayInputStream(code.getBytes());
			processFile = src.getFolder(areaName).getFile( filePath );
			work = new MyProgressMonitor();
			processFile.create(contentInputS, true, work );
			while( !work.isDone() );
			
			 Modifies the addNodeType in Area 
			fileReplaceAll(src.getFolder(areaName).getFile(areaName + ".java"),
					Pattern.quote(KMiddleCodeFactory.addNodeType(areaName, processName, oldLanguage, type)),
					KMiddleCodeFactory.addNodeType(areaName, processName, newLanguage, type));
			
			//Add import in Area
			fileReplaceAll(src.getFolder(areaName).getFile(areaName + ".java"),
					"import kmiddle.nodes.Area.Area;",
					"import kmiddle.nodes.Area.Area;\nimport kmiddle.utils.Languages;");
			
			//Delete duplicates if needed
			fileReplaceAll(src.getFolder(areaName).getFile(areaName + ".java"),
					"import kmiddle.utils.Languages;\nimport kmiddle.utils.Languages;",
					"import kmiddle.utils.Languages;");
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		*/
		return false;
	}
	
	
	public boolean setCognitiveFunctionInput(String aName, String cfName,
			String filter) {
		// Lo que venga en la string filter se pondra en un if y adentr del if
		// se enviara al tipo de nodo
		/**
		 * perndiente
		 */
		return true;
	}

	public boolean setCognitiveFunctionInput(String aName, String cfName,
			String filter, String Index) {
		// Lo que venga en la string filter se pondra en un if y adentr del if
		// se enviara al tipo de nodo
		// La string index es una operacion que da como resulta el indice de el
		// mapped node
		return true;
	}

	public boolean setCognitiveFunctionOutput(String aName, String cfName,
			String areaTarget) {
		// se agrega un sendMessagte al final del codigo de la funcion cognitiva
		return true;
	}

	public boolean setCognitiveFunctionIndexList(String aName, String cfName,
			String IndexList) {
		// Se agrega en el Area, los create Node de esta funcion cognitiva
		return true;
	}
	
}