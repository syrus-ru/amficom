/**
 * $Id: RemoveNodeLinkCommandAtomic.java,v 1.2 2004/09/21 14:59:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Pool;

/**
 * удаление фрагмента линии связи из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/21 14:59:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class RemoveNodeLinkCommandAtomic extends MapActionCommand
{
	MapNodeLinkElement link;
	
	public RemoveNodeLinkCommandAtomic(MapNodeLinkElement link)
	{
		super(MapActionCommand.ACTION_DROP_LINE);
		this.link = link;
	}
	
	public MapNodeLinkElement getLink()
	{
		return link;
	}
	
	public void execute()
	{
		logicalNetLayer.getMapView().getMap().removeNodeLink(link);
		Pool.remove(MapNodeLinkElement.typ, link.getId());
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removeNodeLink(link);
		Pool.remove(MapNodeLinkElement.typ, link.getId());
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addNodeLink(link);
		Pool.put(MapNodeLinkElement.typ, link.getId(), link);
	}
}

