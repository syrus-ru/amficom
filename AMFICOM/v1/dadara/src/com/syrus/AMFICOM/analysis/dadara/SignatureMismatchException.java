/*
 * $Id: SignatureMismatchException.java,v 1.3 2005/04/30 13:02:01 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * Thrown if a signature of input data stream does not mached
 * current version signature of an expected object.
 * @author $Author: saa $
 * @version $Revision: 1.3 $, $Date: 2005/04/30 13:02:01 $
 * @module
 */
public class SignatureMismatchException extends DataFormatException
{
	static final long serialVersionUID = 5660401178631587063L;

	public SignatureMismatchException()
	{
		super();
	}
	
	public SignatureMismatchException(String text)
	{
		super(text);
	}
}

