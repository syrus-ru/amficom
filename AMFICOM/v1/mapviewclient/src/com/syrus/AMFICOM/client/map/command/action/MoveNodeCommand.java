/**
 * $Id: MoveNodeCommand.java,v 1.11 2005/06/06 12:20:30 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;

/**
 * ����������� ����.
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/06/06 12:20:30 $
 * @module mapviewclient_v1
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

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		DoublePoint nodeLocation = this.node.getLocation();

		nodeLocation.setLocation(this.initialLocation.getX() + this.deltaX, this.initialLocation.getY() + this.deltaY);

		this.node.setLocation(nodeLocation);
		setResult(Command.RESULT_OK);
	}
	
	public void undo()
	{
		this.node.setLocation(this.initialLocation);
	}
}
