/*
 * $Id: CheckResultSetPanel.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process.ui;

import com.syrus.AMFICOM.server.LogWriter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import oracle.jdeveloper.layout.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @module serverprocess
 */
final class CheckResultSetPanel extends JPanel implements LogWriter
{
	JScrollPane scrollPane = new JScrollPane();
	JTextArea logText = new JTextArea();
	JButton buttonClearLog = new JButton();

	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	static FileOutputStream fos;
	static PrintWriter pstr;

	static
	{
		try
		{
			fos = new FileOutputStream("rset.log", true);
			pstr = new PrintWriter(fos);
		}
		catch (FileNotFoundException fnfe)
		{
			;
		}
		pstr.println("");
		pstr.println("-------------------------------------------------------");
		pstr.println("started " + sdf.format(new Date(System.currentTimeMillis())));
		pstr.println("-------------------------------------------------------");
		pstr.println("");
	}

	CheckResultSetPanel()
	{
		jbInit();
	}

	private void jbInit()
	{
		setName("Активный архив");
		setSize(new Dimension(700, 550));
		setLayout(new XYLayout());

		logText.setLineWrap(false);
//		logText.setWrapStyleWord(true);
		logText.setText("");
		logText.setEditable(false);

		buttonClearLog.setDefaultCapable(false);
		buttonClearLog.setEnabled(true);
		buttonClearLog.setText("Очистить");
		buttonClearLog.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				logText.setText("");
				logText.repaint();
			}
		});
		logText.setAutoscrolls(true);

		add(scrollPane, new XYConstraints(5, 30, 690, 510));
		add(buttonClearLog, new XYConstraints(500, 3, 195, 25));
		scrollPane.getViewport().add(logText, BorderLayout.CENTER);
	}

	public void log(String s)
	{
		boolean showflag = false;
		int line = logText.getLineCount();
		int lineheight = logText.getFontMetrics(logText.getFont()).getHeight();
		int point = line * lineheight;
		Rectangle rect = scrollPane.getViewport().getViewRect();
		if (rect.contains(rect.x + 1, point))
			showflag = true;

		pstr.println(s);
		logText.append(s);
		logText.append("\n");
		System.out.println("Result set checker: " + s);

		if (showflag)
		{
			Rectangle rect2 = scrollPane.getViewport().getViewRect();
			if (!rect2.contains(rect2.x + 1, point + lineheight))
			{
				Point p = scrollPane.getViewport().getViewPosition();
				p.y += logText.getFontMetrics(logText.getFont()).getHeight();
				scrollPane.getViewport().setViewPosition(p);
			}
		}
	}

	void closeLog()
	{
		pstr.println("");
		pstr.println("-------------------------------------------------------");
		pstr.println("closed " + sdf.format(new Date(System.currentTimeMillis())));
		pstr.println("-------------------------------------------------------");
		pstr.println("");
		pstr.close();
		try
		{
			fos.close();
		}
		catch (IOException ioe)
		{
			;
		}
	}
}
