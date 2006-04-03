/*-
 * $Id: Command.java,v 1.4 2006/04/03 11:57:15 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

/**
 * @author Andrei Kroupennikov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2006/04/03 11:57:15 $
 * @module commonclient
 */
public interface Command extends Cloneable {
	int RESULT_UNSPECIFIED = 0;
	int RESULT_OK = 1;
	int RESULT_YES = 1;
	int RESULT_NO = 2;
	int RESULT_CANCEL = 3;
	
	void execute();			// ������ ���������� �������

	void undo();				// �������� ���������� - ��������������
									// ����������� ���������

	void redo();				// ��������� ���������� �������

	void commitExecute();	// ������������� �������������� ����������
									// ������� � ������������ ��������

	void commitUndo();		// ������������� �������������� ���������
									// ���������� ������� � ������������
									// ��������

	Object getSource();		// �������� �������� �������

	void setParameter(String field, Object value);
									// ���������� �������� ��������� field

	int getResult();
}
