/**
 * $Id: RemoveCablePathCommandAtomic.java,v 1.14 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.util.Log;

/**
 * удаление кабельного пути из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
 */
public class RemoveCablePathCommandAtomic extends MapActionCommand
{
	CablePath cablePath;
	
	public RemoveCablePathCommandAtomic(CablePath cp)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.cablePath = cp;
	}
	
	public CablePath getPath()
	{
		return this.cablePath;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
		setResult(Command.RESULT_OK);
	}

	public void redo()
	{
		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
	}

	public void undo()
	{
		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
	}
}

