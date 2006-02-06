/*-
 * $Id: ReflectometryMeasurementParameters.java,v 1.2 2005/10/10 09:51:22 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * Параметры измерения в рефлектометрии
 * @author $Author: saa $
 * @version $Revision: 1.2 $, $Date: 2005/10/10 09:51:22 $
 * @module
 */
public interface ReflectometryMeasurementParameters {

	/**
	 * возвращает, включен ли режим gain splice
	 * @return true, если включен режим gain splice
	 */
	boolean hasGainSplice();

	/**
	 * возвращает, включен ли режим высокого разрешения
	 * @return true, если включен режим высокого разрешения
	 */
	boolean hasHighResolution();

	/**
	 * возвращает, включен ли контроль активности волокна
	 * @return true, если включен контроль активности волокна
	 */
	boolean hasLiveFiberDetection();

	/**
	 * возвращает	число усреднений
	 * @return		число усреднений
	 */
	int getNumberOfAverages();

	/**
	 * возвращает	длительность импульса, нс
	 * @return		длительность импульса, нс
	 */
	int getPulseWidth();

	/**
	 * возвращает	показатель преломления
	 * @return		показатель преломления
	 */
	double getRefractionIndex();

	/**
	 * возвращает	разрешение, метры
	 * @return		разрешение, метры
	 */
	double getResolution();

	/**
	 * возвращает	дистанцию тестирования, км
	 * @return 		дистанция тестирования, км
	 */
	double getTraceLength();

	/**
	 * возвращает 	длину волны, метры
	 * @return 		длина волны, метры
	 */
	int getWavelength();

}