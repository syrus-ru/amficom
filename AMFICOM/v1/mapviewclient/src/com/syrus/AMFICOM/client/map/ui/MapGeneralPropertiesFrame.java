/**
 * $Id: MapGeneralPropertiesFrame.java,v 1.1 2005/04/28 13:03:25 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.1 $, $Date: 2005/04/28 13:03:25 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapGeneralPropertiesFrame extends MapAbstractPropertiesFrame
{
	public MapGeneralPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	protected StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getGeneralPropertiesPanel();
	}
}
