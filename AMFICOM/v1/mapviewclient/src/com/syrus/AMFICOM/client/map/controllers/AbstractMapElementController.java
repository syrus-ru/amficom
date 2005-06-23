/**
 * $Id: AbstractMapElementController.java,v 1.6 2005/06/23 08:23:39 krupenn Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/06/23 08:23:39 $
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
}
