/**
 * $Id: CreateNodeLinkCommandAtomic.java,v 1.7 2005/02/01 11:34:56 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * �������� ��������� ����� �����, �������� �� � ��� � �� ����� - 
 * ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/02/01 11:34:56 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		return nodeLink;
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
			nodeLink = NodeLink.createInstance(
					new Identifier(aContext.getSessionInterface().getAccessIdentifier().user_id),
					physicalLink, 
					startNode, 
					endNode);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		logicalNetLayer.getMapView().getMap().addNodeLink(nodeLink);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addNodeLink(nodeLink);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removeNodeLink(nodeLink);
	}
}

