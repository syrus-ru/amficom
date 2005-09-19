/**
 * $Id: RemoveUnboundLinkCommandBundle.java,v 1.17 2005/09/19 15:37:44 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * удаление непривязанно линии из карты, включая элементы, из которых она
 * состоит
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.17 $, $Date: 2005/09/19 15:37:44 $
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
			e.printStackTrace();
		}
	}

}
