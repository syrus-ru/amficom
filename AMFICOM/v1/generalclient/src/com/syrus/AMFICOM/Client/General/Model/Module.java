/**
 * $Id: Module.java,v 1.2 2005/04/13 21:40:46 arseniy Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/04/13 21:40:46 $
 * @module generalclient_v1
 * @author $Author: arseniy $
 * @see com.syrus.AMFICOM.Client.General.Command.ExitCommand#execute
 */
public interface Module 
{
	void initModule();
	void finalizeModule();
}
