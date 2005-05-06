/*-
 * $Id: DateSpinner.java,v 1.1 2005/05/06 06:04:09 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter.UI;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/06 06:04:09 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class DateSpinner extends JSpinner {

	private static final String	PATTERN	= "dd  MMMMMMMMM  yyyy";

	public DateSpinner() {
		super(new SpinnerDateModel());
		this.setEditor(new JSpinner.DateEditor(this, PATTERN));
	}
	
	public static String getPattern() {
		return PATTERN;
	}

}
