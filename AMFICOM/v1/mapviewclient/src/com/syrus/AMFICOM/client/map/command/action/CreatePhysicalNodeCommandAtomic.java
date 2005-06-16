/**
 * $Id: CreatePhysicalNodeCommandAtomic.java,v 1.17 2005/06/16 10:57:19 krupenn Exp $
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
import com.syrus.AMFICOM.client.map.controllers.TopologicalNodeController;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * �������� ��������������� ����, �������� ��� � ��� � �� ����� - 
 * ��������� �������� 
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/06/16 10:57:19 $
 * @module mapviewclient_v1
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

		try
		{
			this.node = TopologicalNode.createInstance(
					LoginManager.getUserId(),
				this.physicalLink,
				this.point);

			TopologicalNodeController tnc = (TopologicalNodeController)getLogicalNetLayer().getMapViewController().getController(this.node);
	
			// �� ��������� ������������� ���� �� �������
			tnc.setActive(this.node, false);
			// ���������� ����������� ��� ��������������� �����������
			// � ������������ � ������� ��������� ����������� �����
			tnc.updateScaleCoefficient(this.node);
	
			this.logicalNetLayer.getMapView().getMap().addNode(this.node);
			setResult(Command.RESULT_OK);
		}
		catch (CreateObjectException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
	public void redo() 
	{
		this.logicalNetLayer.getMapView().getMap().addNode(this.node);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().removeNode(this.node);
	}
}
