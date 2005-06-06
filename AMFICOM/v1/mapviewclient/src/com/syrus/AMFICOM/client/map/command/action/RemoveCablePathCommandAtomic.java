/**
 * $Id: RemoveCablePathCommandAtomic.java,v 1.13 2005/06/06 12:20:30 krupenn Exp $
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
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.mapview.CablePath;

/**
 * удаление кабельного пути из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/06/06 12:20:30 $
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

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

