/**
 * $Id: RemoveNodeLinkCommandAtomic.java,v 1.12 2005/08/17 14:14:17 arseniy Exp $
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
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.util.Log;

/**
 * �������� ��������� ����� ����� �� ����� - ��������� �������� 
 * @author $Author: arseniy $
 * @version $Revision: 1.12 $, $Date: 2005/08/17 14:14:17 $
 * @module mapviewclient
 */
public class RemoveNodeLinkCommandAtomic extends MapActionCommand
{
	NodeLink nodeLink;
	
	public RemoveNodeLinkCommandAtomic(NodeLink nodeLink)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.nodeLink = nodeLink;
	}
	
	public NodeLink getNodeLink()
	{
		return this.nodeLink;
	}
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		this.logicalNetLayer.getMapView().getMap().removeNodeLink(this.nodeLink);
		setResult(Command.RESULT_OK);
	}
	
	@Override
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().removeNodeLink(this.nodeLink);
	}
	
	@Override
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().addNodeLink(this.nodeLink);
	}
}

