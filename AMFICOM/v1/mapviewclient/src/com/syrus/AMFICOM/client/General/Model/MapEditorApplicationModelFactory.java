/**
 * $Id: MapEditorApplicationModelFactory.java,v 1.2 2005/02/07 16:09:25 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelFactory;

/**
 * Создает модель 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/02/07 16:09:25 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorApplicationModelFactory
		implements ApplicationModelFactory
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = new MapEditorApplicationModel();
		return aModel;
	}
}
