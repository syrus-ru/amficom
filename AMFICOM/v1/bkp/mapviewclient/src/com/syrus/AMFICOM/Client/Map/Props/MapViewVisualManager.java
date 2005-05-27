/**
 * $Id: MapViewVisualManager.java,v 1.4 2005/05/27 15:14:57 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * Класс используется для управления информацией о канализационной
 * прокладке кабелей и положении узлов и других топологических объектов.
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/05/27 15:14:57 $
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
