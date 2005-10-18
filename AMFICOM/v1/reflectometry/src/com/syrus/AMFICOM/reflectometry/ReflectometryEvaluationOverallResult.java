/*-
 * $Id: ReflectometryEvaluationOverallResult.java,v 1.2 2005/10/18 13:25:25 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/10/18 13:25:25 $
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
	 * Параметр со знаком, измеряется в децибеллах, означает степень отличия
	 * рефлектограммы в ее конце.
	 * @return D-параметр результата сравнения.
	 * @throws IllegalStateException, если <tt>hasDQ() == false</tt>.
	 */
	double getD();
	/**
	 * Возвращает Q-параметр результата сравнения.
	 * Параметр беззнаковый, безразмерный, в диапазоне [0..1], означает
	 * уровень качества:
	 * <ul>
	 * <li>Q=1 - соответствие 100% (полное)
	 * <li>Q=0 - соответствие 0%, отличие превышает предельное значение
	 * @return Q-параметр результата сравнения.
	 * @throws IllegalStateException, если <tt>hasDQ() == false</tt>.
	 */
	double getQ();
}
