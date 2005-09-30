/*-
 * $$Id: MapApplicationModelFactory.java,v 1.4 2005/09/30 16:08:42 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * Фабричный класс создания модели приложения для работы с картой.
 * 
 * @version $Revision: 1.4 $, $Date: 2005/09/30 16:08:42 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapApplicationModelFactory {
	/**
	 * Создать объект модели приложения.
	 * 
	 * @return модель
	 */
	public ApplicationModel create() {
		ApplicationModel aModel = new MapApplicationModel();
		return aModel;
	}
}
