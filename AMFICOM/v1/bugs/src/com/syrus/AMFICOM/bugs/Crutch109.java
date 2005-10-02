/*-
 * $Id: Crutch109.java,v 1.1 2005/10/02 14:00:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.bugs;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/02 14:00:25 $
 * @module bugs
 * @see <a href = "http://bass.science.syrus.ru/bugzilla/show_bug.cgi?id=109">This bug&apos;s homepage</a>
 */
@Retention(value = SOURCE)
@Target(value = {METHOD})
public @interface Crutch109 {
	// empty
}
