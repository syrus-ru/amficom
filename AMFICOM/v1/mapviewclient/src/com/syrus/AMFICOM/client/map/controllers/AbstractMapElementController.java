/**
 * $Id: AbstractMapElementController.java,v 1.8 2005/08/11 17:08:10 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.controllers;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;

/**
 * ���������� �������� �����.
 * @author $Author: arseniy $
 * @version $Revision: 1.8 $, $Date: 2005/08/11 17:08:10 $
 * @module mapviewclient
 */
public abstract class AbstractMapElementController implements MapElementController {
	/**
	 * ���������� ����.
	 */
	protected LogicalNetLayer logicalNetLayer;

	protected NetMapViewer netMapViewer;

	public AbstractMapElementController(final NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = (netMapViewer == null) ? null : this.netMapViewer.getLogicalNetLayer();
	}

	/**
	 * @param netMapViewer
	 *        The netMapViewer to set.
	 */
	public void setNetMapViewer(final NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}
}
