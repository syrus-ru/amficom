/**
 * $Id: CreatePhysicalLinkCommandAtomic.java,v 1.7 2005/02/01 11:34:56 krupenn Exp $
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
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.Map;

/**
 * �������� ���������� �����, �������� �� � ��� � �� ����� - 
 * ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/02/01 11:34:56 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		return link;
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
			link = PhysicalLink.createInstance(
					new Identifier(aContext.getSessionInterface().getAccessIdentifier().user_id),
					startNode, 
					endNode, 
					logicalNetLayer.getPen());
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		logicalNetLayer.getMapView().getMap().addPhysicalLink(link);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().addPhysicalLink(link);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().removePhysicalLink(link);
	}
}

