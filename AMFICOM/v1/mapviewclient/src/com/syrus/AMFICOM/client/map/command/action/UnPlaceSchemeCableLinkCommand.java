/**
 * $Id: UnPlaceSchemeCableLinkCommand.java,v 1.4 2004/11/25 13:00:49 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import java.util.Iterator;

/**
 * убрать кабельный путь с привязкой из карты
 * 
 * @version $Revision: 1.4 $, $Date: 2004/11/25 13:00:49 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class UnPlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	MapCablePathElement cablePath = null;
	
	public UnPlaceSchemeCableLinkCommand(MapCablePathElement cablePath)
	{
		super();
		this.cablePath = cablePath;
	}

	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		SchemeCableLink scl = cablePath.getSchemeCableLink();
		scl.channelingItems.clear();

		for(Iterator it = cablePath.getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link instanceof MapUnboundLinkElement)
				continue;
			scl.channelingItems.add(cablePath.getBinding().getCCI(link));
		}

		super.removeCablePathLinks(cablePath);

		super.removeCablePath(cablePath);

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
}
