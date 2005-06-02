/**
 * $Id: MapAbstractPropertiesFrame.java,v 1.1 2005/04/28 13:04:25 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import javax.swing.event.ChangeEvent;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.Props.MapViewVisualManager;
import com.syrus.AMFICOM.Client.Map.Props.MapVisualManager;
import com.syrus.AMFICOM.client_.general.ui_.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 *  ���� ����������� ������� �������� �����
 * @version $Revision: 1.1 $, $Date: 2005/04/28 13:04:25 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public abstract class MapAbstractPropertiesFrame extends AbstractPropertiesFrame implements OperationListener
{
	protected ApplicationContext aContext;

	private boolean performProcessing = true;
	
	public MapAbstractPropertiesFrame(String title, ApplicationContext aContext) {
		super(title);
		setContext(aContext);
	}
	
	public void setContext(ApplicationContext aContext) {
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null) {
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_NAVIGATE);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_SELECTED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_SELECTED);
			}
		this.aContext = aContext;
		if(aContext.getDispatcher() != null) {
			aContext.getDispatcher().register(this, MapEvent.MAP_NAVIGATE);
			aContext.getDispatcher().register(this, MapEvent.MAP_SELECTED);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_SELECTED);
		}
	}
	
	public void operationPerformed(OperationEvent e) {
		if(!this.performProcessing)
			return;
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
	
	public void stateChanged(ChangeEvent e) {
		super.stateChanged(e);
		if(this.aContext.getDispatcher() != null) {
			Object object = e.getSource();
			this.performProcessing = false;
			if(object instanceof MapElement)
				this.aContext.getDispatcher().notify(new MapEvent(object, MapEvent.MAP_CHANGED));
			else if(object instanceof Map)
				this.aContext.getDispatcher().notify(new MapEvent(object, MapEvent.MAP_SELECTED));
			else if(object instanceof MapView)
				this.aContext.getDispatcher().notify(new MapEvent(object, MapEvent.MAP_VIEW_CHANGED));
			this.performProcessing = true;
		}
	}
}
