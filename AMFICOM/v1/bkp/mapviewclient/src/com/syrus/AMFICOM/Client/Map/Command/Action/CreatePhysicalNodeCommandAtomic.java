/**
 * $Id: CreatePhysicalNodeCommandAtomic.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;

import java.awt.geom.Point2D;

/**
 * �������� ��������������� ����, �������� ��� � ��� � �� ����� - 
 * ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
class CreatePhysicalNodeCommandAtomic extends MapActionCommand
{
	MapPhysicalNodeElement node;
	Point2D.Double point;
	
	public CreatePhysicalNodeCommandAtomic(Point2D.Double point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.point = point;
	}
	
	public MapPhysicalNodeElement getNode()
	{
		return this.node;
	}
	
	public void execute()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		node = new MapPhysicalNodeElement(
				dataSource.GetUId( MapPhysicalNodeElement.typ),
				null,
				point,
				logicalNetLayer.getMapView().getMap(),
				MapNodeElement.DEFAULT_BOUNDS);

		Pool.put(
				MapPhysicalNodeElement.typ, 
				node.getId(), 
				node);

		// ���������� ����������� ��� ��������������� �����������
		// � ������������ � ������� ��������� ����������� �����
		node.setScaleCoefficient(
				logicalNetLayer.getDefaultScale() / logicalNetLayer.getScale());

		// �� ��������� ������������� ���� �� �������
		node.setActive(false);
		logicalNetLayer.getMapView().getMap().addNode(node);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addNode(node);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeNode(node);
	}
}
