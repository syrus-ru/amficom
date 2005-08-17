/*-
 * $Id: EasyDateFormatter.java,v 1.1 2005/08/17 09:22:40 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Форматирует дату способом, удобным для именования рефлектограмм.
 * @author saa
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/08/17 09:22:40 $
 * @module util
 */
public class EasyDateFormatter {
	private static SimpleDateFormat sdf =
			new SimpleDateFormat("yyyy.MM.dd/HH:mm:ss");
	public static final String formatDate(Date date) {
		return sdf.format(date);
	}
}
