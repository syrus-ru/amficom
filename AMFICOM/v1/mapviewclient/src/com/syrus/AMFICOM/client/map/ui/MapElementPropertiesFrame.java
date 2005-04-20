/**
 * $Id: MapElementPropertiesFrame.java,v 1.4 2005/04/20 16:16:37 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import javax.swing.event.ChangeEvent;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Props.MapViewVisualManager;
import com.syrus.AMFICOM.Client.Map.Props.MapVisualManager;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.scheme.ui.GeneralPropertiesFrame;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.4 $, $Date: 2005/04/20 16:16:37 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapElementPropertiesFrame extends GeneralPropertiesFrame
{
	public void stateChanged(ChangeEvent e) {
		super.stateChanged(e);
		if(this.aContext.getDispatcher() != null) {
			Object object = e.getSource();
			if(object instanceof MapElement)
				this.aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_CHANGED));
			else if(object instanceof Map)
				this.aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_SELECTED));
			else if(object instanceof MapView)
				this.aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_VIEW_CHANGED));
		}
	}

	public MapElementPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null) {
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_NAVIGATE);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_SELECTED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_SELECTED);
			}
		super.setContext(aContext);
		if(aContext.getDispatcher() != null) {
			aContext.getDispatcher().register(this, MapEvent.MAP_NAVIGATE);
			aContext.getDispatcher().register(this, MapEvent.MAP_SELECTED);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_SELECTED);
		}
	}
	
	public void operationPerformed(OperationEvent e) {
		super.operationPerformed(e);
		if (e.getActionCommand().equals(MapEvent.MAP_NAVIGATE)) {
			MapNavigateEvent event = (MapNavigateEvent) e;
			if(event.isMapElementSelected()) {
				MapElement mapElement = (MapElement )event.getSource();
				VisualManager vm = MapVisualManager.getVisualManager(mapElement);
				super.setVisualManager(vm);				
				if (this.editor != null)
					this.editor.setObject(mapElement);
			}
		}
		else if (e.getActionCommand().equals(MapEvent.MAP_SELECTED)) {
			Map map = (Map )e.getSource();
			VisualManager vm = MapVisualManager.getInstance();
			super.setVisualManager(vm);				
			if (this.editor != null)
				this.editor.setObject(map);
		}
		else if (e.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED)) {
			MapView mapView = (MapView )e.getSource();
			VisualManager vm = MapViewVisualManager.getInstance();
			super.setVisualManager(vm);				
			if (this.editor != null)
				this.editor.setObject(mapView);
		}

	}
	
}
