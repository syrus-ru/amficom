/**
 * $Id: RemovePhysicalLinkCommandAtomic.java,v 1.12 2005/08/17 14:14:17 arseniy Exp $
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
 * @version $Revision: 1.12 $, $Date: 2005/08/17 14:14:17 $
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
	
	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		this.logicalNetLayer.getMapView().getMap().removePhysicalLink(this.link);
		setResult(Command.RESULT_OK);
	}
	
	@Override
	public void redo()
	{
		this.logicalNetLayer.getMapView().getMap().removePhysicalLink(this.link);
	}
	
	@Override
	public void undo()
	{
		this.logicalNetLayer.getMapView().getMap().addPhysicalLink(this.link);
	}
}

