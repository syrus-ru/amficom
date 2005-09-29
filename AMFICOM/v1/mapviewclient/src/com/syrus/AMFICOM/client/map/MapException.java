/**
 * $Id: MapException.java,v 1.6 2005/09/29 12:48:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map;

import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * 
 * @version $Revision: 1.6 $, $Date: 2005/09/29 12:48:00 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapException extends Exception {
	private static final long serialVersionUID = -3759623595362764062L;

	public static final String DEFAULT_STRING = LangModelMap.getString(
			MapEditorResourceKeys.ERROR_MAP_EXCEPTION_SERVER_CONNECTION);
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
