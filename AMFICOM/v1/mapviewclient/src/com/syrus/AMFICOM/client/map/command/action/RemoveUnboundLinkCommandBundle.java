/**
 * $Id: RemoveUnboundLinkCommandBundle.java,v 1.16 2005/09/16 14:53:33 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
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
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/09/16 14:53:33 $
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
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}

}
