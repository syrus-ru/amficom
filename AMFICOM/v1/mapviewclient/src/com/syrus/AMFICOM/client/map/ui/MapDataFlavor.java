/**
 * $Id: MapDataFlavor.java,v 1.6 2005/09/16 14:53:36 krupenn Exp $
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
 * @version $Revision: 1.6 $, $Date: 2005/09/16 14:53:36 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class MapDataFlavor extends DataFlavor
{
	public static final String MAP_PROTO_LABEL = "ElementLabel"; //$NON-NLS-1$

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
