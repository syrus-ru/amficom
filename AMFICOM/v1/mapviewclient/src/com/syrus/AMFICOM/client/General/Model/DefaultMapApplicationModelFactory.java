/**
 * $Id: DefaultMapApplicationModelFactory.java,v 1.4 2005/01/30 15:34:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

/**
 * Фабричный класс создания модели приложения для работы с картой по умолчанию.
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/01/30 15:34:56 $
 * @module mapviewclient_v1
 */
public class DefaultMapApplicationModelFactory
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		// need to check how distance is measured in equirectangular projection
		aModel.setVisible(MapApplicationModel.OPERATION_MOVE_FIXED, false);

		return aModel;
	}
}
