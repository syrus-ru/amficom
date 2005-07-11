/**
 * $Id: CreateNodeLinkCommandAtomic.java,v 1.15 2005/07/11 13:18:04 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * �������� ��������� ����� �����, �������� �� � ��� � �� ����� - 
 * ��������� �������� 
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/07/11 13:18:04 $
 * @module mapviewclient_v1
 */
public class CreateNodeLinkCommandAtomic extends MapActionCommand
{
	/**
	 * ����������� �������� �����
	 */
	NodeLink nodeLink;
	
	AbstractNode startNode;
	AbstractNode endNode;
	PhysicalLink physicalLink;
	
	public CreateNodeLinkCommandAtomic(
			PhysicalLink physicalLink,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.physicalLink = physicalLink;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public NodeLink getNodeLink()
	{
		return this.nodeLink;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try
		{
			this.nodeLink = NodeLink.createInstance(
					LoginManager.getUserId(),
					this.physicalLink, 
					this.startNode, 
					this.endNode);

			this.logicalNetLayer.getMapView().getMap().addNodeLink(this.nodeLink);
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
		this.logicalNetLayer.getMapView().getMap().addNodeLink(this.nodeLink);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().removeNodeLink(this.nodeLink);
	}
}

