/**
 * $Id: MapMapEditorApplicationModelFactory.java,v 1.3 2005/08/11 12:43:32 arseniy Exp $
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
 * в модуле "Редактор топологических схем".
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/11 12:43:32 $
 * @module mapviewclient
 */
public class MapMapEditorApplicationModelFactory
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

//		aModel.setUsable(MapApplicationModel.ACTION_INDICATION, false);
//		aModel.setUsable(MapApplicationModel.ACTION_USE_MARKER, false);

		return aModel;
	}
}
