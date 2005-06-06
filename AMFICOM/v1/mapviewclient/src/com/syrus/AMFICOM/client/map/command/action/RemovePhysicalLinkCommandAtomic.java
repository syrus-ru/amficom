/**
 * $Id: RemovePhysicalLinkCommandAtomic.java,v 1.8 2005/06/06 12:20:30 krupenn Exp $
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
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * удаление физической линии из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.8 $, $Date: 2005/06/06 12:20:30 $
 * @module mapviewclient_v1
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

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

