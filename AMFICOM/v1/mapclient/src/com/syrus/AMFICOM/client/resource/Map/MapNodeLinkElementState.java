/**
 * $Id: MapNodeLinkElementState.java,v 1.2 2004/09/15 08:28:52 krupenn Exp $
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
 * ��������� ��������� ����� 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/15 08:28:52 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeLinkElementState extends MapLinkElementState
{
	String physicalLinkId;

	public MapNodeLinkElementState(MapNodeLinkElement mnle)
	{
		super(mnle);
		
		physicalLinkId = mnle.physicalLinkId;
	}
}
