/*-
* $Id: ToolBarPanel.java,v 1.1 2005/03/28 14:07:41 bob Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JToolBar;


/**
 * @version $Revision: 1.1 $, $Date: 2005/03/28 14:07:41 $
 * @author $Author: bob $
 * @module analysis_v1
 */
public class ToolBarPanel extends JToolBar
{
	protected ResizableLayeredPanel panel;
	protected int position = 0;

	protected static final String SEPARATOR = "separator";

	protected Map actions = new HashMap();

	protected static String[] buttons = new String[]
	{
	};

	public ToolBarPanel(ResizableLayeredPanel panel)
	{
		this.panel = panel;
		setBorder(BorderFactory.createEtchedBorder());
		setLayout (new BoxLayout(this, BoxLayout.X_AXIS));
	}
	

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Map createGraphButtons()
	{
		return actions;
	}

	AbstractButton createToolButton(
			AbstractButton b,
			Dimension preferred_size,
			String text,
			String tooltip,
			Icon icon,
			ActionListener actionListener,
			boolean isEnabled)
	{
		if (preferred_size != null)
			b.setPreferredSize(preferred_size);
		if (text != null)
			b.setText (text);
		if (tooltip != null)
			b.setToolTipText (tooltip);
		if (icon != null)
			b.setIcon(icon);
		if (actionListener != null)
			b.addActionListener(actionListener);
		b.setEnabled(isEnabled);
		b.setFocusable(false);
		return b;
	}
}