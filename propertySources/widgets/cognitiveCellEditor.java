package CPlugin.propertySources.widgets;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class cognitiveCellEditor extends CellEditor {
	
	Button button;
	String name;
	
	public cognitiveCellEditor(Composite parent, String name){
		super(parent);
		this.name = name;
		
	}
	
	public void setButtonCode( MouseListener listener){
		button.addMouseListener(listener);
		button.setText(name);
	}

	@Override
	protected Control createControl(Composite parent) {
		// TODO Auto-generated method stub
		button = new Button(parent, getStyle() | SWT.BUTTON1);
		return button;
	}

	@Override
	protected Object doGetValue() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	protected void doSetFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doSetValue(Object value) {
		// TODO Auto-generated method stub
		
	}
	
	public Button getButton(){
		return button;
	}
	

}
