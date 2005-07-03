/*-
* $Id: ToolBarPanel.java,v 1.3 2005/04/18 13:17:41 saa Exp $
*
* Copyright © 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JToolBar;


/**
 * @version $Revision: 1.3 $, $Date: 2005/04/18 13:17:41 $
 * @author $Author: saa $
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

	AbstractButton createToolButton(AbstractButton button,
									Dimension preferredSize,
									Insets margin,
									String text,
									String tooltip,
									Icon icon,
									ActionListener actionListener,
									boolean isEnabled) {
		if (preferredSize != null) {
			button.setPreferredSize(preferredSize);
		}
		if (margin != null) {
			button.setMargin(margin);
		}
		if (text != null) {
			button.setText(text);
		}
		if (tooltip != null) {
			button.setToolTipText(tooltip);
		}
		if (icon != null) {
			button.setIcon(icon);
		}
		if (actionListener != null) {
			button.addActionListener(actionListener);
		}
		button.setEnabled(isEnabled);
		button.setFocusable(false);
		return button;
	}
}
