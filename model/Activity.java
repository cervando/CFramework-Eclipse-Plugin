package CPlugin.model;


public class Activity {
	
	
	public Activity(String name){
		this.name = name;
	}
	
	private Area area = null;
	//private String process = "";
	private String name = "";
	private String languageList[] =new String[]{"Java", /*"C++",*/ "Python"};
	private String language = languageList[0];
	
	//Section for mapped Nodes
	private int type = 0;
	//private String indexList = "";
	
	
	//public Input input;
	//public Output output;
	
	public Area getArea(){ return area;}
	public void setArea(Area a){ area = a;}
	
	
	public void setLanguage(String language){ 
		this.language = language;
		area.setChanged();
	}
	
	public void setLanguage(int languageIndex){ 
		this.language = languageList[languageIndex];
		area.setChanged();
	}
	
	//public void setMapped(boolean mapped){ this.mapped = mapped;	}
	//public boolean getMapped(){ return this.mapped;	}
	public void setType(int type){ 
		this.type = type;	
		area.setChanged();
	}
	
	public int getType(){ return type; }
	
	public String getLanguage() { return language; }
	
	
	
	public int getLanguageIndex() {
		return getLanguageIndex(language);
	}
	
	public int getLanguageIndex(String l) {
		for ( int i = 0; i < languageList.length; i++ ){
			if ( languageList[i].equals(l))
				return i;
		}
		return 0; 
	}
	public String[] getLanguageList() { return languageList; } 

	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
		area.setChanged();
	}
	
	
	
	
	public String getFileName() {
		return "src/" + area.getName().replace(" ", "") + "/" + "SN" + area.getName().replace(" ", "") + "_" + name.replace(" ", "")  + ".java";
	}
}
