/**
 * $Id: MapElementPropertiesFrame.java,v 1.3 2005/04/19 15:50:12 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Props.MapVisualManager;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.scheme.ui.GeneralPropertiesFrame;
import com.syrus.AMFICOM.map.MapElement;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.3 $, $Date: 2005/04/19 15:50:12 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapElementPropertiesFrame extends GeneralPropertiesFrame
{
	public void stateChanged(ChangeEvent e) {
		super.stateChanged(e);
		if(this.aContext.getDispatcher() != null)
			this.aContext.getDispatcher().notify(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

	public MapElementPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null) {
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
				super.setVisualManager(vm);				
				if (this.editor != null)
					this.editor.setObject(mapElement);
			}
		}
	}
	
}
