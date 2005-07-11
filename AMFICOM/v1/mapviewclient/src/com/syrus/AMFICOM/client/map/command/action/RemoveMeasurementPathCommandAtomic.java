/**
 * $Id: RemoveMeasurementPathCommandAtomic.java,v 1.15 2005/07/11 13:18:04 bass Exp $
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
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.util.Log;

/**
 * �������� �������������� ���� �� ����� - ��������� �������� 
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/07/11 13:18:04 $
 * @module mapviewclient_v1
 */
public class RemoveMeasurementPathCommandAtomic extends MapActionCommand
{
	MeasurementPath measuremetnPath;
	
	public RemoveMeasurementPathCommandAtomic(MeasurementPath mp)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.measuremetnPath = mp;
	}
	
	public MeasurementPath getPath()
	{
		return this.measuremetnPath;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		this.logicalNetLayer.getMapView().removeMeasurementPath(this.measuremetnPath);
		setResult(Command.RESULT_OK);
	}

	public void redo()
	{
		this.logicalNetLayer.getMapView().removeMeasurementPath(this.measuremetnPath);
	}

	public void undo()
	{
		this.logicalNetLayer.getMapView().addMeasurementPath(this.measuremetnPath);
	}
}

