/**
 * $Id: AbstractMapElementController.java,v 1.2 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

/**
 * ���������� �������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/05/27 15:14:56 $
 * @module mapviewclient_v1
 */
public abstract class AbstractMapElementController implements
		MapElementController {
	/**
	 * ���������� ����.
	 */
	protected LogicalNetLayer logicalNetLayer;

	/**
	 * {@inheritDoc}
	 */
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer) {
		this.logicalNetLayer = logicalNetLayer;
	}

	/**
	 * {@inheritDoc}
	 */
	public LogicalNetLayer getLogicalNetLayer() {
		return this.logicalNetLayer;
	}
}
