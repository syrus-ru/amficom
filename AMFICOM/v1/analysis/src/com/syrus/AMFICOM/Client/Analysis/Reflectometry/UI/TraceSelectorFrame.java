package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;
import java.util.List;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.io.BellcoreStructure;

public class TraceSelectorFrame extends JInternalFrame
																		implements OperationListener
{
	private static List traces = new ArrayList();
	private Dispatcher dispatcher;
	private FixedSizeEditableTableModel tModel; //DefaultTableModel
	private ColorChooserTable jTable;

	BorderLayout borderLayout = new BorderLayout();
	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	boolean here = false;

	public TraceSelectorFrame()
	{
		this(new Dispatcher());
	}

	public TraceSelectorFrame(Dispatcher dispatcher)
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

		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefChangeEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefChangeEvent.typ))
		{
			RefChangeEvent rce = (RefChangeEvent)ae;
			if(rce.OPEN || rce.OPEN_ETALON)
			{
				String id = (String)(rce.getSource());
				if (traces.contains(id))
					return;

				traces.add(id);

				String title = id;
				BellcoreStructure bs = (BellcoreStructure)Pool.get("bellcorestructure", id);
				if (bs != null)
					title = bs.title;

				tModel.addRow(title, new Color[] {ColorManager.getColor(id)});
				setVisible(true);
			}

			if(rce.SELECT)
			{
				here = true;
				String id = (String)(rce.getSource());
				int selected = traces.indexOf(id);
				if (selected != -1)
					jTable.setRowSelectionInterval(selected, selected);
				here = false;
			}
			if(rce.CLOSE)
			{
				String id = (String)(rce.getSource());
				if (id.equals("all"))
				{
					tModel.clearTable();
					traces = new ArrayList();
					setVisible(false);
				}
				else
				{
					int index = traces.indexOf(id);
					if (index != -1)
					{
						tModel.removeRow(index);
						traces.remove(id);
					}
				}
			}
		}
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		tModel = new FixedSizeEditableTableModel(
					new String[] {LangModelAnalyse.getString("selectorKey"),
												LangModelAnalyse.getString("selectorValue")},
					new Object[] {Color.BLACK},
					null,
					null);

//		tModel = new GeneralTableModel();

		jTable = new ColorChooserTable (tModel);
		jTable.getColumnModel().getColumn(0).setPreferredWidth(250);

		setContentPane(mainPanel);
		this.setSize(new Dimension(200, 213));
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		//this.setMaximizable(true);
		this.setTitle(LangModelAnalyse.getString("selectorTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		jTable.setSelectionMode(jTable.getSelectionModel().SINGLE_SELECTION);
		ListSelectionModel rowSM = jTable.getSelectionModel();
		jTable.getColumnModel().setSelectionModel(rowSM);
		rowSM.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e) {
			//Ignore extra messages.
			if (e.getValueIsAdjusting())
				return;

			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (lsm.isSelectionEmpty())
			{
						//no rows are selected
			}
			else
			{
				if (here)
					here = false;
				else
				{
					int selectedRow = lsm.getMinSelectionIndex();
					//selectedRow is selected
					RefChangeEvent event = new RefChangeEvent((String)traces.get(selectedRow), RefChangeEvent.SELECT_EVENT);
					dispatcher.notify(event);
				}
			}
		}
		});

		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable.setMinimumSize(new Dimension(200, 213));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
		updColorModel();
	}

	private void updColorModel()
	{
		scrollPane.getViewport().setBackground(SystemColor.window);
		jTable.setBackground(SystemColor.window);
		jTable.setForeground(ColorManager.getColor("textColor"));
		jTable.setGridColor(ColorManager.getColor("tableGridColor"));
	}

			class DefaultTableModel extends AbstractTableModel {
				final String[] columnNames = {"Element",
																			"Color"};
				final Object[][] data = {
						{"DefaultElement", new Color(255, 255, 255)},
						{"DefaultElement2", new Color(255, 255, 255)}
						};

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

				/*
				 * JTable uses this method to determine the default renderer/
				 * editor for each cell.  If we didn't implement this method,
				 * then the last column would contain text ("true"/"false"),
				 * rather than a check box.
				 */
				public Class getColumnClass(int c) {
						return getValueAt(0, c).getClass();
				}

				/*
				 * Don't need to implement this method unless your table's
				 * editable.
				 */
				public boolean isCellEditable(int row, int col) {
						//Note that the data/cell address is constant,
						//no matter where the cell appears onscreen.
						if (col < 1) {
								return false;
						} else {
								return true;
						}
				}

				public void setValueAt(Object value, int row, int col) {
						data[row][col] = value;
						fireTableCellUpdated(row, col);
				}
		}

}