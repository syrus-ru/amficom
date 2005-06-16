/**
 * $Id: AbstractMapElementController.java,v 1.5 2005/06/16 14:42:52 krupenn Exp $
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
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/06/16 14:42:52 $
 * @module mapviewclient_v1
 */
public abstract class AbstractMapElementController implements
		MapElementController {
	/**
	 * ���������� ����.
	 */
	protected LogicalNetLayer logicalNetLayer;

	protected NetMapViewer netMapViewer;
	
	public AbstractMapElementController(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = (netMapViewer == null) 
				? null 
				: this.netMapViewer.getLogicalNetLayer();
	}

	/**
	 * @param netMapViewer The netMapViewer to set.
	 */
	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	/**
	 * {@inheritDoc}
	 */
//	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer) {
//		this.logicalNetLayer = logicalNetLayer;
//	}

	/**
	 * {@inheritDoc}
	 */
//	public LogicalNetLayer getLogicalNetLayer() {
//		return this.logicalNetLayer;
//	}
}
