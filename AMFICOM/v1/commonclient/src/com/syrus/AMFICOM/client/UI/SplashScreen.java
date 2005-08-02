/*-
 * $Id: SplashScreen.java,v 1.3 2005/08/02 13:03:21 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.*;

import javax.swing.*;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/08/02 13:03:21 $
 * @module commonclient
 */

public final class SplashScreen extends JWindow {

	private static final long	serialVersionUID	= 3256727290258731577L;
	private static SplashScreen	defaultSplash;
	Image						capture;
	Image						splash;

	public SplashScreen(Image splashImage) {
		this.splash = splashImage;
		Dimension screenSize = getToolkit().getScreenSize();
		ImageIcon splashIcon = new ImageIcon(this.splash);
		int w = splashIcon.getIconWidth();
		int h = splashIcon.getIconHeight();
		int x = (screenSize.width - w) / 2;
		int y = (screenSize.height - h) / 2;
		Rectangle r = new Rectangle(x, y, w, h);

		try {
			this.capture = new Robot().createScreenCapture(r);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.setBounds(r);
		this.getContentPane().add(new JComponent() {

			public void paintComponent(Graphics g) {
				g.drawImage(SplashScreen.this.capture, 0, 0, this);
				g.drawImage(SplashScreen.this.splash, 0, 0, this);
			}
		});
	}

	public static SplashScreen getDefaultSplashScreen() {
		if (defaultSplash == null) {
			defaultSplash = new SplashScreen(Toolkit.getDefaultToolkit().getImage("images/splash.gif"));
		}
		return defaultSplash;
	}

}

