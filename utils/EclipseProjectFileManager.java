package CPlugin.utils;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.ResourcesPlugin;

public class EclipseProjectFileManager {
	
	protected String getFullPath(String project){
		String workspacePath =  ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		return  workspacePath + "/" + project;
	}
	/**
	 * Regresa un arreglo con path completo de todos los archivos Java que contiene el proyecto
	 * @param project Nombre del proyecto
	 * @return	Arreglo con los PATH, en caso de no existir archivos Java, enviara un arreglo vacio 
	 */
	public String[] getJavaFilesNames(String project){
		String workspacePath =  ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		String projectPath =  workspacePath + "/" + project;
		
		File sourceFolder = new File(projectPath + "/" + "src");
		
		if (sourceFolder.exists()){
			ArrayList<String> filesPath  = new ArrayList<>();
			getJavaFilesNamesFromFolder(sourceFolder, filesPath);
			String ret[] = new String[filesPath.size()];
			for ( int i = 0; i < filesPath.size(); i++){
				ret[i] = filesPath.get(i);
			}
			return ret;
		}else{
			return new String[0];
		}
	}
	
	private void getJavaFilesNamesFromFolder(File folder, ArrayList<String> filesPath){
		File[] folderFiles = folder.listFiles();
		for(int i =  0; i < folderFiles.length; i++){
			if ( folderFiles[i].isDirectory()){
				getJavaFilesNamesFromFolder(folderFiles[i], filesPath);
			}else{
				if ( folderFiles[i].getName().endsWith(".java") ){
					System.out.println(folderFiles[i].getAbsolutePath());
					filesPath.add(folderFiles[i].getAbsolutePath());
				}
			}
		}
	}
	
	/*
	private String getClassNameFromFile(String path){
		if(path.endsWith(".java")){
			return path.substring(path.lastIndexOf("/"), path.length() - ".java".length());
		}
		return "";
	}
	*/
}
