/**
 * $Id: MapDataFlavor.java,v 1.3 2004/10/18 12:43:09 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.datatransfer.DataFlavor;

/**
 * ������ ������ ��� �������� ��������� ����� ��� �������� drag / drop
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/10/18 12:43:09 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapDataFlavor extends DataFlavor
{
	public static final String MAP_PROTO_LABEL = "ElementLabel";
    public MapDataFlavor(Class representationClass, String humanPresentableName)
	{
		super(representationClass, humanPresentableName);
	}
	
    public boolean isFlavorSerializedObjectType() 
	{
        return false;
    }

}
