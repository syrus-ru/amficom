package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Controllers.MapVisualManager;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.scheme.ui.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.map.MapElement;

public class MapElementCharacteristicsFrame extends CharacteristicPropertiesFrame
{
	public MapElementCharacteristicsFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null)
			if (this.aContext.getDispatcher() != null) {
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_NAVIGATE);
			}
		super.setContext(aContext);
		if(aContext.getDispatcher() != null)
			aContext.getDispatcher().register(this, MapEvent.MAP_NAVIGATE);
	}
	
	public void operationPerformed(OperationEvent e) {
		super.operationPerformed(e);
		if (e.getActionCommand().equals(MapEvent.MAP_NAVIGATE)) {
			MapNavigateEvent event = (MapNavigateEvent) e;
			if(event.isMapElementSelected()) {
				MapElement mapElement = (MapElement )event.getSource();
				VisualManager vm = MapVisualManager.getVisualManager(mapElement);
				setVisualManager(vm);				
				if (this.editor != null)
					this.editor.setObject(mapElement);
			}
		}
	}
	
}
