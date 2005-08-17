/**
 * $Id: MapDataFlavor.java,v 1.5 2005/08/17 14:14:20 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.datatransfer.DataFlavor;

/**
 * ������ ������ ��� �������� ��������� ����� ��� �������� drag / drop
 * @version $Revision: 1.5 $, $Date: 2005/08/17 14:14:20 $
 * @author $Author: arseniy $
 * @module mapviewclient
 */
public class MapDataFlavor extends DataFlavor
{
	public static final String MAP_PROTO_LABEL = "ElementLabel";

    public MapDataFlavor(Class representationClass, String humanPresentableName)
	{
		super(representationClass, humanPresentableName);
	}
	
    @Override
		public boolean isFlavorSerializedObjectType() 
	{
        return false;
    }

}
