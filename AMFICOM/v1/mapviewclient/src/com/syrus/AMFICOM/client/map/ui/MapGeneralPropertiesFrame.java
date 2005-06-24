/**
 * $Id: MapGeneralPropertiesFrame.java,v 1.4 2005/06/24 14:24:25 krupenn Exp $
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
import com.syrus.AMFICOM.client.map.props.PhysicalLinkAddEditor;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkEditor;
import com.syrus.AMFICOM.client.map.props.SiteNodeAddEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.4 $, $Date: 2005/06/24 14:24:25 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapGeneralPropertiesFrame extends MapAbstractPropertiesFrame {
	public MapGeneralPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}

	protected StorableObjectEditor getEditor(VisualManager manager) {
		StorableObjectEditor generalEditor = 
			manager.getGeneralPropertiesPanel();
		
		if(generalEditor != null ) {
			if(generalEditor instanceof PhysicalLinkEditor) {
				PhysicalLinkEditor linkEditor = (PhysicalLinkEditor )generalEditor;
				MapFrame mapFrame = MapDesktopCommand.findMapFrame((JDesktopPane )this.getParent());
				linkEditor.setNetMapViewer(mapFrame.getMapViewer());
			}
		}
		return generalEditor;
	}
}
