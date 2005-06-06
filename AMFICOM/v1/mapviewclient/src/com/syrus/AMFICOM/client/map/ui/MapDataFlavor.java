/**
 * $Id: MapDataFlavor.java,v 1.3 2005/06/06 12:20:35 krupenn Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/06/06 12:20:35 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapDataFlavor extends DataFlavor
{
	public static final String MAP_PROTO_LABEL = "ElementLabel";

    public MapDataFlavor(Class representationClass, String humanPresentableName)
	{
		super(representationClass, humanPresentableName);
	}
	
    public boolean isFlavorSerializedObjectType() 
	{
        return false;
    }

}
