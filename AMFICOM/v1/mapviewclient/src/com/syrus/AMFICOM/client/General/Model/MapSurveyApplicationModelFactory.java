/**
 * $Id: MapSurveyApplicationModelFactory.java,v 1.1 2005/01/30 15:34:34 krupenn Exp $
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
 * в модуле "Наблюдение".
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/01/30 15:34:34 $
 * @module mapviewclient_v1
 */
public class MapSurveyApplicationModelFactory 
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable(MapApplicationModel.ACTION_EDIT_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_PROPERTIES, false);
		aModel.setUsable(MapApplicationModel.ACTION_SAVE_MAP, false);

		aModel.setUsable(MapApplicationModel.MODE_NODE_LINK, false);

		return aModel;
	}
}
