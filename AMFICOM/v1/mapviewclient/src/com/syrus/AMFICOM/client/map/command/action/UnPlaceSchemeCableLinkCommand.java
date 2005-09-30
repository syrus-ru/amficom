/*-
 * $$Id: UnPlaceSchemeCableLinkCommand.java,v 1.30 2005/09/30 16:08:37 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.util.Log;

/**
 * убрать кабельный путь с привязкой из карты
 * 
 * @version $Revision: 1.30 $, $Date: 2005/09/30 16:08:37 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class UnPlaceSchemeCableLinkCommand extends MapActionCommandBundle {
	CablePath cablePath = null;

	public UnPlaceSchemeCableLinkCommand(CablePath cablePath) {
		super();
		this.cablePath = cablePath;
	}

	@Override
	public void execute() {
		Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "unplace cable path " //$NON-NLS-1$
					+ this.cablePath.getName()
					+ " (" + this.cablePath.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		try {
//			SchemeCableLink scl = this.cablePath.getSchemeCableLink();
//			final SortedSet<CableChannelingItem> cableChannelingItems = scl.getPathMembers();
//			for(CableChannelingItem cableChannelingItem : new LinkedList<CableChannelingItem>(cableChannelingItems)) {
//				if(cableChannelingItem.getPhysicalLink() == null)
//					cableChannelingItem.setParentPathOwner(null, false);
//			}

			super.removeCablePathLinks(this.cablePath);
			super.removeCablePath(this.cablePath);
			super.setUndoable(false);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
