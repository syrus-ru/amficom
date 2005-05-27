/**
 * $Id: RemovePhysicalLinkCommandAtomic.java,v 1.7 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * удаление физической линии из карты - атомарное действие 
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/05/27 15:14:55 $
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

