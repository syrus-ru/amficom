/**
 * $Id: CollectorVisualManager.java,v 1.5 2005/08/11 12:43:31 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.5 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public class CollectorVisualManager implements VisualManager {

	private static CollectorVisualManager instance;

	private static CollectorEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static CollectorVisualManager getInstance() {
		if (instance == null) 
			instance = new CollectorVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new CollectorEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MapElementCharacteristicsEditor();
		return charPanel;
	}

	public StorableObjectWrapper getController() {
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
