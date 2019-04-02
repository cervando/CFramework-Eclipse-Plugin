package CPlugin.contributions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.framework.Bundle;


@SuppressWarnings("restriction")
public class PackageExplorerContributions extends AbstractHandler{

	public Object execute(ExecutionEvent event) throws ExecutionException {
	    
		//Obtiene el nombre del proyecto
	    String name = getProjectNameFromEvent(event);
	    
	    //Validar que sea halla seleccionado un proyecto
	    if ( name == null){
			Shell shell = HandlerUtil.getActiveShell(event);
			MessageDialog.openInformation(shell, "Info","Please select a Project");
			return null;
	    }
	    
	    //Llamar a la vista de diagramas y pasarla en nombre del proyecto como argumento
	    try {
	    	
	    	//Get the diagram description file
	    	IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    	IWorkspaceRoot root = workspace.getRoot();
	    	IProject project  = root.getProject(name);
	    	
	    	IFolder libFolder = project.getFolder("libraries");
	    	IFolder logFolder = project.getFolder("log");
	    	IFile file = project.getFile("diagram.cua");
	    	
	    	try {
		    	if ( !libFolder.exists() ) libFolder.create(IResource.NONE, true, null);
		    	if ( !logFolder.exists() ) logFolder.create(IResource.NONE, true, null);
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	
	    	//Create descriptor file if not exists
	    	if ( !file.exists() ) {
	    		addDescriptor(file);
	    	}
	    	
	    	//Add the middleware library
	    	IJavaProject javaP = JavaCore.create(project);
	    	if (  javaP != null ){
	    		InputStream lib = getMiddlewareJarFile();
	    		//addLibrary(javaP, "kmiddle.jar", libFolder, lib);
	    	}
	    	
	    	
	    	//Check if the config/AreaNames File belongs to a diferent version, and modified
	    	//KMiddleProject kmp = new KMiddleProject(name);
	    	//kmp.updateAreaNames();
	    	
	    	
	    	
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			page.openEditor(new FileEditorInput(file), "CPlugin.GraphEditor");
			
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return null;
	}
	
	private String getProjectNameFromEvent(ExecutionEvent event){
		
	    ISelection sel = HandlerUtil.getActiveMenuSelection(event);
	    IStructuredSelection selection = (IStructuredSelection) sel;
	    
	    Object firstElement = selection.getFirstElement();
	    if (firstElement instanceof JavaProject)
		      return  ((JavaProject)firstElement).getProject().getName();
		return null;
	}
	
	private void addDescriptor(IFile file){
		byte[] bytes = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><CModel></CModel>".getBytes();
	    InputStream source = new ByteArrayInputStream(bytes);
	    try{
	    	file.create(source, IResource.NONE, null);
	    }catch( Exception e){
	    	System.out.println("Error creating diagram file");
	    	System.out.println(e);
	    }
	}
	
	
	private void addLibrary(IJavaProject javaP, String newLibraryName, IFolder libFolder, InputStream libStream){
		// ADD library file
		IFile libFile = libFolder.getFile(newLibraryName);
		try {
			if ( libFile.exists() ) 
				return;
			libFile.create(libStream, true, null);
			
		} catch (Exception e) {
			System.out.println("Error creating library file");
		}
		
		
		// ADD library to ClassPath 
		Set<IClasspathEntry> entries = new HashSet<IClasspathEntry>();
    	try {
    		entries.addAll(Arrays.asList(javaP.getRawClasspath()));
    		entries.add(JavaCore.newLibraryEntry(libFile.getFullPath(), null, null));
    		javaP.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
		} catch (Exception e) {
			System.out.println(this.getClass() + "::error reading classpath" );
		}    	
	}
	
	
	private InputStream getMiddlewareJarFile(){
		Bundle bundle = Platform.getBundle("CPlugin");
		URL fileURL = bundle.getEntry("files/kmiddle.jar");
		
		try {
			return fileURL.openConnection().getInputStream();
			
		} catch (Exception e) {
			System.out.println(this.getClass() + "Error reading bytes from library");
		}
		return null;
	}
}
