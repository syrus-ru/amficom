/**
 * $Id: MapStrategy.java,v 1.10 2005/06/06 12:20:34 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map.strategy;

import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.map.MapElement;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/06 12:20:34 $
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
	 * @param logicalNetLayer ���������� ����
	 */
	public abstract void setLogicalNetLayer(LogicalNetLayer logicalNetLayer);

	/**
	 * ��������� ��������� �������� � �������� �����.
	 * @param mapElement ������� �����
	 */
	public abstract void doContextChanges(MouseEvent mapElement)
			throws MapConnectionException, MapDataException;
}
