/**
 * $Id: MapLinkElementState.java,v 1.1 2004/09/13 12:02:01 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import java.util.HashMap;

/**
 * ��������� ��������� �������� ����� 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:02:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapLinkElementState extends MapElementState
{
	String name;
	String description;

	MapNodeElement startNode;//������
	MapNodeElement endNode;//�����

	java.util.HashMap attributes = new HashMap();

	public MapLinkElementState(MapLinkElement mle)
	{
		super();
		name = mle.getName();
		description = mle.getDescription();
		startNode = mle.getStartNode();
		endNode = mle.getEndNode();

		attributes.putAll(mle.attributes);
	}
}
