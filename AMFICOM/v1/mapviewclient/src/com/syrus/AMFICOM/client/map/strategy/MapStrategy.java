/**
 * $Id: MapStrategy.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public interface MapStrategy
{
	public void doContextChanges(MouseEvent me);
	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer);
	public void setMapElement(MapElement me);
}
