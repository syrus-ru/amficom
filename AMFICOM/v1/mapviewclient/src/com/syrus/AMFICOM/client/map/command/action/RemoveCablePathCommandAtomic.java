/**
 * $Id: RemoveCablePathCommandAtomic.java,v 1.17 2005/08/17 14:14:17 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.util.Log;

/**
 * удаление кабельного пути из карты - атомарное действие 
 * @author $Author: arseniy $
 * @version $Revision: 1.17 $, $Date: 2005/08/17 14:14:17 $
 * @module mapviewclient
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
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
		setResult(Command.RESULT_OK);
	}

	@Override
	public void redo()
	{
		this.logicalNetLayer.getMapView().removeCablePath(this.cablePath);
	}

	@Override
	public void undo()
	{
		this.logicalNetLayer.getMapView().addCablePath(this.cablePath);
	}
}

