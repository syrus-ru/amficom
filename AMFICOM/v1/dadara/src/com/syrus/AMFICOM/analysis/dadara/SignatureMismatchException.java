/*
 * $Id: SignatureMismatchException.java,v 1.2 2004/12/17 18:16:20 arseniy Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2004/12/17 18:16:20 $
 * @module
 */
public class SignatureMismatchException extends Exception
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

