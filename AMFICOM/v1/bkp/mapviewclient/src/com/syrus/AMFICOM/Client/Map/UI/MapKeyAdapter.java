/**
 * $Id: MapKeyAdapter.java,v 1.2 2004/09/15 08:21:49 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * ���������� ������� ���������� � ���� �����. ������������ ��� ��������� 
 * ������ ��������� �������� (SHIFT, ALT, CTRL) � ��� �������� ��������� 
 * ��������� (DEL)
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/15 08:21:49 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapKeyAdapter extends KeyAdapter 
{
	LogicalNetLayer lnl;

	public MapKeyAdapter(LogicalNetLayer adaptee)
	{
		this.lnl = adaptee;
	}

	public void keyPressed(KeyEvent ke)
	{
		int code = ke.getKeyCode();

		if (ke.isAltDown())
		{
			lnl.getMapState().setActionMode(MapState.ALT_LINK_ACTION_MODE);
		}

		if (ke.isShiftDown() && ke.isControlDown())
		{
			lnl.getMapState().setActionMode(MapState.FIXDIST_ACTION_MODE);
		}
		else
		{
			if (ke.isShiftDown())
			{
				lnl.getMapState().setActionMode(MapState.SELECT_ACTION_MODE);
			}
			if(ke.isControlDown())
			{
				lnl.getMapState().setActionMode(MapState.MOVE_ACTION_MODE);
			}
		}

		if (code == KeyEvent.VK_DELETE)
		{
			lnl.delete();
		}
		if(ke.isControlDown() && code == KeyEvent.VK_Z)
		{
			lnl.undo();
		}
		if(ke.isControlDown() && code == KeyEvent.VK_Y)
		{
			lnl.redo();
		}
	}

	public void keyReleased(KeyEvent ke)
	{
		lnl.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
	}

	public void keyTyped(KeyEvent ke)
	{
	}
}
