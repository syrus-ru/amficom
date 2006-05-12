/*-
 * $Id: LoginValidatorTest.java,v 1.1 2006/05/12 17:30:27 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/05/12 17:30:27 $
 * @module csbridge
 * @see BaseSessionEnvironment
 */
final class LoginValidatorTest {
	static volatile boolean loggedIn;

	private LoginValidatorTest() {
		assert false;
	}

	/**
	 * @param args
	 */
	public static void main(final String args[]) {
		final Thread loginValidator = new Thread("LoginValidator") {
			@Override
			public void run() {
				while (!interrupted()) {
					if (loggedIn) {
						System.out.print('.');
						try {
							sleep(1000);
						} catch (final InterruptedException ie) {
							System.out.println("LoginValidator is shutting down...");
							return;
						}
					} else {
						synchronized (this) {
							try {
								System.out.println();
								this.wait();
							} catch (final InterruptedException ie) {
								System.out.println("LoginValidator is shutting down...");
								return;
							}
						}
					}
				}
			}
		};
		loginValidator.start();

		Runtime.getRuntime().addShutdownHook(new Thread("LoginValidatorShutdown") {
			@Override
			public void run() {
				loginValidator.interrupt();
			}
		});
		
		final JButton button1 = new JButton("Log In");
		final JButton button2 = new JButton("Log Out");
		final JButton button3 = new JButton("Exit");

		button1.addActionListener(new ActionListener () {
			public void actionPerformed(final ActionEvent e) {
				button1.setEnabled(false);
				button2.setEnabled(true);
				loggedIn = true;
				synchronized (loginValidator) {
					loginValidator.notify();
				}
			}
		});
		button1.setFocusable(false);

		button2.addActionListener(new ActionListener () {
			public void actionPerformed(final ActionEvent e) {
				button1.setEnabled(true);
				button2.setEnabled(false);
				loggedIn = false;
			}
		});
		button2.setEnabled(false);
		button2.setFocusable(false);

		button3.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);
			}
		});
		button3.setFocusable(false);

		final JFrame frame = new JFrame("LoginValidatorTest");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		final JPanel contentPane = (JPanel) frame.getContentPane();
		contentPane.setLayout(new GridLayout(3, 1));
		contentPane.add(button1);
		contentPane.add(button2);
		contentPane.add(button3);

		frame.setResizable(false);
		frame.setUndecorated(true);

		frame.pack();
		frame.setVisible(true);
	}
}
