/**
 * $Id: MapApplicationModelFactory.java,v 1.2 2005/01/30 15:34:56 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.General.Model;

/**
 * ��������� ����� �������� ������ ���������� ��� ������ � ������.
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/01/30 15:34:56 $
 * @module mapviewclient_v1
 */
public class MapApplicationModelFactory
		implements ApplicationModelFactory
{
	/**
	 * ������� ������ ������ ����������.
	 * @return ������
	 */
	public ApplicationModel create()
	{
		ApplicationModel aModel = new MapApplicationModel();
		return aModel;
	}
}
