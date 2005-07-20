/*-
 * $Id: SimpleApplicationException.java,v 1.1 2005/07/20 11:09:36 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

/**
 * Исключение для сообщения о любых критических ошибках обработки данных.
 * Предназначено для тривиального вывода в GUI фиксированного локализованного
 * текста.
 * В качестве сообщения используется ключ для локализации.
 * Метод getLocalizedMessage() возвращает локализованную строку по этому ключу. 
 * @author $Author: saa $
 * @author saa (все шишки за неправильный подход к локализации адресовать сюда)
 * @version $Revision: 1.1 $, $Date: 2005/07/20 11:09:36 $
 * @module
 */
public class SimpleApplicationException extends Exception {
	public static final String KEY_NULL_REFLECTOGRAMMA = "errorNoReflectogramma";
	public SimpleApplicationException(String key) {
		super(key);
	}

	@Override
	public String getLocalizedMessage() {
		return LangModelAnalyse.getString(getMessage());
	}
}
