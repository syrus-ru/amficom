package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;

public class MapCharacteristicPropertiesFrame extends MapAbstractPropertiesFrame
{
	public MapCharacteristicPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getCharacteristicPropertiesPanel();
	}
}
