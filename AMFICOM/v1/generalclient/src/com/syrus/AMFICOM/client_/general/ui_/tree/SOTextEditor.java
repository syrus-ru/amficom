/*
 * $Id: SOTextEditor.java,v 1.1 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.util.EventObject;

import javax.swing.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

public class SOTextEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 3689072858814887473L;
	private static SOTextEditor instance;
	
	private SOTextEditor() {
		super(new JTextField());
	}
	
	public static DefaultCellEditor getInstance() {
		if (instance == null) {
			instance = new SOTextEditor();
		}
		return instance;
	}
}
