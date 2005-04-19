/**
 * $Id: MapViewVisualManager.java,v 1.1 2005/04/19 15:48:32 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * ����� ������������ ��� ���������� ����������� � ���������������
 * ��������� ������� � ��������� ����� � ������ �������������� ��������.
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/04/19 15:48:32 $
 * @module mapviewclient_v1
 */
public final class MapViewVisualManager implements VisualManager
{
	/** Instance. */
	private static MapViewVisualManager instance = null;

	private static MapViewEditor generalPanel;

	private MapViewVisualManager() {
		// empty
	}
	
	/**
	 * Instance getter.
	 */
	public static MapViewVisualManager getInstance()
	{
		if(instance == null)
			instance = new MapViewVisualManager();
		return instance;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new MapViewEditor();
		return generalPanel;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		return null;
	}

	public ObjectResourceController getController() {
		return null;
	}
}
