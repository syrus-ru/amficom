/**
 * $Id: MapAbstractPropertiesFrame.java,v 1.11 2005/08/09 11:06:25 krupenn Exp $
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
import java.util.logging.Level;

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
import com.syrus.util.Log;

/**
 * Окно отображения свойств элемента карты
 * 
 * @version $Revision: 1.11 $, $Date: 2005/08/09 11:06:25 $
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
		long d0 = System.currentTimeMillis();
		if(!this.performProcessing)
			return;
		if(this.getParent() == null)
			return;
		String mesg2 = "";
		if(pce.getPropertyName().equals(MapEvent.SELECTION_CHANGED)) {
			LogicalNetLayer lnl = MapDesktopCommand.findMapFrame(
					(JDesktopPane )this.getParent()).getMapViewer()
					.getLogicalNetLayer();
			long d1 = System.currentTimeMillis();
			Collection selection = (Collection )pce.getNewValue();
			MapElement mapElement;
			// get selection object
			if(selection.size() == 1) {
				mapElement = (MapElement )selection.iterator().next();
			}
			else {
				mapElement = lnl.getCurrentMapElement();
			}
			long d2 = System.currentTimeMillis();
			VisualManager vm = MapVisualManager.getVisualManager(mapElement);
			if(vm instanceof MarkerEditor) {
				MarkerEditor markerEditor = (MarkerEditor )vm;
				markerEditor.setLogicalNetLayer(lnl);
			}
			long d3 = System.currentTimeMillis();
			super.setVisualManager(vm);
			long d4 = System.currentTimeMillis();
			if(this.editor != null)
				this.editor.setObject(mapElement);
			long d5 = System.currentTimeMillis();
			mesg2 = "		 " + (d1 - d0) + " " + (d2 - d1) + " " + (d3 - d2) + " " + (d4 - d3) + " " + (d5 - d4) + " ms ---------";
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
		long f = System.currentTimeMillis();
		Log.debugMessage(this.getClass().getName() + "::propertyChange(" + pce.getPropertyName() + ") -------- " + (f - d0) + " ms --------- " + mesg2, Level.INFO);
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
