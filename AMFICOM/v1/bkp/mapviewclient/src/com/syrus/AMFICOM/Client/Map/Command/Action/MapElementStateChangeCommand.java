/**
 * $Id: MapElementStateChangeCommand.java,v 1.2 2004/09/17 11:39:25 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementState;

/**
 * ��������� ������� ��������� ��������� �������� ����� 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/17 11:39:25 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapElementStateChangeCommand extends MapActionCommand
{
	MapElement me;
	MapElementState initialState;
	MapElementState finalState;
	
	public MapElementStateChangeCommand(MapElement me, MapElementState initialState, MapElementState finalState)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.me = me;
		this.initialState = initialState;
		this.finalState = finalState;
	}
	
	public MapElement getElement()
	{
		return me;
	}
	
	public void execute()
	{
		me.revert(finalState);
	}
	
	public void redo()
	{
		me.revert(finalState);
	}
	
	public void undo()
	{
		me.revert(initialState);
	}
}
