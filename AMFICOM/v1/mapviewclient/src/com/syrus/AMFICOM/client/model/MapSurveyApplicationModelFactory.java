/*-
 * $$Id: MapSurveyApplicationModelFactory.java,v 1.6 2005/10/22 13:49:02 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * ‘абричный класс создани€ модели приложени€ дл€ работы с картой в модуле
 * "Ќаблюдение".
 * 
 * @version $Revision: 1.6 $, $Date: 2005/10/22 13:49:02 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapSurveyApplicationModelFactory extends
		MapApplicationModelFactory {
	@Override
	public ApplicationModel create() {
		ApplicationModel aModel = super.create();

		aModel.setUsable(MapApplicationModel.ACTION_EDIT_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_MAP_VIEW, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_PROPERTIES, false);
		aModel.setUsable(MapApplicationModel.ACTION_SAVE_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_SAVE_MAP_VIEW, false);

		aModel.setUsable(MapApplicationModel.MODE_NODE_LINK, false);

		return aModel;
	}
}
