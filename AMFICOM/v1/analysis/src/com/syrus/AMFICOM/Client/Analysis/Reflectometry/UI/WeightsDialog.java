package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.io.*;
import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ATable;

public class WeightsDialog extends JDialog
{
	private ParametersTable parametersTable = new ParametersTable();
	private ATable dataTable = new ATable();
	private JPanel tablePanel = new JPanel();
	private JPanel textPanel = new JPanel();
	private JTextArea messageArea = new JTextArea();
	private JPanel buttonsPanel = new JPanel();
	private JButton countButton = new JButton();
	private JButton setDefaultButton = new JButton();
	private JButton disposeButton = new JButton();
	private JScrollPane jScrollPane1 = new JScrollPane();

	public native double getWeights (
			int nOfEpochs,
			int nOfSamples,
			int nOfLayers,
			int eventSize_,
			int []netArchitecture,
			double weldMinimalAmplitude_,
		double weldMaximalAmplitude_,
		double connectorMinimalLet_,
		double connectorMaximalLet_,
		double maximalNoise_);

	static
	{
		System.loadLibrary("neuroRAweightsDLL");
	}

	public WeightsDialog()
	{
		super(Environment.getActiveWindow());
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	private void jbInit() throws Exception
	{
		setModal(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (500, 350);
		setSize(frameSize);

		setLocation(screenSize.width / 2 - 250, screenSize.height / 2 - 175);

		setTitle("Обучение нейронной сети АМФИКОМ");
		this.getContentPane().setLayout(new BorderLayout());

		dataTable.setModel(parametersTable);
		setDefaultData();

		tablePanel.setLayout(new BorderLayout());
		tablePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		textPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		textPanel.setPreferredSize(new Dimension(390, 100));
		textPanel.setLayout(new BorderLayout());
		messageArea.setBorder(null);
		messageArea.setEditable(false);
		countButton.setText("Пересчитать");
		countButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				countButton_actionPerformed(e);
			}
		});
		setDefaultButton.setText("По умолчанию");
		setDefaultButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDefaultButton_actionPerformed(e);
			}
		});
		disposeButton.setText("Закрыть");
		disposeButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		jScrollPane1.setBorder(null);
		getContentPane().add(tablePanel, BorderLayout.NORTH);
		tablePanel.add(dataTable);
		this.getContentPane().add(textPanel, BorderLayout.CENTER);
		textPanel.add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(messageArea, null);
		this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		buttonsPanel.add(countButton);
		buttonsPanel.add(setDefaultButton);
		buttonsPanel.add(disposeButton);

		OutStream os = new OutStream();
		os.setTextArea(messageArea);
		PrintStream ps = new PrintStream(os);
		System.setOut(ps);
	}

	private void performCalculation()
	{
		String []s = new String[9];
		for(int i=0; i<9; i++)
		{
			s[i] = (String)(parametersTable.data[i][1]);
		}

		String architecture = canBeArchitecture(s[8]);

		if(!canBeDouble(s[0]) ||
			 !canBeDouble(s[1]) ||
			 !canBeDouble(s[2]) ||
			 !canBeDouble(s[3]) ||
			 !canBeDouble(s[4]) ||

			 !canBeInteger(s[5]) ||
			 !canBeInteger(s[6]) ||
			 !canBeInteger(s[7]) ||
			 architecture.equals("false"))
		{
			System.out.println(" Ошибка. Проверьте параметры.");
			return;
		}

//    {"Макс. амплитуда сварки, дБ", "0.05"},0
//    {"Мин. амплитуда сварки, дБ", "0.015"},1
//    {"Макс. амплитуда отражательного всплеска, дБ", "10."},2
//    {"Мин. амплитуда отражательного всплеска, дБ", "0.2"},3
//    {"Максимальная амплитуда шума, дБ", "0.007"},4
//    {"Число обучающих событий ", "5000"},5
//    {"Число эпох обучения", "10"},6
//    {"Размерность входного сигнала", "30"},7
//    {"Архитектура сети", "30-10-"},8
		double maxWeld = Double.parseDouble(s[0]);
		double minWeld = Double.parseDouble(s[1]);
		double maxLet  = Double.parseDouble(s[2]);
		double minLet  = Double.parseDouble(s[3]);
		double maxNoise = Double.parseDouble(s[4]);

		int nEvents = Integer.parseInt(s[5]);
		int nEpochs = Integer.parseInt(s[6]);
		int eventSize = Integer.parseInt(s[7]);

		int []netArchitecture = getArchitecture(s[8]);

		System.out.println("Выполнение...");
		setCursor(new Cursor(Cursor.WAIT_CURSOR));

		// Call of the dll' function;
		double res = getWeights(nEpochs, nEvents, netArchitecture.length, eventSize, netArchitecture, minWeld, maxWeld, minLet, maxLet, maxNoise);
		System.out.println("res = " + res);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		System.out.println("Вычисление весов для заданной архитектуры завершено");
	}

	private int []getArchitecture(String s)
	{
		List v = new ArrayList();
		int from = 0;
		for(int i=0; i<s.length(); i++)
		{
			if(s.charAt(i) == '-' )
			{
			v.add(s.substring(from, i));
			from = i+1;
		}
		}

		int []ret = new int[v.size()];
		for(int i=0; i<ret.length; i++)
		{
			ret[i] = Integer.parseInt((String)(v.get(i)));
		}

		return ret;

	}


	private String canBeArchitecture(String s)
	{
		List v = new ArrayList();
		String ret = "";
		int from = 0;
		for(int i=0; i<s.length(); i++)
		{
			if(s.charAt(i) == '-' )
			{
				v.add(s.substring(from, i));
				from = i+1;
			}
		}

		if(v.size() == 0) return "false";

		for(int i=0; i<v.size(); i++)
		{
			if(!canBeInteger((String)(v.get(i))))
			{
				return "false";
			}
			else
			{
				ret = ret + (String)v.get(i) + " ";
			}
		}
//    System.out.println(ret);
		return ret;
	}


	public void setDefaultData()
	{
		Object[][] o =
		{
			{"Макс. амплитуда неотражательного события, дБ", "0.05"},
			{"Мин. амплитуда неотражательного события, дБ", "0.015"},
			{"Макс. амплитуда отражательного события, дБ", "10."},
			{"Мин. амплитуда отражательного события, дБ", "0.2"},
			{"Максимальная амплитуда шума, дБ", "0.007"},

			{"Число обучающих событий ", "5000"},
			{"Число эпох обучения", "10"},
			{"Размерность входного сигнала", "30"},
			{"Архитектура сети", "30-10-"},
		};
		this.parametersTable.setTableData(o);
		dataTable.getColumnModel().getColumn(0).setPreferredWidth(400);
	}



	void countButton_actionPerformed(ActionEvent e)
	{
		performCalculation();
	}



	void setDefaultButton_actionPerformed(ActionEvent e)
	{
		setDefaultData();
	}


	private boolean canBeInteger(String s)
	{
		int tmp;
		try
		{
			tmp = Integer.parseInt(s);
			if(tmp>0)
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	private boolean canBeDouble(String s)
	{
		double tmp;
		try
		{
			tmp = Double.parseDouble(s);
			if(tmp>0.)
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}



class ParametersTable extends AbstractTableModel
{
	String[] columnNames = {"Параметр",
													"Значение"};

	Object[][] data = {
		{"--", "--"},
	};


	ParametersTable()
	{
		super();
		clearTable();
	}

	public void clearTable()
	{
		data = new Object[][]{};
		super.fireTableDataChanged();
	}

	public void setTableData(Object[][] data)
	{
		this.data = data;
		super.fireTableDataChanged();
	}


	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col)
	{
		if (col==1 && row != 7)
			return true;
		return false;
	}

	public void setValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}

	class SystemExec
	{
		public static void executeCommand(String command)
		{
			Runtime r = Runtime.getRuntime();
			try
			{
				r.exec(command);
			}
			catch(Exception e)
			{
				System.out.println("Ошибка выполнения команды  <"+command+">.");
			}
		}
	}

	class OutStream extends OutputStream
	{
		JTextArea ta = new JTextArea();
		byte []s = new byte[1];

		public void setTextArea(JTextArea ta)
		{
			this.ta = ta;
		}

		public void write(int i)
		{
			this.s[0] = (byte)i;
			ta.append(new String(this.s));
		}
	}

