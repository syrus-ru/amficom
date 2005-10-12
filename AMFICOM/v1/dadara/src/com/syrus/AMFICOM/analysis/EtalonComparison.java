/*-
 * $Id: EtalonComparison.java,v 1.2 2005/10/12 12:06:02 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import java.util.List;

import com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;

/**
 * ќбъедин€ет результаты сравнени€.
 * Ќе обеспечивает неизмен€емости - это целиком на совести вызывающего.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/10/12 12:06:02 $
 * @module dadara
 */
public interface EtalonComparison {
	/**
	 * ¬озвращает список полученных несоответствий
	 * XXX: неплохо бы переделать со списка на на массив несоответствий
	 * @return список полученных несоответствий
	 */
	List<ReflectogramMismatchImpl> getAlarms();
	/**
	 * ¬озвращает общие параметры качества р/г, полученные по результатам сравнени€
	 * в соответствии с контрактом {@link ReflectometryEvaluationOverallResult}
	 * @return общие параметры качества р/г, полученные по результатам сравнени€
	 */
	ReflectometryEvaluationOverallResult getOverallResult();
	/**
	 * ¬озвращает параметры качества р/г по каждому событию, полученные по результатам сравнени€ 
	 * в соответствии с контрактом {@link EvaluationPerEventResult}
	 * @return параметры качества р/г по каждому событию, полученные по результатам сравнени€
	 */
	EvaluationPerEventResult getPerEventResult();
}
