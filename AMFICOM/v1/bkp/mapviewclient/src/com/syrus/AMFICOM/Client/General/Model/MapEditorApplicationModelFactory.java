/**
 * $Id: MapEditorApplicationModelFactory.java,v 1.1 2004/09/18 11:27:04 krupenn Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/09/18 11:27:04 $
 * @module
 * @author $Author: krupenn $
 * @see
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
