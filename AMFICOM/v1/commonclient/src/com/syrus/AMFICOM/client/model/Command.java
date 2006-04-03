/*-
 * $Id: Command.java,v 1.4 2006/04/03 11:57:15 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
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
	
	void execute();			// первое выполнение команды

	void undo();				// обратное выполнение - восстановление
									// предыдущего состояния

	void redo();				// повторное выполнение команды

	void commitExecute();	// подтверждение окончательного выполнения
									// команды и освобождение ресурсов

	void commitUndo();		// подтверждение окончательного обратного
									// выполнения команды и освобождение
									// ресурсов

	Object getSource();		// получить источник команды

	void setParameter(String field, Object value);
									// установить значение параметра field

	int getResult();
}
