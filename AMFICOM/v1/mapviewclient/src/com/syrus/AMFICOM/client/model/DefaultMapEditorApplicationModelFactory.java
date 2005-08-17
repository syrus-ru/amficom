/**
 * $Id: DefaultMapEditorApplicationModelFactory.java,v 1.5 2005/08/17 14:14:21 arseniy Exp $
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
 * Модель приложения по умолчанию - описывает, какие функции по умолчанию
 * доступны или не достпны пользователю
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2005/08/17 14:14:21 $
 * @module mapviewclient
 * @author $Author: arseniy $
 */
public class DefaultMapEditorApplicationModelFactory
		extends MapEditorApplicationModelFactory 
{
	@Override
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		return aModel;
	}
}
