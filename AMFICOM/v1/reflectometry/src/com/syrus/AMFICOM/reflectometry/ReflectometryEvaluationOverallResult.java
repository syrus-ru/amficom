/*-
 * $Id: ReflectometryEvaluationOverallResult.java,v 1.1 2005/10/11 13:22:40 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * Интерфейс для обмена
 * общими D и Q параметры (для всей рефлектограмы) результата сравнения.
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/11 13:22:40 $
 * @module reflectometry
 */
public interface ReflectometryEvaluationOverallResult {
	/**
	 * Возвращает true, если D и Q параметры доступны.
	 * Недоступность параметров подразумевает, что их было невозможно
	 * определить.
	 * @return true, если D и Q параметры доступны.
	 */
	boolean hasDQ();
	/**
	 * Возвращает D-параметр результата сравнения.
	 * @return D-параметр результата сравнения.
	 * @throws IllegalStateException, если <tt>hasDQ() == false</tt>.
	 */
	double getD();
	/**
	 * Возвращает Q-параметр результата сравнения.
	 * @return Q-параметр результата сравнения.
	 * @throws IllegalStateException, если <tt>hasDQ() == false</tt>.
	 */
	double getQ();
}
