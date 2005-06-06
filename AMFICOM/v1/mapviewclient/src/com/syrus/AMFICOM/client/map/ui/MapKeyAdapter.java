/**
 * $Id: MapKeyAdapter.java,v 1.6 2005/06/06 12:20:35 krupenn Exp $
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * обработчик событий клавиатуры в окне карты. Используется для изменения 
 * режима обработки действий (SHIFT, ALT, CTRL) и для удаления выбранных 
 * элементов (DEL)
 * @version $Revision: 1.6 $, $Date: 2005/06/06 12:20:35 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapKeyAdapter extends KeyAdapter 
{
	LogicalNetLayer logicalNetLayer;

	public MapKeyAdapter(LogicalNetLayer adaptee)
	{
		this.logicalNetLayer = adaptee;
	}

	public void keyPressed(KeyEvent ke)
	{
		int code = ke.getKeyCode();

		if (ke.isAltDown())
		{
			this.logicalNetLayer.getMapState().setActionMode(MapState.ALT_LINK_ACTION_MODE);
		}

//		if (ke.isShiftDown() && ke.isControlDown())
//		{
//			lnl.getMapState().setActionMode(MapState.FIXDIST_ACTION_MODE);
//		}
//		else
//		{
			if (ke.isShiftDown())
			{
				this.logicalNetLayer.getMapState().setActionMode(MapState.SELECT_ACTION_MODE);
			}
			if(ke.isControlDown())
			{
				this.logicalNetLayer.getMapState().setActionMode(MapState.MOVE_ACTION_MODE);
			}
//		}

		try {
			if (code == KeyEvent.VK_DELETE)
			{
				this.logicalNetLayer.delete();
			}
			if(ke.isControlDown() && code == KeyEvent.VK_Z)
			{
				this.logicalNetLayer.undo();
			}
			if(ke.isControlDown() && code == KeyEvent.VK_Y)
			{
				this.logicalNetLayer.redo();
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
		this.logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
	}

	public void keyTyped(KeyEvent ke)
	{//empty
	}
}
