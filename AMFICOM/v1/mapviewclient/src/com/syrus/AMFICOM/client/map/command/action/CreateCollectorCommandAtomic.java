/**
 * $Id: CreateCollectorCommandAtomic.java,v 1.9 2005/02/18 12:19:44 krupenn Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.Collector;

/**
 * �������� ����������, �������� ��� � ��� � �� ����� - 
 * ��������� �������� 
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/02/18 12:19:44 $
 * @module mapviewclient_v1
 */
public class CreateCollectorCommandAtomic extends MapActionCommand
{
	/** ��������� */
	Collector collector;

	/** �������� */
	String name;
	
	public CreateCollectorCommandAtomic(String name)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.name = name;
	}
	
	public Collector getCollector()
	{
		return this.collector;
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
			this.collector = Collector.createInstance(
					this.logicalNetLayer.getUserId(),
					this.logicalNetLayer.getMapView().getMap(),
					this.name,
					"");
			this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
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
		this.logicalNetLayer.getMapView().getMap().addCollector(this.collector);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().removeCollector(this.collector);
	}
}

