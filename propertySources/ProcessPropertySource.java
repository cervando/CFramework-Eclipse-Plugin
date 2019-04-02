package CPlugin.propertySources;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import CPlugin.gefView.Policies.commands.activity.ActivityChangedLanguageCommand;
import CPlugin.gefView.Policies.commands.activity.ActivityChangedNameCommand;
import CPlugin.gefView.Policies.commands.activity.ActivityTypeChangedCommand;
import CPlugin.model.Activity;
import CPlugin.propertySources.widgets.ButtonPropertyDescriptor;

public class ProcessPropertySource implements IPropertySource{
	
	private final Activity process;
	//private JgraphListener ui;
	
	
	public ProcessPropertySource(Activity cf/*, JgraphListener ui*/){
		this.process = cf; 
	}

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		
		
		ButtonPropertyDescriptor openFile = new ButtonPropertyDescriptor("cognitive_fileName", "Archivo",
				"Editar codigo",
				new MouseListener() {

					@Override
					public void mouseUp(MouseEvent e) {
						// TODO Auto-generated method stub
						/*ui.getUI().getDisplay().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								
								File fileToOpen = new File(ui.getProjectPath() +"/" + cf.getFileName());
								System.out.println(fileToOpen.getAbsolutePath());
								if (fileToOpen.exists() && fileToOpen.isFile()) {
								    IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
								    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
								 
								    try {
								        IDE.openEditorOnFileStore( page, fileStore );
								    } catch ( PartInitException e ) {
								        //Put your exception handler here if you wish to
								    }
								} 
							}
						});*/
					}
					
					public void mouseDown(MouseEvent e) {}
					public void mouseDoubleClick(MouseEvent e) {}
				}
			);
			
		TextPropertyDescriptor activityPropName = new TextPropertyDescriptor("name", "Name");
		//ComboBoxPropertyDescriptor activityPropertyType = new ComboBoxPropertyDescriptor("type", "Type", new String[]{"Singleton","Parallel"});
		//ComboBoxPropertyDescriptor prPropLanguaje = new ComboBoxPropertyDescriptor("language", "Language", process.getLanguageList());
		
		

		return new IPropertyDescriptor[] {
				openFile,
				activityPropName,
				//activityPropertyType,
				//prPropLanguaje
		};
	}

	@Override
	public Object getPropertyValue(Object id) {
	    if (id.equals("name")) {
	    	return process.getName();
	    }
	    if (id.equals("type")) {
	    	return process.getType();
	    }
	    if (id.equals("language")) {
	    	return process.getLanguageIndex();
	    }
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		//System.out.println("CF: " + value);
		
		String val;
		if ( value instanceof String )
			val = (String) value;
		else
			val = value.toString();
		
		if (id.equals("name")) {
			if ( val.equals(""))
				return;
			
			val = validateClassName(val);
			ActivityChangedNameCommand nnc = new ActivityChangedNameCommand(process, val);
			process.getArea().executeCommand(nnc);
	    }
	    if (id.equals("type")) {	
	    	
	    	ActivityTypeChangedCommand pcm = new ActivityTypeChangedCommand(process, Integer.parseInt(val));
			process.getArea().executeCommand(pcm);
				
	    }
	    
	    if (id.equals("language")) {
	    	ActivityChangedLanguageCommand pcl = new ActivityChangedLanguageCommand(process, Integer.valueOf(val));
	    	process.getArea().executeCommand(pcl);
	    	//process.setLanguage(Integer.valueOf(val));
	    }
	}
	
	private String validateClassName(String in){
		if ( in.length() > 1)
			return in.substring(0,1).toUpperCase() + in.substring(1);
		return (in.charAt(0) + "").toUpperCase();
	}
}