/**
 * $Id: UnPlaceSchemeCableLinkCommand.java,v 1.26 2005/08/17 14:14:17 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.util.Log;

/**
 * убрать кабельный путь с привязкой из карты
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.26 $, $Date: 2005/08/17 14:14:17 $
 * @module mapviewclient
 */
public class UnPlaceSchemeCableLinkCommand extends MapActionCommandBundle
{
	CablePath cablePath = null;
	
	public UnPlaceSchemeCableLinkCommand(CablePath cablePath)
	{
		super();
		this.cablePath = cablePath;
	}

	@Override
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try {
//			SchemeCableLink scl = this.cablePath.getSchemeCableLink();
//			final SortedSet<CableChannelingItem> cableChannelingItems = scl.getPathMembers();
//			for(CableChannelingItem cableChannelingItem : new LinkedList<CableChannelingItem>(cableChannelingItems)) {
//				if(cableChannelingItem.getPhysicalLink() == null)
//					cableChannelingItem.setParentPathOwner(null, false);
//			}

			super.removeCablePathLinks(this.cablePath);
			super.removeCablePath(this.cablePath);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
