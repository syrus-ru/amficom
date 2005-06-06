/**
 * $Id: MapGeneralPropertiesFrame.java,v 1.3 2005/06/06 12:20:35 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.3 $, $Date: 2005/06/06 12:20:35 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapGeneralPropertiesFrame extends MapAbstractPropertiesFrame {
	public MapGeneralPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}

	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getGeneralPropertiesPanel();
	}
}
