/*
 * $Id: HelpAboutCommand.java,v 1.6 2005/09/12 06:30:38 bob Exp $
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

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
/**
 * 
 * @version $Revision: 1.6 $, $Date: 2005/09/12 06:30:38 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class HelpAboutCommand extends AbstractCommand {

	private Window	parent;
	private JPanel	about;

	public HelpAboutCommand(final Window parent) {
		this.parent = parent;
	}

	@Override
	public void execute() {
		JOptionPane.showMessageDialog(this.parent, this.getAboutPanel(), LangModelGeneral.getString("Text.About"), JOptionPane.PLAIN_MESSAGE);
	}

	private JPanel getAboutPanel() {
		if (this.about == null) {
			this.about = new JPanel(new GridBagLayout());
			this.about.setBorder(BorderFactory.createEtchedBorder());
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.insets =  new Insets(5, 5, 0, 5);
			gbc.anchor = GridBagConstraints.WEST;
			
			this.about.add(new JLabel(LangModelGeneral.getString("Text.About.AMFICOM")), gbc);
			
			gbc.insets = new Insets(0, 5, 0, 5);
			this.about.add(new JLabel(LangModelGeneral.getString("Text.About.Version") + ' ' + Version.getVersionNumber() + ' ' + 
				LangModelGeneral.getString("Text.About.update") + ' ' + Version.getPatchVersion()), gbc);
			this.about.add(new JLabel(Version.getVersionText()), gbc);
			
			gbc.insets = new Insets(0, 5, 5, 5);
			this.about.add(new JLabel(Version.getVersionCopyright()), gbc);

		}
		return this.about;
	}
}
