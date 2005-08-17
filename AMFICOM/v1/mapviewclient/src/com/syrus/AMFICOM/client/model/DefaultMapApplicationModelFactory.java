/**
 * $Id: DefaultMapApplicationModelFactory.java,v 1.4 2005/08/17 14:14:20 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;


/**
 * Фабричный класс создания модели приложения для работы с картой по умолчанию.
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/17 14:14:20 $
 * @module mapviewclient
 */
public class DefaultMapApplicationModelFactory
		extends MapApplicationModelFactory 
{
	@Override
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		// need to check how distance is measured in equirectangular projection
		aModel.setVisible(MapApplicationModel.OPERATION_MOVE_FIXED, false);

		return aModel;
	}
}
