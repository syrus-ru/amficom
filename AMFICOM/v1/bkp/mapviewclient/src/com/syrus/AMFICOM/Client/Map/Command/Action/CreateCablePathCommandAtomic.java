/**
 * $Id: CreateCablePathCommandAtomic.java,v 1.12 2005/03/16 12:54:57 bass Exp $
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
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * �������� ���� ��������� ������, �������� ��� � ��� � �� ����� - 
 * ��������� �������� 
 * 
 * 
 * 
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/03/16 12:54:57 $
 * @module mapviewclient_v1
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
			SchemeCableLink scl,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemeCableLink = scl;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public CablePath getPath()
	{
		return this.cablePath;
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
			this.cablePath = com.syrus.AMFICOM.mapview.CablePath.createInstance(
					this.schemeCableLink,
					this.startNode, 
					this.endNode, 
					this.logicalNetLayer.getMapView());
	
			this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
			setResult(Command.RESULT_OK);
		}
		catch (ApplicationException e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
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

