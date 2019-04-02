package CPlugin.utils;

import org.eclipse.core.resources.IProject;

import CPlugin.model.Activity;
import CPlugin.model.Area;
import CPlugin.model.Graph;
import CPlugin.parser.NamesManager;
import CPlugin.parser.DescriptorManager;
import CPlugin.parser.InitManager;
import CPlugin.parser.SourceCodeManager;


/**
 * This class is in charge of create the java code of the interface and invoke the descriptor modifier
 * @author armcinvestav
 *
 */
public class KMiddleProject extends EclipseProjectFileManager {
	
	//private String projectName;
	private DescriptorManager descriptorManager;
	private NamesManager areaNamesManager;
	private InitManager initManager;
	private SourceCodeManager sourceCodeManager;
	//String fullPath;
	
	
	public KMiddleProject(IProject project){
		
		descriptorManager = new DescriptorManager(project);
		areaNamesManager = new NamesManager(project);
		sourceCodeManager = new SourceCodeManager(project);
		initManager = new InitManager(project);
	}
	
	
	
	public Graph getGraph(){
		return descriptorManager.getGraph();
	}
	
	public boolean createAreaNamePython(){
		return false;//sourceCodeManager.createAreaNamePython();
	}
	
	
	private String validateClassName(String in){
		return in.substring(0,1).toUpperCase() + in.substring(1);
	}
	
	
	
	public boolean addArea(String groupName, Area area, String areaName){
		
		String areaN = areaName;
		if ( descriptorManager.addArea(groupName, areaN) ){
			if ( areaNamesManager.addArea(areaN)){
				if ( initManager.addArea(areaN) ){
					if ( sourceCodeManager.addArea( areaN) ){
						return true;
					}else
						System.out.println("Error al generar el codigo");
				}
			}
		}
		return false;
	}
	
	
	public boolean setAreaName(String oldAreaName, String newAreaName, boolean isBlackBox){
		if (  descriptorManager.setAreaName(oldAreaName, newAreaName) ){
			if ( areaNamesManager.setNameArea(oldAreaName,newAreaName)){
				if ( !isBlackBox ) {
					if ( initManager.setNameArea(oldAreaName,newAreaName) ){
						if ( sourceCodeManager.setAreaName(oldAreaName, newAreaName)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	
	public boolean setAreaBlackbox(String areaName, boolean isBlackBox){
		if (  descriptorManager.setAreaBlackBox(areaName, isBlackBox) ){
			if ( isBlackBox) {
				if ( initManager.deleteArea( areaName ) ){
					if ( sourceCodeManager.deleteArea( areaName ) ){
						return true;
					}
				}
				
			}else {
				if ( initManager.addArea(areaName) )
					if ( sourceCodeManager.addArea( areaName) )
						return true;
				
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param areaName
	 * @return
	 */
	public boolean deleteArea(String areaName, boolean isblackBox){
		if ( descriptorManager.deleteArea(areaName) ){
			if ( areaNamesManager.deleteArea(areaName)){
				if ( !isblackBox) {
					if ( initManager.deleteArea(areaName) ){
						if ( sourceCodeManager.deleteArea(areaName)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/*------------------- Start activity section ------------*/
	
	/**
	 * 
	 * @param aName
	 * @param p
	 * @param processName
	 * @return
	 */
	public boolean addActivity(String aName, Activity p, String processName){
		
		String name = processName;
		if ( descriptorManager.addProcess(aName, name) ){
			if ( areaNamesManager.addProcess(aName, name)){
				return sourceCodeManager.addActivity(aName, name);
			}
		}
		return false;
	}
	
	
	
	public boolean setActivityName(String aName, String cfOldName, String cfNewName, String lenguage ){
		if ( descriptorManager.setProcessName(aName, cfOldName, cfNewName) )
			if ( areaNamesManager.setNameActivity(aName, aName, cfOldName, cfNewName) ) {
				return sourceCodeManager.setActivityName(aName, cfOldName, cfNewName, lenguage);
			}
		return false;
	}
	
	
	/**
	 * Funtion to delete a cognitive function
	 * @param aName Name of the area
	 * @param cfName Name of the cognitive function to delete
	 * @return
	 */
	public boolean deleteActivity(String aName, String cfName, String lenguage){
		if ( descriptorManager.deleteProccess(aName, cfName) ){
			if ( areaNamesManager.deleteActivity(aName, cfName)){
				return sourceCodeManager.deleteProcess(aName, cfName,lenguage);
			}
		}
		return false;
	}

	
	/**
	 * Change the lenguage of the Activity
	 * @param areaName
	 * @param activityName
	 * @param oldLanguage
	 * @param newLanguage
	 * @param type
	 * @return
	 */
	public boolean setActivityLanguage(String areaName, String activityName, String oldLanguage, String newLanguage ){
		if ( sourceCodeManager.setActivityLanguage(areaName, activityName, oldLanguage, newLanguage ) )
			return descriptorManager.setProcessLanguage(areaName, activityName, newLanguage);
		return false;
	}
	
	
	/**
	 * 
	 * @param areaName
	 * @param activityName
	 * @param type
	 * @param lenguage
	 * @return
	 */
	public boolean setActivityType(String areaName, String activityName, int type){
		areaName = validateClassName(areaName);
		activityName = validateClassName(activityName);
		if( descriptorManager.setProcessType(areaName, activityName, type ) ){
			return sourceCodeManager.setActivityType(areaName, activityName, type);
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	public boolean setAreaXY(String areaName, int x, int y, int width, int height ){
		return descriptorManager.setAreaXY(areaName, x, y, width, height);
	}
	
	
	public boolean addGroup(String groupName){
		return descriptorManager.addGroup(groupName);
	}
	
	public boolean setGroupName(String oldGroupName, String newGroupName){
		return descriptorManager.setGroupName(oldGroupName, newGroupName);
	}
	
	public boolean deleteGroup(String groupName){
		return descriptorManager.deleteGroup(groupName);
	}
	
	public boolean addConnection(String sourceName, String targetName){
		return descriptorManager.addConnection(sourceName, targetName);
	}
	
	public boolean deleteConection(String aNameSource, String aNameTarget){
		return descriptorManager.deleteConection(aNameSource, aNameTarget);
	}
	
	
	
	/*
	public boolean setCognitiveFunctionInputID(String aName, String cfName, String ID){
		aName = validateClassName(aName);
		cfName = validateClassName(cfName);
		return descriptorManager.setCognitiveFunctionInputID(aName, cfName, ID);
	}
	
	public boolean setCognitiveFunctionOutputID(String aName, String cfName, String ID){
		aName = validateClassName(aName);
		cfName = validateClassName(cfName);
		return descriptorManager.setCognitiveFunctionOutputID(aName, cfName, ID);
	}
	
	
	
	public boolean setCognitiveFunctionSubfunction(String aName, String cfName, boolean Subfunction ){
		aName = validateClassName(aName);
		cfName = validateClassName(cfName);
		return descriptorManager.setCognitiveFunctionSubfunction(aName, cfName, Subfunction);
	}
	
	public boolean setCognitiveFunctionInput(String aName, String cfName, String filter){
		aName = validateClassName(aName);
		cfName = validateClassName(cfName);
		if ( descriptorManager.setCognitiveFunctionInput(aName, cfName, filter) )
			return sourceCodeManager.setCognitiveFunctionInput(aName, cfName, filter);
		return false;
	}
	
	public boolean setCognitiveFunctionInput(String aName, String cfName, String filter, String Index){
		aName = validateClassName(aName);
		cfName = validateClassName(cfName);
		return descriptorManager.setCognitiveFunctionInput(aName, cfName, filter, Index);
	}
	
	
	public boolean setCognitiveFunctionProccess(String aName, String cfName, String fullPath){
		aName = validateClassName(aName);
		cfName = validateClassName(cfName);
		return descriptorManager.setCognitiveFunctionProccess(aName, cfName, fullPath);
	}
	
	
	
	public boolean setCognitiveFunctionIndexList(String aName, String cfName, String IndexList){
		aName = validateClassName(aName);
		cfName = validateClassName(cfName);
		return descriptorManager.setCognitiveFunctionIndexList(aName, cfName, IndexList);
	}*/
	
	
	public void updateAreaNames(){
		//sourceCodeManager.updateAreaNamesVersion();
	}
	
}