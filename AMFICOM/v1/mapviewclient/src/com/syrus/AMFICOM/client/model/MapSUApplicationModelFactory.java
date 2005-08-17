/**
 * $Id: MapSUApplicationModelFactory.java,v 1.4 2005/08/17 14:14:21 arseniy Exp $
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
 * ��������� ����� �������� ������ ���������� ��� ������ � ������ 
 * � ������ ����������������� (��� �������� ���������).
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/08/17 14:14:21 $
 * @module mapviewclient
 */
public class MapSUApplicationModelFactory
		extends MapApplicationModelFactory 
{
	@Override
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		return aModel;
	}
}
