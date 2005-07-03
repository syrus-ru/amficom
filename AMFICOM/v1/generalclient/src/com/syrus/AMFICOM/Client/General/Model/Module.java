/**
 * $Id: Module.java,v 1.3 2005/05/18 14:01:20 bass Exp $
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
 * ��������� ����� ��� ��������� ������������� ������ ������ � ������������
 * ������������ �������� ������ (������� �� �������, ����������� ������ � �.�.)
 * ��� �������� �������� ���� ������
 *
 *
 *
 * @version $Revision: 1.3 $, $Date: 2005/05/18 14:01:20 $
 * @module generalclient_v1
 * @author $Author: bass $
 * @see com.syrus.AMFICOM.Client.General.Command.ExitCommand#execute
 */
public interface Module
{
	void initModule();
	void finalizeModule();
}
