/**
 * $Id: MapElementStateChangeCommand.java,v 1.7 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;

/**
 * ��������� ������� ��������� ��������� �������� ����� 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
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
		return this.me;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		this.me.revert(this.finalState);
		setResult(Command.RESULT_OK);
	}
	
	public void redo()
	{
		this.me.revert(this.finalState);
	}
	
	public void undo()
	{
		this.me.revert(this.initialState);
	}
}
