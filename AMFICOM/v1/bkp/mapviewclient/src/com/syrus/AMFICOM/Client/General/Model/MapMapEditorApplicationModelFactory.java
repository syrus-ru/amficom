/**
 * $Id: MapMapEditorApplicationModelFactory.java,v 1.4 2005/01/30 15:34:56 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;

/**
 * Фабричный класс создания модели приложения для работы с картой 
 * в модуле "Редактор топологических схем".
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/01/30 15:34:56 $
 * @module mapviewclient_v1
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
