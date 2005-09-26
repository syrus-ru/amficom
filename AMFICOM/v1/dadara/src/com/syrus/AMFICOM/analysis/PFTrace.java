/*-
 * $Id: PFTrace.java,v 1.2 2005/09/26 11:12:30 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/09/26 11:12:30 $
 * @module
 */
public class PFTrace {
	private static final double FILTER_DECAY = 0.5; // dB / m
	private BellcoreStructure bs;
	double[] rawTrace; // lazy значение исходной рефлектограммы
	double[] filteredTrace; // lazy пред-обработанная рефлектограмма

	public PFTrace(BellcoreStructure bs) {
		assert bs != null;
		this.bs = bs;
		this.rawTrace = null;
		this.filteredTrace = null;
	}

	/**
	 * Получить копию исходной кривой
	 * (используется только при определении уровня шума)
	 * <p>
	 * NB: Метод сделан protected, чтобы избежать ошибочного обращения к нему
	 * извне: для доступа к пред-фильтрованной р/г используйте
	 * {@link #getFilteredTraceClone()}.
	 * Если же доступ к этому методу все же необходим, его, в принципе,
	 * можно открыть. 
	 * @return массив точек. Клиент может изменять полученный массив.
	 */
	protected double[] getRawTraceClone() {
		return getRawTrace().clone();
	}

	/**
	 * Получить копию фильтрованной кривой
	 * (используется для анализа и отображения).
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
	 * Считает, что клиент не будет изменять полученную BellcoreStructure. 
	 * @return BellcoreStructure
	 */
	public BellcoreStructure getBS() {
		return this.bs;
	}
}
