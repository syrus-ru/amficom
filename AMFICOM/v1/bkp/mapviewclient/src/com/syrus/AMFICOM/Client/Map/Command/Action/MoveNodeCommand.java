/**
 * $Id: MoveNodeCommand.java,v 1.3 2004/10/19 10:07:43 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;

import java.awt.geom.Point2D;

/**
 * ����������� ����
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/19 10:07:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveNodeCommand extends MapActionCommand
{
	/**
	 * ��������� ������� ������������� ��������
	 */
	Point2D.Double anchor;

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
	MapNodeElement node;

	public MoveNodeCommand(MapNodeElement node)
	{
		super(MapActionCommand.ACTION_MOVE_SELECTION);
		this.node = node;

		// ��������� ��������� ���������
		anchor = node.getAnchor();
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
		
		node.setAnchor(new Point2D.Double(anchor.x + deltaX, anchor.y + deltaY));
	}
	
	public void undo()
	{
		node.setAnchor(anchor);
	}
}
