/**
 * $Id: MapSUApplicationModelFactory.java,v 1.4 2005/05/27 15:14:54 krupenn Exp $
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
 * Фабричный класс создания модели приложения для работы с картой 
 * в режиме суперпользователя (все операции разрешены).
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/05/27 15:14:54 $
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
