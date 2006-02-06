/*-
 * $Id: ReflectometryEtalon.java,v 1.1 2005/10/10 07:38:40 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.reflectometry;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/10 07:38:40 $
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
	byte[] getReflectogramma();

}