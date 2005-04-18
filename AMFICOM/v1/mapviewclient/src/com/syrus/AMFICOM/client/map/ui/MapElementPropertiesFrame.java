/**
 * $Id: MapElementPropertiesFrame.java,v 1.1 2005/04/18 11:23:13 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Controllers.MapVisualManager;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.scheme.ui.GeneralPropertiesFrame;
import com.syrus.AMFICOM.map.MapElement;

/**
 *  ���� ����������� ������� �������� �����
 * @version $Revision: 1.1 $, $Date: 2005/04/18 11:23:13 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapElementPropertiesFrame extends GeneralPropertiesFrame
{
	public MapElementPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
//		if (this.dispatcher != null) {
//			this.dispatcher.unregister(this, MapEvent.MAP_NAVIGATE);
//		}
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
