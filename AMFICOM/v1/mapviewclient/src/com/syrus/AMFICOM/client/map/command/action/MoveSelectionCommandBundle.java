/**
 * $Id: MoveSelectionCommandBundle.java,v 1.11 2005/06/06 12:20:30 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;

/**
 * ����������� �������� �� �����. ������� �������� ������ ������ 
 * (CommandBundle), ������������ ��������� ��������.
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/06/06 12:20:30 $
 * @module mapviewclient_v1
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
		this.startPoint = point;
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
			this.deltaX = Double.parseDouble((String )value);
			super.setParameter(field, value);
		}
		else
		if(field.equals(DELTA_Y))
		{
			this.deltaY = Double.parseDouble((String )value);
			super.setParameter(field, value);
		}
		else
		if(field.equals(START_POINT))
		{
			this.startPoint = (Point )value;
			this.endPoint = (Point )value;
			//����������� ��������
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(this.deltaX));
			super.setParameter(DELTA_Y, String.valueOf(this.deltaY));
		}
		else
		if(field.equals(END_POINT))
		{
			this.endPoint = (Point )value;
			//����������� ��������
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(this.deltaX));
			super.setParameter(DELTA_Y, String.valueOf(this.deltaY));
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
		try {
			DoublePoint sp = this.logicalNetLayer.convertScreenToMap(this.startPoint);
			DoublePoint ep = this.logicalNetLayer.convertScreenToMap(this.endPoint);
			this.deltaX = ep.getX() - sp.getX();
			this.deltaY = ep.getY() - sp.getY();
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ������� ��������� ������� �� ����������� ��� ���� ����������
	 * �������� ��������
	 */
	protected void setElements()
	{
		Map map = this.logicalNetLayer.getMapView().getMap();
		Iterator iter = map.getSelectedElements().iterator();

		while (iter.hasNext() )
		{
			MapElement mapElement = (MapElement )iter.next();
			if(mapElement instanceof AbstractNode) {
				AbstractNode node = (AbstractNode)mapElement;
				if(node instanceof Mark) {
					Mark mme = (Mark)node;
					super.add(new MoveMarkCommand(mme));
				}
				// do not move external nodes
				else if(!map.getExternalNodes().contains(node))
					super.add(new MoveNodeCommand(node));
			}
		}
	}
	
	public void execute()
	{
		super.execute();
		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
