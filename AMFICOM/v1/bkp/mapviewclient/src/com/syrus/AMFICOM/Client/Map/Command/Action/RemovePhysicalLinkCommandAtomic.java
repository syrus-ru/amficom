/**
 * $Id: RemovePhysicalLinkCommandAtomic.java,v 1.4 2004/12/22 16:38:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.map.Map;

/**
 * удаление физической линии из карты - атомарное действие 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/22 16:38:40 $
 * @module
 * @author $Author: krupenn $
 * @see
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
		return link;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		logicalNetLayer.getMapView().getMap().removePhysicalLink(link);
	}
	
	public void redo()
	{
		logicalNetLayer.getMapView().getMap().removePhysicalLink(link);
	}
	
	public void undo()
	{
		logicalNetLayer.getMapView().getMap().addPhysicalLink(link);
	}
}

