/*
 * $Id: MapCloseCommand.java,v 1.11 2005/06/06 12:20:31 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.map;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;

/**
 * ����� $RCSfile: MapCloseCommand.java,v $ ������������ ��� �������� 
 * ����� ��� ���������� �� ������
 * ������ ���� �����. ��� ���� � ��������� ���� ������������ ���������� � ���,
 * ��� �������� ����� ���, � ����� ������������ �� ���������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/06/06 12:20:31 $
 * @module mapviewclient_v1
 */
public class MapCloseCommand extends AbstractCommand
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
