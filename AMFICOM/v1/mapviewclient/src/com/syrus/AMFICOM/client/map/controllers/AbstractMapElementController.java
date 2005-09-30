/*-
 * $$Id: AbstractMapElementController.java,v 1.10 2005/09/30 16:08:39 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.controllers;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;

/**
 * Контроллер элемента карты.
 * 
 * @version $Revision: 1.10 $, $Date: 2005/09/30 16:08:39 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class AbstractMapElementController implements MapElementController {
	/**
	 * Логический слой.
	 */
	protected LogicalNetLayer logicalNetLayer;

	protected NetMapViewer netMapViewer;

	public AbstractMapElementController(final NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
		this.logicalNetLayer = (netMapViewer == null) ? null : this.netMapViewer.getLogicalNetLayer();
	}

	/**
	 * @param netMapViewer
	 *        The netMapViewer to set.
	 */
	public void setNetMapViewer(final NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

}
