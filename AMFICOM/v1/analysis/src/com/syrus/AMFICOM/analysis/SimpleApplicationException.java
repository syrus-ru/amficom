/*-
 * $Id: SimpleApplicationException.java,v 1.3 2005/09/07 02:56:49 arseniy Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

/**
 * »сключение дл€ сообщени€ о любых критических ошибках обработки данных.
 * ѕредназначено дл€ тривиального вывода в GUI фиксированного локализованного
 * текста.
 * ¬ качестве сообщени€ используетс€ ключ дл€ локализации.
 * ћетод getLocalizedMessage() возвращает локализованную строку по этому ключу.
 * @author $Author: arseniy $
 * @author saa (все шишки за неправильный подход к локализации адресовать сюда)
 * @version $Revision: 1.3 $, $Date: 2005/09/07 02:56:49 $
 * @module
 */
public class SimpleApplicationException extends Exception {
	private static final long serialVersionUID = 4385984337203935705L;

	// XXX: строка KEY_NULL_REFLECTOGRAMMA дублирует по смыслу строку GUIUtil.MSG_ERROR_NO_ONE_RESULT_HAS_TRACE
	public static final String KEY_NULL_REFLECTOGRAMMA = "errorNoReflectogramma";
	public SimpleApplicationException(String key) {
		super(key);
	}

	// XXX: пока не используетс€
	@Override
	public String getLocalizedMessage() {
		return LangModelAnalyse.getString(getMessage());
	}
}
