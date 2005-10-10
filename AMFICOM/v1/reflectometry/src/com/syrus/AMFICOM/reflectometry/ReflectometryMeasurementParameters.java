/*-
 * $Id: ReflectometryMeasurementParameters.java,v 1.1 2005/10/10 07:38:40 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * Параметры измерения в рефлектометрии
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/10 07:38:40 $
 * @module
 */
public interface ReflectometryMeasurementParameters {

	/**
	 * @return true, если включен режим gain splice
	 */
	boolean hasGainSplice();

	/**
	 * @return true, если включен режим высокого разрешения
	 */
	boolean hasHighResolution();

	/**
	 * @return true, если включен контроль активности волокна
	 */
	boolean hasLiveFiberDetection();

	/**
	 * @return число усреднений
	 */
	int getNumberOfAverages();

	/**
	 * @return длительность импульса, нс
	 */
	int getPulseWidth();

	/**
	 * @return показатель преломления
	 */
	double getRefractionIndex();

	/**
	 * @return разрешение, метры
	 */
	double getResolution();

	/**
	 * @return дистанция тестирования, км
	 */
	double getTraceLength();

	/**
	 * @return длина волны, метры
	 */
	int getWavelength();

}