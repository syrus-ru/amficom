/**
 * $Id: MapSUApplicationModelFactory.java,v 1.2 2005/06/22 08:43:50 krupenn Exp $
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
 * Фабричный класс создания модели приложения для работы с картой 
 * в режиме суперпользователя (все операции разрешены).
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/06/22 08:43:50 $
 * @module mapviewclient_v1
 */
public class MapSUApplicationModelFactory
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		return aModel;
	}
}
