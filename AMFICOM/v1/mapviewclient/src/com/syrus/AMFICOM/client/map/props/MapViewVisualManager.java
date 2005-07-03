/**
 * $Id: MapViewVisualManager.java,v 1.5 2005/06/06 12:20:33 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * ����� ������������ ��� ���������� ����������� � ���������������
 * ��������� ������� � ��������� ����� � ������ �������������� ��������.
 * @author $Author: krupenn $
 * @version $Revision: 1.5 $, $Date: 2005/06/06 12:20:33 $
 * @module mapviewclient_v1
 */
public final class MapViewVisualManager implements VisualManager {
	/** Instance. */
	private static MapViewVisualManager instance = null;

	private static MapViewEditor generalPanel;

	private MapViewVisualManager() {
		// empty
	}
	
	/**
	 * Instance getter.
	 */
	public static MapViewVisualManager getInstance() 	{
		if(instance == null)
			instance = new MapViewVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new MapViewEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	public StorableObjectWrapper getController() {
		// TODO Auto-generated method stub
		return null;
	}
}
