/**
 * $Id: MapAdditionalPropertiesFrame.java,v 1.1 2005/04/28 13:04:47 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;

/**
 *  ���� ����������� ������� �������� �����
 * @version $Revision: 1.1 $, $Date: 2005/04/28 13:04:47 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapAdditionalPropertiesFrame extends MapAbstractPropertiesFrame
{
	public MapAdditionalPropertiesFrame(String title, ApplicationContext aContext) {
		super(title, aContext);
	}
	
	public StorableObjectEditor getEditor(VisualManager manager) {
		return manager.getAdditionalPropertiesPanel();
	}
}
