/**
 * $Id: MapStrategy.java,v 1.2 2004/10/19 11:48:28 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;

import java.awt.event.MouseEvent;

/**
 * ����� ��������� ��������� ��������� ����� ��� ���������� �������� �� �����
 * � ����������� �� ����������� ��������� �����. ��������� ����� ������������
 * ������� ������ ������������ � ������, ������� �����������, ����������
 * ��� ���������� ��������� ���� � ����������
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/19 11:48:28 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public interface MapStrategy
{
	void doContextChanges(MouseEvent me);
	void setLogicalNetLayer(LogicalNetLayer logicalNetLayer);
	void setMapElement(MapElement me);
}
