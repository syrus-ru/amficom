/*-
 * $Id: Crutch524.java,v 1.1 2006/03/23 18:15:54 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
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
 * @version $Revision: 1.1 $, $Date: 2006/03/23 18:15:54 $
 * @module bugs
 * @see <a href = "http://ararat.science.syrus.ru/cgi-bin/bugzilla/show_bug.cgi?id=524">This bug&apos;s homepage</a>
 */
@Retention(value = SOURCE)
@Target(value = {TYPE, FIELD, METHOD})
public @interface Crutch524 {
	String notes();
}
