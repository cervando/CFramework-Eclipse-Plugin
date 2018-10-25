package kmiddlePlugin.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public class AreaNamesManager extends Manager {

	private IFile areaNamesFile;

	public AreaNamesManager(IProject project) {
    	super(project);
    	
		//Creates AreaNames file
		areaNamesFile = config.getFile("AreaNames.java");
		if ( !areaNamesFile.exists()){
			byte[] code = KMiddleCodeFactory.newAreaNames().getBytes();
			InputStream source = new ByteArrayInputStream(code);
		    try{
		    	work = new MyProgressMonitor();
		    	areaNamesFile.create(source, true, work);
		    	while( !work.isDone() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	public boolean addArea(String className) {
		try {
			//String value = String.valueOf(getAreaNames_NextAreaIndex());
			String value = "\"" + getClassName(className) + "\"";
			fileReplaceAll(areaNamesFile,
					"\n}", 
					"\n\tpublic static int " + className + "\t= IDHelper.generateID(" + value + ", 0, 0);\n}");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setNameArea(String oldClassName, String newClassName) {
		try {
			// Replace area name
			fileReplaceAll(areaNamesFile,
						"public static int " + Pattern.quote(getClassName(oldClassName))	+ "[_\\s\\t\\/=]", 
						Pattern.quote(getClassName(oldClassName)),
						getClassName(newClassName));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteArea(String className) {
		
		className = getClassName(className);
		return fileReplaceAll(areaNamesFile,
				".*" + Pattern.quote( className ) + ".*\n",
				"");	
		/*
		
		try {
			StringBuilder newContent = new StringBuilder();
			InputStream fileInputStream =  areaNamesFile.getContents();
			BufferedReader fileBr = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = null;	
			while ((line = fileBr.readLine()) != null) {
				if ( !line.contains("public static int " + Pattern.quote(className) + "\t= ") && !line.contains("public static int " + Pattern.quote(className) + "_") )
					newContent.append(line + "\n");
			}
			fileBr.close();
			areaNamesFile.setContents(new ByteArrayInputStream(newContent.toString().getBytes()), true, true, null);
			return true;
		} catch (Exception e) {
			System.out.println(e.toString());
		}*/
	}

	/*private int getAreaNames_NextAreaIndex() {
		try {
			InputStream fileInputStream =  areaNamesFile.getContents();
			BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

			String line = null;
			int value = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("\tpublic static int ")) {
					if (!line.contains("_")) {
						value = getValueFromAreaLine(line) + 1;
					}
				}
			}
			br.close();
			if (value == 0) {
				return 1;
			}
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}*/
	
	
	/*private int getValueFromAreaLine(String line){
		String funtion = line.substring(line.indexOf("=") + 1);
		funtion = funtion.replace("IDHelper.generateID(", "");
		funtion = funtion.substring(0, funtion.indexOf(','));
		return Integer.valueOf(funtion.trim());
	}*/

	public boolean addActivity(String areaName, String cfName) {
		
		
		String value = getNextAreaNamesActivity(areaName);
		try {
			StringBuilder newContent = new StringBuilder();
			
			InputStream fileInputStream =  areaNamesFile.getContents();
			BufferedReader fileBr = new BufferedReader(new InputStreamReader(fileInputStream));
			
			String line = null;

			boolean finded = false;
			while ((line = fileBr.readLine()) != null) {
				if (line.contains("public static int " + areaName + "\t= ")
						|| line.contains("public static int " + areaName + "_")) {
					finded = true;
				} else {
					if (finded) {
						String nl = "\tpublic static int " + areaName + "_" + cfName + "\t= " + value + ";\n";
						newContent.append(nl);
						finded = false;
					}
				}
				newContent.append(line + "\n");
			}
			fileBr.close();
			
			areaNamesFile.setContents(new ByteArrayInputStream(newContent.toString().getBytes()), true, true, null);
			return true;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return false;
	}

	public boolean setNameActivity(String oldAreaName, String newAreaName,  String cfOldName, String cfNewName) {
		try {
			fileReplaceAll(areaNamesFile,
					"public static int " + Pattern.quote(getClassName(oldAreaName) + "_" + getClassName(cfOldName)), 
					"public static int " + getClassName(newAreaName) + "_" + getClassName(cfNewName));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param name
	 * @param actiName
	 */
	public boolean deleteActivity(String name, String actiName) {
		try {
			StringBuilder newContent = new StringBuilder();
			InputStream fileInputStream =  areaNamesFile.getContents();
			BufferedReader fileBr = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = null;
			while ((line = fileBr.readLine()) != null) {
				if (!line.contains("public static int " + name + "_" + actiName)) {
					newContent.append(line + "\n");
				}
			}
			fileBr.close();
			areaNamesFile.setContents(new ByteArrayInputStream(newContent.toString().getBytes()), true, true, null);
			return true;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return false;
	}

	private String getNextAreaNamesActivity(String areaName) {
		//areaName = "SN" + areaName;
		try {
			String areaVal = areaName;
			BufferedReader br = new BufferedReader(new InputStreamReader(areaNamesFile.getContents()));

			String line = null;
			int value = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("\tpublic static int ")) {
					/*if (line.contains(" " + areaName + "\t")){
						areaVal = getValueFromAreaLine(line);
					}else*/ 
					if (line.contains(" " + areaName + "_")){
						value = getValueFromActivityLine(line) + 1;
					}
				}
			}
			br.close();
			if (value == 0)
				value = 1;
			
			return "IDHelper.generateID(\"" + areaVal + "\"," + value + ",0)";
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	
	private int getValueFromActivityLine(String line){
		String funtion = line.substring(line.indexOf("=") + 1);
		funtion = funtion.replace("IDHelper.generateID(", "");
		funtion = funtion.substring(funtion.indexOf(',') + 1);
		funtion = funtion.substring(0, funtion.indexOf(','));
		return Integer.valueOf(funtion.trim());
	}
	
	
/*	public boolean createAreaNamePython(){
		String[] lines = readAllContente(areaNamesFile).split("\n");
		StringBuilder content = new StringBuilder("");
		
		//Get the values from the AreaNames.java File
		for ( int x = 0; x < lines.length; x++){
			if ( lines[x].contains("public static int") ){
				String[] vals = lines[x].substring(lines[x].indexOf("int") + 3, lines[x].lastIndexOf(";")).split("=");
				content.append(vals[0].replace(" ", "").replace("\t", "") 
								+ " = " 
								+ vals[1].replace(" ", "").replace("\t", "") 
								+ "\n"); 
			}
		}
		
		
		//Writhe the AreaNames.py file and the __init__
		IFile areaNamesPython = ((IFolder)areaNamesFile.getParent()).getFile("AreaNames.py");
		try {
			if ( areaNamesPython.exists() )
				areaNamesPython.delete(true, null);
			areaNamesPython.create(new ByteArrayInputStream(content.toString().getBytes()), true, null);
			
			
			areaNamesPython =  ((IFolder)areaNamesFile.getParent()).getFile("__init__.py");
			if ( !areaNamesPython.exists() )
				areaNamesPython.create(new ByteArrayInputStream(new byte[]{}), true, null);
			
			areaNamesPython =  ((IFolder)areaNamesFile.getParent().getParent()).getFile("__init__.py");
			if ( !areaNamesPython.exists() )
				areaNamesPython.create(new ByteArrayInputStream(new byte[]{}), true, null);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}*/
	
	
/*	public void updateAreaNamesVersion(){
		
		try {
			StringBuilder newContent = new StringBuilder();
			
			InputStream fileInputStream =  areaNamesFile.getContents();
			BufferedReader fileBr = new BufferedReader(new InputStreamReader(fileInputStream));
			
			String line = null;
			boolean modified = false;
			int areaVal = 0;
			
			while ((line = fileBr.readLine()) != null) {
				
				if ( line.contains("public static int") ){
					
					if (!line.contains("_") ){
						areaVal = Integer.valueOf(line.substring( line.lastIndexOf(' ') + 1, line.lastIndexOf(";")));
						newContent.append(line + "\n");
						
					}else{
						//String vals = line.substring(line.indexOf("int") + 3, line.lastIndexOf(";")).split("=");
						int value = Integer.valueOf(line.substring( line.lastIndexOf(' ') + 1, line.lastIndexOf(";")));
						if ( value < (1 << 15) ){
							modified = true;
							line = line.substring( 0, line.lastIndexOf(' ') + 1 ) + (areaVal + (value << 15)) + ";";
						}
						newContent.append(line + "\n");
					}
				}else{
					newContent.append(line + "\n");
				}
			}
			fileBr.close();
			
			if ( modified ){
				areaNamesFile.setContents(new ByteArrayInputStream(newContent.toString().getBytes()), true, true, null);
				createAreaNamePython();
			}
				
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}*/
	

/*	public String getSNClassName(String areaName, String cfName) {
		return areaName + cfName;
	}
	private boolean createPackage(String area) {
		File f = new File(src + "/" + area);
		if (!f.exists()) {
			f.mkdir();
		}
		return true;
	}
*/
}