/*-
 * $Id: IncompatibleTracesException.java,v 1.1 2005/04/18 16:30:08 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Thrown when an operation like making etalon or averaging is requested
 * for traces (BellcoreStructure) set (Colletion) that are incompatibly
 * different, e.g. have different trace length or resolution.
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/04/18 16:30:08 $
 * @module
 */
public class IncompatibleTracesException extends Exception
{
	static final long serialVersionUID = -6805120554296801731L;

	public IncompatibleTracesException()
	{
		super();
	}

	public IncompatibleTracesException(String text)
	{
		super(text);
	}
}
