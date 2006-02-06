/*-
 * $$Id: MapDataFlavor.java,v 1.7 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.datatransfer.DataFlavor;

/**
 * Формат данных для переноса элементов карты для операций drag / drop
 * 
 * @version $Revision: 1.7 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapDataFlavor extends DataFlavor {
	public static final String MAP_PROTO_LABEL = "ElementLabel"; //$NON-NLS-1$

	public MapDataFlavor(Class representationClass, String humanPresentableName) {
		super(representationClass, humanPresentableName);
	}

	@Override
	public boolean isFlavorSerializedObjectType() {
		return false;
	}

}
