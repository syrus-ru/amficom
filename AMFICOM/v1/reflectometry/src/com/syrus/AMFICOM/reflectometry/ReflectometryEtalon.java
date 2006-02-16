/*-
 * $Id: ReflectometryEtalon.java,v 1.1.2.1 2006/02/16 12:43:25 arseniy Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.1.2.1 $, $Date: 2006/02/16 12:43:25 $
 * @module
 */
public interface ReflectometryEtalon {

	/**
	 * @return массив байт для восстановления объекта-эталона dadara
	 */
	byte[] getDadaraEtalon();

	/**
	 * @return массив байт для восстановления рефлектограммы {@link com.syrus.io.BellcoreStructure}
	 */
	byte[] getReflectogrammaEtalon();

}