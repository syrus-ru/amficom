/**
 * $Id: MoveSelectionCommandBundle.java,v 1.4 2004/10/20 10:14:39 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.Iterator;

/**
 * ����������� �������� �� �����. ������� �������� ������ ������ 
 * (CommandBundle), ������������ ��������� ��������.
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/20 10:14:39 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MoveSelectionCommandBundle extends MapActionCommandBundle
{
	/** �������� ����� ������ */
	public static final String END_POINT = "endpoint";
	/** ��������� ����� ������ */
	public static final String START_POINT = "startpoint";
	/** ���������� �������� �� ��� ������� */
	public static final String DELTA_X = "deltax";
	/** ���������� �������� �� ��� ������� */
	public static final String DELTA_Y = "deltay";

	/**
	 * ��������� ����� ��������
	 */
	Point startPoint;
	
	/**
	 * �������� ����� ��������
	 */
	Point endPoint;
	
	/**
	 * ���������� �������� �� ��� �������
	 */
	double deltaX = 0.0D;
	
	/**
	 * ���������� �������� �� ��� �������
	 */
	double deltaY = 0.0D;

	public MoveSelectionCommandBundle(Point point)
	{
		startPoint = point;
	}

	public MoveSelectionCommandBundle(LogicalNetLayer logicalNetLayer)
	{
		super();
		super.setLogicalNetLayer(logicalNetLayer);
	}
	
	/**
	 * ��� ��������� ���������� ����������� ��������� ���������� ���� ��������
	 * � �����
	 */
	public void setParameter(String field, Object value)
	{
		if(field.equals(DELTA_X))
		{
			deltaX = Double.parseDouble((String )value);
			super.setParameter(field, value);
		}
		else
		if(field.equals(DELTA_Y))
		{
			deltaY = Double.parseDouble((String )value);
			super.setParameter(field, value);
		}
		else
		if(field.equals(START_POINT))
		{
			startPoint = (Point )value;
			endPoint = (Point )value;
			//����������� ��������
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(deltaX));
			super.setParameter(DELTA_Y, String.valueOf(deltaY));
		}
		else
		if(field.equals(END_POINT))
		{
			endPoint = (Point )value;
			//����������� ��������
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(deltaX));
			super.setParameter(DELTA_Y, String.valueOf(deltaY));
		}
			
	}

	/**
	 * ��� ��������� ����������� �������� ���� �������� ������� �� �����������
	 * ��������� ��������. ���������� �������� �������������� ������
	 * ��� ������ execute()
	 */
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		super.setLogicalNetLayer(logicalNetLayer);

		this.setElements();
	}
	
	/**
	 * �������� ���������� �������� �� ��������� � �������� ������ ������
	 */
	protected void setShift()
	{
		Point2D.Double sp = logicalNetLayer.convertScreenToMap(startPoint);
		Point2D.Double ep = logicalNetLayer.convertScreenToMap(endPoint);
		deltaX = ep.getX() - sp.getX();
		deltaY = ep.getY() - sp.getY();
	}
	
	/**
	 * ������� ��������� ������� �� ����������� ��� ���� ����������
	 * �������� ��������
	 */
	protected void setElements()
	{
		Iterator e = logicalNetLayer.getMapView().getMap().getNodes().iterator();

		while (e.hasNext() )
		{
			MapNodeElement node = (MapNodeElement )e.next();
			if (node.isSelected())
			{
				if(node instanceof MapMarkElement)
				{
					MapMarkElement mme = (MapMarkElement )node;
					super.add(new MoveMarkCommand(mme));
				}
				super.add(new MoveNodeCommand(node));
			}
		}
	}
	
	public void execute()
	{
		super.execute();
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
