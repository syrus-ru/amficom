/**
 * $Id: MapAdditionalPropertiesFrame.java,v 1.3 2005/05/27 15:14:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.Client.Map.UI;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.Client.Map.Command.MapDesktopCommand;
import com.syrus.AMFICOM.Client.Map.Props.PhysicalLinkAddEditor;
import com.syrus.AMFICOM.Client.Map.Props.SiteNodeAddEditor;
import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 *  Окно отображения свойств элемента карты
 * @version $Revision: 1.3 $, $Date: 2005/05/27 15:14:59 $
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
				siteEditor.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());
			}
			else if(addEditor instanceof PhysicalLinkAddEditor) {
				PhysicalLinkAddEditor linkEditor = (PhysicalLinkAddEditor )addEditor;
				MapFrame mapFrame = MapDesktopCommand.findMapFrame((JDesktopPane )this.getParent());
				linkEditor.setLogicalNetLayer(mapFrame.getMapViewer().getLogicalNetLayer());
			}
		}
		return addEditor;
	}
}
