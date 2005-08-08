/**
 * $Id: MapViewTreeSelectionListener.java,v 1.2 2005/08/08 10:45:07 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.ui;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

public class MapViewTreeSelectionListener implements TreeSelectionListener {

	private boolean performProcessing = true;
	private final ApplicationContext context;
	private final MapViewTreePanel mapViewTreePanel;

	public MapViewTreeSelectionListener(MapViewTreePanel mapViewTreePanel) {
		this.mapViewTreePanel = mapViewTreePanel;
		this.context = mapViewTreePanel.aContext;
	}

	public void valueChanged(TreeSelectionEvent e) {
		if(!this.performProcessing) {
			return;
		}
		Dispatcher dispatcher = this.context.getDispatcher();
		if(dispatcher != null) {
			this.performProcessing = false;
			this.mapViewTreePanel.performProcessing = false;
			TreePath paths[] = e.getPaths();
			Collection toSelect = new LinkedList();
			Collection toDeSelect = new LinkedList();
			boolean sendSelectionEvent = true;
			for (int i = 0; i < paths.length; i++) 
			{
				Item node = (Item )paths[i].getLastPathComponent();
				Object userObject = node.getObject();
				if(userObject instanceof MapElement
						|| userObject instanceof SchemeElement
						|| userObject instanceof SchemeCableLink
						|| userObject instanceof SchemePath) {
					Object mapElement = userObject;
					if(e.isAddedPath(paths[i]))
						toSelect.add(mapElement);
					else
						toDeSelect.add(mapElement);
				}
				else if(userObject instanceof Map) {
					Map map = (Map )userObject;
					if(e.isAddedPath(paths[i])) {
						dispatcher.firePropertyChange(new MapEvent(map, MapEvent.MAP_SELECTED));
						sendSelectionEvent = false;
					}
				}
				else if(userObject instanceof MapView) {
					MapView mapView = (MapView )userObject;
					if(e.isAddedPath(paths[i])) {
						dispatcher.firePropertyChange(new MapEvent(mapView, MapEvent.MAP_VIEW_SELECTED));
						sendSelectionEvent = false;
					}
				}
				else if(userObject instanceof SiteNodeType
						|| userObject instanceof PhysicalLinkType
						|| userObject instanceof MapLibrary) {
					if(e.isAddedPath(paths[i])) {
						dispatcher.firePropertyChange(new MapEvent(userObject, MapEvent.OTHER_SELECTED));
						sendSelectionEvent = false;
					}
				}
			}
			if(sendSelectionEvent) {
				dispatcher.firePropertyChange(new MapEvent(this, MapEvent.NEED_SELECT, toSelect));
				dispatcher.firePropertyChange(new MapEvent(this, MapEvent.NEED_DESELECT, toDeSelect));
			}
			this.performProcessing = true;
			this.mapViewTreePanel.performProcessing = true;
		}
	}
}
