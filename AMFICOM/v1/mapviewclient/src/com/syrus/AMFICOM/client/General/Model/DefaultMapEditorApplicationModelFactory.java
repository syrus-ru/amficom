/**
 * $Id: DefaultMapEditorApplicationModelFactory.java,v 1.4 2004/10/26 13:32:01 krupenn Exp $
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
 * @version $Revision: 1.4 $, $Date: 2004/10/26 13:32:01 $
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

		aModel.setVisible("menuScheme", false);

		aModel.setVisible("menuHelpContents", false);
		aModel.setVisible("menuHelpFind", false);
		aModel.setVisible("menuHelpTips", false);
		aModel.setVisible("menuHelpStart", false);
		aModel.setVisible("menuHelpCourse", false);
		aModel.setVisible("menuHelpHelp", false);
		aModel.setVisible("menuHelpSupport", false);
		aModel.setVisible("menuHelpLicense", false);

		return aModel;
	}
}
