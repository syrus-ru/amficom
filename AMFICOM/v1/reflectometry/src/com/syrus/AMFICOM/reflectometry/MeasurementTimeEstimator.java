/*-
 * $Id: MeasurementTimeEstimator.java,v 1.1 2006/02/17 12:47:36 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2006/02/17 12:47:36 $
 * @module reflectometry
 */
public interface MeasurementTimeEstimator {
	/**
	 * Оценивает время проведения измерения.
	 * @param rmp параметры измерения
	 * @param upper true, если нужна оценка сверху;
	 *   false, если нужна несмещенная оценка.
	 * @return время измерения в секундах
	 */
	double getEstimatedMeasurementTime (
			final ReflectometryMeasurementParameters rmp,
			final boolean upper);
}
