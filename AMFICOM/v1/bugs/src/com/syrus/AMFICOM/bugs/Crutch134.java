/*-
 * $Id: Crutch134.java,v 1.1 2005/09/25 15:42:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.bugs;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/09/25 15:42:47 $
 * @module bugs
 * @see <a href = "http://bass.science.syrus.ru/bugzilla/show_bug.cgi?id=134">This bug&apos;s homepage</a>
 */
@Retention(value = SOURCE)
@Target(value = {TYPE, FIELD, METHOD})
public @interface Crutch134 {
	String notes();
}
