/**
 * $Id: AbstractMapElementController.java,v 1.3 2005/06/06 12:20:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.controllers;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;

/**
 * Контроллер элемента карты.
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/06/06 12:20:32 $
 * @module mapviewclient_v1
 */
public abstract class AbstractMapElementController implements
		MapElementController {
	/**
	 * Логический слой.
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
