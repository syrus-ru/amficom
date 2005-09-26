/*-
 * $Id: PFTrace.java,v 1.1 2005/09/26 07:27:56 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis;

import com.syrus.io.BellcoreStructure;

/**
 * Надстройка над {@link BellcoreStructure}, выполняющая предварительную
 * фильтрацию и кэширующая getTraceData() и результат фильтрации.
 * Данные о самой полученной рефлектограмме нужно брать через методы
 * доступа этого класса; дополнительные данные - через getBS()
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/09/26 07:27:56 $
 * @module
 */
public class PFTrace {
	private static final double FILTER_DECAY = 0.5; // dB / m
	private BellcoreStructure bs;
	double[] rawTrace; // lazy значение исходной рефлектограммы
	double[] filteredTrace; // lazy пред-обработанная рефлектограмма

	public PFTrace(BellcoreStructure bs) {
		this.bs = bs;
	}

	/**
	 * Получить копию исходной кривой
	 * (используется только при определении уровня шума)
	 * @return массив точек. Клиент может изменять полученный массив.
	 */
	public double[] getRawTraceClone() {
		return getRawTrace().clone();
	}

	/**
	 * Получить копию фильтрованной кривой
	 * (используется для анализа и отображения)
	 * @return массив точек. Клиент может изменять полученный массив.
	 */
	public double[] getFilteredTraceClone() {
		return getFilteredTrace().clone();
	}

	/**
	 * аналогично {@link #getRawTraceClone()}, но не делает копию.
	 * Клиент не должен будет изменять полученный массив.
	 */
	protected double[] getRawTrace() {
		if (this.rawTrace == null) {
			this.rawTrace = this.bs.getTraceData();
		}
		return this.rawTrace;
	}

	/**
	 * аналогично {@link #getFilteredTraceClone()}, но не делает копию.
	 * Клиент не должен будет изменять полученный массив.
	 */
	protected double[] getFilteredTrace() {
		if (this.filteredTrace == null) {
			this.filteredTrace = preFilter(getRawTrace(), getResolution());
		}
		return this.filteredTrace;
	}

	private static double[] preFilter(double[] y, double resolution) {
		double[] ret = y.clone();
		double delta = resolution * FILTER_DECAY;
		for (int i = 1; i < ret.length; i++) {
			double level = ret[i - 1] - delta;
			if (ret[i] < level) {
				ret[i] = level;
			}
		}
		return ret;
	}

	/**
	 * @see BellcoreStructure#getIOR()
	 */
	public double getIOR() {
		return this.bs.getIOR();
	}

	/**
	 * @see BellcoreStructure#getPulsewidth()
	 */
	public int getPulsewidth() {
		return this.bs.getPulsewidth();
	}

	/**
	 * @see BellcoreStructure#getResolution()
	 */
	public double getResolution() {
		return this.bs.getResolution();
	}

	/**
	 * @see BellcoreStructure#getWavelength()
	 */
	public int getWavelength() {
		return this.bs.getWavelength();
	}

	/**
	 * Возвращает нижележащую BellcoreStructure для целей
	 * сохранения/восстановления и доступа к специфическим параметрам.
	 * @return BellcoreStructure
	 */
	public BellcoreStructure getBS() {
		return this.bs;
	}
}
