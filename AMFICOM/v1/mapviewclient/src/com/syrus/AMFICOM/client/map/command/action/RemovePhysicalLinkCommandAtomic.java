/**
 * $Id: RemovePhysicalLinkCommandAtomic.java,v 1.11 2005/08/11 12:43:30 arseniy Exp $
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
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * удаление физической линии из карты - атомарное действие 
 * @author $Author: arseniy $
 * @version $Revision: 1.11 $, $Date: 2005/08/11 12:43:30 $
 * @module mapviewclient
 */
public class RemovePhysicalLinkCommandAtomic extends MapActionCommand
{
	PhysicalLink link;
	
	public RemovePhysicalLinkCommandAtomic(PhysicalLink link)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.link = link;
	}
	
	public PhysicalLink getLink()
	{
		return this.link;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		this.logicalNetLayer.getMapView().getMap().removePhysicalLink(this.link);
		setResult(Command.RESULT_OK);
	}
	
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().removePhysicalLink(this.link);
	}
	
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
	}
}

