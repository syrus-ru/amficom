/*-
* $Id: HelpAboutCommand.java,v 1.7 2005/09/12 14:25:46 bob Exp $
*
* Copyright ¿ 2004-2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.model;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
/**
 * 
 * @version $Revision: 1.7 $, $Date: 2005/09/12 14:25:46 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class HelpAboutCommand extends AbstractCommand {

	private Window	parent;
	JLabel	about;

	public HelpAboutCommand(final Window parent) {
		this.parent = parent;
	}

	@Override
	public void execute() {
		JOptionPane.showMessageDialog(this.parent, this.getAboutPanel(), LangModelGeneral.getString("Text.About"), JOptionPane.PLAIN_MESSAGE);
	}

	private JLabel getAboutPanel() {
		if (this.about == null) {
			
			final StringBuffer buffer = new StringBuffer("<html><center>");
			buffer.append(LangModelGeneral.getString("Text.About.AMFICOM"));
			buffer.append("<br>");
			buffer.append(LangModelGeneral.getString("Text.About.Version"));
			buffer.append(' '); 
			buffer.append(LangModelGeneral.getString("Text.About.Version.Number"));
			buffer.append("<br>");
			buffer.append(LangModelGeneral.getString("Text.About.Version.VersionName"));
			buffer.append("<br>");
			buffer.append(LangModelGeneral.getString("Text.About.Version.Copyright"));
			buffer.append("</center></html>");

			this.about = new JLabel(buffer.toString());
			this.about.setBorder(BorderFactory.createEtchedBorder());
			this.about.setHorizontalAlignment(SwingConstants.CENTER);
			this.about.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && e.isAltDown() && e.isShiftDown() && e.isControlDown()) {
						final StringBuffer buffer1 = new StringBuffer("<html><center>");
						buffer1.append(LangModelGeneral.getString("Text.About.Version.Codename.AMFICOM"));
						buffer1.append("<br>");
						buffer1.append(LangModelGeneral.getString("Text.About.Version"));
						buffer1.append(' '); 
						buffer1.append(LangModelGeneral.getString("Text.About.Version.Codename"));
						buffer1.append("<br>");
						buffer1.append(LangModelGeneral.getString("Text.About.Version.VersionName"));
						buffer1.append("<br>");
						buffer1.append(LangModelGeneral.getString("Text.About.Version.Copyright"));
						buffer1.append("<br>");
						buffer1.append("<br>");
						buffer1.append(LangModelGeneral.getString("Text.About.Version.Codename.info").replaceAll("\n", "<br>"));
						buffer1.append("</center><html>");
						HelpAboutCommand.this.about.setText(buffer1.toString());
						final Window superParent = this.getSuperParent(HelpAboutCommand.this.about.getParent());
						if (superParent != null) {
							superParent.pack();
						}
					}
				}
				
				private Window getSuperParent(final Container parent1) {
					if (parent1 instanceof Window) {
						return (Window)parent1;
					}
					return parent1 == null ? null : this.getSuperParent(parent1.getParent());					
				}
				
			});

		}
		return this.about;
	}
	
	public static void main(String[] args) {
		new HelpAboutCommand(null).execute();
	}
}
