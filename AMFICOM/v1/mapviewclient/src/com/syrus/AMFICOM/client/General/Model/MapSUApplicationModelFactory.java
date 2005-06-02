/**
 * $Id: MapSUApplicationModelFactory.java,v 1.3 2005/01/30 15:34:56 krupenn Exp $
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
 * Фабричный класс создания модели приложения для работы с картой 
 * в режиме суперпользователя (все операции разрешены).
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/01/30 15:34:56 $
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
