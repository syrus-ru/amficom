/**
 * $Id: MoveNodeCommand.java,v 1.6 2004/12/23 16:57:59 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.AbstractNode;

import java.awt.geom.Point2D;

/**
 * ����������� ����
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/23 16:57:59 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveNodeCommand extends MapActionCommand
{
	/**
	 * ��������� ������� ������������� ��������
	 */
	DoublePoint location;

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
		location = node.getLocation();
	}

	public void setParameter(String field, Object value)
	{
		if(field.equals(MoveSelectionCommandBundle.DELTA_X))
		{
			deltaX = Double.parseDouble((String )value);
			execute();
		}
		else
		if(field.equals(MoveSelectionCommandBundle.DELTA_Y))
		{
			deltaY = Double.parseDouble((String )value);
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
		
		DoublePoint dp = node.getLocation();

		dp.setLocation(dp.getX() + deltaX, dp.getY() + deltaY);

		node.setLocation(dp);
	}
	
	public void undo()
	{
		node.setLocation(location);
	}
}
