/**
 * $Id: CreateUnboundLinkCommandAtomic.java,v 1.5 2004/12/22 16:38:40 krupenn Exp $
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
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * �������� ������������� �����, �������� �� � ��� � �� ����� - 
 * ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class CreateUnboundLinkCommandAtomic extends MapActionCommand
{
	MapUnboundLinkElement link;
	
	AbstractNode startNode;
	AbstractNode endNode;
	
	Map map;
	
	public CreateUnboundLinkCommandAtomic(
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public MapUnboundLinkElement getLink()
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

		DataSourceInterface dataSource = aContext.getDataSource();
		
		map = logicalNetLayer.getMapView().getMap();
		
		try
		{
			link = new MapUnboundLinkElement(
					IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE),
					startNode, 
					endNode, 
					map,
					logicalNetLayer.getUnboundPen());
	
			map.addPhysicalLink(link);
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
		}
	}
	
}

