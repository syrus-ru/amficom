package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;

public class MapCharacteristicPropertiesFrame extends MapAbstractPropertiesFrame
{
	public MapCharacteristicPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getCharacteristicPropertiesPanel();
	}
}
