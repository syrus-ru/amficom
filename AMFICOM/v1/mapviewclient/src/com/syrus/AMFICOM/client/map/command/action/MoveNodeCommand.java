/**
 * $Id: MoveNodeCommand.java,v 1.17 2005/08/17 14:14:16 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * ����������� ����.
 * @author $Author: arseniy $
 * @version $Revision: 1.17 $, $Date: 2005/08/17 14:14:16 $
 * @module mapviewclient
 */
public class MoveNodeCommand extends MapActionCommand
{
	/**
	 * ��������� ������� ������������� ��������
	 */
	DoublePoint initialLocation;

	/**
	 * ���������� �������� �� ��� �������
	 */
	double deltaX = 0.0D;

	/**
	 * ���������� �������� �� ��� �������
	 */
	double deltaY = 0.0D;

	/**
	 * ������������ �������
	 */	
	AbstractNode node;

	public MoveNodeCommand(AbstractNode node)
	{
		super(MapActionCommand.ACTION_MOVE_SELECTION);
		this.node = node;

		// ��������� ��������� ���������
		this.initialLocation = node.getLocation();
	}

	@Override
	public void setParameter(String field, Object value)
	{
		if(field.equals(MoveSelectionCommandBundle.DELTA_X))
		{
			this.deltaX = Double.parseDouble((String )value);
			execute();
		}
		else
		if(field.equals(MoveSelectionCommandBundle.DELTA_Y))
		{
			this.deltaY = Double.parseDouble((String )value);
			execute();
		}
	}

	@Override
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
		
		DoublePoint nodeLocation = this.node.getLocation();

		nodeLocation.setLocation(this.initialLocation.getX() + this.deltaX, this.initialLocation.getY() + this.deltaY);

		this.node.setLocation(nodeLocation);
		setResult(Command.RESULT_OK);
	}
	
	@Override
	public void undo()
	{
		this.node.setLocation(this.initialLocation);
	}
}
