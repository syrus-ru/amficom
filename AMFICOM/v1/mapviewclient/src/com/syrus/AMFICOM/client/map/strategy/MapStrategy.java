/*-
 * $$Id: MapStrategy.java,v 1.12 2005/09/30 16:08:41 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.MapElement;

/**
 * @version $Revision: 1.12 $, $Date: 2005/09/30 16:08:41 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
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
