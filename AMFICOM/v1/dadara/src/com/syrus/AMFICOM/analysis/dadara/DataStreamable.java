/*-
 * $Id: DataStreamable.java,v 1.1 2005/04/11 14:46:07 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/04/11 14:46:07 $
 * @module
 */
public interface DataStreamable
{
	void writeToDOS(DataOutputStream dos) throws IOException;
	static interface Reader
	{
		DataStreamable readFromDIS(DataInputStream dis) throws IOException, SignatureMismatchException;
	}
	// you are also expected to implement factory method:
	// static DataStreamable.Reader getReader()
	// and use this for readDataStreamableFromBA
}
