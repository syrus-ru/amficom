/**
 * $Id: MapException.java,v 1.4 2005/09/16 14:53:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map;

import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * 
 * @version $Revision: 1.4 $, $Date: 2005/09/16 14:53:32 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapException extends Exception {
	public static final String DEFAULT_STRING = LangModelMap.getString("MapException.ServerConnectionException"); //$NON-NLS-1$
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
