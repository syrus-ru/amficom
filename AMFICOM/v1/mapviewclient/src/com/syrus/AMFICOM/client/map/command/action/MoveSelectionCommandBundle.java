/**
 * $Id: MoveSelectionCommandBundle.java,v 1.22 2005/09/16 14:53:33 krupenn Exp $
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

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * ����������� �������� �� �����. ������� �������� ������ ������ 
 * (CommandBundle), ������������ ��������� ��������.
 * @author $Author: krupenn $
 * @version $Revision: 1.22 $, $Date: 2005/09/16 14:53:33 $
 * @module mapviewclient
 */
public class MoveSelectionCommandBundle extends MapActionCommandBundle
{
	/** �������� ����� ������ */
	public static final String END_POINT = "endpoint"; //$NON-NLS-1$
	/** ��������� ����� ������ */
	public static final String START_POINT = "startpoint"; //$NON-NLS-1$
	/** ���������� �������� �� ��� ������� */
	public static final String DELTA_X = "deltax"; //$NON-NLS-1$
	/** ���������� �������� �� ��� ������� */
	public static final String DELTA_Y = "deltay"; //$NON-NLS-1$

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

	public MoveSelectionCommandBundle(NetMapViewer netMapViewer)
	{
		super();
		setNetMapViewer(netMapViewer);
	}
	
	/**
	 * ��� ��������� ���������� ����������� ��������� ���������� ���� ��������
	 * � �����
	 */
	@Override
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
	@Override
	public void setNetMapViewer(NetMapViewer netMapViewer)
	{
		super.setNetMapViewer(netMapViewer);

		this.setElements();
	}
	
	/**
	 * �������� ���������� �������� �� ��������� � �������� ������ ������
	 */
	protected void setShift()
	{
		try {
			DoublePoint sp = this.logicalNetLayer.getConverter().convertScreenToMap(this.startPoint);
			DoublePoint ep = this.logicalNetLayer.getConverter().convertScreenToMap(this.endPoint);
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
	protected void setElements() {
		Map map = this.logicalNetLayer.getMapView().getMap();
		Iterator iter = map.getSelectedElements().iterator();

		while (iter.hasNext() ) {
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
	
	@Override
	public void execute() {
		super.execute();
	}
}
