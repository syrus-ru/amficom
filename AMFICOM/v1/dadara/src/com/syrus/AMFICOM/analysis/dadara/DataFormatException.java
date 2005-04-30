/*-
 * $Id: DataFormatException.java,v 1.1 2005/04/30 13:02:01 saa Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/04/30 13:02:01 $
 * @module
 */
public class DataFormatException extends Exception {
    public static final long serialVersionUID = 7925323030302250981L;

    public DataFormatException() {
        super();
    }

    public DataFormatException(String s) {
        super(s);
    }
}
