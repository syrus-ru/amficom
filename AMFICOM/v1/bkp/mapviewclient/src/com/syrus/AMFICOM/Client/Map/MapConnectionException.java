/**
 * $Id: MapConnectionException.java,v 1.1 2005/02/18 12:19:44 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.Client.Map;

/**
 * 
 * @version $Revision: 1.1 $, $Date: 2005/02/18 12:19:44 $
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