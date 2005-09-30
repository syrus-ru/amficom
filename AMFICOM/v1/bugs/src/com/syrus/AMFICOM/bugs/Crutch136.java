/*-
 * $Id: Crutch136.java,v 1.2 2005/09/30 10:51:42 bass Exp $
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
 * @author Tashoyan Arseniy Feliksovich
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/09/30 10:51:42 $
 * @module bugs
 * @see <a href = "http://bass.science.syrus.ru/bugzilla/show_bug.cgi?id=136">This bug&apos;s homepage</a>
 */
@Retention(value = SOURCE)
@Target(value = {TYPE, FIELD, METHOD})
public @interface Crutch136 {
	String notes();
}
