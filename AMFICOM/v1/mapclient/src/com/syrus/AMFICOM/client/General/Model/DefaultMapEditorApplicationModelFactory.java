/**
 * $Id: DefaultMapEditorApplicationModelFactory.java,v 1.3 2004/09/13 12:02:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;

/**
 * Модель приложения по умолчанию - описывает, какие функции по умолчанию
 * доступны или не достпны пользователю
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/09/13 12:02:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class DefaultMapEditorApplicationModelFactory
		extends MapEditorApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable("mapActionViewProperties", true);
		aModel.setUsable("mapActionEditProperties", true);

//		aModel.setVisible("menuMapOptions", false);
//		aModel.setVisible("menuMapCatalogue", false);

		aModel.setUsable("menuHelpContents", false);
		aModel.setUsable("menuHelpFind", false);
		aModel.setUsable("menuHelpTips", false);
		aModel.setUsable("menuHelpStart", false);
		aModel.setUsable("menuHelpCourse", false);
		aModel.setUsable("menuHelpHelp", false);
		aModel.setUsable("menuHelpSupport", false);
		aModel.setUsable("menuHelpLicense", false);

		return aModel;
	}
}
