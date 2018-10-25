package kmiddlePlugin.parser.codeTemplates;

public class ActivityTemplate {
	public static String javaSourceCode = 
"package %s;" +
"\n" +
"\nimport config.AreaNames;" +
"\nimport kmiddle2.nodes.activities.Activity;" +
"\n" +
"\n" +
"\npublic class %s_%s extends Activity {" +
"\n	public %s_%s(){" +
"\n		this.ID = AreaNames.%s_%s;" +
"\n		this.namer = AreaNames.class;" +
"\n	}" +
"\n	" +
"\n	public void init(){" +
"\n		" +
"\n	}" +
"\n	" +
"\n	public void receive(int nodeID, byte[] data){" +
"\n" +
"\n	}" +
"\n}";
	
	
	
	
	public static String pythonSourceCode = 
"from SmallNode import SmallNode" + 
"\nfrom config import AreaNames" +
"\n" +
"\nclass %s_%s(SmallNode):" +
"\n" +
"\n" +
"\n    def __init__(self, myName, father, options, areaNamesClass):" +
"\n        super(%s_%s,self).__init__(myName, father, options, areaNamesClass)" +
"\n	" +
"\n	" +
"\n	" +
"\n    def afferents(self, senderName, msg):" +
"\n        None";
}





/*
package Hipocampo;

import nodes.activities.Activity;
import util.BinaryHelper;
import util.IDHelper;

public class Hipo_proc1 extends Activity {

	public Hipo_proc1(){
		this.ID = IDHelper.generateID(2, 1, 0);
	}
	
	
	public void init(){
	}
	
	public void receive(int nodeID, byte[] data) {
		
		System.out.println("Proceso mensaje:" + BinaryHelper.byteToInt(data, 0));
		
	}

}
*/