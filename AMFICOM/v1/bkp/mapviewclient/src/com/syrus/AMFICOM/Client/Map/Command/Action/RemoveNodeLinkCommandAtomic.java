/**
 * $Id: RemoveNodeLinkCommandAtomic.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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

/**
 * удаление фрагмента линии связи из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
class RemoveNodeLinkCommandAtomic extends MapActionCommand
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
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removeNodeLink(link);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addNodeLink(link);
	}
}

