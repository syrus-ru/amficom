/**
 * $Id: MapGeneralPropertiesFrame.java,v 1.10 2005/08/18 13:57:11 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkEditor;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkTypeEditor;
import com.syrus.AMFICOM.client.map.props.SiteNodeTypeEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.10 $, $Date: 2005/08/18 13:57:11 $
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
