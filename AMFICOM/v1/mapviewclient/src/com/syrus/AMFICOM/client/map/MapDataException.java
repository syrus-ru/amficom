/**
 * $Id: MapDataException.java,v 1.2 2005/06/06 12:20:29 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map;

/**
 * 
 * @version $Revision: 1.2 $, $Date: 2005/06/06 12:20:29 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapDataException extends Exception {
	public MapDataException(String message) {
		super(message);
	}

	public MapDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapDataException(Throwable cause) {
		super(cause);
	}
}