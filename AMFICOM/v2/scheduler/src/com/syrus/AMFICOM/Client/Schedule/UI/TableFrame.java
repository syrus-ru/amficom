/*
 * SaveParametersFrame.java Created on 17.05.2004 18:23:26
 *  
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.TestType;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

/**
 * @author Vladimir Dolzhenko
 */
public class TableFrame extends JInternalFrame implements OperationListener {

	private class ColumnSorter implements Comparator {

		boolean	ascending;

		int		colIndex;

		ColumnSorter(int colIndex, boolean ascending) {
			this.colIndex = colIndex;
			this.ascending = ascending;
		}

		public int compare(Object a, Object b) {
			int result = 0;
			TestTableRow v1 = (TestTableRow) a;
			TestTableRow v2 = (TestTableRow) b;
			Object o1 = v1.get(colIndex);
			Object o2 = v2.get(colIndex);

			// Treat empty strains like nulls
			if (o1 instanceof String && ((String) o1).length() == 0) {
				o1 = null;
			}
			if (o2 instanceof String && ((String) o2).length() == 0) {
				o2 = null;
			}

			// Sort nulls so they appear last, regardless
			// of sort order
			if (o1 == null && o2 == null) {
				result = 0;
			} else if (o1 == null) {
				result = 1;
			} else if (o2 == null) {
				result = -1;
			} else if (o1 instanceof Comparable) {
				if (ascending) {
					result = ((Comparable) o1).compareTo(o2);
				} else
					result = ((Comparable) o2).compareTo(o1);

			} else {
				if (ascending) {
					result = o1.toString().compareTo(o2.toString());
				} else
					result = o2.toString().compareTo(o1.toString());

			}
			return result;
		}
	}

	private class TestTableCellRenderer extends JLabel implements
			TableCellRenderer {

		//protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

		//		 We need a place to store the color the JLabel should be returned
		// to after its foreground and background colors have been set
		// to the selection background color.
		// These ivars will be made protected when their names are finalized.
		private Color	unselectedForeground;

		//private Color unselectedBackground;

		/**
		 * Creates a default table cell renderer.
		 */
		public TestTableCellRenderer() {
			super();
			setOpaque(true);
			//setBorder(noFocusBorder);
		}

		// This method is called each time a cell in a column
		// using this renderer needs to be rendered.
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus,
				int rowIndex, int vColIndex) {
			// 'value' is value contained in the cell located at
			// (rowIndex, vColIndex)
			//
			TestTableModel model = (TestTableModel) table.getModel();
			TestTableRow line = (TestTableRow) model.getRow(rowIndex);

			Color color = table.getBackground();
			Test test = line.getTest();
			if (test.getStatus().equals(TestStatus.TEST_STATUS_COMPLETED)) {
				color = TestLine.COLOR_COMPLETED;
			} else if (test.getStatus()
					.equals(TestStatus.TEST_STATUS_SCHEDULED)) {
				color = TestLine.COLOR_SCHEDULED;
			} else if (test.getStatus().equals(
					TestStatus.TEST_STATUS_PROCESSING)) {
				color = TestLine.COLOR_PROCCESSING;
			} else if (test.getStatus().equals(TestStatus.TEST_STATUS_ABORTED)) {
				color = TestLine.COLOR_ABORDED;
			} else {
				color = TestLine.COLOR_UNRECOGNIZED;
			}

			if (isSelected) {
				super
						.setForeground((unselectedForeground != null) ? unselectedForeground
								: table.getForeground());
				Font font = table.getFont();
				font = new Font(font.getName(), Font.BOLD | Font.ITALIC, font
						.getSize());
				setFont(font);
				Color c = table.getSelectionBackground();
				double k = 0.3;
				super.setBackground(new Color((int) (c.getRed() * (1.0 - k) + k
						* color.getRed()) % 256, (int) (c.getGreen()
						* (1.0 - k) + k * color.getGreen()) % 256, (int) (c
						.getBlue()
						* (1.0 - k) + k * color.getBlue()) % 256));
			} else {
				super
						.setForeground((unselectedForeground != null) ? unselectedForeground
								: table.getForeground());
				setFont(table.getFont());
				super.setBackground(color);
			}

			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); //$NON-NLS-1$
				if (table.isCellEditable(rowIndex, vColIndex)) {
					super.setForeground(UIManager
							.getColor("Table.focusCellForeground")); //$NON-NLS-1$
					super.setBackground(UIManager
							.getColor("Table.focusCellBackground")); //$NON-NLS-1$
				}
			} else {
				//setBorder(noFocusBorder);
			}

			setValue(value);

			return this;
		}

		/**
		 * Overrides <code>JComponent.setBackground</code> to assign the
		 * unselected-background color to the specified color.
		 * 
		 * @param c
		 *            set the background color to this value
		 */
		public void setBackground(Color c) {
			super.setBackground(c);
			//unselectedBackground = c;
		}

		/**
		 * Overrides <code>JComponent.setForeground</code> to assign the
		 * unselected-foreground color to the specified color.
		 * 
		 * @param c
		 *            set the foreground color to this value
		 */
		public void setForeground(Color c) {
			super.setForeground(c);
			unselectedForeground = c;
		}

		protected void setValue(Object value) {
			setText((value == null) ? "" : value.toString()); //$NON-NLS-1$
		}

	}

	private class TestTableModel extends AbstractTableModel {

		private int			columnCount	= 7;

		private boolean[]	sortOrders	= new boolean[columnCount];

		private ArrayList	testLines;

		public void addRow(TestTableRow value) {
			if (testLines == null) testLines = new ArrayList();
			testLines.add(value);
		}

		public Class getColumnClass(int columnIndex) {
			Class clazz;
			switch (columnIndex) {
				default:
					clazz = String.class;
					break;
			}
			return clazz;
		}

		public int getColumnCount() {
			return columnCount;
		}

		public String getColumnName(int columnIndex) {
			String name;
			switch (columnIndex) {
				case 0:
					name = LangModelSchedule.getString("TemporalType"); //$NON-NLS-1$
					break;
				case 1:
					name = LangModelSchedule.getString("RTU"); //$NON-NLS-1$
					break;
				case 2:
					name = LangModelSchedule.getString("Port"); //$NON-NLS-1$
					break;
				case 3:
					name = LangModelSchedule.getString("TestObject"); //$NON-NLS-1$
					break;
				case 4:
					name = LangModelSchedule.getString("MeasurementType"); //$NON-NLS-1$
					break;
				case 5:
					name = LangModelSchedule.getString("TestStartTime"); //$NON-NLS-1$
					break;
				case 6:
					name = LangModelSchedule.getString("Status"); //$NON-NLS-1$
					break;
				default:
					name = null;
					break;
			}
			return name;
		}

		public Vector getDataVector() {
			Vector vec = new Vector();
			for (int i = 0; i < getRowCount(); i++)
				vec.add(getRowData(i));
			return vec;
		}

		public Object getRow(int rowIndex) {
			if (testLines == null) return null;
			return testLines.get(rowIndex);
		}

		public int getRowCount() {
			int count = 0;
			if (testLines != null) count = testLines.size();
			return count;
		}

		public ArrayList getRowData(int rowIndex) {
			TestTableRow line = (TestTableRow) getRow(rowIndex);
			return line.getData();
		}

		public boolean getSortOrder(int columnIndex) {
			return sortOrders[columnIndex];
		}

		public int getTestRowIndex(Test test) {
			int rowIndex = 0;
			for (int i = 0; i < testLines.size(); i++) {
				TestTableRow row = (TestTableRow) testLines.get(i);
				if (row.getTest().equals(test)) {
					rowIndex = i;
					break;
				}
			}
			return rowIndex;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Object obj = null;
			if (testLines != null) {
				TestTableRow line = (TestTableRow) testLines.get(rowIndex);
				ArrayList data = line.getData();
				if (columnIndex < data.size()) obj = data.get(columnIndex);
			}
			return obj;
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public void remove(int rowIndex) {
			testLines.remove(rowIndex);
		}

		public void removeAll() {
			if (testLines != null) testLines.clear();
		}

		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			if (testLines == null) testLines = new ArrayList();
			TestTableRow line;
			if (testLines.size() < rowIndex) {
				line = new TestTableRow();
				testLines.add(rowIndex, line);
			} else
				line = (TestTableRow) testLines.get(rowIndex);
			/**
			 * @todo setValue
			 */
			//			switch (columnIndex) {
			//
			//			}
		}

		public void sortRows(int columnIndex) {
			sortRows(columnIndex, sortOrders[columnIndex]);
			sortOrders[columnIndex] = !sortOrders[columnIndex];
		}

		public void sortRows(int columnIndex, boolean ascending) {
			if (testLines != null)
					Collections.sort(testLines, new ColumnSorter(columnIndex,
							ascending));
		}

	}

	private class TestTableRow {

		private ArrayList	data;
		private String		me;
		private String		port;
		private String		rtu;
		private String		statusName;

		private String		temporalType;

		private Test		test;
		private String		testType;
		private String		time;

		public TestTableRow() {
			// nothing to do
		}

		public TestTableRow(Test test) {
			this.setTest(test);
		}

		public Object get(int index) {
			return getData().get(index);
		}

		public ArrayList getData() {
			if (data == null) {
				data = new ArrayList();
				data.add(temporalType);
				data.add(rtu);
				data.add(port);
				data.add(me);
				data.add(testType);
				data.add(time);
				data.add(statusName);
			}
			return data;
		}

		public Test getTest() {
			return test;
		}

		public void setTest(Test test) {
			data = null;
			this.test = test;
			switch (test.getTimeStamp().getType()) {
				case TimeStamp.TIMESTAMPTYPE_ONETIME:
					this.temporalType = LangModelSchedule.getString("Onetime"); //$NON-NLS-1$
					break;
				case TimeStamp.TIMESTAMPTYPE_CONTINUOS:
					this.temporalType = LangModelSchedule
							.getString("Continual"); //$NON-NLS-1$
					break;
				case TimeStamp.TIMESTAMPTYPE_PERIODIC:
					this.temporalType = LangModelSchedule
							.getString("Periodical"); //$NON-NLS-1$
					break;
			}
			KIS kis = (KIS) Pool.get(KIS.typ, test.getKisId());
			this.rtu = kis.name;
			Vector accessPorts = kis.access_ports;
			MonitoredElement me = (MonitoredElement) Pool.get(
					MonitoredElement.typ, test.getMonitoredElementId());
			AccessPort port = null;
			for (int i = 0; i < accessPorts.size(); i++) {
				AccessPort aport = (AccessPort) accessPorts.get(i);
				if (me.access_port_id.equals(aport.getId())) {
					port = aport;
					break;
				}
			}
			if (port != null) this.port = port.name;
			this.me = me.getName();
			TestType testType = (TestType) Pool.get(TestType.typ, test
					.getTestTypeId());
			this.testType = testType.getName();
			this.time = UIStorage.SDF.format(new Date(test.getTimeStamp()
					.getPeriodStart()));

			//this.id = test.id;
			//this.kis = test.kis;
			if (test.getStatus().equals(TestStatus.TEST_STATUS_COMPLETED)) {
				this.statusName = LangModelSchedule.getString("Done"); //$NON-NLS-1$
			} else if (test.getStatus()
					.equals(TestStatus.TEST_STATUS_SCHEDULED)) {
				this.statusName = LangModelSchedule.getString("Scheduled"); //$NON-NLS-1$
			} else if (test.getStatus().equals(
					TestStatus.TEST_STATUS_PROCESSING)) {
				this.statusName = LangModelSchedule.getString("Running"); //$NON-NLS-1$
			} else if (test.getStatus().equals(TestStatus.TEST_STATUS_ABORTED)) {
				this.statusName = LangModelSchedule.getString("Aborted"); //$NON-NLS-1$
			} else {
				this.statusName = LangModelSchedule.getString("Unrecognized"); //$NON-NLS-1$
			}

		}

	}

	/** 
	 * @todo only for testing mode
	 */
	public static void main(String[] args) {

		TableFrame frame = new TableFrame(null);
		JFrame mainFrame = new JFrame(LangModelSchedule
				.getString("Tests_status_and_characters")); //$NON-NLS-1$
		mainFrame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		mainFrame.getContentPane().add(frame.getPanel());
		mainFrame.pack();
		mainFrame.setSize(new Dimension(250, 465));
		mainFrame.setVisible(true);
	}

	Dispatcher			dispatcher;
	JTable				listTable;
	//private ApplicationContext aContext;
	private JPanel		panel;
	ArrayList			savedTests;
	boolean				skipTestUpdate	= false;
	private ArrayList	unsavedTests;

	public TableFrame(ApplicationContext aContext) {
		//this.aContext = aContext;
		if (aContext != null) initModule(aContext.getDispatcher());
		init();
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		if (commandName.equals(TestUpdateEvent.TYPE)) {
			if (!skipTestUpdate) {
				TestUpdateEvent tue = (TestUpdateEvent) ae;
				Test test = tue.test;
				boolean found = false;
				if (savedTests != null) {
					if (savedTests.contains(test)) found = true;
				}
				if (!found) {
					if (unsavedTests != null) {
						if (unsavedTests.contains(test)) found = true;
					}
				}

				if (!found) {
					if (test.isChanged()) {
						if (unsavedTests == null)
								unsavedTests = new ArrayList();
						unsavedTests.add(test);
						TestTableModel model = (TestTableModel) listTable
								.getModel();
						//model.removeAll();
						TestTableRow row = new TestTableRow(test);
						model.addRow(row);
						listTable.repaint();
						listTable.revalidate();
					} else
						savedTests.add(test);
				} else {
					int row = ((TestTableModel) listTable.getModel())
							.getTestRowIndex(test);
					listTable.setRowSelectionInterval(row, row);
				}
			}

		} else if (commandName.equals(PlanPanel.COMMAND_NAME_ALL_TESTS)) {
			setSavedTests((DataSet) ae.getSource());
		}
	}

	public void setSavedTests(final DataSet tests) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				if (savedTests == null) savedTests = new ArrayList();
				TestTableModel model = (TestTableModel) listTable.getModel();
				model.removeAll();
				for (int i = 0; i < tests.size(); i++) {
					Test test = (Test) tests.get(i);
					savedTests.add(test);
					TestTableRow row = new TestTableRow(test);
					model.addRow(row);
				}
				listTable.repaint();
				listTable.revalidate();
			}
		});
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel(new BorderLayout());

			TestTableModel tableModel = new TestTableModel();
			listTable = new JTable(tableModel);
			listTable.setColumnSelectionAllowed(false);
			listTable.setRowSelectionAllowed(true);
			listTable.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent evt) {
					JTable table = ((JTable) evt.getSource());
					if (SwingUtilities.isLeftMouseButton(evt)) {
						int rowIndex = table.getSelectedRow();
						TestTableModel model = (TestTableModel) table
								.getModel();
						TestTableRow line = (TestTableRow) model
								.getRow(rowIndex);
						//System.out.println("test:" + line.getTest());
						Test test = line.getTest();
						skipTestUpdate = true;
						dispatcher.notify(new TestUpdateEvent(this, test,
								TestUpdateEvent.TEST_SELECTED_EVENT));
						skipTestUpdate = false;
					} else if (SwingUtilities.isRightMouseButton(evt)) {
						final int[] rowIndices = table.getSelectedRows();
						if ((rowIndices != null) && (rowIndices.length > 0)) {
							final TestTableModel model = (TestTableModel) table
									.getModel();
							JPopupMenu popup = new JPopupMenu();
							JMenuItem deleteTestMenuItem = new JMenuItem(
									LangModelSchedule.getString("delete_tests")); //$NON-NLS-1$
							deleteTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									for (int i = 0; i < rowIndices.length; i++) {
										TestTableRow line = (TestTableRow) model
												.getRow(rowIndices[i]);
										System.out.println("test:" //$NON-NLS-1$
												+ line.getTest().getId());
									}
								}
							});
							popup.add(deleteTestMenuItem);
							popup.show(table, evt.getX(), evt.getY());
						}
					}

				}
			});
			{
				//int vColIndex = 0;
				for (int vColIndex = 0; vColIndex < tableModel.getColumnCount(); vColIndex++) {
					TableColumn col = listTable.getColumnModel().getColumn(
							vColIndex);
					col.setCellRenderer(new TestTableCellRenderer());
				}
			}
			JTableHeader header = listTable.getTableHeader();
			header.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent evt) {
					JTableHeader header = (JTableHeader) evt.getSource();
					JTable table = header.getTable();
					TableColumnModel colModel = table.getColumnModel();

					// The index of the column whose header was clicked
					int columnIndex = colModel.getColumnIndexAtX(evt.getX());
					int mColIndex = table
							.convertColumnIndexToModel(columnIndex);
					TestTableModel model = (TestTableModel) table.getModel();
					String s;
					if (model.getSortOrder(mColIndex))
						s = " v "; //$NON-NLS-1$
					else
						s = " ^ "; //$NON-NLS-1$
					table.getColumnModel().getColumn(columnIndex)
							.setHeaderValue(
									s + model.getColumnName(mColIndex) + s);

					for (int i = 0; i < model.getColumnCount(); i++) {
						if (i != mColIndex)
								table.getColumnModel().getColumn(
										table.convertColumnIndexToView(i))
										.setHeaderValue(model.getColumnName(i));
					}

					// Force the header to resize and repaint itself
					header.resizeAndRepaint();
					model.sortRows(mColIndex);

					// Return if not clicked on any column header
					if (columnIndex == -1) { return; }

					// Determine if mouse was clicked between column heads
					Rectangle headerRect = table.getTableHeader()
							.getHeaderRect(columnIndex);
					if (columnIndex == 0) {
						headerRect.width -= 3; // Hard-coded constant
					} else {
						headerRect.grow(-3, 0); // Hard-coded constant
					}
					if (!headerRect.contains(evt.getX(), evt.getY())) {
						// Mouse was clicked between column heads
						// vColIndex is the column head closest to the click

						// vLeftColIndex is the column head to the left of the
						// click
						int vLeftColIndex = columnIndex;
						if (evt.getX() < headerRect.x) {
							vLeftColIndex--;
						}
					}
				}
			});

			panel.add(header, BorderLayout.NORTH);
			panel.add(new JScrollPane(listTable,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
					BorderLayout.CENTER);
			//panel.add(listPane, BorderLayout.CENTER);

		}
		return panel;

	}

	private void init() {
		setTitle(LangModelSchedule.getString("Tests_status_and_characters")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		panel = getPanel();
		setContentPane(panel);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this, PlanPanel.COMMAND_NAME_ALL_TESTS);
	}

}
