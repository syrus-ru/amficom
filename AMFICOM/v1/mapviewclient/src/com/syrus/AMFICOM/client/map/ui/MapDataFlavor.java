/**
 * $Id: MapDataFlavor.java,v 1.5 2005/08/17 14:14:20 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.datatransfer.DataFlavor;

/**
 * Формат данных для переноса элементов карты для операций drag / drop
 * @version $Revision: 1.5 $, $Date: 2005/08/17 14:14:20 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public class MapDataFlavor extends DataFlavor
{
	public static final String MAP_PROTO_LABEL = "ElementLabel";

    public MapDataFlavor(Class representationClass, String humanPresentableName)
	{
		super(representationClass, humanPresentableName);
	}
	
    @Override
		public boolean isFlavorSerializedObjectType() 
	{
        return false;
    }

}
