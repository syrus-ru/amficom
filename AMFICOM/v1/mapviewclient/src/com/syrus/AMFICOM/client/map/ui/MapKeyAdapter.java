/**
 * $Id: MapKeyAdapter.java,v 1.14 2005/08/19 15:43:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
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
 * обработчик событий клавиатуры в окне карты. Используется для изменения 
 * режима обработки действий (SHIFT, ALT, CTRL) и для удаления выбранных 
 * элементов (DEL)
 * @version $Revision: 1.14 $, $Date: 2005/08/19 15:43:32 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public final class MapKeyAdapter extends KeyAdapter 
{
	private final NetMapViewer viewer;

	public MapKeyAdapter(NetMapViewer viewer)
	{
		this.viewer = viewer;
	}

	@Override
	public void keyPressed(KeyEvent ke)
	{
		int code = ke.getKeyCode();

		LogicalNetLayer logicalNetLayer = this.viewer.getLogicalNetLayer();
		if (ke.isAltDown())
		{
			logicalNetLayer.getMapState().setActionMode(MapState.ALT_LINK_ACTION_MODE);
		}

//		if (ke.isShiftDown() && ke.isControlDown())
//		{
//			lnl.getMapState().setActionMode(MapState.FIXDIST_ACTION_MODE);
//		}
//		else
//		{
			if (ke.isShiftDown())
			{
				logicalNetLayer.getMapState().setActionMode(MapState.SELECT_ACTION_MODE);
			}
			if(ke.isControlDown())
			{
				logicalNetLayer.getMapState().setActionMode(MapState.MOVE_ACTION_MODE);
			}
//		}

		try {
			if (code == KeyEvent.VK_ESCAPE)
			{
				this.viewer.cancelMode();
			}
			if (code == KeyEvent.VK_DELETE)
			{
				this.viewer.delete();
			}
			if(ke.isControlDown() && code == KeyEvent.VK_Z)
			{
				logicalNetLayer.undo();
			}
			if(ke.isControlDown() && code == KeyEvent.VK_Y)
			{
				logicalNetLayer.redo();
			}
			if(ke.isControlDown() && code == KeyEvent.VK_1)
			{
				Rectangle2D.Double visibleBounds = this.viewer.getVisibleBounds();
				long f;
				long d;
				f = System.currentTimeMillis();
				java.util.Collection nodeLinks = logicalNetLayer.getMapView().getMap().getNodeLinks();
				d = System.currentTimeMillis();
				System.out.println("get node links in " + String.valueOf(d - f) + " ms");
				f = System.currentTimeMillis();

				for(Iterator iter = nodeLinks.iterator(); iter.hasNext();) {
					NodeLink nodeLink = (NodeLink )iter.next();
					logicalNetLayer.getMapViewController().getController(nodeLink).isElementVisible(nodeLink, visibleBounds);
				}
				d = System.currentTimeMillis();
				System.out.println("node links::isVisible performed in " + String.valueOf(d - f) + " ms (total) with average of " + String.valueOf((d - f) / nodeLinks.size() + " ms"));
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
	public void keyReleased(KeyEvent ke)
	{
		this.viewer.getLogicalNetLayer().getMapState().setActionMode(MapState.NULL_ACTION_MODE);
	}

	@Override
	public void keyTyped(KeyEvent ke)
	{//empty
	}
}
