/*-
 * $Id: ReflectometryAnalysisResult.java,v 1.1 2005/10/10 10:16:24 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/10 10:16:24 $
 * @module reflectometry
 */
public interface ReflectometryAnalysisResult {
	/**
	 * Возвращает byte[]-представление для AnalysisResult из dadara.
	 * @return byte[]-представление для AnalysisResult из dadara;
	 *   null, если анализа не было.
	 */
	byte[] getDadaraAnalysisResultBytes();
	/**
	 * Возвращает byte[]-представление для ReflectogramMismatchImpl[] из dadara.
	 * @return byte[]-представление для ReflectogramMismatchImpl[] из dadara;
	 *   null, если сравнения не было.
	 */
	byte[] getDadaraReflectogramMismatchBytes();
	/**
	 * Возвращает byte[]-поток для EvaluationResult из dadara.
	 * <p>
	 * Note: для создания EvaluationResult может быть недостаточно
	 * одного этого потока, а могут понадобиться также D- и Q- параметры.
	 * </p>
	 * @return byte[]-поток для EvaluationResult из dadara;
	 *   null, если сравнения не было.
	 */
	byte[] getDadaraEvaluationResultBytes();
	/**
	 * Возвращает true, если D и Q параметры доступны.
	 * Параметры могут быть недоступны как из-за того, что сравнение
	 * не проводилось, так и из-за того, что параметры D и Q определить
	 * не удалось из-за сильного изменения состояния линии, или
	 * по каким-то другим причинам.
	 * @return true, если D и Q параметры доступны.
	 */
	boolean hasDQ();
	/**
	 * Возвращает D-параметр результата сравнения.
	 * @return D-параметр результата сравнения.
	 * @throws IllegalStateException, если <tt>hasDQParameters() == false</tt>.
	 */
	double getD();
	/**
	 * Возвращает Q-параметр результата сравнения.
	 * @return Q-параметр результата сравнения.
	 * @throws IllegalStateException, если <tt>hasDQParameters() == false</tt>.
	 */
	double getQ();
}
