/**
 * $Id: MapGeneralPropertiesFrame.java,v 1.11 2005/08/24 08:19:59 krupenn Exp $
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
 * @version $Revision: 1.11 $, $Date: 2005/08/24 08:19:59 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public final class MapGeneralPropertiesFrame extends MapAbstractPropertiesFrame {
	public static final String	NAME = "mapGeneralPropertiesFrame";

	public MapGeneralPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}

	@Override
	protected StorableObjectEditor getEditor(VisualManager manager) {
		StorableObjectEditor generalEditor = 
			manager.getGeneralPropertiesPanel();
		
		return generalEditor;
	}
}
