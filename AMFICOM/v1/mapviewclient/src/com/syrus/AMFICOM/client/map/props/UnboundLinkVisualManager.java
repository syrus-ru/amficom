/*-
 * $$Id: UnboundLinkVisualManager.java,v 1.5 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import com.syrus.AMFICOM.client.UI.StorableObjectEditor;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class UnboundLinkVisualManager implements VisualManager {

	private static UnboundLinkVisualManager instance;

	private static UnboundLinkEditor generalPanel;
	private static MapElementCharacteristicsEditor charPanel;

	public static UnboundLinkVisualManager getInstance() {
		if (instance == null) 
			instance = new UnboundLinkVisualManager();
		return instance;
	}

	public StorableObjectEditor getGeneralPropertiesPanel() {
		if (generalPanel == null)
			generalPanel = new UnboundLinkEditor();
		return generalPanel;
	}

	public StorableObjectEditor getCharacteristicPropertiesPanel() {
		if (charPanel == null)
			charPanel = new MapElementCharacteristicsEditor();
		return charPanel;
	}

	public StorableObjectWrapper getController() {
//		PhysicalLinkController plc = (PhysicalLinkController)com.syrus.AMFICOM.Client.Map.Controllers.PhysicalLinkController.getInstance();
//		if (key.equals(PROPERTY_COLOR))
//		{
//			result = plc.getColor(link);
//		}
//		else
//		if (key.equals(PROPERTY_STYLE))
//		{
//			result = plc.getStyle(link);
//		}
//		else
//		if (key.equals(PROPERTY_THICKNESS))
//		{
//			result = String.valueOf(plc.getLineSize(link));
//		}
		return null;
	}

	public StorableObjectEditor getAdditionalPropertiesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
