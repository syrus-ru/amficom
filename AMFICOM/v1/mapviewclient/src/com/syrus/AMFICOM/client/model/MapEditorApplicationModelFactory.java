/**
 * $Id: MapEditorApplicationModelFactory.java,v 1.3 2005/08/11 12:43:32 arseniy Exp $
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
 * Создает модель 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/08/11 12:43:32 $
 * @module mapviewclient
 * @author $Author: arseniy $
 */
public class MapEditorApplicationModelFactory
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = new MapEditorApplicationModel();
		return aModel;
	}
}
