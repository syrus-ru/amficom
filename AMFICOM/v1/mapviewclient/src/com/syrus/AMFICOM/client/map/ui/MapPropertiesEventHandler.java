/*-
 * $$Id: MapPropertiesEventHandler.java,v 1.15 2006/06/08 10:23:29 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import javax.swing.JDesktopPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.client.UI.AbstractEventHandler;
import com.syrus.AMFICOM.client.UI.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.props.CablePathAddEditor;
import com.syrus.AMFICOM.client.map.props.MapEditor;
import com.syrus.AMFICOM.client.map.props.MapViewVisualManager;
import com.syrus.AMFICOM.client.map.props.MapVisualManager;
import com.syrus.AMFICOM.client.map.props.MarkerEditor;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkAddEditor;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkEditor;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkTypeEditor;
import com.syrus.AMFICOM.client.map.props.SiteNodeAddEditor;
import com.syrus.AMFICOM.client.map.props.SiteNodeEditor;
import com.syrus.AMFICOM.client.map.props.SiteNodeTypeEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.VoidElement;

/**
 * @version $Revision: 1.15 $, $Date: 2006/06/08 10:23:29 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */

public class MapPropertiesEventHandler extends AbstractEventHandler implements ChangeListener {
	private boolean performProcessing = true;

	public MapPropertiesEventHandler(AbstractPropertiesFrame frame, ApplicationContext aContext) {
		super(frame);
		setContext(aContext);
	}

	@Override
	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null) {
			this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
		}
		this.aContext = aContext;
		this.aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
	}
	
	public void propertyChange(PropertyChangeEvent pce) {
		if(!this.performProcessing)
			return;
		if(this.frame.getParent() == null)
			return;
		if(pce.getPropertyName().equals(MapEvent.MAP_EVENT_TYPE)) {
			MapEvent mapEvent = (MapEvent )pce;
			String mapEventType = mapEvent.getMapEventType();
			Identifiable selectedObject = null;

			MapFrame mapFrame = MapDesktopCommand.findMapFrame(
					(JDesktopPane )this.frame.getParent());
			if(mapFrame == null)
				return;

			StorableObjectEditor previousEditor = this.frame.getCurrentEditor();
			StorableObjectEditor editor = null;
			
			NetMapViewer netMapViewer = mapFrame.getMapViewer();

			if(mapEventType.equals(MapEvent.SELECTION_CHANGED)) {
				LogicalNetLayer lnl = netMapViewer.getLogicalNetLayer();
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
				this.frame.setVisualManager(vm);
				selectedObject = mapElement;
				editor = this.frame.getCurrentEditor();
			}
			else if(mapEventType.equals(MapEvent.MAP_SELECTED)) {
				selectedObject = (Identifiable)pce.getNewValue();
				VisualManager vm = MapVisualManager.getInstance();
				this.frame.setVisualManager(vm);
				editor = this.frame.getCurrentEditor();
			}
			else if(mapEventType.equals(MapEvent.MAP_VIEW_SELECTED)) {
				selectedObject = (Identifiable)pce.getNewValue();
				VisualManager vm = MapViewVisualManager.getInstance();
				this.frame.setVisualManager(vm);
				editor = this.frame.getCurrentEditor();
			}
			else if(mapEventType.equals(MapEvent.OTHER_SELECTED)) {
				selectedObject = (Identifiable)pce.getNewValue();
				VisualManager vm = MapVisualManager.getVisualManager(selectedObject);
				if(vm != null) {
					this.frame.setVisualManager(vm);
					editor = this.frame.getCurrentEditor();
				}
			}
			
			if(editor != null) {
				if(editor instanceof PhysicalLinkEditor) {
					PhysicalLinkEditor linkEditor = (PhysicalLinkEditor )editor;
					linkEditor.setNetMapViewer(netMapViewer);
				}
				else if(editor instanceof PhysicalLinkAddEditor) {
					PhysicalLinkAddEditor linkEditor = (PhysicalLinkAddEditor )editor;
					linkEditor.setNetMapViewer(netMapViewer);
				}
				else if(editor instanceof PhysicalLinkTypeEditor) {
					PhysicalLinkTypeEditor linkTypeEditor = (PhysicalLinkTypeEditor )editor;
					linkTypeEditor.setNetMapViewer(netMapViewer);
				}
				else if(editor instanceof SiteNodeTypeEditor) {
					SiteNodeTypeEditor nodeTypeEditor = (SiteNodeTypeEditor )editor;
					nodeTypeEditor.setNetMapViewer(netMapViewer);
				}
				else if(editor instanceof SiteNodeEditor) {
					SiteNodeEditor siteEditor = (SiteNodeEditor )editor;
					siteEditor.setNetMapViewer(netMapViewer);
				}
				else if(editor instanceof SiteNodeAddEditor) {
					SiteNodeAddEditor siteEditor = (SiteNodeAddEditor )editor;
					siteEditor.setNetMapViewer(netMapViewer);
				}
				else if(editor instanceof CablePathAddEditor) {
					CablePathAddEditor cableEditor = (CablePathAddEditor )editor;
					cableEditor.setNetMapViewer(netMapViewer);
				}
				else if(editor instanceof MarkerEditor) {
					MarkerEditor markerEditor = (MarkerEditor )editor;
					markerEditor.setNetMapViewer(netMapViewer);
				}
				else if(editor instanceof MapEditor) {
					if(selectedObject instanceof VoidElement) {
						selectedObject = ((VoidElement)selectedObject).getMapView().getMap();
					}
				}
				if(selectedObject != null) {
					if(previousEditor != null) {
						previousEditor.removeChangeListener(this);
					}
					editor.addChangeListener(this);
					editor.setObject(selectedObject);
				}
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
		if(this.aContext.getDispatcher() != null) {
			Object object = e.getSource();
			this.performProcessing = false;
			if(object instanceof MapLibrary
					|| object instanceof SiteNodeType
					|| object instanceof PhysicalLinkType) {
				MapFrame mapFrame = MapDesktopCommand.findMapFrame(
						(JDesktopPane )this.frame.getParent());
				if(mapFrame != null) {
					Map map = mapFrame.getMap();
					this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.LIBRARY_SET_CHANGED, map.getMapLibraries()));
				}
			}
			else if(object instanceof MapElement) {
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_CHANGED, object));
			}
			else if(object instanceof Map) {
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_SELECTED, object));
			}
			else if(object instanceof MapView) {
				this.aContext.getDispatcher().firePropertyChange(
						new MapEvent(this, MapEvent.MAP_VIEW_CHANGED, object));
			}
			this.performProcessing = true;
		}
	}
}
