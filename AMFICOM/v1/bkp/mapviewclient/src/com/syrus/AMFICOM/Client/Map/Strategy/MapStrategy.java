/**
 * $Id: MapStrategy.java,v 1.9 2005/03/02 12:35:40 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.Client.Map.Strategy;

import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.map.MapElement;

/**
 * @version $Revision: 1.9 $, $Date: 2005/03/02 12:35:40 $
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
