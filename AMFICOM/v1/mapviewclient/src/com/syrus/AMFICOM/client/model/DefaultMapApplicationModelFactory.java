/*-
 * $$Id: DefaultMapApplicationModelFactory.java,v 1.5 2005/09/30 16:08:42 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * Фабричный класс создания модели приложения для работы с картой по умолчанию.
 * 
 * @version $Revision: 1.5 $, $Date: 2005/09/30 16:08:42 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class DefaultMapApplicationModelFactory extends
		MapApplicationModelFactory {
	@Override
	public ApplicationModel create() {
		ApplicationModel aModel = super.create();

		// need to check how distance is measured in equirectangular projection
		aModel.setVisible(MapApplicationModel.OPERATION_MOVE_FIXED, false);

		return aModel;
	}
}
