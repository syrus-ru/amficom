/*-
 * $Id: AnalysisParametersStorage.java,v 1.10 2005/12/22 14:37:28 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.io.SignatureMismatchException;

/**
 * Хранилище набора параметров анализа, не реализующее контроля допустимости.
 * Рекомендуется использовать {@link AnalysisParameters},
 * а {@link AnalysisParametersStorage} использовать тогда, когда нужно изменить
 * сразу несколько параметров.
 * @author $Author: saa $
 * @version $Revision: 1.10 $, $Date: 2005/12/22 14:37:28 $
 * @todo add extended parameters save to DOS / restore from DIS
 * @module
 */
public class AnalysisParametersStorage
implements DataStreamable, Cloneable
{
	private static final double DEFAULT_EVENT_TO_SPLICE_RATIO = 0.4;
	private static final double ABS_MIN_SPLICE_TH = 0.001;
	private static final double ABS_MAX_END_TH = 30.0;

	private static final double ABS_MIN_EVENT_TH = ABS_MIN_SPLICE_TH
			* DEFAULT_EVENT_TO_SPLICE_RATIO * 0.9; // 0.9 - запас на ошибку округления

	private static final double[] RECOMMENDED_NOISE_FACTORS = new double[] {
		1.0, 1.3, 1.5, 2.0, 2.5 };

	// основные параметры анализа
	private double[] param;

	private static final double LEVEL_EOT_FRACTION = 10;
	private static final double LEVEL_EOT_MIN = -99.0;
	private static final double LEVEL_EOT_MAX = 0.0;
	private static final double LEVEL_EOT_DEFAULT = LEVEL_EOT_MIN;
	// FIXME: levelEot: save to String & ini / restore from String & ini
	private double levelEot;

	// дополнительные параметры анализа
	private double tau2nrs = 1.0;
	private int nrsMin = 15;
	private double rsaCrit = 0.5;
	private double nrs2rsaSmall = 1.5;
	private double nrs2rsaBig = 10.0;
	private double l2rsaBig = 0.1;

	// еще дополнительный параметр
	private double scaleFactor = 1.65;
	private static final long SIGNATURE_BASE_VER = 8679213050930145800L;

	/**
	 * Устанавливает все свои свойства так же, как и у другого экземпляря
	 * @param that другой экземпляр
	 */
	public void setAllFrom(AnalysisParametersStorage that) {
		System.arraycopy(that.param, 0, this.param, 0, this.param.length);
		this.tau2nrs = that.tau2nrs;
		this.nrsMin = that.nrsMin;
		this.rsaCrit = that.rsaCrit;
		this.nrs2rsaSmall = that.nrs2rsaSmall;
		this.nrs2rsaBig = that.nrs2rsaBig;
		this.l2rsaBig = that.l2rsaBig;
		this.levelEot = that.levelEot;
	}

	private boolean doublesDiffer(double a, double b) {
		return Double.doubleToLongBits(a) != Double.doubleToLongBits(b);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AnalysisParametersStorage)) {
			return false;
		}
		AnalysisParametersStorage that = (AnalysisParametersStorage)obj;
		if (obj == this)
			return true;
		if (this.param.length != that.param.length)
			return false;
		for (int i = 0; i < this.param.length; i++) {
			if (doublesDiffer(this.param[i], that.param[i])) {
				return false;
			}
		}
		if (doublesDiffer(this.tau2nrs, that.tau2nrs)
				|| doublesDiffer(this.nrsMin, that.nrsMin)
				|| doublesDiffer(this.rsaCrit, that.rsaCrit)
				|| doublesDiffer(this.nrs2rsaSmall, that.nrs2rsaSmall)
				|| doublesDiffer(this.nrs2rsaBig, that.nrs2rsaBig)
				|| doublesDiffer(this.l2rsaBig, that.l2rsaBig)
				|| doublesDiffer(this.levelEot, that.levelEot)) {
			return false;
		}
		return true;
	}

	/**
	 * Определяет допустимость набора параметров.
	 * @return true, если набор допустим, false, если недопустим
	 */
	public boolean isCorrect() {
		// проверяем основные параметры
		if (getEventTh() < getMinEventTh() || getEventTh() > getMaxEventTh())
			return false;
		if (getSpliceTh() < getMinSpliceTh() || getSpliceTh() > getMaxSpliceTh())
			return false;
		if (getConnectorTh() < getMinConnectorTh() | getConnectorTh() > getMaxConectorTh())
			return false;
		if (getEndTh() < getMinEndTh() || getEndTh() > getMaxEndTh())
			return false;
		if (getLevelEot() < getMinLevelEot() || getLevelEot() > getMaxLevelEot())
			return false;

		// проверяем дополнительные параметры
		if (this.tau2nrs < getMinTau2nrs() || this.tau2nrs > getMaxTau2nrs())
			return false;
		if (this.nrsMin < getMinRsaCrit() || this.nrsMin > getMaxNrsMin())
			return false;

		if (this.rsaCrit < getMinRsaCrit() || this.rsaCrit > getMaxRsaCrit())
			return false;
		if (this.nrs2rsaSmall < getMinNrs2rsaSmall() || this.nrs2rsaSmall > getMaxNrs2rsaSmall())
			return false;
		if (this.nrs2rsaBig < getMinNrs2rsaBig() || this.nrs2rsaBig > getMaxNrs2rsaBig())
			return false;
		if (this.l2rsaBig < getMinL2rsaBig() || this.l2rsaBig > getMaxL2rsaBig())
			return false;

		if (this.scaleFactor < getMinScaleFactor() || this.scaleFactor > getMaxScaleFactor())
			return false;

		// дополнительные проверки
		if (this.tau2nrs == 0 && this.nrsMin == 0)
			return false;
		if (this.nrs2rsaSmall <= 0)
			return false;

		return true;
	}

	private double getMinLevelEot() {
		return LEVEL_EOT_MIN;
	}
	private double getMaxLevelEot() {
		return LEVEL_EOT_MAX;
	}

	public double getEventTh() {
		return this.param[0];
	}
	public double getSpliceTh() {
		return this.param[1];
	}
	public double getConnectorTh() {
		return this.param[2];
	}
	public double getEndTh() {
		return this.param[3];
	}
	public double getNoiseFactor() {
		return this.param[4];
	}

	public double getSentitivity() {
		return getSpliceTh();
	}

	public void setEventTh(double v) {
		this.param[0] = v;
	}

	public void setSpliceTh(double v) {
		this.param[1] = v;
	}

	public void setSensitivity(double v) {
		this.setSpliceTh(v, false);
		this.setEventTh(v * DEFAULT_EVENT_TO_SPLICE_RATIO, false);
	}

	public void setConnectorTh(double v) {
		this.param[2] = v;
	}

	public void setEndTh(double v) {
		this.param[3] = v;
	}

	public void setNoiseFactor(double v) {
		this.param[4] = v;
	}

	/**
	 * @param v значение, которое желательно установить
	 * @param nearest true, если желательно ограничить входное значение
	 * диапазоном допустимых значений
	 */
	public void setEventTh(double v, boolean nearest) {
		setEventTh(nearest ? limit(v, getMinEventTh(), getMaxEventTh()) : v);
	}

	/**
	 * @see #setEventTh(double, boolean)
	 */
	public void setSpliceTh(double v, boolean nearest) {
		setSpliceTh(nearest ? limit(v, getMinSpliceTh(), getMaxSpliceTh()) : v);
	}

	/**
	 * @see #setEventTh(double, boolean)
	 */
	public void setSensitivity(double v, boolean nearest) {
		setSensitivity(nearest ? limit(v, getMinSensitivity(), getMaxSensitivity()) : v);
	}

	/**
	 * @see #setEventTh(double, boolean)
	 */
	public void setConnectorTh(double v, boolean nearest) {
		setConnectorTh(nearest ? limit(v, getMinConnectorTh(), getMaxConectorTh()) : v);
	}

	/**
	 * @see #setEventTh(double, boolean)
	 */
	public void setEndTh(double v, boolean nearest) {
		setEndTh(nearest ? limit(v, getMinEndTh(), getMaxEndTh()) : v);
	}

	/**
	 * @see #setEventTh(double, boolean)
	 */
	public void setNoiseFactor(double v, boolean nearest) {
		setNoiseFactor(nearest ? limit(v, getMinNoiseFactor(), getMaxNoiseFactor()) : v);
	}

	/**
	 * @return список рекомендуемых значений noiseFactor
	 */
	public static double[] getRecommendedNoiseFactors() {
		return RECOMMENDED_NOISE_FACTORS.clone();
	}

	public AnalysisParametersStorage(double eventTh,
			double spliceTh,
			double connectorTh,
			double endTh,
			double noiseFactor)
	{
		this.param = new double[] {
			eventTh,
			spliceTh,
			connectorTh,
			endTh,
			noiseFactor
		};
		this.levelEot = LEVEL_EOT_DEFAULT;
	}

	public AnalysisParametersStorage(DataInputStream dis)
	throws IOException, SignatureMismatchException {
		long version = dis.readLong() - SIGNATURE_BASE_VER; 
		if (version < 0 || version > 1) {
			throw new SignatureMismatchException();
		}
		this.param = new double[5];
		this.param[0] = dis.readDouble();
		this.param[1] = dis.readDouble();
		this.param[2] = dis.readDouble();
		this.param[3] = dis.readDouble();
		this.param[4] = dis.readDouble();
		if (version == 1) {
			this.levelEot = dis.readDouble();
		} else {
			this.levelEot = LEVEL_EOT_DEFAULT;
		}
	}

	// returns true if all fields were initialized,
	// false otherwise.
	private boolean setParamsFromString(String val) {
		int i = 0;
		int bind = -1;
		int ind = val.indexOf(";");
		while ((ind != -1) && (i < this.param.length)) {
			this.param[i++] = Double.parseDouble(val.substring(bind + 1, ind));
			bind = ind;
			ind = val.indexOf(";", bind + 1);
		}
		return i == this.param.length;
	}

	/**
	 * creates via string of parameters using the default values
	 * @param val text representation of parameters
	 * @param defaults default values
	 */
	public static AnalysisParametersStorage createFromStringWithDefaults(
			String val,
			AnalysisParametersStorage defaults) {
		AnalysisParametersStorage ret = defaults.clone();
		ret.setParamsFromString(val);
		return ret;
	}

	/**
	 * creates via string of parameters
	 * @param val text representation of parameters
	 * @throws IllegalArgumentException if input string is malformed
	 */
	public AnalysisParametersStorage(String val) {
		this.param = new double[5];
		this.levelEot = LEVEL_EOT_DEFAULT;
		if (!setParamsFromString(val))
			throw new IllegalArgumentException(
					"couldn't parse analysis parameters string");
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < this.param.length; i++)
			str = str + String.valueOf(this.param[i]) + ";";
		return str;
	}

	@Override
	public AnalysisParametersStorage clone() {
		try {
			AnalysisParametersStorage ret = (AnalysisParametersStorage)super.clone();
			ret.param = this.param.clone();
			return ret;
		} catch (CloneNotSupportedException e) {
			throw new InternalError("Unexpected exception: " + e.getMessage());
		}
	}

	public void writeToDOS(DataOutputStream dos)
	throws IOException {
		long version = this.levelEot == LEVEL_EOT_DEFAULT ? 0 : 1;
		dos.writeLong(SIGNATURE_BASE_VER + version);
		dos.writeDouble(this.param[0]);
		dos.writeDouble(this.param[1]);
		dos.writeDouble(this.param[2]);
		dos.writeDouble(this.param[3]);
		dos.writeDouble(this.param[4]);
		if (version == 1) {
			dos.writeDouble(this.levelEot);
		}
	}

	public double getL2rsaBig() {
		return this.l2rsaBig;
	}
	public void setL2rsaBig(double big) {
		this.l2rsaBig = big;
	}
	public double getNrs2rsaBig() {
		return this.nrs2rsaBig;
	}
	public void setNrs2rsaBig(double nrs2rsaBig) {
		this.nrs2rsaBig = nrs2rsaBig;
	}
	public double getNrs2rsaSmall() {
		return this.nrs2rsaSmall;
	}
	public void setNrs2rsaSmall(double nrs2rsaSmall) {
		this.nrs2rsaSmall = nrs2rsaSmall;
	}
	public int getNrsMin() {
		return this.nrsMin;
	}
	public void setNrsMin(int nrsMin) {
		this.nrsMin = nrsMin;
	}
	public double getRsaCrit() {
		return this.rsaCrit;
	}
	public void setRsaCrit(double rsaCrit) {
		this.rsaCrit = rsaCrit;
	}
	public double getTau2nrs() {
		return this.tau2nrs;
	}
	public void setTau2nrs(double tau2nrs) {
		this.tau2nrs = tau2nrs;
	}

	public double getScaleFactor() {
		return this.scaleFactor;
	}

	public void setScaleFactor(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	/**
	 * методы getMin*, getMax* - возвращают допустимые пределы вариации
	 * каждого параметра, считая, что остальные параметры зафиксированы.
	 * используются:
	 * <ul>
	 * <li> внутри - как часть {@link #isCorrect()}
	 * <li> извне - для возможности "попытаться установить допустимое значение,
	 * ближайшее к заданному пользователем". <b>Внимание:</b>
	 * Эти методы не гарантируют, что полученные через них min/max значения
	 * будут давать согласованный (с точки зрения {@link #isCorrect()})
	 * набор параметров. Поэтому после установки min/max значения надо будет
	 * сделать дополнительную проверку isCorrect. Если проверка вернет
	 * false, то надо считать, что такая возможность для данного набора
	 * параметров на данный момент не поддерживается. Так получается из-за
	 * сложной природы ограничений, которая приводит к тому, что
	 * некоторые проверки плохо укладываются в концепцию min/max значений
	 * (самый типичный пример - выколотая точка в двумерном пространстве).
	 * </ul>
	 * 
	 * <p>Методы предназначены для отслеживания связей между параметрами
	 * eventTh/spliceTh/connTh/endTh, для которых допустимые пределы вариации,
	 * как правило, определены строго.</p>
	 * 
	 * <p>Для параметров, не предназначенных для непосредственного
	 * ввода со стороны пользователя (например, noseFactor, для которого выбор
	 * предполагается из списка), пределы могут быть вида min=0, max=MAX_VALUE.</p>
	 */
	public double getMinEventTh() {
		return ABS_MIN_EVENT_TH;
	}

	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxEventTh() {
		return getSpliceTh();
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinSpliceTh() {
		return getEventTh();
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxSpliceTh() {
		return getConnectorTh();
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinSensitivity() {
		return ABS_MIN_SPLICE_TH;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxSensitivity() {
		return getConnectorTh();
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinConnectorTh() {
		return getSpliceTh();
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxConectorTh() {
		return getEndTh();
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinEndTh() {
		return getConnectorTh();
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxEndTh() {
		return ABS_MAX_END_TH;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinNoiseFactor() {
		return 0.0; // фактически, это уже недопустимое значение, но мы считаем, что GUI будет выбирать только из рекомендованных значений
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxNoiseFactor() {
		return Double.MAX_VALUE; // фактически, это недопустимое значение
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinL2rsaBig() {
		return 0.0;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxL2rsaBig() {
		return Double.MAX_VALUE;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinNrs2rsaBig() {
		return getNrs2rsaSmall();
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxNrs2rsaBig() {
		return Double.MAX_VALUE;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinNrs2rsaSmall() {
		return 0.0;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxNrs2rsaSmall() {
		return getNrs2rsaBig();
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public int getMinNrsMin() {
		return 0;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public int getMaxNrsMin() {
		return Integer.MAX_VALUE;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinRsaCrit() {
		return 0.0;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxRsaCrit() {
		return Double.MAX_VALUE;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinScaleFactor() {
		return 1.0;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxScaleFactor() {
		return 10.0; // XXX
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMinTau2nrs() {
		return 0.0;
	}
	/**
	 * See documentation for {@link #getMinEventTh()} for more information.
	 * @see #getMinEventTh()
	 */
	public double getMaxTau2nrs() {
		return Double.MAX_VALUE;
	}

	private static double limit(double v, double low, double high) {
		return v < high ? v < low ? low : v : high; 
	}

	public double getLevelEot() {
		return this.levelEot;
	}

	public void setLevelEot(double levelEot) {
		this.levelEot = Math.round(levelEot * LEVEL_EOT_FRACTION)
			/ LEVEL_EOT_FRACTION;
	}

	public void setLevelEot(double v, boolean useLimits) {
		setLevelEot(useLimits ? limit(v, getMinLevelEot(), getMaxLevelEot()) : v);
	}
}
