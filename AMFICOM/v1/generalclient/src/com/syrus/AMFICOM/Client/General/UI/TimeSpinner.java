/*-
 * $Id: TimeSpinner.java,v 1.4 2005/04/07 07:01:03 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.UI;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * @version $Revision: 1.4 $, $Date: 2005/04/07 07:01:03 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class TimeSpinner extends JSpinner {

	private DateEditor			editor;
	private static final String	PATTERN	= "HH:mm";

	public TimeSpinner() {
		super(new SpinnerDateModel());
	}

	static public String getPattern() {
		return PATTERN;
	}

	public JComponent getEditor() {
		if (editor == null)
			editor = new JSpinner.DateEditor(this, PATTERN);
		return editor;
	}

}
