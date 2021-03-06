/*-
 * $Id: DataStreamable.java,v 1.7 2005/09/01 12:07:45 saa Exp $
 * 
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.syrus.io.SignatureMismatchException;

/**
 * Declares things required to save/load objects to/from
 * DataOutputStream/DataInputStream via DataStreamableUtil.
 * To save an object, writeToDOS() method is used;
 * to create object from stream, a factory is required.
 * <p>
 * Although you are not obliged to implement anything else,
 * you are expected to create getReader() that will return a factory object
 * <pre><code>
 * private static DataStreamable.Reader DS_READER = null;
 * public static DataStreamable.Reader getReader() {
 *     if (DS_READER == null)
 *         DS_READER = new DSReader() {
 *             public DataStreamable readFromDIS() { ... } // a factory method 
 *         };
 *     return DS_READER;
 * }
 * </code></pre>
 * This will make use of
 * {@link DataStreamableUtil#readDataStreamableFromBA}.
 * @author $Author: saa $
 * @version $Revision: 1.7 $, $Date: 2005/09/01 12:07:45 $
 * @module
 */
public interface DataStreamable {
	/**
	 * Saves the object state to {@link DataOutputStream}
	 * @param dos stream to save to
	 * @throws IOException an error occured during writing to dos
	 */
	void writeToDOS(DataOutputStream dos) throws IOException;

	interface Reader {
		DataStreamable readFromDIS(DataInputStream dis) throws IOException, SignatureMismatchException;
	}
}
