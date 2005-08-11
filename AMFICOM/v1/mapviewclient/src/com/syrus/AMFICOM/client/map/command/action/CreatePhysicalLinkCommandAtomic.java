/**
 * $Id: CreatePhysicalLinkCommandAtomic.java,v 1.19 2005/08/11 12:43:29 arseniy Exp $
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * �������� ���������� �����, �������� �� � ��� � �� ����� - 
 * ��������� �������� 
 * @author $Author: arseniy $
 * @version $Revision: 1.19 $, $Date: 2005/08/11 12:43:29 $
 * @module mapviewclient
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try
		{
			this.link = PhysicalLink.createInstance(
					LoginManager.getUserId(),
					this.startNode.getId(), 
					this.endNode.getId(), 
					this.logicalNetLayer.getCurrentPhysicalLinkType());
			this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
			setResult(Command.RESULT_OK);
		}
		catch (CreateObjectException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		} catch(ApplicationException e) {
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

