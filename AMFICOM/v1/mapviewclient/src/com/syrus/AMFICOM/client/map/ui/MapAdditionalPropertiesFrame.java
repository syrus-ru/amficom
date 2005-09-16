/**
 * $Id: MapAdditionalPropertiesFrame.java,v 1.12 2005/09/16 14:53:36 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.ui;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.12 $, $Date: 2005/09/16 14:53:36 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public final class MapAdditionalPropertiesFrame extends MapAbstractPropertiesFrame {
	public static final String	NAME = "mapAdditionalPropertiesFrame"; //$NON-NLS-1$

	public MapAdditionalPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	@Override
	public StorableObjectEditor getEditor(VisualManager manager) {
		StorableObjectEditor addEditor = 
			manager.getAdditionalPropertiesPanel();
		
		return addEditor;
	}
}
