/*-
 * $Id: InvalidAnalysisParametersException.java,v 1.2 2005/09/21 10:47:50 arseniy Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/09/21 10:47:50 $
 * Бросается при создании/обработке недействительного набора параметров анализа
 * @module
 */
public class InvalidAnalysisParametersException extends Exception {
	private static final long serialVersionUID = 1781421718621829323L;

	public InvalidAnalysisParametersException() {
		super();
	}
}
