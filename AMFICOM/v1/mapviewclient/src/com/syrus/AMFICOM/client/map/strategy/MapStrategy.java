/**
 * $Id: MapStrategy.java,v 1.3 2004/12/22 16:38:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.event.MouseEvent;

/**
 * ����� ��������� ��������� ��������� ����� ��� ���������� �������� �� �����
 * � ����������� �� ����������� ��������� �����. ��������� ����� ������������
 * ������� ������ ������������ � ������, ������� �����������, ����������
 * ��� ���������� ��������� ���� � ����������
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/12/22 16:38:42 $
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
