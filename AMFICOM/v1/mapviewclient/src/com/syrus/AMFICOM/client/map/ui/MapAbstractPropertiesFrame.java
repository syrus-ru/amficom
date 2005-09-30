/*-
 * $$Id: MapAbstractPropertiesFrame.java,v 1.15 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import com.syrus.AMFICOM.client.UI.AbstractPropertiesFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;

/**
 * Окно отображения свойств элемента карты
 * 
 * @version $Revision: 1.15 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public abstract class MapAbstractPropertiesFrame extends
		AbstractPropertiesFrame {

	protected ApplicationContext aContext;

	private MapPropertiesEventHandler handler;

	public MapAbstractPropertiesFrame(String title, ApplicationContext aContext) {
		super(title);
		this.handler = new MapPropertiesEventHandler(this, aContext);
	}
}
