/**
 * $Id: MapException.java,v 1.2 2005/07/01 16:09:37 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map;

/**
 * 
 * @version $Revision: 1.2 $, $Date: 2005/07/01 16:09:37 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapException extends Exception {
	public static final String DEFAULT_STRING = "Ошибка соединения с сервером картографической информации";
    public MapException() {
		super();
	    }

	public MapException(String message) {
		super(message);
	}

	public MapException(String message, Throwable cause) {
		super(message, cause);
	}

	public MapException(Throwable cause) {
		super(cause);
	}
}
