/**
 * $Id: MapReportApplicationModelFactory.java,v 1.2 2005/06/22 08:43:50 krupenn Exp $
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
 * в модуле "Редактор отчетов".
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/06/22 08:43:50 $
 * @module mapviewclient_v1
 */
public class MapReportApplicationModelFactory 
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		aModel.setUsable(MapApplicationModel.ACTION_INDICATION, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_MAP_VIEW, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_BINDING, false);
		aModel.setUsable(MapApplicationModel.ACTION_EDIT_PROPERTIES, false);
		aModel.setUsable(MapApplicationModel.ACTION_SAVE_MAP, false);
		aModel.setUsable(MapApplicationModel.ACTION_SAVE_MAP_VIEW, false);
		aModel.setUsable(MapApplicationModel.ACTION_USE_MARKER, false);

		aModel.setUsable(MapApplicationModel.MODE_NODE_LINK, false);

		return aModel;
	}
}
