/*
 * $Id: JAlertingMessageDialogTestCase.java,v 1.1 2004/08/06 06:29:14 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Object.test;

import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.syrus.AMFICOM.Client.Object.ui.JAlertingMessageDialog;
import com.syrus.AMFICOM.corba.portable.alarm.MessageImpl;
import com.syrus.AMFICOM.corba.portable.common.IdentifierImpl;
import java.util.Locale;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import junit.framework.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/06 06:29:14 $
 * @author $Author: cvsadmin $
 */
public class JAlertingMessageDialogTestCase extends TestCase {
	public JAlertingMessageDialogTestCase(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(JAlertingMessageDialogTestCase.class);
	}

	public static void main(String[] args) {
		Locale locale = Locale.getDefault();
		if (args.length == 1)
			locale = new Locale(args[0]);
		else if (args.length == 2)
			locale = new Locale(args[0], args[1]);
		else if (args.length == 3)
			locale = new Locale(args[0], args[1], args[2]);
		Locale.setDefault(locale);

		junit.awtui.TestRunner.run(JAlertingMessageDialogTestCase.class);
//		junit.swingui.TestRunner.run(JAlertingMessageDialogTestCase.class);
//		junit.textui.TestRunner.run(JAlertingMessageDialogTestCase.class);
	}

	private void runAlertingMessageDialogTest() {
		MessageImpl messageSeq[] = new MessageImpl[17];

		messageSeq[0] = new MessageImpl();
		messageSeq[0].setAlertingId(new IdentifierImpl("Alerting Id #00"));
		messageSeq[0].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[0].setEventId(new IdentifierImpl("Event Id #00"));
		messageSeq[0].setEventDate(System.currentTimeMillis());
		messageSeq[0].setEventSourceName("");
		messageSeq[0].setEventSourceDescription("");
		messageSeq[0].setTransmissionPathName("");
		messageSeq[0].setTransmissionPathDescription("");
		messageSeq[0].setText("My mistress' eyes are nothing like the sun;");

		messageSeq[1] = new MessageImpl();
		messageSeq[1].setAlertingId(new IdentifierImpl("Alerting Id #01"));
		messageSeq[1].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[1].setEventId(new IdentifierImpl("Event Id #01"));
		messageSeq[1].setEventDate(System.currentTimeMillis());
		messageSeq[1].setEventSourceName("");
		messageSeq[1].setEventSourceDescription("");
		messageSeq[1].setTransmissionPathName("");
		messageSeq[1].setTransmissionPathDescription("");
		messageSeq[1].setText("Coral is far more red than her lip's red...");

		messageSeq[2] = new MessageImpl();
		messageSeq[2].setAlertingId(new IdentifierImpl("Alerting Id #02"));
		messageSeq[2].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[2].setEventId(new IdentifierImpl("Event Id #02"));
		messageSeq[2].setEventDate(System.currentTimeMillis());
		messageSeq[2].setEventSourceName("");
		messageSeq[2].setEventSourceDescription("");
		messageSeq[2].setTransmissionPathName("");
		messageSeq[2].setTransmissionPathDescription("");
		messageSeq[2].setText("If snow be white, why then her breasts are dun?");

		messageSeq[3] = new MessageImpl();
		messageSeq[3].setAlertingId(new IdentifierImpl("Alerting Id #03"));
		messageSeq[3].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[3].setEventId(new IdentifierImpl("Event Id #03"));
		messageSeq[3].setEventDate(System.currentTimeMillis());
		messageSeq[3].setEventSourceName("");
		messageSeq[3].setEventSourceDescription("");
		messageSeq[3].setTransmissionPathName("");
		messageSeq[3].setTransmissionPathDescription("");
		messageSeq[3].setText("If hairs be wires, black wires grow on her head.");

		messageSeq[4] = new MessageImpl();
		messageSeq[4].setAlertingId(new IdentifierImpl("Alerting Id #04"));
		messageSeq[4].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[4].setEventId(new IdentifierImpl("Event Id #04"));
		messageSeq[4].setEventDate(System.currentTimeMillis());
		messageSeq[4].setEventSourceName("");
		messageSeq[4].setEventSourceDescription("");
		messageSeq[4].setTransmissionPathName("");
		messageSeq[4].setTransmissionPathDescription("");
		messageSeq[4].setText(" ");

		messageSeq[5] = new MessageImpl();
		messageSeq[5].setAlertingId(new IdentifierImpl("Alerting Id #05"));
		messageSeq[5].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[5].setEventId(new IdentifierImpl("Event Id #05"));
		messageSeq[5].setEventDate(System.currentTimeMillis());
		messageSeq[5].setEventSourceName("");
		messageSeq[5].setEventSourceDescription("");
		messageSeq[5].setTransmissionPathName("");
		messageSeq[5].setTransmissionPathDescription("");
		messageSeq[5].setText("I have seen roses damask'd, red and white,");

		messageSeq[6] = new MessageImpl();
		messageSeq[6].setAlertingId(new IdentifierImpl("Alerting Id #06"));
		messageSeq[6].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[6].setEventId(new IdentifierImpl("Event Id #06"));
		messageSeq[6].setEventDate(System.currentTimeMillis());
		messageSeq[6].setEventSourceName("");
		messageSeq[6].setEventSourceDescription("");
		messageSeq[6].setTransmissionPathName("");
		messageSeq[6].setTransmissionPathDescription("");
		messageSeq[6].setText("But no such roses see I in her cheeks;");

		messageSeq[7] = new MessageImpl();
		messageSeq[7].setAlertingId(new IdentifierImpl("Alerting Id #07"));
		messageSeq[7].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[7].setEventId(new IdentifierImpl("Event Id #07"));
		messageSeq[7].setEventDate(System.currentTimeMillis());
		messageSeq[7].setEventSourceName("");
		messageSeq[7].setEventSourceDescription("");
		messageSeq[7].setTransmissionPathName("");
		messageSeq[7].setTransmissionPathDescription("");
		messageSeq[7].setText("And in some perfumes is there more delight");

		messageSeq[8] = new MessageImpl();
		messageSeq[8].setAlertingId(new IdentifierImpl("Alerting Id #08"));
		messageSeq[8].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[8].setEventId(new IdentifierImpl("Event Id #08"));
		messageSeq[8].setEventDate(System.currentTimeMillis());
		messageSeq[8].setEventSourceName("");
		messageSeq[8].setEventSourceDescription("");
		messageSeq[8].setTransmissionPathName("");
		messageSeq[8].setTransmissionPathDescription("");
		messageSeq[8].setText("Than in the breath that from my mistress reeks.");

		messageSeq[9] = new MessageImpl();
		messageSeq[9].setAlertingId(new IdentifierImpl("Alerting Id #09"));
		messageSeq[9].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[9].setEventId(new IdentifierImpl("Event Id #09"));
		messageSeq[9].setEventDate(System.currentTimeMillis());
		messageSeq[9].setEventSourceName("");
		messageSeq[9].setEventSourceDescription("");
		messageSeq[9].setTransmissionPathName("");
		messageSeq[9].setTransmissionPathDescription("");
		messageSeq[9].setText(" ");

		messageSeq[10] = new MessageImpl();
		messageSeq[10].setAlertingId(new IdentifierImpl("Alerting Id #0A"));
		messageSeq[10].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[10].setEventId(new IdentifierImpl("Event Id #0A"));
		messageSeq[10].setEventDate(System.currentTimeMillis());
		messageSeq[10].setEventSourceName("");
		messageSeq[10].setEventSourceDescription("");
		messageSeq[10].setTransmissionPathName("");
		messageSeq[10].setTransmissionPathDescription("");
		messageSeq[10].setText("I love to hear her speak, yet well I know");

		messageSeq[11] = new MessageImpl();
		messageSeq[11].setAlertingId(new IdentifierImpl("Alerting Id #0B"));
		messageSeq[11].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[11].setEventId(new IdentifierImpl("Event Id #0B"));
		messageSeq[11].setEventDate(System.currentTimeMillis());
		messageSeq[11].setEventSourceName("");
		messageSeq[11].setEventSourceDescription("");
		messageSeq[11].setTransmissionPathName("");
		messageSeq[11].setTransmissionPathDescription("");
		messageSeq[11].setText("That music hath a far more pleasing sound.");

		messageSeq[12] = new MessageImpl();
		messageSeq[12].setAlertingId(new IdentifierImpl("Alerting Id #0C"));
		messageSeq[12].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[12].setEventId(new IdentifierImpl("Event Id #0C"));
		messageSeq[12].setEventDate(System.currentTimeMillis());
		messageSeq[12].setEventSourceName("");
		messageSeq[12].setEventSourceDescription("");
		messageSeq[12].setTransmissionPathName("");
		messageSeq[12].setTransmissionPathDescription("");
		messageSeq[12].setText("I grant I never saw a goddess go,");

		messageSeq[13] = new MessageImpl();
		messageSeq[13].setAlertingId(new IdentifierImpl("Alerting Id #0D"));
		messageSeq[13].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[13].setEventId(new IdentifierImpl("Event Id #0D"));
		messageSeq[13].setEventDate(System.currentTimeMillis());
		messageSeq[13].setEventSourceName("");
		messageSeq[13].setEventSourceDescription("");
		messageSeq[13].setTransmissionPathName("");
		messageSeq[13].setTransmissionPathDescription("");
		messageSeq[13].setText("My mistress when she walks treads on the ground.");

		messageSeq[14] = new MessageImpl();
		messageSeq[14].setAlertingId(new IdentifierImpl("Alerting Id #0E"));
		messageSeq[14].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[14].setEventId(new IdentifierImpl("Event Id #0E"));
		messageSeq[14].setEventDate(System.currentTimeMillis());
		messageSeq[14].setEventSourceName("");
		messageSeq[14].setEventSourceDescription("");
		messageSeq[14].setTransmissionPathName("");
		messageSeq[14].setTransmissionPathDescription("");
		messageSeq[14].setText(" ");

		messageSeq[15] = new MessageImpl();
		messageSeq[15].setAlertingId(new IdentifierImpl("Alerting Id #0F"));
		messageSeq[15].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[15].setEventId(new IdentifierImpl("Event Id #0F"));
		messageSeq[15].setEventDate(System.currentTimeMillis());
		messageSeq[15].setEventSourceName("");
		messageSeq[15].setEventSourceDescription("");
		messageSeq[15].setTransmissionPathName("");
		messageSeq[15].setTransmissionPathDescription("");
		messageSeq[15].setText("And yet by heaven I think my love as rare");

		messageSeq[16] = new MessageImpl();
		messageSeq[16].setAlertingId(new IdentifierImpl("Alerting Id #10"));
		messageSeq[16].setMessageTypeId(new IdentifierImpl(""));
		messageSeq[16].setEventId(new IdentifierImpl("Event Id #10"));
		messageSeq[16].setEventDate(System.currentTimeMillis());
		messageSeq[16].setEventSourceName("");
		messageSeq[16].setEventSourceDescription("");
		messageSeq[16].setTransmissionPathName("");
		messageSeq[16].setTransmissionPathDescription("");
		messageSeq[16].setText("As any she belied with false compare.");

		final JAlertingMessageDialog jAlertingMessageDialog = new JAlertingMessageDialog(new JFrame(), true);
		jAlertingMessageDialog.setModal(false);
		Thread thread = new Thread() {
			public void run() {
				jAlertingMessageDialog.show();
			}
		};
		thread.start();
		for (int i = 0; i < messageSeq.length; i++) {
			jAlertingMessageDialog.appendMessage(messageSeq[i]);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
				;
			}
		}
		try {
			thread.join();
		} catch (InterruptedException ie) {
			;
		}
		jAlertingMessageDialog.dispose();
	}

	public void testAlertingMessageDialogMetal()
			throws UnsupportedLookAndFeelException {
		LookAndFeel lookAndFeel = new MetalLookAndFeel();
		if (lookAndFeel.isSupportedLookAndFeel()) {
			UIManager.setLookAndFeel(lookAndFeel);
			runAlertingMessageDialogTest();
		}
	}

	public void testAlertingMessageDialogKunststoff()
			throws UnsupportedLookAndFeelException {
		LookAndFeel lookAndFeel = new KunststoffLookAndFeel();
		if (lookAndFeel.isSupportedLookAndFeel()) {
			UIManager.setLookAndFeel(lookAndFeel);
			runAlertingMessageDialogTest();
		}
	}

	public void testAlertingMessageDialogGTK()
			throws UnsupportedLookAndFeelException {
		LookAndFeel lookAndFeel = new GTKLookAndFeel();
		if (lookAndFeel.isSupportedLookAndFeel()) {
			UIManager.setLookAndFeel(lookAndFeel);
			runAlertingMessageDialogTest();
		}
	}

	public void testAlertingMessageDialogMotif()
			throws UnsupportedLookAndFeelException {
		LookAndFeel lookAndFeel = new MotifLookAndFeel();
		if (lookAndFeel.isSupportedLookAndFeel()) {
			UIManager.setLookAndFeel(lookAndFeel);
			runAlertingMessageDialogTest();
		}
	}

	public void testAlertingMessageDialogWindows()
			throws UnsupportedLookAndFeelException {
		LookAndFeel lookAndFeel = new WindowsLookAndFeel();
		if (lookAndFeel.isSupportedLookAndFeel()) {
			UIManager.setLookAndFeel(lookAndFeel);
			runAlertingMessageDialogTest();
		}
	}
}
