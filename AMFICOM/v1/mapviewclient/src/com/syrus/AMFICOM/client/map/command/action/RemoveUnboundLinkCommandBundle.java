/*-
 * $$Id: RemoveUnboundLinkCommandBundle.java,v 1.19 2005/10/18 07:21:12 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * �������� ������������ ����� �� �����, ������� ��������, �� ������� ���
 * �������
 * 
 * @version $Revision: 1.19 $, $Date: 2005/10/18 07:21:12 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class RemoveUnboundLinkCommandBundle extends MapActionCommandBundle {
	UnboundLink unbound;

	public RemoveUnboundLinkCommandBundle(UnboundLink unbound) {
		this.unbound = unbound;
	}

	public UnboundLink getUnbound() {
		return this.unbound;
	}

	@Override
	public void execute() {
		Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "remove unbound link " //$NON-NLS-1$
					+ this.unbound.getName()
					+ " (" + this.unbound.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		try {
			super.removeUnboundLink(this.unbound);
			super.setUndoable(false);
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			Log.debugException(e, Level.SEVERE);
		}
	}

}
