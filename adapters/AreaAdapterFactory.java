package kmiddlePlugin.adapters;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import kmiddlePlugin.model.Area;
import kmiddlePlugin.propertySources.AreaPropertySource;

public class AreaAdapterFactory implements IAdapterFactory{

	@SuppressWarnings("rawtypes")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
	    if (adapterType  == IPropertySource.class && adaptableObject instanceof Area){
	        return new AreaPropertySource((Area) adaptableObject);
	    }
	    return null;
	}
	
	public Class<?>[] getAdapterList() {
		// TODO Auto-generated method stub
		return new Class[] { IPropertySource.class };
	}

}
