/**
 * $Id: MapAbstractPropertiesFrame.java,v 1.14 2005/08/18 14:16:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import com.syrus.AMFICOM.client.UI.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * Окно отображения свойств элемента карты
 * 
 * @version $Revision: 1.14 $, $Date: 2005/08/18 14:16:59 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public abstract class MapAbstractPropertiesFrame extends
		AbstractPropertiesFrame {

	protected ApplicationContext aContext;

	private MapPropertiesEventHandler handler;

	public MapAbstractPropertiesFrame(String title, ApplicationContext aContext) {
		super(title);
		this.handler = new MapPropertiesEventHandler(this, aContext);
	}
}
