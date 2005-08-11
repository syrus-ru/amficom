/**
 * $Id: MapDataException.java,v 1.4 2005/08/11 12:43:29 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map;

/**
 * 
 * @version $Revision: 1.4 $, $Date: 2005/08/11 12:43:29 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public class MapDataException extends MapException {
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