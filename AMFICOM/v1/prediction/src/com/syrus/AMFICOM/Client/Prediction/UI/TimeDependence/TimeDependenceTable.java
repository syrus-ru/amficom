package com.syrus.AMFICOM.Client.Prediction.UI.TimeDependence;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.ATableFrame;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.General.UI.ATable;

import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.TimeDependenceData;

// Author: Levchenko Alexandre S.

public class TimeDependenceTable extends ATableFrame implements OperationListener
{
	Dispatcher dispatcher;
	JScrollPane scrollPane = new JScrollPane();
	ATable dataTable = new ATable();
	TimeDependenceTableModel timeDependenceTableModel;



//-------------------------------------------------
	public TimeDependenceTable(Dispatcher dispatcher)
	{
		this.setDispatcher(dispatcher);
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

//-------------------------------------------------
	private void jbInit() throws Exception
	{
		this.setClosable(true);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setIconifiable(true);
		this.setResizable(true);
		this.getContentPane().setBackground(Color.white);
		scrollPane.getViewport().setBackground(Color.white);
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(dataTable, null);
		timeDependenceTableModel = new TimeDependenceTableModel();
		timeDependenceTableModel.clearTable();
		this.dataTable.setModel(timeDependenceTableModel);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().
		getImage("images/general.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		setTitle(LangModelPrediction.getString("TimedTableTitle"));

//jTable.getColumnModel().getColumn(0).setPreferredWidth(120);

		dataTable.getColumnModel().getColumn(0).setPreferredWidth(130);
		dataTable.getColumnModel().getColumn(1).setPreferredWidth(40);
	}

	public String getReportTitle()
	{
		return LangModelPrediction.getString("TimedTableTitle");
	}

	public TableModel getTableModel()
	{
		return timeDependenceTableModel;
	}

//-------------------------------------------------
	public void setDispatcher(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, "timeDependentDataIsSet");
	}



	public void operationPerformed(OperationEvent oe)
	{
		if(oe.getActionCommand().equals("timeDependentDataIsSet"))
		{
			Object o = Pool.get("timeDependentDataId", "timeDependentDataId");
			Object o2 = Pool.get("linearCoeffs", "MyLinearCoeffs");
			Object o3 = Pool.get("dimension", "dimension");
			if(o!=null && o2!=null)
			{
				setData((TimeDependenceData [])o, (com.syrus.AMFICOM.Client.Prediction.StatisticsMath.LinearCoeffs)o2, (String)o3);
			}
		}
	}

	private void setData(TimeDependenceData [] tdd, com.syrus.AMFICOM.Client.Prediction.StatisticsMath.LinearCoeffs linearCoeffs, String dim)
	{
		if(dim == null)
			dim = "";
		else if(dim.equals("connector_db"))
			dim = "дБ";
		else if(dim.equals("weld_db"))
			dim = "дБ";
		else if(dim.equals("linear_db"))
			dim = "дБ";
		else if(dim.equals("linear_db/km"))
			dim = "дБ/км";
		else dim = "";



		double day = 1000.*60.*60.*24;

		long maxTime = tdd[tdd.length-1].date;
		long minTime = tdd[0].date;

		double max = tdd[0].value;
		double min = tdd[0].value;

		for(int i=0; i<tdd.length; i++)
		{
			if(max<tdd[i].value)  max = tdd[i].value;
			if(min>tdd[i].value)  min = tdd[i].value;
		}
		double sigma = 100000.;

		max = (int)(max*sigma)/sigma;
		min = (int)(min*sigma)/sigma;
		double beginVal = (int)(linearCoeffs.f(tdd[0].date)*sigma)/sigma;
		double deriv = (int)(linearCoeffs.k*day*sigma)/sigma;
		double disp = (int)(linearCoeffs.dispersia*sigma)/sigma;
		double absDisp = (int)(linearCoeffs.absDispersia*sigma)/sigma;
		double quality = (int)((1./(linearCoeffs.chi2))*sigma)/sigma;
		double statError = (int)((1./Math.sqrt((double)tdd.length))*100.*sigma)/sigma;
		double systErr = (int)((1./Math.sqrt((double)(maxTime-minTime)/day))*100.*sigma)/sigma;
		if(systErr>100.) systErr=100.;

		double totalErr = (int)Math.sqrt(systErr*systErr + statError*statError);
		if(totalErr>100.) totalErr = 100.;

		Object[][] data = {
		{"Выборка", String.valueOf(tdd.length)},
		{"Начальное значение, "+dim, String.valueOf(beginVal)},
		{"Изменение (наклон), "+"("+dim+")"+"/день", String.valueOf(deriv)},
		{"Максимальное значение, "+dim, String.valueOf(max)},
		{"Минимальное значение, "+dim, String.valueOf(min)},
		{"Дисперсия, "+dim, String.valueOf(disp)},
		{"Среднее отклонение, "+dim, String.valueOf(absDisp)},
		{"Качество аппроксимации", String.valueOf(quality)},
		{"Статистическая ошибка, %", String.valueOf(statError)},
		{"Систематическая ошибка, %", String.valueOf(systErr)},
		{"Полная ошибка (грубо), %", String.valueOf(totalErr)}

	};

		this.timeDependenceTableModel.setTableData(data);
	}


}







//==============================================================================
class TimeDependenceTableModel extends AbstractTableModel
{
	String[] columnNames = {"", ""};

	Object[][] data = {
		{"--", "--"},
		{"--", "--"},
		{"--", "--"},
		{"--", "--"}
	};


	TimeDependenceTableModel()
	{
		super();
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

	public double getvalueat(int row, int col) {
		return ((Double)(data[row][col])).doubleValue();
	}

	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col)
	{
		return false;
	}

	public void setValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
}


