/*-
 * $Id: TimeSpinner.java,v 1.2 2005/06/14 07:33:04 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter.UI;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/14 07:33:04 $
 * @author $Author: arseniy $
 * @module filterclient_v1
 */
public class TimeSpinner extends JSpinner {
	private static final long serialVersionUID = 3690758401483223600L;

	private static final String	PATTERN	= "HH:mm";

	public TimeSpinner() {
		super(new SpinnerDateModel(new Date(), null, null, Calendar.HOUR));
		this.setEditor(new JSpinner.DateEditor(this, PATTERN));
	}

	public static String getPattern() {
		return PATTERN;
	}

}
