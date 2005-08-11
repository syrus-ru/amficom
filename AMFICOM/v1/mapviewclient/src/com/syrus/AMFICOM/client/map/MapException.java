/**
 * $Id: MapException.java,v 1.3 2005/08/11 12:43:29 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map;

/**
 * 
 * @version $Revision: 1.3 $, $Date: 2005/08/11 12:43:29 $
 * @author $Author: arseniy $
 * @module mapviewclient
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
