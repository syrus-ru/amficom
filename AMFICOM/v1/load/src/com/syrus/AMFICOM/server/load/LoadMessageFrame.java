
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.server.load;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import oracle.jdeveloper.layout.*;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.syrus.AMFICOM.server.*;

public class LoadMessageFrame extends JFrame implements LogWriter{
	JPanel jPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	BorderLayout borderLayout1 = new BorderLayout();
	XYLayout xYLayout1 = new XYLayout();
	BorderLayout borderLayout2 = new BorderLayout();
	JButton buttonConnect = new JButton();
	JButton buttonImages = new JButton();
	JButton buttonSys = new JButton();
	JButton buttonProto = new JButton();
	JButton buttonTest = new JButton();
	JButton buttonEqCatalog = new JButton();

	JButton buttonEqCatalog2 = new JButton();
	JButton buttonProto2 = new JButton();

	JButton buttonCORBA = new JButton();
	JButton buttonData = new JButton();
	JButton buttonExit = new JButton();
	JTextArea logText = new JTextArea();

	JButton buttonMaintain = new JButton();
	JButton buttonLog = new JButton();
	JButton buttonClearLog = new JButton();

	public boolean connected = false;
	ConnectionManager connectionManager;

	static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
	static FileOutputStream fos;
	static PrintWriter pstr;
	JButton buttonCORBA2 = new JButton();
	JButton buttonCORBA5 = new JButton();
	JButton buttonCORBA6 = new JButton();
	static
	{
		try
		{
			fos = new FileOutputStream("serverload.log", true);
			pstr = new PrintWriter(fos);
		}
		catch(FileNotFoundException e)
		{
		}
		pstr.println("");
		pstr.println("-------------------------------------------------------");
		pstr.println("started " + sdf.format(new Date(System.currentTimeMillis())));
		pstr.println("-------------------------------------------------------");
		pstr.println("");
	}

	LoadMessageFrame this_frame;

	public LoadMessageFrame()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		connectionManager = new ConnectionManager(this);
		this_frame = this;
	}

	private void jbInit() throws Exception
	{
		this.getContentPane().setLayout(borderLayout1);
		this.setSize(new Dimension(969, 605));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});
		buttonConnect.setText("Соединиться");
		buttonConnect.addActionListener(new LoadMessageFrame_buttonConnect_actionAdapter(this));
		buttonImages.setEnabled(false);
		buttonImages.setText("Изображения");
		buttonImages.addActionListener(new LoadMessageFrame_buttonImages_actionAdapter(this));
		buttonSys.setEnabled(false);
		buttonSys.setText("Администратор");
		buttonSys.addActionListener(new LoadMessageFrame_buttonSys_actionAdapter(this));
		buttonProto.setEnabled(false);
		buttonProto.setText("Протоэлементы");
		buttonProto.addActionListener(new LoadMessageFrame_jButton4_actionAdapter(this));
		buttonTest.setEnabled(false);
		buttonTest.setText("Тест изображений");
		buttonTest.addActionListener(new LoadMessageFrame_buttonTest_actionAdapter(this));
		buttonCORBA.setEnabled(false);
		buttonCORBA.setText("ТЕст CORBA");
		buttonCORBA.addActionListener(new LoadMessageFrame_buttonCORBA_actionAdapter(this));
		buttonData.setEnabled(false);
		buttonData.setText("Измерения");
		buttonData.addActionListener(new LoadMessageFrame_buttonData_actionAdapter(this));
		buttonEqCatalog.setEnabled(false);
		buttonEqCatalog.setText("Каталог");
		buttonEqCatalog.addActionListener(new LoadMessageFrame_buttonEqCatalog_actionAdapter(this));
		buttonEqCatalog2.setEnabled(false);
		buttonEqCatalog2.setText("Каталог 2");
		buttonProto2.setEnabled(false);
		buttonProto2.setText("Протоэлементы 2");
		buttonProto2.addActionListener(new LoadMessageFrame_jButton42_actionAdapter(this));
		buttonEqCatalog2.addActionListener(new LoadMessageFrame_buttonEqCatalog2_actionAdapter(this));
		buttonMaintain.setEnabled(false);
		buttonMaintain.setText("Сопровождение");
		buttonMaintain.addActionListener(new LoadMessageFrame_buttonMaintain_actionAdapter(this));

		buttonExit.setText("Закрыть");
//		buttonExit.setLabel("Закрыть");
		buttonExit.addActionListener(new LoadMessageFrame_buttonExit_actionAdapter(this));

		logText.setLineWrap(false);
//		logText.setWrapStyleWord(true);
		logText.setText("");
		logText.setEditable(false);
		buttonCORBA2.setEnabled(false);
		buttonCORBA2.setText("Тест CORBA");
		buttonCORBA5.setEnabled(false);
		buttonCORBA5.setText("Тест CORBA");
		buttonCORBA6.setEnabled(false);
		buttonCORBA6.setText("ТЕст CORBA");
		buttonLog.setEnabled(true);
		buttonLog.setText("Проверка лога");
		buttonLog.addActionListener(new LoadMessageFrame_buttonLog_actionAdapter(this));
		buttonClearLog.setEnabled(true);
		buttonClearLog.setText("Очистить");
		buttonClearLog.addActionListener(new LoadMessageFrame_buttonClearLog_actionAdapter(this));
//		logText.setColumns(60);
//		logText.setRows(20);
		logText.setAutoscrolls(true);

//		scrollPane.getViewport().setLayout(borderLayout2);
		jPanel.setLayout(xYLayout1);
		this.setTitle("Окно загрузки данных на сервер АМФИКОМ");
		this.getContentPane().add(jPanel, BorderLayout.NORTH);
		jPanel.add(scrollPane, new XYConstraints(4, 4, 681, 558));
		scrollPane.getViewport().add(logText, BorderLayout.CENTER);
		jPanel.add(buttonConnect, new XYConstraints(690, 20, 130, 26));
		jPanel.add(buttonImages, new XYConstraints(690, 60, 130, 26));
		jPanel.add(buttonSys, new XYConstraints(690, 100, 130, 26));
		jPanel.add(buttonData, new XYConstraints(690, 140, 130, 26));
		jPanel.add(buttonEqCatalog, new XYConstraints(690, 180, 130, 26));
		jPanel.add(buttonMaintain, new XYConstraints(690, 220, 130, 26));
		jPanel.add(buttonProto, new XYConstraints(690, 260, 130, 26));
//		jPanel.add(buttonProto2, new XYConstraints(690, 300, 109, 26));
//		jPanel.add(buttonEqCatalog2, new XYConstraints(690, 340, 109, 26));
//		jPanel.add(buttonCORBA2, new XYConstraints(691, 425, 109, 26));
//		jPanel.add(buttonCORBA5, new XYConstraints(691, 468, 109, 26));
//		jPanel.add(buttonCORBA6, new XYConstraints(691, 509, 109, 26));
		jPanel.add(buttonClearLog, new XYConstraints(835, 20, 109, 26));
		jPanel.add(buttonLog, new XYConstraints(828, 420, 109, 26));
//		jPanel.add(buttonTest, new XYConstraints(828, 450, 109, 26));
//		jPanel.add(buttonCORBA, new XYConstraints(829, 480, 109, 26));
		jPanel.add(buttonExit, new XYConstraints(833, 540, 109, 26));
	}

	public void log(String s)
	{
		boolean showflag = false;
		int line = logText.getLineCount();
		int lineheight = logText.getFontMetrics(logText.getFont()).getHeight();
		int point = line * lineheight;
		Rectangle rect = scrollPane.getViewport().getViewRect();
		if(rect.contains(rect.x + 1, point))
			showflag = true;

		pstr.println(s);
		logText.append(s);
		logText.append("\n");
		System.out.println(s);

		if(showflag)
		{
			logText.setCaretPosition(logText.getDocument().getLength());
			Rectangle rect2 = scrollPane.getViewport().getViewRect();
			if(!rect2.contains(rect2.x + 1, point + lineheight))
			{
				Point p = scrollPane.getViewport().getViewPosition();
				p.y += logText.getFontMetrics(logText.getFont()).getHeight();
				scrollPane.getViewport().setViewPosition(p);
			}
		}
	}

	void helpAbout_ActionPerformed(ActionEvent e)
	{
		JOptionPane.showMessageDialog(this, new LoadMessageFrame_AboutBoxPanel1(), "About", JOptionPane.PLAIN_MESSAGE);
	}

	void buttonConnect_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				if(!connected)
				{
					if(connectionManager.connect())
					{
						connected = true;
						buttonConnect.setText("Отсоединиться");
						buttonImages.setEnabled(true);
						buttonSys.setEnabled(true);
						buttonProto.setEnabled(true);
						buttonTest.setEnabled(true);
						buttonCORBA.setEnabled(true);
						buttonData.setEnabled(true);
						buttonEqCatalog.setEnabled(true);
						buttonMaintain.setEnabled(true);
//						buttonEqCatalog2.setEnabled(true);
//						buttonProto2.setEnabled(true);
					}
				}
				else
				{
						connectionManager.disconnect();
						connected = false;
						buttonConnect.setText("Cоединиться");
						buttonImages.setEnabled(false);
						buttonSys.setEnabled(false);
						buttonProto.setEnabled(false);
						buttonTest.setEnabled(false);

						buttonCORBA.setEnabled(false);
						buttonData.setEnabled(false);
						buttonEqCatalog.setEnabled(false);
						buttonMaintain.setEnabled(false);

//						buttonEqCatalog2.setEnabled(false);
//						buttonProto2.setEnabled(false);
				}
			}
		};
		thread.start();
	}

	void buttonExit_actionPerformed(ActionEvent e)
	{
		System.out.println("exiting");
		if(connected)
			connectionManager.disconnect();
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
		catch(IOException ex)
		{
		}
		this.dispose();
		System.exit(0);
	}

	void this_windowClosing(WindowEvent e)
	{
		buttonExit_actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null));
	}

	void buttonImages_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				new LoadImages(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonSys_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				new LoadSystemAdministrator(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonProto_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				new LoadProtoElementsNew(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonProto2_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
//				new LoadProtoElements2(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonTest_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				new TestImages(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonCORBA_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				new TestCORBA(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonData_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				new LoadTestData(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonEqCatalog_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				new LoadEquipmentCatalogNew(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonEqCatalog2_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
//				new LoadEquipmentCatalog2(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonLog_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				for(int i = 0; i < 100; i++)
				{
					log(" String " + i);
					try{ this.sleep(100); }
					catch(Exception e) {}
//					for(long j = 0; j < 5000000; j++);
  //						for(long k = 0; k < 5000000; k++);
				}
			}
		};
		thread.start();
	}

	void buttonMaintain_actionPerformed(ActionEvent e)
	{
		Thread thread = new Thread() {
			public void run()
			{
				new LoadMaintenanceData(this_frame, this_frame.connectionManager).run();
			}
		};
		thread.start();
	}

	void buttonClearLog_actionPerformed(ActionEvent e)
	{
		logText.setText("");
		logText.repaint();
	}
}

class LoadMessageFrame_buttonConnect_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonConnect_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonConnect_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonExit_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonExit_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonExit_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonImages_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonImages_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonImages_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonSys_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonSys_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonSys_actionPerformed(e);
	}
}

class LoadMessageFrame_jButton4_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_jButton4_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonProto_actionPerformed(e);
	}
}

class LoadMessageFrame_jButton42_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_jButton42_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonProto2_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonTest_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonTest_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonTest_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonCORBA_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonCORBA_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonCORBA_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonData_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonData_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonData_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonEqCatalog_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonEqCatalog_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonEqCatalog_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonEqCatalog2_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonEqCatalog2_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonEqCatalog2_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonLog_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonLog_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonLog_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonMaintain_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonMaintain_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonMaintain_actionPerformed(e);
	}
}

class LoadMessageFrame_buttonClearLog_actionAdapter implements java.awt.event.ActionListener{
	LoadMessageFrame adaptee;

	LoadMessageFrame_buttonClearLog_actionAdapter(LoadMessageFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.buttonClearLog_actionPerformed(e);
	}
}

