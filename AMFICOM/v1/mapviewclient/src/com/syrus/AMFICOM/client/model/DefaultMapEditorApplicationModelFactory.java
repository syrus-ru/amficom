/*-
 * $$Id: DefaultMapEditorApplicationModelFactory.java,v 1.7 2005/10/07 14:28:57 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * Модель приложения по умолчанию - описывает, какие функции по умолчанию
 * доступны или не достпны пользователю
 * 
 * @version $Revision: 1.7 $, $Date: 2005/10/07 14:28:57 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class DefaultMapEditorApplicationModelFactory extends
		MapEditorApplicationModelFactory {
	@Override
	public ApplicationModel create() {
		ApplicationModel aModel = super.create();

		// TODO disabled until map, mapview, maplibrary cloning will be tested
		aModel.setVisible(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_MAP_LIBRARY_SAVE_AS, false);

		return aModel;
	}
}
