package CPlugin.parser;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

public class NamesManager extends Manager {

	private IFile areaNamesFile;

	public NamesManager(IProject project) {
    	super(project);
    	
		//Creates AreaNames file
		areaNamesFile = config.getFile("Names.java");
		
	    try{
	    	if ( !areaNamesFile.exists() )
	    		saveFile(areaNamesFile, KMiddleCodeFactory.newNames());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean addRouter(String name) {
		try {
			String className = getClassName(name);
			fileReplaceAll(areaNamesFile,
					"\n}", 
					"\n\tpublic static long " + className + "\t= IDHelper.generateID(\"" + name + "\");\n}");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteRouter(String name) {
		
		String className = getClassName(name);
		return fileReplaceAll(areaNamesFile,
				".*" + Pattern.quote( className ) + "[_\\s\\t\\/= ].*\n",
				"");	
	}
	
	public boolean setRouterName(String oldName, String newName) {
		try {
			String oldClassName = getClassName(oldName);
			String newClassName = getClassName(newName);
			// Replace area name
			fileReplaceAll(areaNamesFile,
						"public static long " + Pattern.quote(oldClassName)	+ "[_\\s\\t\\/= ]", 
						Pattern.quote(oldName),
						newClassName );
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean addProcess(String areaName, String processName) {
		
		try {
			String areaClass = getClassName(areaName);
			String processClass = getClassName(processName);
			addAfterLine(areaNamesFile,
					".*public static long " + areaClass + "[\\s\\t\\/= ].*",
					"\tpublic static long " + areaClass + "_" + processClass + "\t= IDHelper.generateID(\"" + areaName + "\",\"" + processClass + "\");");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setProcessName(String routerName,  String oldProcessName, String newProcessName) {
		try {
			replaceLine(areaNamesFile,
					".*public static long " + getClassName(routerName) + "_" + getClassName(oldProcessName) + ".*", 
					"\tpublic static long " + getClassName(routerName) + "_" + getClassName(newProcessName) + "\t= IDHelper.generateID(\"" + routerName + "\",\"" + newProcessName + "\");");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param routerName
	 * @param processName
	 */
	public boolean deleteProcess(String routerName, String processName) {

		String areaClass = getClassName(routerName);
		String processClass = getClassName(processName);
		return fileReplaceAll(areaNamesFile,
				".*" + Pattern.quote( areaClass ) + "_" + Pattern.quote( processClass ) + ".*\n",
				"");
	}
	
	
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