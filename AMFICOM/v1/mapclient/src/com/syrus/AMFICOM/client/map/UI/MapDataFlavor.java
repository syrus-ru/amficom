/**
 * $Id: MapDataFlavor.java,v 1.2 2004/10/15 14:09:00 krupenn Exp $
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
 * @version $Revision: 1.2 $, $Date: 2004/10/15 14:09:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapDataFlavor extends DataFlavor
{
    public MapDataFlavor(Class representationClass, String humanPresentableName)
	{
		super(representationClass, humanPresentableName);
	}
	
    public boolean isFlavorSerializedObjectType() 
	{
        return false;
    }

}
