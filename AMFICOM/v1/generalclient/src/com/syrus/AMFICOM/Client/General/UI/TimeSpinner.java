/*-
 * $Id: TimeSpinner.java,v 1.6 2005/05/05 11:04:47 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.UI;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/05 11:04:47 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class TimeSpinner extends JSpinner {

	private static final String	PATTERN	= "HH:mm";

	public TimeSpinner() {
		super(new SpinnerDateModel(new Date(), null, null, Calendar.HOUR));
		this.setEditor(new JSpinner.DateEditor(this, PATTERN));
	}

	public static String getPattern() {
		return PATTERN;
	}

}
