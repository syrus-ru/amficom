/**
 * $Id: DefaultMapApplicationModelFactory.java,v 1.3 2005/08/11 12:43:32 arseniy Exp $
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
 * ��������� ����� �������� ������ ���������� ��� ������ � ������ �� ���������.
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/11 12:43:32 $
 * @module mapviewclient
 */
public class DefaultMapApplicationModelFactory
		extends MapApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		// need to check how distance is measured in equirectangular projection
		aModel.setVisible(MapApplicationModel.OPERATION_MOVE_FIXED, false);

		return aModel;
	}
}
