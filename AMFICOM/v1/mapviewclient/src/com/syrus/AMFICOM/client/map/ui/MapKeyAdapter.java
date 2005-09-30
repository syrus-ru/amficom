/*-
 * $$Id: MapKeyAdapter.java,v 1.17 2005/09/30 16:08:41 krupenn Exp $$
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

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.NodeLink;

/**
 * ���������� ������� ���������� � ���� �����. ������������ ��� ��������� ������
 * ��������� �������� (SHIFT, ALT, CTRL) � ��� �������� ��������� ���������
 * (DEL)
 * 
 * @version $Revision: 1.17 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
