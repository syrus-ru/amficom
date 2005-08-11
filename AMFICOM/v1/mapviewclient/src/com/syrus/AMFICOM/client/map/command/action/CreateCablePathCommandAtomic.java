/**
 * $Id: CreateCablePathCommandAtomic.java,v 1.19 2005/08/11 12:43:29 arseniy Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * �������� ���� ��������� ������, �������� ��� � ��� � �� ����� - 
 * ��������� �������� 
 * 
 * 
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.19 $, $Date: 2005/08/11 12:43:29 $
 * @module mapviewclient
 */
public class CreateCablePathCommandAtomic extends MapActionCommand
{
	/** ��������� ���� */
	CablePath cablePath;
	
	/** ������ */
	SchemeCableLink schemeCableLink;
	
	/** ��������� ���� */
	AbstractNode startNode;
	
	/** �������� ���� */
	AbstractNode endNode;
	
	public CreateCablePathCommandAtomic(
			SchemeCableLink schemeCableLink,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemeCableLink = schemeCableLink;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public CablePath getPath()
	{
		return this.cablePath;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);
		
		this.cablePath = com.syrus.AMFICOM.mapview.CablePath.createInstance(
				this.schemeCableLink,
				this.startNode, 
				this.endNode);

		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
		setResult(Command.RESULT_OK);
	}
	
	public void redo()
	{
		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
	}
}

