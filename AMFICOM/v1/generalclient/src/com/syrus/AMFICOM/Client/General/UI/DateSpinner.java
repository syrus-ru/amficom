/*-
 * $Id: DateSpinner.java,v 1.2 2005/04/07 06:59:54 bob Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/04/07 06:59:54 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class DateSpinner extends JSpinner {

	private static final String	PATTERN	= "dd  MMMMMMMMM  yyyy";
	private DateEditor			editor;

	public DateSpinner() {
		super(new SpinnerDateModel());
	}

	public JComponent getEditor() {
		if (editor == null)
			editor = new JSpinner.DateEditor(this, PATTERN);
		return editor;
	}

	public static String getPattern() {
		return PATTERN;
	}

}
