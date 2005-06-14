/**
 * $Id: DefaultMapEditorApplicationModelFactory.java,v 1.2 2005/06/14 12:05:09 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.model;

import com.syrus.AMFICOM.client.model.ApplicationModel;

/**
 * ������ ���������� �� ��������� - ���������, ����� ������� �� ���������
 * �������� ��� �� ������� ������������
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/06/14 12:05:09 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class DefaultMapEditorApplicationModelFactory
		extends MapEditorApplicationModelFactory 
{
	public ApplicationModel create()
	{
		ApplicationModel aModel = super.create();

		return aModel;
	}
}
