/**
 * $Id: MapAdditionalPropertiesFrame.java,v 1.6 2005/06/24 13:04:05 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.ui;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkAddEditor;
import com.syrus.AMFICOM.client.map.props.SiteNodeAddEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.6 $, $Date: 2005/06/24 13:04:05 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapAdditionalPropertiesFrame extends MapAbstractPropertiesFrame
{
	public MapAdditionalPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	public StorableObjectEditor getEditor(VisualManager manager) {
		StorableObjectEditor addEditor = 
			manager.getAdditionalPropertiesPanel();
		
		if(addEditor != null ) {
			if(addEditor instanceof SiteNodeAddEditor) {
				SiteNodeAddEditor siteEditor = (SiteNodeAddEditor )addEditor;
				MapFrame mapFrame = MapDesktopCommand.findMapFrame((JDesktopPane )this.getParent());
				siteEditor.setNetMapViewer(mapFrame.getMapViewer());
			}
			else if(addEditor instanceof PhysicalLinkAddEditor) {
				PhysicalLinkAddEditor linkEditor = (PhysicalLinkAddEditor )addEditor;
				MapFrame mapFrame = MapDesktopCommand.findMapFrame((JDesktopPane )this.getParent());
				linkEditor.setNetMapViewer(mapFrame.getMapViewer());
			}
		}
		return addEditor;
	}
}
