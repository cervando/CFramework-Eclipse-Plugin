package kmiddlePlugin.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class Manager {

	protected IProject javaProject = null;
	protected IFolder src;
	protected IFolder config;
	MyProgressMonitor work;
	
	
	public Manager(IProject project) {
    	src = project.getFolder("src");
    	/*
    	try {
			if (project.isNatureEnabled("org.eclipse.jdt.core.javanature"))
			      javaProject = JavaCore.create(project);
		} catch (CoreException e1) {
			System.out.println("Not a JAVA project");
			e1.printStackTrace();
		}
		*/   	
    	try {
    		work = new MyProgressMonitor();
        	if (!src.exists()){
        		src.create(IResource.NONE, true, work);
        		while( !work.isDone() );
        	}
		} catch (Exception e) {
			System.out.println(this.getClass() + ":: Error creating config package");
			System.out.println(e.getMessage());
		}
    	
    	config = src.getFolder("config");
    	try {
    		work = new MyProgressMonitor();
        	if (!config.exists()){
        		config.create(IResource.NONE, true, work);
            	while( !work.isDone() );
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected IFolder getPackage(String name){
		String pack = getPackageName(name);
		return src.getFolder(pack);
	}
	
	protected IFile getAreaFile(String name){
		String pack = getPackageName(name);
		String cName = getClassName(name);
		return src.getFolder(pack).getFile(cName + ".java");
	}
	
	protected IFile getActivityFile(String areaName, String activityName){
		String pack = getPackageName(areaName);
		String cName = getClassName(areaName) + "_" + getClassName(activityName);
		try {
			IResource resources[] = getPackage(pack).members();
			for ( IResource res: resources )
				if( res.getType() == IResource.FILE )
					if ( res.getName().contains(cName) )
						return (IFile)res;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return src.getFolder(pack).getFile(cName);
	}
	
	protected String getPackageName(String area){
		return area.replace(" ", "").substring(0, 1).toLowerCase() + area.substring(1);
	}
	
	
	protected String getClassName(String areaName){
		return areaName.replace(" ", "").substring(0, 1).toUpperCase() + areaName.substring(1);
	}
	
	protected String readAllContente( IFile file ){
		StringBuilder fileCode = new StringBuilder("");
		String line = "";
		try {
			BufferedReader fileBr = new BufferedReader(new InputStreamReader(file.getContents()));
			while ((line = fileBr.readLine()) != null) {
				fileCode.append(line + "\n");
			}
			fileCode.deleteCharAt(fileCode.length()-1);
			fileBr.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(this.getClass() + "::readAllContente:: Error reading contents");
		}
		return fileCode.toString();
	}
	
	protected boolean fileReplaceAll(IFile file, String regex, String replacement){
		try {
			BufferedReader fileInput = new BufferedReader(new InputStreamReader(file.getContents()));
			
			String line;
			StringBuilder content = new StringBuilder();
			
			while ( (line = fileInput.readLine()) != null ) {
				content.append(line + "\n");
			}
			fileInput.close();
			
			line = content.toString().replaceAll(regex,replacement);
			
			InputStream contenInputStream = new ByteArrayInputStream(line.getBytes());
			work = new MyProgressMonitor();
			file.setContents(contenInputStream,true,true,work);
			while( !work.isDone() );
			contenInputStream.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	protected boolean fileReplaceAll(IFile file,String lineSearchRegex, String regex, String replacement){
		try {
			BufferedReader fileInput = new BufferedReader(new InputStreamReader(file.getContents()));
			
			String fullText;
			StringBuilder content = new StringBuilder();
			
			while ( (fullText = fileInput.readLine()) != null ) {
				content.append(fullText + "\n");
			}
			fileInput.close();
			
			fullText = content.toString();
		  	final Pattern pattern = Pattern.compile(lineSearchRegex + ".*");
			final Matcher matcher = pattern.matcher(fullText);
			String line = null;
			while ( matcher.find() ){
				line  = matcher.group();
				fullText = fullText.replace(line, line.replaceAll(regex, replacement));
			}
			InputStream contenInputStream = new ByteArrayInputStream(fullText.getBytes());
			work = new MyProgressMonitor();
			file.setContents(contenInputStream,true,true,work);
			while( !work.isDone() );
			contenInputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	class MyProgressMonitor implements IProgressMonitor{

		
		int minimum = 0;
		int work = 250;
		
		public void beginTask(String arg0, int MINIMUM_RESOLUTION) {
			// TODO Auto-generated method stub
			System.out.println("Empezo");
			System.out.println(MINIMUM_RESOLUTION);
			minimum = MINIMUM_RESOLUTION;
		}
		
		private boolean done = false;
		
		public void done() {
			System.out.println("done\n\n\n\n");
			done = true;
		}
		
		public boolean isDone(){
			return done;
		}

		public void internalWorked(double arg0) {

			System.out.println("internalwork");
		}

		public boolean isCanceled() {
			// TODO Auto-generated method stub

			System.out.println("iscancelled");
			return false;
		}

		public void setCanceled(boolean arg0) {
			// TODO Auto-generated method stub

			System.out.println("setcanceled");
		}

		public void setTaskName(String arg0) {
			// TODO Auto-generated method stub

			System.out.println("setName");
		}

		public void subTask(String arg0) {
			// TODO Auto-generated method stub

			System.out.println("subtask");
			done();
		}

		//Quick fix, sub monitor most be implemented
		public void worked(int worked) {
			// TODO Auto-generated method stub

			System.out.println("trabajo");
			work += worked;
			if ( work >= minimum )
				done();
		} 
		
	}
	
	
	
}