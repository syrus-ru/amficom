/**
 * $Id: RemovePhysicalLinkCommandAtomic.java,v 1.2 2004/09/21 14:59:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление физической линии из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/21 14:59:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemovePhysicalLinkCommandAtomic extends MapActionCommand
{
	MapPhysicalLinkElement link;
	
	public RemovePhysicalLinkCommandAtomic(MapPhysicalLinkElement link)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.link = link;
	}
	
	public MapPhysicalLinkElement getLink()
	{
		return link;
	}
	
	public void execute()
	{
		logicalNetLayer.getMapView().getMap().removePhysicalLink(link);
		Pool.remove(MapPhysicalLinkElement.typ, link.getId());
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removePhysicalLink(link);
		Pool.remove(MapPhysicalLinkElement.typ, link.getId());
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addPhysicalLink(link);
		Pool.put(MapPhysicalLinkElement.typ, link.getId(), link);
	}
}

