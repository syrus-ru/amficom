/*-
 * $$Id: MapEditorApplicationModelFactory.java,v 1.4 2005/09/30 16:08:42 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * Создает модель
 * 
 * @version $Revision: 1.4 $, $Date: 2005/09/30 16:08:42 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapEditorApplicationModelFactory {
	public ApplicationModel create() {
		ApplicationModel aModel = new MapEditorApplicationModel();
		return aModel;
	}
}
