/**
 * $Id: CreatePhysicalLinkCommandAtomic.java,v 1.13 2005/06/06 12:20:30 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * �������� ���������� �����, �������� �� � ��� � �� ����� - 
 * ��������� �������� 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/06/06 12:20:30 $
 * @module mapviewclient_v1
 */
public class CreatePhysicalLinkCommandAtomic extends MapActionCommand
{
	/** ����������� ����� */
	PhysicalLink link;
	
	/** ��������� ���� */
	AbstractNode startNode;
	
	/** �������� ���� */
	AbstractNode endNode;
	
	public CreatePhysicalLinkCommandAtomic(
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public PhysicalLink getLink()
	{
		return this.link;
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
			this.link = PhysicalLink.createInstance(
					this.logicalNetLayer.getUserId(),
					this.startNode, 
					this.endNode, 
					this.logicalNetLayer.getCurrentPhysicalLinkType());
			this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
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
		this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().removePhysicalLink(this.link);
	}
}

