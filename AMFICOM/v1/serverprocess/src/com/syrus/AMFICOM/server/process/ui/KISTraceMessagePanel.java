/*
 * $Id: KISTraceMessagePanel.java,v 1.1 2004/06/22 09:57:10 bass Exp $
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
final class KISTraceMessagePanel extends JPanel implements LogWriter
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
			fos = new FileOutputStream("kistrace.log", true);
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

	KISTraceMessagePanel()
	{
		jbInit();
	}

	private void jbInit()
	{
		setName("Связь КИС с сервером");
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
		System.out.println("KISTracer: " + s);
//		System.out.println("flag: " + showflag);

		if (showflag)
		{
//			System.out.println("line: " + line);
//			System.out.println("lineheight: " + lineheight);
//			System.out.println("point: " + point);
//			System.out.println("rect: " + rect.x + ":" + rect.y + " dim " + rect.width + ", " + rect.height);
			Rectangle rect2 = scrollPane.getViewport().getViewRect();
//			System.out.println("rect2: " + rect2.x + ":" + rect2.y + " dim " + rect2.width + ", " + rect2.height);
//			System.out.println("check point: " + (int)(point + lineheight));
			if (!rect2.contains(rect2.x + 1, point + lineheight))
			{
//				System.out.println("do scroll!");
				Point p = scrollPane.getViewport().getViewPosition();
//				System.out.println("from point: " + p.x + ":" + p.y);
				p.y += logText.getFontMetrics(logText.getFont()).getHeight();
//				System.out.println("to point: " + p.x + ":" + p.y);
				scrollPane.getViewport().setViewPosition(p);
			}
//			else
//				System.out.println("NO SCROLL");
		}
//		else
//		{
//			System.out.println("line: " + line);
//			System.out.println("lineheight: " + lineheight);
//			System.out.println("point: " + point);
//			System.out.println("rect: " + rect.x + ":" + rect.y + " dim " + rect.width + ", " + rect.height);
//			System.out.println("check point: " + (int)(point + lineheight));
//		}
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
