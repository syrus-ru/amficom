/**
 * $Id: RemoveMeasurementPathCommandAtomic.java,v 1.6 2004/12/24 15:42:12 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * �������� �������������� ���� �� ����� - ��������� �������� 
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/24 15:42:12 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveMeasurementPathCommandAtomic extends MapActionCommand
{
	MeasurementPath mp;
	
	public RemoveMeasurementPathCommandAtomic(MeasurementPath mp)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.mp = mp;
	}
	
	public MeasurementPath getPath()
	{
		return mp;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		logicalNetLayer.getMapView().removeMeasurementPath(mp);
	}

	public void redo()
	{
		logicalNetLayer.getMapView().removeMeasurementPath(mp);
	}

	public void undo()
	{
		logicalNetLayer.getMapView().addMeasurementPath(mp);
	}
}

