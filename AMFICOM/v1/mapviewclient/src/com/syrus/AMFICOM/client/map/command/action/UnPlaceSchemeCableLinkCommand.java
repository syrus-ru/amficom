/*-
 * $$Id: UnPlaceSchemeCableLinkCommand.java,v 1.34 2005/10/30 16:31:18 bass Exp $$
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
 * @version $Revision: 1.34 $, $Date: 2005/10/30 16:31:18 $
 * @author $Author: bass $
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
		assert Log.debugMessage("unplace cable path " //$NON-NLS-1$
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
			assert Log.debugMessage(e, Level.SEVERE);
		}
	}
}
