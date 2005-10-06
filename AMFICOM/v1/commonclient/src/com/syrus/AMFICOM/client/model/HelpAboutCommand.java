/*-
* $Id: HelpAboutCommand.java,v 1.11 2005/10/06 14:34:35 bob Exp $
*
* Copyright ¿ 2004-2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.client.model;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
/**
 * 
 * @version $Revision: 1.11 $, $Date: 2005/10/06 14:34:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class HelpAboutCommand extends AbstractCommand {

	private Window	parent;
	private JPanel  panel; 
	private JLabel	about;

	public HelpAboutCommand(final Window parent) {
		this.parent = parent;
		
		this.panel = new JPanel(new BorderLayout());
		this.about = new JLabel();
		Icon image = UIManager.getIcon(ResourceKeys.IMAGE_LOGIN_LOGO);
		if (image == null) {
			image = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/logo2.jpg"));
		}
		if (image != null) {
			final JLabel logo = new JLabel(image);
			
			final JLabel label = this.about;
			logo.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {					
					final int x = e.getX();
					final int y = e.getY();
					if (e.getClickCount() == 2 && 
							e.isAltDown() && 
							e.isShiftDown() && 
							e.isControlDown() && 
							x >= 410 && x <= 430 &&
							y >= 26 && y <= 38) {
						SwingUtilities.invokeLater(new Runnable() {
							private Window getSuperParent(final Container parent1) {
								if (parent1 instanceof Window) {
									return (Window)parent1;
								}
								return parent1 == null ? null : this.getSuperParent(parent1.getParent());					
							}
							
							public void run() {
								updateCodenameInfo();

								final Window superParent = this.getSuperParent(label.getParent());
								if (superParent != null) {
									superParent.pack();
									final GraphicsEnvironment localGraphicsEnvironment = 
										GraphicsEnvironment.getLocalGraphicsEnvironment();			
									final Rectangle maximumWindowBounds = 
										localGraphicsEnvironment.getMaximumWindowBounds();
									superParent.setLocation((maximumWindowBounds.width - superParent.getWidth()) / 2, 
										(maximumWindowBounds.height - superParent.getHeight()) / 2);
								} 
							}
						});
						
					}
				}
				
				
				
			});
			this.panel.add(logo, BorderLayout.NORTH);
		}
		this.panel.setBorder(BorderFactory.createEtchedBorder());
		this.about.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		
		this.panel.add(this.about, BorderLayout.SOUTH);
	}

	@Override
	public void execute() {
		this.updateAboutInfo();
		JOptionPane.showMessageDialog(this.parent, this.panel, I18N.getString("Common.About"), JOptionPane.PLAIN_MESSAGE);
	}

	private void updateAboutInfo() {
		final StringBuffer buffer = new StringBuffer("<html><center><br>");
		buffer.append(I18N.getString("Common.About.AMFICOM"));
		buffer.append("<br><br>");
		buffer.append(I18N.getString("Common.About.Version"));
		buffer.append(' '); 
		buffer.append(I18N.getString("Common.About.Version.Number"));
		buffer.append("<br>");
		buffer.append(I18N.getString("Common.About.Version.VersionName"));
		buffer.append("<br>");
		buffer.append(I18N.getString("Common.About.Version.Copyright"));
		buffer.append("</center></html>");
		this.about.setToolTipText(null);
		this.about.setText(buffer.toString());
		this.about.revalidate();
	}
	
	void updateCodenameInfo() {
		final StringBuffer buffer = new StringBuffer("<html><center><br>");
		buffer.append(I18N.getString("Common.About.Version.Codename.AMFICOM"));
		buffer.append("<br><br>");
		buffer.append(I18N.getString("Common.About.Version"));
		buffer.append(' ');		
		buffer.append(I18N.getString("Common.About.Version.Number"));		
		buffer.append(" \u00AB");
		buffer.append(I18N.getString("Common.About.Version.Codename"));
		buffer.append("\u00BB<br>");
		buffer.append(I18N.getString("Common.About.Version.VersionName"));
		buffer.append("<br>");
		buffer.append(I18N.getString("Common.About.Version.Copyright"));
		buffer.append("<br><br>");
		buffer.append(I18N.getString("Common.About.Version.Codename.info").replaceAll("\n", "<br>"));
		buffer.append("</center><html>");
		this.about.setToolTipText("\u0431/\u043f");
		this.about.setText(buffer.toString());
		this.about.revalidate();
	}
}
