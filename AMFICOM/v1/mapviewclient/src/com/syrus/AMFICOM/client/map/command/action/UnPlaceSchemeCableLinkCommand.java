/**
 * $Id: UnPlaceSchemeCableLinkCommand.java,v 1.23 2005/07/24 12:41:05 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.util.LinkedList;
import java.util.SortedSet;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * убрать кабельный путь с привязкой из карты
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.23 $, $Date: 2005/07/24 12:41:05 $
 * @module mapviewclient_v1
 */
public class UnPlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	CablePath cablePath = null;
	
	public UnPlaceSchemeCableLinkCommand(CablePath cablePath)
	{
		super();
		this.cablePath = cablePath;
	}

	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try {
			SchemeCableLink scl = this.cablePath.getSchemeCableLink();
			final SortedSet<CableChannelingItem> cableChannelingItems = scl.getPathMembers();
			for(CableChannelingItem cableChannelingItem : new LinkedList<CableChannelingItem>(cableChannelingItems)) {
				if(cableChannelingItem.getPhysicalLink() == null)
					cableChannelingItem.setParentPathOwner(null, false);
			}

			super.removeCablePathLinks(this.cablePath);
			super.removeCablePath(this.cablePath);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
