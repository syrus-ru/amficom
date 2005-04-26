/*-
 * $Id: SplashScreen.java,v 1.1 2005/04/26 11:47:24 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_;

import java.awt.*;

import javax.swing.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/26 11:47:24 $
 * @module schemeclient_v1
 */

public final class SplashScreen extends JWindow {
	private static final long serialVersionUID = 3256727290258731577L;
	private static SplashScreen defaultSplash;
	Image capture;
	Image splash;
	public SplashScreen(Image splashImage) {
		splash = splashImage;
		Dimension screenSize = getToolkit().getScreenSize();
		ImageIcon splashIcon = new ImageIcon(splash);
		int w = splashIcon.getIconWidth();
		int h = splashIcon.getIconHeight();
		int x = (screenSize.width - w) / 2;
		int y = (screenSize.height - h) / 2;
		Rectangle r = new Rectangle(x, y, w, h);
				
		try {
			capture = new Robot().createScreenCapture(r);
		} catch (Exception e) {
			// ignore
		}
		setBounds(r);
		getContentPane().add(new Splash());
	}
	
	public static SplashScreen getDefaultSplashScreen() {
		if (defaultSplash == null)
			defaultSplash = new SplashScreen(Toolkit.getDefaultToolkit().getImage("images/splash.gif"));
		return defaultSplash;
	}
	
	private class Splash extends JComponent {
		private static final long serialVersionUID = 3257562897654689844L;

		public void paintComponent(Graphics g) {
			g.drawImage(capture, 0, 0, this);
			g.drawImage(splash, 0, 0, this);
		}
	}
}
