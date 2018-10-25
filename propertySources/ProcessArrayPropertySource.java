package kmiddlePlugin.propertySources;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import kmiddlePlugin.gefView.Policies.commands.activity.ActivityDeletedCommand;
import kmiddlePlugin.model.Activity;
import kmiddlePlugin.model.Area;
import kmiddlePlugin.propertySources.widgets.ButtonPropertyDescriptor;

public class ProcessArrayPropertySource implements IPropertySource{
	
	private ArrayList<Activity>  proccessArray;
	private Area area;
	
	
	public ProcessArrayPropertySource(Area a){
		area = a;
		this.proccessArray = a.getProcess();
	}
	

	@Override
	public Object getEditableValue() {
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		//CognitiveFunctionPropertyDescriptor cf = new CognitiveFunctionPropertyDescriptor("cfArray", "Funciones");
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[proccessArray.size()];
		for ( int i = 0; i <  proccessArray.size(); i++) {
			final int index = i;
			descriptors[i] = new ButtonPropertyDescriptor("CFArray_cf" + (i+1), (i+1) + "", 
					"Eliminar Proceso",
					new MouseListener() {
						public void mouseUp(MouseEvent e) {
							//ui.deleteProcess(cfarray.get(index).area.getName(),cfarray.get(index).name );
							MessageBox dialog = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
							dialog.setText("Confirmar");
							dialog.setMessage("¿Esta seguro de que desea eliminar este proceso?");

							if ( SWT.OK == dialog.open()){
								
								PropertySheetPage page = null;
								IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
								if ( part.getClass() == PropertySheet.class   ){
									IPage pageT = ((PropertySheet)part).getCurrentPage();
									if ( pageT instanceof PropertySheetPage)
										page = ((PropertySheetPage)pageT);
								}
								
								
								ActivityDeletedCommand pdc = new ActivityDeletedCommand(area, proccessArray.get(index), page );
								area.executeCommand(pdc);
							}
						}
						public void mouseDown(MouseEvent e) {}
						public void mouseDoubleClick(MouseEvent e) {}
					});
		}
		return descriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {		
		for ( int i = 0; i < proccessArray.size(); i++){
			if ( id.equals("CFArray_cf" + (i+1))){
				return new ProcessPropertySource(proccessArray.get(i)/*, ui*/);
			}
		}
		return "";		
	}

	public boolean isPropertySet(Object id) {	return false; }
	public void resetPropertyValue(Object id) {}
	public void setPropertyValue(Object id, Object value) {}
}