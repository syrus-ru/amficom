/**
 * $Id: CreateUnboundLinkCommandAtomic.java,v 1.12 2005/03/02 12:32:22 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * �������� ������������� �����, �������� �� � ��� � �� ����� - 
 * ��������� �������� 
 * @author $Author: krupenn $
 * @version $Revision: 1.12 $, $Date: 2005/03/02 12:32:22 $
 * @module mapviewclient_v1
 */
public class CreateUnboundLinkCommandAtomic extends MapActionCommand
{
	UnboundLink link;
	
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
	
	public UnboundLink getLink()
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

		this.map = this.logicalNetLayer.getMapView().getMap();
		
		try
		{
			this.link = (UnboundLink )UnboundLink.createInstance(
					this.logicalNetLayer.getUserId(),
					this.startNode, 
					this.endNode, 
					this.logicalNetLayer.getUnboundPen());
	
			this.map.addPhysicalLink(this.link);
			setResult(Command.RESULT_OK);
		}
		catch (ApplicationException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
}

