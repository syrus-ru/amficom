/**
 * $Id: MapConnectionException.java,v 1.2 2005/06/06 12:20:29 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map;

/**
 * 
 * @version $Revision: 1.2 $, $Date: 2005/06/06 12:20:29 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapConnectionException extends Exception {
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