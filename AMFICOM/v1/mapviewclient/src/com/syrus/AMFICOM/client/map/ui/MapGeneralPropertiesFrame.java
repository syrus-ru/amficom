/**
 * $Id: MapGeneralPropertiesFrame.java,v 1.9 2005/08/17 14:14:20 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
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
 *  ���� ����������� ������� �������� �����
 * @version $Revision: 1.9 $, $Date: 2005/08/17 14:14:20 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public final class MapGeneralPropertiesFrame extends MapAbstractPropertiesFrame {
	public MapGeneralPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}

	@Override
	protected StorableObjectEditor getEditor(VisualManager manager) {
		StorableObjectEditor generalEditor = 
			manager.getGeneralPropertiesPanel();
		
		if(generalEditor != null ) {
			if(generalEditor instanceof PhysicalLinkEditor) {
				PhysicalLinkEditor linkEditor = (PhysicalLinkEditor )generalEditor;
				MapFrame mapFrame = MapDesktopCommand.findMapFrame((JDesktopPane )this.getParent());
				linkEditor.setNetMapViewer(mapFrame.getMapViewer());
			}
			if(generalEditor instanceof PhysicalLinkTypeEditor) {
				PhysicalLinkTypeEditor linkTypeEditor = (PhysicalLinkTypeEditor )generalEditor;
				MapFrame mapFrame = MapDesktopCommand.findMapFrame((JDesktopPane )this.getParent());
				linkTypeEditor.setNetMapViewer(mapFrame.getMapViewer());
			}
			if(generalEditor instanceof SiteNodeTypeEditor) {
				SiteNodeTypeEditor nodeTypeEditor = (SiteNodeTypeEditor )generalEditor;
				MapFrame mapFrame = MapDesktopCommand.findMapFrame((JDesktopPane )this.getParent());
				nodeTypeEditor.setNetMapViewer(mapFrame.getMapViewer());
			}
		}
		return generalEditor;
	}
}
