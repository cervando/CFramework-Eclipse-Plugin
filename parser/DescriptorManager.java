package kmiddlePlugin.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import kmiddlePlugin.model.Area;
import kmiddlePlugin.model.Connection;
import kmiddlePlugin.model.Graph;
import kmiddlePlugin.model.Activity;
import kmiddlePlugin.utils.Constants;
public class DescriptorManager {

	private IFile descriptorIfile;
	private Document doc;
	
	public DescriptorManager(IProject project){
		
    	descriptorIfile = project.getFile("diagram.kua");
    	if ( descriptorIfile.exists() ){
    		loadProject( descriptorIfile.getLocation().toFile() );
    	}else{
    		System.out.println(this.getClass() + ":: diagram file dont exist");
    	}
		
		
	}
	
	
	/**
	 * Loads the Kmiddle description file
	 */
	private void loadProject(File file){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			if ( file.exists()){
				prepairFile(file);
				doc = builder.parse(file);
				
				
			}else{
				doc = builder.newDocument();
				doc.appendChild(doc.createElement("Kproject"));
				saveDoc();
			}
		}catch (Exception e ){
			System.out.println("Error loading KMeta");
		} 
	}
	
	
	/**
	 * Prepair the file (Delete tabs and car return from the XML)
	 * @param f
	 */
	private void prepairFile(File f){
		Path file = Paths.get(f.getAbsolutePath());
		Charset charset = StandardCharsets.UTF_8;
		
		try {
			String content = new String(Files.readAllBytes(file), charset);
			content = content.replaceAll("\t","");
			content = content.replaceAll("\n","");
			Files.write(file, content.getBytes(charset));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error prepairen XML file");
		}
		
	}
	
	
	
	
	/**
	 * Adds an Area to description file
	 * @param groupName Group name, where the area belongs
	 * @param areaName Area name
	 * @return Returns true if sucess, false if not
	 */
	public boolean addArea(String groupName, String areaName){
		Element group;		
		
		//Gets the groups node, groupName must not be null or ""
		if ( groupName != null && !groupName.equals("")){
			
			group = searchNode(Constants.TagGroup,Constants.TagGroup_name , groupName);
			//if the group doesnt exist it create it
			if ( group == null ){
				
				group =  doc.createElement(Constants.TagGroup);
				group.setAttribute(Constants.TagGroup_name, groupName);
				doc.getFirstChild().appendChild(group);
				System.out.println("Grupo agregado: " + groupName);
			}
			
		//If the area is not in a group it is added to the root node
		}else{
			group = (org.w3c.dom.Element)doc.getFirstChild();
		}
		
		if ( searchNode(Constants.TagArea, Constants.TagArea_name, areaName) != null ){
			//System.out.println("ya existe un elemento con ese nombre");
			return false;
		}
		
		Element e = doc.createElement(Constants.TagArea);
		e.setAttribute(Constants.TagArea_name, areaName);
		e.appendChild(doc.createElement(Constants.TagArea__outputs));
		group.appendChild(e);
		saveDoc();
		return true;
	}
	
	
	
	/**
	 * Delete an area
	 * @param areaName Area Name
	 * @return true if success
	 */
	public boolean deleteArea(String areaName){
		Element area = searchNode(Constants.TagArea, Constants.TagArea_name, areaName);
		if ( area != null ){
			Element outputToMe = null;
			
			//Delete all the connections to this node
			while ( (outputToMe = searchNode(Constants.TagArea__outputs__output, Constants.TagArea__outputs__output_area, areaName)) != null ){
				outputToMe.getParentNode().removeChild(outputToMe);
				outputToMe = null;
			}
			
			area.getParentNode().removeChild(area);
			saveDoc();
			return true;
		}else{
			System.out.println("Area no existente");
			return false;
		}
	}
	
	
	/**
	 * Rename an area
	 * @param oldAreaName	Old Area name
	 * @param newAreaName	New Area name
	 * @return true if success
	 */
	public boolean setAreaName(String oldAreaName, String newAreaName){
		System.out.println("**************************");
		System.out.println("DescriptorManager::setAreaName");
		System.out.println("Old Name: " + oldAreaName + " \n New Name: " + newAreaName);
		Element Area = searchNode(Constants.TagArea, Constants.TagArea_name, oldAreaName);
		if ( Area != null ){
			Element outputToMe = null;
			while ( (outputToMe = searchNode(Constants.TagArea__outputs__output, Constants.TagArea__outputs__output_area, oldAreaName)) != null ){
				outputToMe.setAttribute(Constants.TagArea__outputs__output_area, newAreaName);
				outputToMe = null;
			}
			
			
			Area.setAttribute(Constants.TagArea_name, newAreaName);			
			System.out.println("Area Renombrada");
			saveDoc();
			return true;
		}else{
			System.out.println("Area no existente");
			return false;
		}
	}
	
	
	
	
	
	
	
	
	public boolean setAreaXY(String areaName, int x, int y, int width, int height ){
		System.out.println("Modificando XY de Area " + areaName);
		Element area = searchNode(Constants.TagArea, Constants.TagArea_name, areaName);
		if ( area != null ){
			
			area.setAttribute(Constants.TagArea_X, String.valueOf(x) );
			area.setAttribute(Constants.TagArea_Y, String.valueOf(y) );
			area.setAttribute(Constants.TagArea_width, String.valueOf(width) );
			area.setAttribute(Constants.TagArea_height, String.valueOf(height) );
			
			System.out.println("Area reubicada");
			saveDoc();
			return true;
		}else{
			System.out.println("Area no existente");
			return false;
		}
	}
	
	
	
	
	
	public boolean setAreaBlackBox(String areaName, boolean isBlackBox){
		System.out.println("Modificando Blackbox de Area " + areaName);
		Element area = searchNode(Constants.TagArea, Constants.TagArea_name, areaName);
		if ( area != null ){
			
			area.setAttribute(Constants.TagArea_blackBox, String.valueOf(isBlackBox) );
			saveDoc();
			return true;
		}else{
			System.out.println("Area no existente");
			return false;
		}
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * @param groupName
	 * @return
	 */
	public boolean addGroup(String groupName){
		System.out.println("Creando grupo: " + groupName);
		if ( searchNode(Constants.TagGroup,Constants.TagGroup_name , groupName) != null){
			System.out.println("Grupo ya existente");
			return false;
		}
		Element group =  doc.createElement(Constants.TagGroup);
		group.setAttribute(Constants.TagGroup_name, groupName);
		doc.getFirstChild().appendChild(group);
		saveDoc();
		System.out.println("Grupo agregado: " + groupName);
		return true;
	}
	
	
	
	
	/**
	 * 
	 * @param groupName
	 * @return
	 */
	public boolean deleteGroup(String groupName){
		System.out.println("Eliminando area" + groupName);
		Element group = searchNode(Constants.TagGroup, Constants.TagGroup_name, groupName);
		if ( group != null ){
			NodeList childs = group.getChildNodes();
			for ( int i = 0; i < childs.getLength(); i++){
				group.getParentNode().appendChild(childs.item(i).cloneNode(false));
			}
			
			group.getParentNode().removeChild(group);
			
			System.out.println("grupo Eliminada");
			saveDoc();
			return true;
		}else{
			System.out.println("grupo no existente");
			return false;
		}
	}
	
	
	public boolean setGroupName(String oldGroupName, String newGroupName){
		System.out.println("Renombrar grupo de " + oldGroupName + " a " + newGroupName);
		Element group = searchNode(Constants.TagGroup, Constants.TagGroup_name, oldGroupName);
		if ( group != null ){
			group.setAttribute(Constants.TagGroup_name, newGroupName);			
			System.out.println("grupo Renombrado");
			saveDoc();
			return true;
		}else{
			System.out.println("grupo no existente");
			return false;
		}
	}
	
	
	
	
	public boolean addActivity(String aName, String cfName){
		System.out.println("Agregando funcion cognitiva: " + aName + "_"+ cfName);
		Element area = searchNode(Constants.TagArea, Constants.TagArea_name, aName);
		if ( area == null )
			return false;
		
		Element function = doc.createElement(Constants.TagProcess);
		function.setAttribute(Constants.TagProcess_name, cfName);
		area.appendChild(function);
		saveDoc();
		System.out.println("funcion cognitiva agregada a descriptor");
		return true;
	}
	
	public boolean deleteProccess(String aName, String cfName){
		System.out.println("Descriptor --- Eliminando funcion cognitiva: " + aName + "_"+ cfName);
		Element cf = searchCognitiveFunction(aName, cfName);
		if ( cf == null )
			return false;
		
		cf.getParentNode().removeChild(cf);
		
		saveDoc();
		System.out.println("funcion cognitiva eliminada de descriptor");
		return true;
	}
	
	
	
	public boolean setProcessName(String aName, String cfOldName, String cfNewName){
		Element cf = searchCognitiveFunction(aName, cfOldName);
		if ( searchCognitiveFunction(aName, cfNewName) == null){
			cf.setAttribute(Constants.TagProcess_name, cfNewName);
			saveDoc();
			return true;
		}
		return false;
	}
	
	public boolean setProcessLanguage(String aName, String pName, String language){
		Element cf = searchCognitiveFunction(aName, pName);
		cf.setAttribute(Constants.TagProcess_language, language);
		saveDoc();
		return true;
	}
	
	
	
	public boolean setCognitiveFunctionInputID(String aName, String cfName, String ID){
		Element cf = searchCognitiveFunction(aName, cfName);
		NodeList cfChild = cf.getChildNodes();
		Element input = null;
		for ( int i = 0; i < cfChild.getLength(); i++ ){
			if ( ((Element)cfChild.item(i)).getTagName().equals(Constants.TagCognitiveFunction__Input)){
				input = (Element) cfChild.item(i);
				break;
			}
		}
		
		if ( input != null ){
			input.setAttribute(Constants.ID, ID);
			saveDoc();
			return true;
		}
		return false;
	}
	
	public boolean setCognitiveFunctionOutputID(String aName, String cfName, String ID){
		Element cf = searchCognitiveFunction(aName, cfName);
		NodeList cfChild = cf.getChildNodes();
		Element output = null;
		for ( int i = 0; i < cfChild.getLength(); i++ ){
			if ( ((Element)cfChild.item(i)).getTagName().equals(Constants.TagCognitiveFunction__Output) ){
				output = (Element) cfChild.item(i);
				break;
			}
		}
		
		if ( output != null ){
			output.setAttribute(Constants.ID, ID);
			saveDoc();
			return true;
		}
		return false;
	}
	
	
	public boolean setProcessType(String aName, String cfName, int type){
		Element cf = searchCognitiveFunction(aName, cfName);
		cf.setAttribute(Constants.TagProcess_type, String.valueOf( type ) );
		saveDoc();
		return true;
	}
	
	public boolean setCognitiveFunctionSubfunction(String aName, String cfName, boolean Subfunction ){
		Element cf = searchCognitiveFunction(aName, cfName);
		String value = (Subfunction)?"true":"false";
		cf.setAttribute(Constants.TagCognitiveFunction_subFunction, value);
		saveDoc();
		return true;
	}
	
	
	public boolean setCognitiveFunctionInput(String aName, String cfName, String filter){
		Element cf = searchCognitiveFunction(aName, cfName);
		NodeList cfChild = cf.getChildNodes();
		Element input = null;
		for ( int i = 0; i < cfChild.getLength(); i++ ){
			if ( ((Element)cfChild.item(i)).getTagName().equals(Constants.TagCognitiveFunction__Input)){
				input = (Element) cfChild.item(i);
				break;
			}
		}
		
		if ( input == null ){
			input = doc.createElement(Constants.TagCognitiveFunction__Input);
			cf.appendChild(input);
		}
		input.setAttribute(Constants.TagCognitiveFunction__Input_filter, filter);
		saveDoc();
		return true;
	}
	
	
	public boolean setCognitiveFunctionInput(String aName, String cfName, String filter, String Index){
		setCognitiveFunctionInput(aName, cfName, filter);
		return true;
	}
	
	
	public boolean setCognitiveFunctionProccess(String aName, String cfName, String fullPath){
		//Element cf = searchCognitiveFunction(aName, cfName);
		//cf.setAttribute(LenguageTags.TagCognitiveFunction__Proccess, fullPath);
		saveDoc();
		return true;
	}
	
	public boolean addConnection(String sourceName,  String areaTarget){
		Element sourceArea = searchNode(Constants.TagArea, Constants.TagArea_name, sourceName);
		Element outputs = (Element) sourceArea.getFirstChild();
		
		if ( outputs == null )
			return false;
		NodeList outputsChild = outputs.getChildNodes();
		Element output = null;
		for ( int i = 0; i < outputsChild.getLength(); i++ ){
			output = (Element)outputsChild.item(i); 
			if ( output.getAttribute(Constants.TagArea__outputs__output_area).equals(areaTarget))
				return false;
		}
		
		
		output = doc.createElement(Constants.TagArea__outputs__output);
		output.setAttribute(Constants.TagArea__outputs__output_area, areaTarget);
		outputs.appendChild(output);
		saveDoc();
		return true;
	}
	
	public boolean deleteConection(String aName, String areaTarget){
		Element area = searchNode(Constants.TagArea, Constants.TagArea_name, aName);
		Element outputs = (Element) area.getFirstChild();
		
		if ( outputs == null )
			return false;
		NodeList outputsChild = outputs.getChildNodes();
		Element output = null;
		for ( int i = 0; i < outputsChild.getLength(); i++ ){
			output = (Element)outputsChild.item(i); 
			if ( output.getAttribute(Constants.TagArea__outputs__output_area).equals(areaTarget)){
				outputs.removeChild(output);
				saveDoc();
				return true;
			}
		}
		return false;
	}
	
	public boolean setCognitiveFunctionIndexList(String aName, String cfName, String IndexList){
		return true;
	}
	
	
	
	/**
	 * 
	 * @param tag
	 * @param attr
	 * @param attrValue
	 * @return
	 */
	private Element searchNode(String tag, String attr, String attrValue){
		NodeList nodesTagged = doc.getElementsByTagName(tag);
		
		//si existen areas
		if ( nodesTagged.getLength() != 0){
		
			for (int i = 0 ; i < nodesTagged.getLength(); i++){
				if( ((Element)nodesTagged.item(i)).getAttribute(attr).equals(attrValue) ){
					return (Element)nodesTagged.item(i);
				}
			}
			
		}
		return null;
		
	}
	
	
	private Element searchCognitiveFunction(String areaName, String cognitiveFunctionName){
		
		
		Element area = searchNode(Constants.TagArea, Constants.TagArea_name, areaName);
		if (area == null )
			return null;
		
		NodeList functions = area.getChildNodes();
		
		//si existen areas
		if ( functions.getLength() != 0){
		
			for (int i = 0 ; i < functions.getLength(); i++){
				if( ((Element)functions.item(i)).getAttribute(Constants.TagProcess_name).equals(cognitiveFunctionName) ){
					return (Element)functions.item(i);
				}
			}
			
		}
		return null;	
	}
	
	
	
	public Graph getGraph(){
		Graph result = new Graph();
		
		NodeList nodesTagged = doc.getElementsByTagName(Constants.TagArea);
		Area area;
		
		//Add Areas
		for ( int i = 0; i < nodesTagged.getLength(); i++){
			area = getArea((Element) nodesTagged.item(i));
			if ( area != null ){
				result.addNode(area);
			}
		}
		//Add Connections
		List<Area> areas = result.getNodes();
		
		for (int i = 0; i < areas.size(); i++ ){
			//this is the source area
			area = areas.get(i);
			//Get the outputs of this area
			nodesTagged = searchNode(Constants.TagArea, Constants.TagArea_name, area.getName()).getFirstChild().getChildNodes();
			for ( int c = 0; c < nodesTagged.getLength(); c++){
				Area target = null;
				String targetName = ((Element) nodesTagged.item(c)).getAttribute(Constants.TagArea__outputs__output_area);
				for (int searchTarget = 0; searchTarget < areas.size(); searchTarget++ ){
					if ( areas.get(searchTarget).getName().equals(targetName)){
						target = areas.get(searchTarget);
						break;
					}
				}
				if ( target != null){
					Connection con = new Connection();
					con.setSource(area);
					con.setTarget(target);
				}
			}
		}
		
		
		
		
		
		
		
		return result;
	}
	
	public Area getArea(String areaName){
		Element area = (Element)searchNode(Constants.TagArea, Constants.TagArea_name, areaName);
		if ( area != null)
			return getArea(area);
		return null;
	}
	
	
	
	
	
	private Area getArea(Element elementArea){
		Area area = new Area();
		String areaName = elementArea.getAttribute(Constants.TagArea_name);
		area.setName(areaName);
		
		int x = Integer.parseInt(elementArea.getAttribute(Constants.TagArea_X));
		int y = Integer.parseInt(elementArea.getAttribute(Constants.TagArea_Y));
		
		//Set the size of the area
		int width = 65;
		int height = 35;
		String val = elementArea.getAttribute(Constants.TagArea_width);
		if (!val.equals(""))
			width = Integer.parseInt(val);
		
		val = elementArea.getAttribute(Constants.TagArea_height);
		if (!val.equals(""))
			height = Integer.parseInt(val);
		
		area.setConstraint(
				new Rectangle(new Point(x, y),
				new Dimension(width, height)));
		
		
		//Get process
		NodeList areaChilds = elementArea.getChildNodes();
		if ( areaChilds.getLength() != 0 ){
			
			for ( int cfIndex = 0; cfIndex < areaChilds.getLength(); cfIndex++ ){
				Element child = (Element)areaChilds.item(cfIndex);
				
				if (child.getTagName().equals(Constants.TagProcess)){
					
					Activity p = area.addProcess(child.getAttribute(Constants.TagProcess_name)); 
					p.setArea(area);
					
					//Set the lenguage of the process
					String language = child.getAttribute(Constants.TagProcess_language);
					if( language.equals("") )
						p.setLanguage(0);
					else
						p.setLanguage(language);
					
					String type = child.getAttribute(Constants.TagProcess_type);
					if ( type == "")
						type = "0";
					p.setType( Integer.parseInt(type) );
				}
			}
		}
		
		return area;
	}
	

	private void saveDoc(){
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Source xmlSource = new DOMSource(doc);
			Result outputTarget = new StreamResult(outputStream);
			TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
			InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
			descriptorIfile.setContents(is, true, false, null);
			
		}catch(Exception e){
			System.out.println("Error saving descriptor file");
			System.out.println(e.toString());
		}	
	}	
}
