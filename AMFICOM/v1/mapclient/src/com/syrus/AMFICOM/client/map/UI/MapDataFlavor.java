/**
 * $Id: MapDataFlavor.java,v 1.4 2004/12/07 17:02:02 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.datatransfer.DataFlavor;

/**
 * Формат данных для переноса элементов карты для операций drag / drop
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/07 17:02:02 $
 * @module
 * @author $Author: krupenn $
 * @see
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
