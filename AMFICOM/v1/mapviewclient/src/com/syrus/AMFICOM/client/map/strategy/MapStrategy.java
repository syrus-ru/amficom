/**
 * $Id: MapStrategy.java,v 1.11 2005/06/16 10:57:21 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map.strategy;

import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.MapElement;

/**
 * @version $Revision: 1.11 $, $Date: 2005/06/16 10:57:21 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public interface MapStrategy {
	/**
	 * ���������� ������� �����, � �������� ����������� ��������� ��������.
	 * @param mapElement ������� ����
	 */
	public abstract void setMapElement(MapElement mapElement);

	/**
	 * ���������� ������ �� ���������� ����, �� ������� �����������
	 * �������� ���������.
	 * @param netMapViewer ���������� ����
	 */
	public abstract void setNetMapViewer(NetMapViewer netMapViewer);

	/**
	 * ��������� ��������� �������� � �������� �����.
	 * @param mapElement ������� �����
	 */
	public abstract void doContextChanges(MouseEvent mapElement)
			throws MapConnectionException, MapDataException;
}
