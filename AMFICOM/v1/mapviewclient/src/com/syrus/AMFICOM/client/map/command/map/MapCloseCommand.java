/*
 * $Id: MapCloseCommand.java,v 1.9 2005/02/08 15:11:10 krupenn Exp $
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
import com.syrus.AMFICOM.map.Map;

/**
 * ����� $RCSfile: MapCloseCommand.java,v $ ������������ ��� �������� 
 * ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.9 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
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
