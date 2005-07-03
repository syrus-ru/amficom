/*-
 * $Id: DataFormatException.java,v 1.2 2005/05/01 06:12:58 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * Thrown during decoding of input stream when input data is not satisfiable
 * for target object creation.
 * Most probable reasons are end of input stream
 * and {@link SignatureMismatchException}
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.2 $, $Date: 2005/05/01 06:12:58 $
 * @module
 */
public class DataFormatException extends Exception {
    private static final long serialVersionUID = 7925323030302250981L;

    public DataFormatException() {
        super();
    }

    public DataFormatException(String s) {
        super(s);
    }
}
