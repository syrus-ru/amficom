/**
 * $Id: AbstractMapElementController.java,v 1.1 2005/03/02 12:31:39 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

/**
 * ���������� ����.
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/03/02 12:31:39 $
 * @module mapviewclient_v1
 */
public abstract class AbstractMapElementController implements MapElementController
{
	/**
	 * ���������� ����.
	 */
	protected LogicalNetLayer logicalNetLayer;

	/**
	 * {@inheritDoc}
	 */
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public LogicalNetLayer getLogicalNetLayer()
	{
		return this.logicalNetLayer;
	}
}
