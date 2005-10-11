/*-
 * $Id: ReflectometryAnalysisResult.java,v 1.2 2005/10/11 13:22:40 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * Результаты анализа и оценки.
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/10/11 13:22:40 $
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
	 * Возвращает byte[]-поток для EvaluationPerEventResult из dadara.
	 * @return byte[]-поток для EvaluationPerEventResult из dadara;
	 *   null, если сравнения не было.
	 */
	byte[] getDadaraEvaluationPerEventResultBytes();
	/**
	 * Возвращает общие параметры результата сравнения с эталоном.
	 * @return общие параметры результата сравнения с эталоном;
	 *   null, если сравнения не было.
	 */
	ReflectometryEvaluationOverallResult
			getReflectometryEvaluationOverallResult();
}
