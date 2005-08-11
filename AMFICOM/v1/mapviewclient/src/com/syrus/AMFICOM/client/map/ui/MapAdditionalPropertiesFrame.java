/**
 * $Id: MapAdditionalPropertiesFrame.java,v 1.8 2005/08/11 12:43:32 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.ui;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.props.CablePathAddEditor;
import com.syrus.AMFICOM.client.map.props.PhysicalLinkAddEditor;
import com.syrus.AMFICOM.client.map.props.SiteNodeAddEditor;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 *  ���� ����������� ������� �������� �����
 * @version $Revision: 1.8 $, $Date: 2005/08/11 12:43:32 $
 * @author $Author: arseniy $
 * @module mapviewclient
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
			else if(addEditor instanceof CablePathAddEditor) {
				CablePathAddEditor cableEditor = (CablePathAddEditor )addEditor;
				MapFrame mapFrame = MapDesktopCommand.findMapFrame((JDesktopPane )this.getParent());
				cableEditor.setNetMapViewer(mapFrame.getMapViewer());
			}
		}
		return addEditor;
	}
}
