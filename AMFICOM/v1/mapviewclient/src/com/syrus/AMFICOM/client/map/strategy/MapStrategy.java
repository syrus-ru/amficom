/**
 * $Id: MapStrategy.java,v 1.4 2005/02/01 16:16:13 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.event.MouseEvent;

/**
 * ����� ��������� ��������� ��������� ����� ��� ���������� �������� �� �����
 * � ����������� �� ����������� ��������� �����. ��������� ����� ������������
 * ������� ������ ������������ � ������, ������� �����������, ����������
 * ��� ���������� ��������� ���� � ����������.
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $, $Date: 2005/02/01 16:16:13 $
 * @module mapviewclient_v1
 */
public abstract class MapStrategy
{
	/**
	 * ���������� ����.
	 */
	protected LogicalNetLayer logicalNetLayer;
	/**
	 * �������� ����������
	 */
	protected ApplicationContext aContext;

	/**
	 * ��������� ��������� �������� � �������� �����.
	 * @param mapElement ������� �����
	 */
	public abstract void doContextChanges(MouseEvent mapElement);
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
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}

}
