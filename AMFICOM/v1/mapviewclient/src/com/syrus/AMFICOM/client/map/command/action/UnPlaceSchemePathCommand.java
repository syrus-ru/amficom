/**
 * $Id: UnPlaceSchemePathCommand.java,v 1.11 2005/06/16 10:57:20 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.mapview.MeasurementPath;

/**
 * ������ �������� �������������� ���� � �����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/06/16 10:57:20 $
 * @module mapviewclient_v1
 */
public class UnPlaceSchemePathCommand extends MapActionCommandBundle
{
	/**
	 * ��������� �������� �����
	 */
	MeasurementPath path = null;

	public UnPlaceSchemePathCommand(MeasurementPath path)
	{
		super();
		this.path = path;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		try {
			super.removeMeasurementPath(this.path);
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
