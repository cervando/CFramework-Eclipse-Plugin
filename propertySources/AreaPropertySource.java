package CPlugin.propertySources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import CPlugin.gefView.Policies.commands.activity.ActivityCreatedCommand;
import CPlugin.gefView.Policies.commands.activity.ActivityTypeChangedCommand;
import CPlugin.gefView.Policies.commands.area.NodeChangedBlackBoxCommand;
import CPlugin.gefView.Policies.commands.area.NodeChangedNameCommand;
import CPlugin.gefView.Policies.commands.area.NodeChangedXCommand;
import CPlugin.gefView.Policies.commands.area.NodeChangedYCommand;
import CPlugin.model.Area;
import CPlugin.propertySources.widgets.ButtonPropertyDescriptor;
import CPlugin.utils.ModelValidator;

public class AreaPropertySource implements IPropertySource{
	
	private Area area;
	//private ProcessArrayPropertySource proccessPS;
	private ButtonPropertyDescriptor openFile;
	
	public AreaPropertySource(Area area){
		this.area = area;
	}
	
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		
		///Boton para editar el codigo
		openFile = new ButtonPropertyDescriptor("Area_fileName", "Archivo",
				"Editar codigo",
				new MouseListener() {

					@Override
					public void mouseUp(MouseEvent e) {
						// TODO Auto-generated method stub
						Display.getCurrent().asyncExec(new Runnable() {
							public void run() {
								if ( area.hasChanged() ){									
									
									MessageBox dialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_INFORMATION | SWT.OK);
									dialog.setText("Information");
									dialog.setMessage("To writte code, you must save");
									dialog.open();
								}else{
									
									
//									try {
//										IEditorInput input = ((DefaultEditDomain) areaEditPart.getViewer().getEditDomain()).getEditorPart().getEditorInput();
//										IFile file = (input instanceof IFileEditorInput) ? ((IFileEditorInput) input).getFile() : null;
//										if ( file != null ){
//											IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//											IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
//											page.openEditor(new FileEditorInput(file), desc.getId());
//										}else
//											throw new Exception("File is null");
//									} catch (Exception e2) {
//										System.out.println(this.getClass() + ":: Error opening file");
//										System.out.println(e2.getStackTrace());
//									}
								}
							}
						});
					}					
					public void mouseDown(MouseEvent e) {}
					public void mouseDoubleClick(MouseEvent e) {}
				}
			);
		
		
		//Boton para agregar nuevas procesos cognitivos
		ButtonPropertyDescriptor cf = new ButtonPropertyDescriptor("Area_cf", "Procesos",
			"Agregar Proceso",
			new MouseListener() {

				@Override
				public void mouseUp(MouseEvent e) {
					Display.getCurrent().asyncExec(new Runnable() {
						public void run() {
							MessageBox dialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
							dialog.setText("Confirm");
							dialog.setMessage("Add a new proccess");

							if ( SWT.OK == dialog.open()){
								PropertySheetPage page = getPropertySheetPage();
								ActivityCreatedCommand pcc = new ActivityCreatedCommand( area, page);
								area.executeCommand(pcc);
																	
							}							
						}
					});
				}
				
				public void mouseDown(MouseEvent e) {}
				public void mouseDoubleClick(MouseEvent e) {}
			}
		);
		ComboBoxPropertyDescriptor blackBox = new ComboBoxPropertyDescriptor("blackBox", "Is blackBox", new String[]{"False","True"});
		
		
		return new IPropertyDescriptor[] {
		        //new TextPropertyDescriptor("Area_fileName", "Archivo"),
				openFile,
		        new TextPropertyDescriptor("Area_name", "Name"),
		        new TextPropertyDescriptor("Area_x", "X"),
		        new TextPropertyDescriptor("Area_y", "Y"),
		        //blackBox,
		        cf
		        
		};
	}

	@Override
	public Object getPropertyValue(Object id) {
		
	    
	    if (id.equals("Area_name")) {
	    	return area.getName();
	    }
	    if (id.equals("Area_x")) {
	    	return String.valueOf( area.getX() );
	    }
	    if (id.equals("Area_y")) {
	    	return String.valueOf( area.getY() );
	    }
	    if (id.equals("Area_cf")) {
	    	return new ProcessArrayPropertySource(area);
	    }
	    
	    if (id.equals("blackBox")) {
	    	area.isBlackBox();
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
		String val = (String) value;
		
	    if (id.equals("Area_name")) {
	    	val = validateClassName(val);
	    	
	    	if ( ModelValidator.setNodeName(area.getGraph(), area, val) ){
	    		System.out.println("Se crea comando para cambiar nombre");
				NodeChangedNameCommand nnc = new NodeChangedNameCommand(area, val);
				//this command also update the viewer
				area.executeCommand(nnc);
	    	}
	    }
	    if (id.equals("Area_x") && area.getX() != Integer.valueOf(val)) {
	    	NodeChangedXCommand ncx = new NodeChangedXCommand(area, Integer.valueOf(val), getPropertySheetPage());
	    	area.executeCommand(ncx);
	    }
	    if (id.equals("Area_y") && area.getY() != Integer.valueOf(val)) {
	    	NodeChangedYCommand ncy = new NodeChangedYCommand(area, Integer.valueOf(val), getPropertySheetPage());
	    	area.executeCommand(ncy);
	    }
	    
	    if (id.equals("blackBox")) {	
	    	boolean newVal = Integer.parseInt(val)!=0;
	    	if ( newVal != area.isBlackBox()) {
		    	NodeChangedBlackBoxCommand ncbb = new NodeChangedBlackBoxCommand(area, newVal , getPropertySheetPage());
		    	area.executeCommand(ncbb);
	    	}
	    }
	}
	
	
	private PropertySheetPage getPropertySheetPage(){
		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		if ( part.getClass() == PropertySheet.class   ){
			IPage page = ((PropertySheet)part).getCurrentPage();
			if ( page instanceof PropertySheetPage)
				return ((PropertySheetPage)page);
		}
		return null;
	}
	
	private String validateClassName(String in){
		return in.substring(0,1).toUpperCase() + in.substring(1);
	}
}
