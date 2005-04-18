/**
 * $Id: MapViewVisualManager.java,v 1.2 2005/04/18 12:09:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.client_.general.ui_.StorableObjectEditor;
import com.syrus.AMFICOM.client_.general.ui_.VisualManager;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * Класс используется для управления информацией о канализационной
 * прокладке кабелей и положении узлов и других топологических объектов.
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/04/18 12:09:09 $
 * @module mapviewclient_v1
 */
public final class MapViewVisualManager implements VisualManager
{
	/** Instance. */
	private static MapViewVisualManager instance = null;
	
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
		// TODO Auto-generated method stub
		return null;
	}
	public ObjectResourceController getController() {
//		return MapViewWrapper.getInstance();
		return null;
	}
	public StorableObjectEditor getGeneralPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
