/**
 * $Id: MapApplicationModelFactory.java,v 1.2 2005/06/22 08:43:49 krupenn Exp $
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
 * Фабричный класс создания модели приложения для работы с картой.
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/06/22 08:43:49 $
 * @module mapviewclient_v1
 */
public class MapApplicationModelFactory
{
	/**
	 * Создать объект модели приложения.
	 * @return модель
	 */
	public ApplicationModel create()
	{
		ApplicationModel aModel = new MapApplicationModel();
		return aModel;
	}
}
