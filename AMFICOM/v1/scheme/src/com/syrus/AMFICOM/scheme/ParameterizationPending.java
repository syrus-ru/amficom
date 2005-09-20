/*-
 * $Id: ParameterizationPending.java,v 1.1 2005/09/20 07:49:51 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/09/20 07:49:51 $
 * @module scheme
 */
@Retention(SOURCE)
@Target(METHOD)
@interface ParameterizationPending {
	String[] value();
}
