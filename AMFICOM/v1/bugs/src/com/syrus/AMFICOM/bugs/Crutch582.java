/*-
 * $Id: Crutch582.java,v 1.1 2006/06/21 10:56:05 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.bugs;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/06/21 10:56:05 $
 * @module bugs
 * @see <a href = "http://ararat.science.syrus.ru/cgi-bin/bugzilla/show_bug.cgi?id=582">This bug&apos;s homepage</a>
 */
@Retention(value = SOURCE)
@Target(value = {TYPE, FIELD, METHOD, CONSTRUCTOR})
public @interface Crutch582 {
	String notes();
}
