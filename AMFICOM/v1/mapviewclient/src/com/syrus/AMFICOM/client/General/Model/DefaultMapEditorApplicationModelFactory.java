/**
 * $Id: DefaultMapEditorApplicationModelFactory.java,v 1.7 2005/05/27 15:14:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

/**
 * Модель приложения по умолчанию - описывает, какие функции по умолчанию
 * доступны или не достпны пользователю
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/05/27 15:14:54 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class DefaultMapEditorApplicationModelFactory
		extends MapEditorApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_CONTENTS, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_FIND, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_FIND, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_START, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_COURSE, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_HELP, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_SUPPORT, false);
		aModel.setVisible(MapEditorApplicationModel.ITEM_HELP_LICENSE, false);

		return aModel;
	}
}
