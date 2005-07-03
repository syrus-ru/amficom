/**
 * $Id: MapConnectionException.java,v 1.3 2005/06/08 09:49:24 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map;

/**
 * 
 * @version $Revision: 1.3 $, $Date: 2005/06/08 09:49:24 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapConnectionException extends MapException {
	public MapConnectionException(String message) {
		super(message);
	}

	public MapConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapConnectionException(Throwable cause) {
		super(cause);
	}
}