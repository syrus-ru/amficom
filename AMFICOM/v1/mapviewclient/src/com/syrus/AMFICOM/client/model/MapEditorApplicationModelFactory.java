/**
 * $Id: MapEditorApplicationModelFactory.java,v 1.2 2005/06/22 08:43:50 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;


/**
 * ������� ������ 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/06/22 08:43:50 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorApplicationModelFactory
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = new MapEditorApplicationModel();
		return aModel;
	}
}
