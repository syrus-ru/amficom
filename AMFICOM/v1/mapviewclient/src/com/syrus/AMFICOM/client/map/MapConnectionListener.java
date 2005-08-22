/**
 * $Id: MapConnectionListener.java,v 1.1 2005/08/22 15:10:39 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

public interface MapConnectionListener {

	void mapConnectionChanged() throws MapConnectionException;
}
