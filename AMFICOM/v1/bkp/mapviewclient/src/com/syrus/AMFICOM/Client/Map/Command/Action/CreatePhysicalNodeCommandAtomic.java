/**
 * $Id: CreatePhysicalNodeCommandAtomic.java,v 1.6 2004/12/07 17:05:54 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.TopologicalNodeController;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.geom.Point2D;

/**
 * �������� ��������������� ����, �������� ��� � ��� � �� ����� - 
 * ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/07 17:05:54 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreatePhysicalNodeCommandAtomic extends MapActionCommand
{
	/** ����������� �������������� ���� */
	MapPhysicalNodeElement node;
	
	/** �������������� ���������� ���� */
	DoublePoint point;
	
	public CreatePhysicalNodeCommandAtomic(DoublePoint point)
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		DataSourceInterface dataSource = aContext.getDataSource();
		
		node = new MapPhysicalNodeElement(
				dataSource.GetUId( MapPhysicalNodeElement.typ),
				null,
				point,
				logicalNetLayer.getMapView().getMap());

		Pool.put(MapPhysicalNodeElement.typ, node.getId(), node);

		TopologicalNodeController tnc = (TopologicalNodeController )getLogicalNetLayer().getMapViewController().getController(node);

		// ���������� ����������� ��� ��������������� �����������
		// � ������������ � ������� ��������� ����������� �����
		tnc.updateScaleCoefficient(node);

		// �� ��������� ������������� ���� �� �������
		node.setActive(false);
		logicalNetLayer.getMapView().getMap().addNode(node);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addNode(node);
		Pool.put(MapPhysicalNodeElement.typ, node.getId(), node);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeNode(node);
		Pool.remove(MapPhysicalNodeElement.typ, node.getId());
	}
}
