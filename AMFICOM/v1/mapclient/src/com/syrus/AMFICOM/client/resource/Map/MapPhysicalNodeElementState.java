/**
 * $Id: MapPhysicalNodeElementState.java,v 1.1 2004/09/13 12:02:01 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

/**
 * ��������� ��������������� ���� 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:02:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPhysicalNodeElementState extends MapNodeElementState
{
	boolean active;
	String physicalLinkId;
	
	public MapPhysicalNodeElementState(MapPhysicalNodeElement mpne)
	{
		super(mpne);

		active = mpne.isActive();
		physicalLinkId = mpne.physicalLinkId;
	}
}
