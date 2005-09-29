/**
 * $Id: MapConnectionException.java,v 1.5 2005/09/29 12:48:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map;

/**
 * 
 * @version $Revision: 1.5 $, $Date: 2005/09/29 12:48:00 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapConnectionException extends MapException {
	private static final long serialVersionUID = -3314029462771257053L;

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