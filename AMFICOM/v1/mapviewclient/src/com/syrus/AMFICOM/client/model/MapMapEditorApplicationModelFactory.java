/*-
 * $$Id: MapMapEditorApplicationModelFactory.java,v 1.5 2005/09/30 16:08:42 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * Фабричный класс создания модели приложения для работы с картой в модуле
 * "Редактор топологических схем".
 * 
 * @version $Revision: 1.5 $, $Date: 2005/09/30 16:08:42 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapMapEditorApplicationModelFactory extends
		MapApplicationModelFactory {
	@Override
	public ApplicationModel create() {
		ApplicationModel aModel = super.create();

		//uncomment for test purposes
		// aModel.setUsable(MapApplicationModel.ACTION_INDICATION, false);
		// aModel.setUsable(MapApplicationModel.ACTION_USE_MARKER, false);

		return aModel;
	}
}
