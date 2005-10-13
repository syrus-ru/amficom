/*-
 * $Id: ReflectometryAnalysisResultImpl.java,v 1.1 2005/10/13 11:20:39 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.util.List;

import com.syrus.AMFICOM.analysis.EtalonComparison;
import com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;

/**
 * Предоставляет доступ к результатам анализа в виде
 * ReflectometryAnalysisResult.
 * Внимание: Не обеспечивает защиты объектов от изменения, не делает копий.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/10/13 11:20:39 $
 * @module dadara
 */
public class ReflectometryAnalysisResultImpl implements
		ReflectometryAnalysisResult {
	private byte[] dadaraARBytes;
	private byte[] dadaraRMBytes;
	private byte[] evalPEBytes;
	private ReflectometryEvaluationOverallResult evalOverall;

	/**
	 * 
	 * NB: предполагает, что входной EtalonComparison и его объекты
	 * не будут изменяться извне.
	 */
	public ReflectometryAnalysisResultImpl(AnalysisResult ar,
			EtalonComparison ec) {
		this.dadaraARBytes = ar.toByteArray();

		final List<ReflectogramMismatchImpl> alarms = ec.getAlarms();
		this.dadaraRMBytes = ReflectogramMismatchImpl.alarmsToByteArray(
			alarms.toArray(new ReflectogramMismatchImpl[alarms.size()]));

		EvaluationPerEventResultImpl eper = new EvaluationPerEventResultImpl(
				ec.getPerEventResult());
		this.evalPEBytes = DataStreamableUtil.writeDataStreamableToBA(eper);

		this.evalOverall = ec.getOverallResult();
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraAnalysisResultBytes()
	 */
	public byte[] getDadaraAnalysisResultBytes() {
		return this.dadaraARBytes;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraReflectogramMismatchBytes()
	 */
	public byte[] getDadaraReflectogramMismatchBytes() {
		return this.dadaraRMBytes;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getDadaraEvaluationPerEventResultBytes()
	 */
	public byte[] getDadaraEvaluationPerEventResultBytes() {
		return this.evalPEBytes;
	}

	/**
	 * @see com.syrus.AMFICOM.reflectometry.ReflectometryAnalysisResult#getReflectometryEvaluationOverallResult()
	 */
	public ReflectometryEvaluationOverallResult getReflectometryEvaluationOverallResult() {
		return this.evalOverall;
	}
}
