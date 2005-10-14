/*-
 * $Id: DadaraReflectometryAnalysisResult.java,v 1.2 2005/10/14 16:53:48 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.analysis.EtalonComparison;
import com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;
import com.syrus.io.DataFormatException;

/**
 * Предоставляет проеобразование результатов анализа и сравнения
 * между ReflectometryAnalysisResult и объектным представлением dadara.
 * <p>Обеспечивает полную защиту данных - и на входе, и на выходе.
 * <p>Поддерживает два способа создания:
 * на основе {@link ReflectometryAnalysisResult}
 * и
 * на основе пары {@link AnalysisResult} и {@link EtalonComparison}.
 * <p>При создании на основе {@link ReflectometryAnalysisResult}
 * возможны нулевые значения любых свойств, поскольку это допускается
 * интерфейсом входных данных {@link ReflectometryAnalysisResult}.
 * <p>При создании
 * на основе пары {@link AnalysisResult} и {@link EtalonComparison}
 * также допустимы null-значения, однако, если входные ar и ec будут not null,
 * то гарантировано, что и все свойства созданного объекта будут not null.
 * <p>XXX: допустимость null-значений свойств зависит от способа создания.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/10/14 16:53:48 $
 * @module dadara
 */
public class DadaraReflectometryAnalysisResult implements
		ReflectometryAnalysisResult {

	private AnalysisResult ar; // may be null
	private ReflectogramMismatchImpl[] rma; // may be null
	private EvaluationPerEventResultImpl epe; // may be null
	private ReflectometryEvaluationOverallResultImpl eOverall; // may be null

	/**
	 * Создается на основе результатов анализа и сравнения dadara
	 * Создает копии входных данных.
	 * Если ar != null, то гарантировано, что выходные свойства также будут
	 * not null. Если ar == null, то гарантировано, что
	 * все выходные свойства будут null ;-)
	 * @param ar may be null.
	 * @param ec not null unless ar == null.
	 */
	public DadaraReflectometryAnalysisResult(AnalysisResult ar,
			EtalonComparison ec) {
		if (ar == null) {
			this.ar = null;
			this.rma = null;
			this.epe = null;
			this.eOverall = null;
			return;
		}
		this.ar = new AnalysisResult(ar);

		final List<ReflectogramMismatchImpl> alarms = ec.getAlarms();
		this.rma = alarms.toArray(new ReflectogramMismatchImpl[alarms.size()]);
		// надо склонировать алармы, т.к. они modifiable
		for (int i = 0; i < this.rma.length; i++) {
			this.rma[i] = this.rma[i].copy();
		}

		this.epe = new EvaluationPerEventResultImpl(ec.getPerEventResult());

		this.eOverall = new ReflectometryEvaluationOverallResultImpl(
				ec.getOverallResult());
	}

	/**
	 * Считывает(импортирует) данные другого
	 * {@link ReflectometryAnalysisResult}
	 * (copy-конструктор)
	 * @param that копируемый {@link ReflectometryAnalysisResult}
	 * @throws DataFormatException 
	 */
	public DadaraReflectometryAnalysisResult(ReflectometryAnalysisResult that)
	throws DataFormatException {
		// распаковываем AR
		final byte[] arBytes = that.getDadaraAnalysisResultBytes();
		this.ar = arBytes == null ? null
				: (AnalysisResult)DataStreamableUtil.readDataStreamableFromBA(
					arBytes,
					AnalysisResult.getDSReader());
		// распаковываем RM
		final byte[] rmBytes = that.getDadaraReflectogramMismatchBytes();
		this.rma = rmBytes == null ? null
				: ReflectogramMismatchImpl.alarmsFromByteArray( rmBytes);
		// распаковываем PE
		final byte[] epeBytes = that.getDadaraEvaluationPerEventResultBytes();
		this.epe = epeBytes == null ? null
				: (EvaluationPerEventResultImpl)
					DataStreamableUtil.readDataStreamableFromBA(
						epeBytes,
						EvaluationPerEventResultImpl.getDSReader());
		// делаем копию Overall
		final ReflectometryEvaluationOverallResult eoResult =
			that.getReflectometryEvaluationOverallResult();
		this.eOverall = eoResult == null ? null
				: new ReflectometryEvaluationOverallResultImpl(eoResult);
	}

	/**
	 * Возвращает копию своего AnalysisResult, may be null
	 * @return копию своего AnalysisResult, may be null
	 */
	public AnalysisResult getDadaraAnalysisResult() {
		return this.ar == null ? null
				: new AnalysisResult(this.ar); // делаем копию
	}

	/**
	 * Возвращает представление в виде {@link EtalonComparison}, may be null.
	 * Если не определено хотя бы одно свойство, необходимое для
	 * {@link EtalonComparison}, также возвращает null.
	 */
	public EtalonComparison getDadaraEtalonComparison() {
		if (this.rma == null || this.epe == null || this.eOverall == null) {
			return null;
		}
		// копируем элементы массива rma в список, клонируя их при этом
		final ArrayList<ReflectogramMismatchImpl> rmArray =
			new ArrayList<ReflectogramMismatchImpl>(this.rma.length);
		for (int i = 0; i < this.rma.length; i++) {
			rmArray.add(this.rma[i].copy());
		}
		// остальные поля - immutable
		final ReflectometryEvaluationOverallResult overall = this.eOverall;
		final EvaluationPerEventResultImpl perEvent = this.epe;
		// создаем EvalonComparison
		return new EtalonComparison() {
			public List<ReflectogramMismatchImpl> getAlarms() {
				return rmArray;
			}
			public ReflectometryEvaluationOverallResult getOverallResult() {
				return overall;
			}
			public EvaluationPerEventResult getPerEventResult() {
				return perEvent;
			}
		};
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraAnalysisResultBytes()
	 */
	public byte[] getDadaraAnalysisResultBytes() {
		return this.ar == null ? null
				: this.ar.toByteArray();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraReflectogramMismatchBytes()
	 */
	public byte[] getDadaraReflectogramMismatchBytes() {
		return this.rma == null ? null
				: ReflectogramMismatchImpl.alarmsToByteArray(this.rma);
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraEvaluationPerEventResultBytes()
	 */
	public byte[] getDadaraEvaluationPerEventResultBytes() {
		return this.epe == null ? null
				: DataStreamableUtil.writeDataStreamableToBA(this.epe);
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getReflectometryEvaluationOverallResult()
	 */
	public ReflectometryEvaluationOverallResult
	getReflectometryEvaluationOverallResult() {
		return this.eOverall; // это unmodifiable-реализация либо null
	}
}
