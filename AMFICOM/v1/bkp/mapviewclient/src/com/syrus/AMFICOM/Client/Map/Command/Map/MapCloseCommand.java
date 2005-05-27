/*
 * $Id: MapCloseCommand.java,v 1.10 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Map;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;

/**
 * Класс $RCSfile: MapCloseCommand.java,v $ используется для закрытия 
 * карты при сохранении на экране
 * самого окна карты. При этом в азголовке окна отображается информация о том,
 * что активной карты нет, и карта центрируется по умолчанию
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.10 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class MapCloseCommand extends AbstractCommand
{
	/**
	 * окно карты
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
