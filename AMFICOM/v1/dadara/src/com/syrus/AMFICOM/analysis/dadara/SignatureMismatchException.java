/*
 * $Id: SignatureMismatchException.java,v 1.1 2004/12/17 15:26:14 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2004/12/17 15:26:14 $
 * @module
 */
public class SignatureMismatchException extends Exception
{
	public SignatureMismatchException()
	{
		super();
	}
	
	public SignatureMismatchException(String text)
	{
		super(text);
	}
}
