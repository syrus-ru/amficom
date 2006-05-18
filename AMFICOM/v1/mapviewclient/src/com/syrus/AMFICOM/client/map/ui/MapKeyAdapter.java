/*-
 * $$Id: MapKeyAdapter.java,v 1.21 2006/02/15 12:54:38 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.util.Log;

/**
 * обработчик событий клавиатуры в окне карты. »спользуетс€ дл€ изменени€ режима
 * обработки действий (SHIFT, ALT, CTRL) и дл€ удалени€ выбранных элементов
 * (DEL)
 * 
 * @version $Revision: 1.21 $, $Date: 2006/02/15 12:54:38 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapKeyAdapter extends KeyAdapter {
	private final NetMapViewer viewer;

	public MapKeyAdapter(NetMapViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		int code = ke.getKeyCode();

		LogicalNetLayer logicalNetLayer = this.viewer.getLogicalNetLayer();
		if(ke.isAltDown()) {
			if(logicalNetLayer.getMapState().getActionMode() == MapState.NULL_ACTION_MODE)
				logicalNetLayer.getMapState().setActionMode(
						MapState.ALT_LINK_ACTION_MODE);
		}

		// if (ke.isShiftDown() && ke.isControlDown())
		// {
		// lnl.getMapState().setActionMode(MapState.FIXDIST_ACTION_MODE);
		// }
		// else
		// {
		if(ke.isShiftDown()) {
			if(logicalNetLayer.getMapState().getActionMode() == MapState.NULL_ACTION_MODE)
				logicalNetLayer.getMapState().setActionMode(
						MapState.SELECT_ACTION_MODE);
		}
		if(ke.isControlDown()) {
			if(logicalNetLayer.getMapState().getActionMode() == MapState.NULL_ACTION_MODE)
				logicalNetLayer.getMapState().setActionMode(
						MapState.MOVE_ACTION_MODE);
			if(code == KeyEvent.VK_A) {
				final Set<MapElement> selectedElements = logicalNetLayer.getMapView().getMap().getSelectedElements();
				for(MapElement mapElement : selectedElements) {
					this.viewer.animateTimer.add(mapElement);
				}
			}
			if(code == KeyEvent.VK_R) {
				final Set<MapElement> selectedElements = logicalNetLayer.getMapView().getMap().getSelectedElements();
				for(MapElement mapElement : selectedElements) {
					this.viewer.animateTimer.remove(mapElement);
				}
			}
			if(code == KeyEvent.VK_U) {
				final Set<MapElement> selectedElements = logicalNetLayer.getMapView().getMap().getSelectedElements();
				if(selectedElements.size() == 1) {
					MapElement mapElement = selectedElements.iterator().next();
					XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance(); 
					mapElement.getId().getXmlTransferable(xmlId, "ucm");
					Log.debugMessage("un for '" + mapElement.getName() 
							+ "' is '" + xmlId.getStringValue() + "'", Log.DEBUGLEVEL09);
				}
			}
		}
		// }

		try {
			if(code == KeyEvent.VK_ESCAPE) {
				this.viewer.cancelMode();
			}
			if(code == KeyEvent.VK_DELETE) {
				this.viewer.delete();
			}
			if(ke.isControlDown() && code == KeyEvent.VK_Z) {
				logicalNetLayer.undo();
			}
			if(ke.isControlDown() && code == KeyEvent.VK_Y) {
				logicalNetLayer.redo();
			}
			if(ke.isControlDown() && code == KeyEvent.VK_1) {
				Rectangle2D.Double visibleBounds = this.viewer
						.getVisibleBounds();
				long f;
				long d;
				f = System.currentTimeMillis();
				java.util.Collection nodeLinks = logicalNetLayer.getMapView()
						.getMap().getNodeLinks();
				d = System.currentTimeMillis();
				System.out
						.println("get node links in " + String.valueOf(d - f) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$
				f = System.currentTimeMillis();

				for(Iterator iter = nodeLinks.iterator(); iter.hasNext();) {
					NodeLink nodeLink = (NodeLink) iter.next();
					logicalNetLayer.getMapViewController().getController(
							nodeLink).isElementVisible(nodeLink, visibleBounds);
				}
				d = System.currentTimeMillis();
				System.out
						.println("node links::isVisible performed in " + String.valueOf(d - f) + " ms (total) with average of " + String.valueOf((d - f) / nodeLinks.size() + " ms")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		} catch(MapConnectionException e) {
			Log.errorMessage(e);
		} catch(MapDataException e) {
			Log.errorMessage(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		if(this.viewer.getLogicalNetLayer().getMapState().getMouseMode() == MapState.MOUSE_NONE)
			this.viewer.getLogicalNetLayer().getMapState().setActionMode(
					MapState.NULL_ACTION_MODE);
	}

	@Override
	public void keyTyped(KeyEvent ke) {// empty
	}
}
