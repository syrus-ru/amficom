/**
 * $Id: CreateMeasurementPathCommandAtomic.java,v 1.11 2005/03/16 12:54:57 bass Exp $
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
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * �������� ��������� �������������� ���� 
 * 
 * 
 * 
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/03/16 12:54:57 $
 * @module mapviewclient_v1
 */
public class CreateMeasurementPathCommandAtomic extends MapActionCommand
{
	/** ����������� ������������� ���� */
	MeasurementPath mp;
	
	/** ������� ���� */
	SchemePath path;
	
	public CreateMeasurementPathCommandAtomic(
			SchemePath path)
	{
		super(MapActionCommand.ACTION_DRAW_LINE);
		this.path = path;
	}
	
	public MeasurementPath getPath()
	{
		return this.mp;
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
			this.mp = MeasurementPath.createInstance(
					this.path,
					this.logicalNetLayer.getMapView());
	
			this.logicalNetLayer.getMapView().addMeasurementPath(this.mp);
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

