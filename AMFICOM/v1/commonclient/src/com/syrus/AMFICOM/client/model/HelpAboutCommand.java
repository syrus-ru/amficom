/*
 * $Id: HelpAboutCommand.java,v 1.1 2005/05/19 14:06:42 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.model;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import oracle.jdeveloper.layout.GridBagConstraints2;
/**
 * 
 * @version $Revision: 1.1 $, $Date: 2005/05/19 14:06:42 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient_v1
 */
public class HelpAboutCommand extends AbstractCommand {

	private Window	parent;
	private JPanel	about;

	public HelpAboutCommand(Window parent) {
		this.parent = parent;
	}

	public void execute() {
		JOptionPane.showMessageDialog(this.parent, this.getAboutPanel(), "О программе", JOptionPane.PLAIN_MESSAGE);
	}

	private JPanel getAboutPanel() {
		if (this.about == null) {
			this.about = new JPanel(new GridBagLayout());
			JLabel jLabel1 = new JLabel("АМФИКОМ");
			JLabel jLabel2 = new JLabel("Версия " + Version.getVersionNumber() + " обновление " + Patch.getVersion());
			JLabel jLabel3 = new JLabel(Version.getVersionText());
			JLabel jLabel4 = new JLabel(Version.getVersionCopyright());
			this.about.setBorder(BorderFactory.createEtchedBorder());
			this.about.add(jLabel1, new GridBagConstraints2(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
															GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
			this.about.add(jLabel2, new GridBagConstraints2(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
															GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
			this.about.add(jLabel3, new GridBagConstraints2(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
															GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
			this.about.add(jLabel4, new GridBagConstraints2(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
															GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		}
		return this.about;
	}
}
