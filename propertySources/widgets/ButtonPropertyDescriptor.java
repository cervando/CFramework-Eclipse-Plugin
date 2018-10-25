package kmiddlePlugin.propertySources.widgets;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ButtonPropertyDescriptor extends PropertyDescriptor{

	private String ButtonLabel;
	private MouseListener code;
	private cognitiveCellEditor editor;
	
	public ButtonPropertyDescriptor(Object id, String displayName, String buttonName,MouseListener code) {
		super(id, displayName);
		this.code = code;
		this.ButtonLabel = buttonName;
		// TODO Auto-generated constructor stub
		//if ( )
		
		
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		// TODO Auto-generated method stub
		editor =  new cognitiveCellEditor(parent, ButtonLabel);
		editor.setButtonCode(code);
		//import org.eclipse.ui.views.properties.
		return editor;
	}
	
	public Button getButton(){
		return editor.getButton();
	}
}
