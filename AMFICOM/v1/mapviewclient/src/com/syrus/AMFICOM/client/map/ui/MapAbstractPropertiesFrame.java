/**
 * $Id: MapAbstractPropertiesFrame.java,v 1.10 2005/08/08 10:21:33 krupenn Exp $
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
import java.util.Collection;

import javax.swing.JDesktopPane;
import javax.swing.event.ChangeEvent;

import com.syrus.AMFICOM.client.UI.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.props.MapViewVisualManager;
import com.syrus.AMFICOM.client.map.props.MapVisualManager;
import com.syrus.AMFICOM.client.map.props.MarkerEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Окно отображения свойств элемента карты
 * 
 * @version $Revision: 1.10 $, $Date: 2005/08/08 10:21:33 $
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
						MapEvent.OTHER_SELECTED,
						this);
				this.aContext.getDispatcher().removePropertyChangeListener(
						MapEvent.SELECTION_CHANGED,
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
					MapEvent.OTHER_SELECTED,
					this);
			aContext.getDispatcher().addPropertyChangeListener(
					MapEvent.SELECTION_CHANGED,
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
		if(this.getParent() == null)
			return;
		if(pce.getPropertyName().equals(MapEvent.SELECTION_CHANGED)) {
			LogicalNetLayer lnl = MapDesktopCommand.findMapFrame(
					(JDesktopPane )this.getParent()).getMapViewer()
					.getLogicalNetLayer();
			Collection selection = (Collection )pce.getNewValue();
			MapElement mapElement;
			// get selection object
			if(selection.size() == 1) {
				mapElement = (MapElement )selection.iterator().next();
			}
			else {
				mapElement = lnl.getCurrentMapElement();
			}
			VisualManager vm = MapVisualManager.getVisualManager(mapElement);
			if(vm instanceof MarkerEditor) {
				MarkerEditor markerEditor = (MarkerEditor )vm;
				markerEditor.setLogicalNetLayer(lnl);
			}
			super.setVisualManager(vm);
			if(this.editor != null)
				this.editor.setObject(mapElement);
		}
		else if(pce.getPropertyName().equals(MapEvent.MAP_SELECTED)) {
			Map map = (Map )pce.getSource();
			VisualManager vm = MapVisualManager.getInstance();
			super.setVisualManager(vm);
			if(this.editor != null)
				this.editor.setObject(map);
		}
		else if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_SELECTED)) {
			MapView mapView = (MapView )pce.getSource();
			VisualManager vm = MapViewVisualManager.getInstance();
			super.setVisualManager(vm);
			if(this.editor != null)
				this.editor.setObject(mapView);
		}
		else if(pce.getPropertyName().equals(MapEvent.OTHER_SELECTED)) {
			Object selectedObject = pce.getSource();
			VisualManager vm = MapVisualManager.getVisualManager(selectedObject);
			if(vm != null) {
				super.setVisualManager(vm);
				if(this.editor != null)
					this.editor.setObject(selectedObject);
			}
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
			else if(object instanceof Map)
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(object, MapEvent.MAP_SELECTED));
			else if(object instanceof MapView)
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(object, MapEvent.MAP_VIEW_CHANGED));
			this.performProcessing = true;
		}
	}
}
