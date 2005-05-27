/**
 * $Id: MapGeneralPropertiesFrame.java,v 1.2 2005/05/27 15:14:59 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 *  ���� ����������� ������� �������� �����
 * @version $Revision: 1.2 $, $Date: 2005/05/27 15:14:59 $
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
