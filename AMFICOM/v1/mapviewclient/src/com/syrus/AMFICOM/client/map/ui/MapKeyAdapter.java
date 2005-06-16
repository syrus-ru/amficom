/**
 * $Id: MapKeyAdapter.java,v 1.7 2005/06/16 10:57:21 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * обработчик событий клавиатуры в окне карты. Используется для изменения 
 * режима обработки действий (SHIFT, ALT, CTRL) и для удаления выбранных 
 * элементов (DEL)
 * @version $Revision: 1.7 $, $Date: 2005/06/16 10:57:21 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapKeyAdapter extends KeyAdapter 
{
	private final NetMapViewer viewer;

	public MapKeyAdapter(NetMapViewer viewer)
	{
		this.viewer = viewer;
	}

	public void keyPressed(KeyEvent ke)
	{
		int code = ke.getKeyCode();

		if (ke.isAltDown())
		{
			this.viewer.getLogicalNetLayer().getMapState().setActionMode(MapState.ALT_LINK_ACTION_MODE);
		}

//		if (ke.isShiftDown() && ke.isControlDown())
//		{
//			lnl.getMapState().setActionMode(MapState.FIXDIST_ACTION_MODE);
//		}
//		else
//		{
			if (ke.isShiftDown())
			{
				this.viewer.getLogicalNetLayer().getMapState().setActionMode(MapState.SELECT_ACTION_MODE);
			}
			if(ke.isControlDown())
			{
				this.viewer.getLogicalNetLayer().getMapState().setActionMode(MapState.MOVE_ACTION_MODE);
			}
//		}

		try {
			if (code == KeyEvent.VK_DELETE)
			{
				this.viewer.getLogicalNetLayer().delete();
				this.viewer.repaint(false);
			}
			if(ke.isControlDown() && code == KeyEvent.VK_Z)
			{
				this.viewer.getLogicalNetLayer().undo();
				this.viewer.repaint(false);
			}
			if(ke.isControlDown() && code == KeyEvent.VK_Y)
			{
				this.viewer.getLogicalNetLayer().redo();
				this.viewer.repaint(false);
			}
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void keyReleased(KeyEvent ke)
	{
		this.viewer.getLogicalNetLayer().getMapState().setActionMode(MapState.NULL_ACTION_MODE);
	}

	public void keyTyped(KeyEvent ke)
	{//empty
	}
}
