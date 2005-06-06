/**
 * $Id: MapAbstractPropertiesFrame.java,v 1.3 2005/06/06 12:20:35 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.ChangeEvent;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.client.map.props.MapViewVisualManager;
import com.syrus.AMFICOM.client.map.props.MapVisualManager;
import com.syrus.AMFICOM.client.UI.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.3 $, $Date: 2005/06/06 12:20:35 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public abstract class MapAbstractPropertiesFrame extends
		AbstractPropertiesFrame implements PropertyChangeListener {
	protected ApplicationContext aContext;

	private boolean performProcessing = true;

	public MapAbstractPropertiesFrame(String title, ApplicationContext aContext) {
		super(title);
		setContext(aContext);
	}

	public void setContext(ApplicationContext aContext) {
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null) {
				this.aContext.getDispatcher().removePropertyChangeListener(
						MapEvent.MAP_NAVIGATE,
						this);
				this.aContext.getDispatcher().removePropertyChangeListener(
						MapEvent.MAP_SELECTED,
						this);
				this.aContext.getDispatcher().removePropertyChangeListener(
						MapEvent.MAP_VIEW_SELECTED,
						this);
			}
		this.aContext = aContext;
		if(aContext.getDispatcher() != null) {
			aContext.getDispatcher().addPropertyChangeListener(
					MapEvent.MAP_NAVIGATE,
					this);
			aContext.getDispatcher().addPropertyChangeListener(
					MapEvent.MAP_SELECTED,
					this);
			aContext.getDispatcher().addPropertyChangeListener(
					MapEvent.MAP_VIEW_SELECTED,
					this);
		}
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if(!this.performProcessing)
			return;
		if(pce.getPropertyName().equals(MapEvent.MAP_NAVIGATE)) {
			MapNavigateEvent event = (MapNavigateEvent )pce;
			if(event.isMapElementSelected()) {
				MapElement mapElement = (MapElement )event.getSource();
				VisualManager vm = MapVisualManager.getVisualManager(mapElement);
				super.setVisualManager(vm);
				if(this.editor != null)
					this.editor.setObject(mapElement);
			}
		}
		else
			if(pce.getPropertyName().equals(MapEvent.MAP_SELECTED)) {
				Map map = (Map )pce.getSource();
				VisualManager vm = MapVisualManager.getInstance();
				super.setVisualManager(vm);
				if(this.editor != null)
					this.editor.setObject(map);
			}
			else
				if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_SELECTED)) {
					MapView mapView = (MapView )pce.getSource();
					VisualManager vm = MapViewVisualManager.getInstance();
					super.setVisualManager(vm);
					if(this.editor != null)
						this.editor.setObject(mapView);
				}

	}

	public void stateChanged(ChangeEvent e) {
		super.stateChanged(e);
		if(this.aContext.getDispatcher() != null) {
			Object object = e.getSource();
			this.performProcessing = false;
			if(object instanceof MapElement)
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(object, MapEvent.MAP_CHANGED));
			else
				if(object instanceof Map)
					this.aContext.getDispatcher().firePropertyChange(
							new MapEvent(object, MapEvent.MAP_SELECTED));
				else
					if(object instanceof MapView)
						this.aContext.getDispatcher()
								.firePropertyChange(
										new MapEvent(
												object,
												MapEvent.MAP_VIEW_CHANGED));
			this.performProcessing = true;
		}
	}
}
