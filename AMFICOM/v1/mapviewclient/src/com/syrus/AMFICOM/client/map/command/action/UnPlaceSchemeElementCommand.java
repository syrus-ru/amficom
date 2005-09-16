/**
 * $Id: UnPlaceSchemeElementCommand.java,v 1.26 2005/09/16 14:53:33 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * ������ �������� �������� �������� � �����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.26 $, $Date: 2005/09/16 14:53:33 $
 * @module mapviewclient
 */
public class UnPlaceSchemeElementCommand extends MapActionCommandBundle {
	/**
	 * ��������� �������� �����
	 */
	SiteNode node = null;
	SchemeElement schemeElement = null;

	MapView mapView;

	public UnPlaceSchemeElementCommand(
			SiteNode node,
			SchemeElement schemeElement) {
		super();
		this.node = node;
		this.schemeElement = schemeElement;
	}

	@Override
	public void execute() {
		Log.debugMessage(
				getClass().getName() + "::execute() | " //$NON-NLS-1$
					+ "unplace scheme element " //$NON-NLS-1$
					+ this.schemeElement.getName()
					+ " (" + this.schemeElement.getId() + ") from site " //$NON-NLS-1$ //$NON-NLS-2$
					+ this.node.getName()
					+ " (" + this.node.getId() + ")",  //$NON-NLS-1$ //$NON-NLS-2$
				Level.FINEST);

		this.mapView = this.logicalNetLayer.getMapView();

		try {
			if(this.node instanceof UnboundNode)
				super.removeNode(this.node);
			this.schemeElement.setSiteNode(null);
			this.logicalNetLayer.getMapViewController().scanCables(
					this.schemeElement.getParentScheme());
			setResult(Command.RESULT_OK);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}
	}
}
