/*
 * $Id: MapCloseCommand.java,v 1.8 2005/01/30 15:38:17 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;

import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.MapStorableObjectPool;

/**
 * ����� $RCSfile: MapCloseCommand.java,v $ ������������ ��� �������� 
 * ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @version $Revision: 1.8 $, $Date: 2005/01/30 15:38:17 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapCloseCommand extends VoidCommand
{
	/**
	 * ���� �����
	 */
	Map map;

	public MapCloseCommand(Map map)
	{
		this.map = map;
	}

	public void execute()
	{
//		mapFrame.saveConfig();

//		if(map != null)
//		try
//		{
//			// TODO should be 'remove', node 'delete'
//			MapStorableObjectPool.delete(map.getId());
//		}
//		catch (CommunicationException e)
//		{
//			e.printStackTrace();
//		}
//		catch (DatabaseException e)
//		{
//			e.printStackTrace();
//		}

		setResult(Command.RESULT_OK);
	}

}
