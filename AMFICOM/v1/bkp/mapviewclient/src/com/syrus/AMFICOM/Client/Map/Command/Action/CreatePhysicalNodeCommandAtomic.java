/**
 * $Id: CreatePhysicalNodeCommandAtomic.java,v 1.10 2005/01/11 16:43:05 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.Client.Map.Controllers.TopologicalNodeController;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.geom.Point2D;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;

/**
 * �������� ��������������� ����, �������� ��� � ��� � �� ����� - 
 * ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.10 $, $Date: 2005/01/11 16:43:05 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreatePhysicalNodeCommandAtomic extends MapActionCommand
{
	/** ����������� �������������� ���� */
	TopologicalNode node;
	
	/** �������������� ���������� ���� */
	DoublePoint point;
	
	PhysicalLink physicalLink;

	public CreatePhysicalNodeCommandAtomic(PhysicalLink physicalLink, DoublePoint point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.point = point;
		this.physicalLink = physicalLink;
	}
	
	/**
	 * @deprecated
	 */	
	public CreatePhysicalNodeCommandAtomic(DoublePoint point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.point = point;
		this.physicalLink = null;
	}
	
	public TopologicalNode getNode()
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
		
		try
		{
			node = TopologicalNode.createInstance(
				new Identifier(aContext.getSessionInterface().getAccessIdentifier().user_id),
				physicalLink,
				point);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		TopologicalNodeController tnc = (TopologicalNodeController)getLogicalNetLayer().getMapViewController().getController(node);

		// �� ��������� ������������� ���� �� �������
		tnc.setActive(node, false);
		// ���������� ����������� ��� ��������������� �����������
		// � ������������ � ������� ��������� ����������� �����
		tnc.updateScaleCoefficient(node);

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
