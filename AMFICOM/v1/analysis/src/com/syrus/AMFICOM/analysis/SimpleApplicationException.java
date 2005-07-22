/*-
 * $Id: SimpleApplicationException.java,v 1.2 2005/07/22 08:35:06 saa Exp $
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
 * @author $Author: saa $
 * @author saa (все шишки за неправильный подход к локализации адресовать сюда)
 * @version $Revision: 1.2 $, $Date: 2005/07/22 08:35:06 $
 * @module
 */
public class SimpleApplicationException extends Exception {
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
