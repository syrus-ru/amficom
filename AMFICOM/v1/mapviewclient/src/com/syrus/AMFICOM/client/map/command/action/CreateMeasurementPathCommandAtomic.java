/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.13 2005/04/28 13:07:29 krupenn Exp $
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
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * �������� ��������� �������������� ���� 
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/04/28 13:07:29 $
 * @module mapviewclient_v1
 */
public class CreateMeasurementPathCommandAtomic extends MapActionCommand
{
	/** ����������� ������������� ���� */
	MeasurementPath measurementPath;
	
	/** ������� ���� */
	SchemePath schemePath;
	
	/** ��������� ���� */
	AbstractNode startNode;
	
	/** �������� ���� */
	AbstractNode endNode;
	
	public CreateMeasurementPathCommandAtomic(
			SchemePath schemePath,
			AbstractNode startNode,
			AbstractNode endNode)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.schemePath = schemePath;
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public MeasurementPath getPath()
	{
		return this.measurementPath;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");
		
		this.measurementPath = MeasurementPath.createInstance(
				this.schemePath,
				this.startNode, 
				this.endNode,
				this.logicalNetLayer.getMapView());

		this.logicalNetLayer.getMapView().addMeasurementPath(this.measurementPath);
		setResult(Command.RESULT_OK);
	}
}

