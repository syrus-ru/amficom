/*
 * SaveParametersFrame.java Created on 17.05.2004 18:23:26
 *  
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TestUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTable;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourceTableModel;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestController;

/**
 * @author Vladimir Dolzhenko
 */
public class TableFrame extends JInternalFrame implements OperationListener {

//	private class TestTableCellRenderer extends ObjectResourceTableCellRenderer {
//
//		protected void customRendering(	JTable table,
//										ObjectResource objectResource,
//										boolean isSelected,
//										boolean hasFocus,
//										int rowIndex,
//										int vColIndex) {
//			if (objectResource instanceof Test) {
//				Test test = (Test) objectResource;
//				Color color = table.getBackground();
//				TestTableModel model = (TestTableModel) table.getModel();
//
//				ElementaryTestAlarm[] testAlarms = test.getElementaryTestAlarms();
//				if (testAlarms.length != 0) {
//					//System.out.println("testAlarms.length:"+testAlarms.length);
//					for (int i = 0; i < testAlarms.length; i++) {
//						Alarm alarm = (Alarm) Pool.get(Alarm.typ, testAlarms[i].alarm_id);
//						//System.out.println("alarm:"+alarm.type_id);
//						if (alarm != null) {
//							//System.out.println("alarm.type_id:" +
//							// alarm.type_id);
//							if (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_ALARM)) {
//								//System.out.println("ID_RTU_TEST_ALARM");
//								color = TestLine.COLOR_ALARM;
//							} else if (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_WARNING)) {
//								//System.out.println("ID_RTU_TEST_WARNING");
//								color = TestLine.COLOR_WARNING;
//							}
//						}
//					}
//				}
//				int statusIndex = -1;
//				{
//					int columnCount = model.getColumnCount();
//					String statusString = LangModelSchedule.getString("Status");
//					for (int i = 0; i < columnCount; i++)
//						if (model.getColumnName(i).equals(statusString)) {
//							statusIndex = i;
//							break;
//						}
//				}
//
//				if (vColIndex == table.convertColumnIndexToView(statusIndex)) {
//					//System.out.println("statusIndex:"+statusIndex);
//					if (test.getStatus().equals(TestStatus.TEST_STATUS_COMPLETED)) {
//						color = TestLine.COLOR_COMPLETED;
//					} else if (test.getStatus().equals(TestStatus.TEST_STATUS_SCHEDULED)) {
//						color = TestLine.COLOR_SCHEDULED;
//					} else if (test.getStatus().equals(TestStatus.TEST_STATUS_PROCESSING)) {
//						color = TestLine.COLOR_PROCCESSING;
//					} else if (test.getStatus().equals(TestStatus.TEST_STATUS_ABORTED)) {
//						color = TestLine.COLOR_ABORDED;
//					} else {
//						color = TestLine.COLOR_UNRECOGNIZED;
//					}
//
//				}
//
//				super.setBackground(color);
//
//			}
//		}
//
//	}
//
//	private class TestTableModel extends ObjResTableModel {
//
//		public TestTableModel() {
//			super(7);
//		}
//
//		public Class getColumnClass(int columnIndex) {
//			Class clazz;
//			switch (columnIndex) {
//				default:
//					clazz = String.class;
//					break;
//			}
//			return clazz;
//		}
//
//		public String getColumnName(int columnIndex) {
//			String name;
//			switch (columnIndex) {
//				case 0:
//					name = LangModelSchedule.getString("TemporalType"); //$NON-NLS-1$
//					break;
//				case 1:
//					name = LangModelSchedule.getString("RTU"); //$NON-NLS-1$
//					break;
//				case 2:
//					name = LangModelSchedule.getString("Port"); //$NON-NLS-1$
//					break;
//				case 3:
//					name = LangModelSchedule.getString("TestObject"); //$NON-NLS-1$
//					break;
//				case 4:
//					name = LangModelSchedule.getString("MeasurementType"); //$NON-NLS-1$
//					break;
//				case 5:
//					name = LangModelSchedule.getString("TestStartTime"); //$NON-NLS-1$
//					break;
//				case 6:
//					name = LangModelSchedule.getString("Status"); //$NON-NLS-1$
//					break;
//				default:
//					name = null;
//					break;
//			}
//			return name;
//		}
//
//		//		public Vector getDataVector() {
//		//			Vector vec = new Vector();
//		//			for (int i = 0; i < getRowCount(); i++)
//		//				vec.add(getRowData(i));
//		//			return vec;
//		//		}
//
//		public boolean isCellEditable(int rowIndex, int columnIndex) {
//			return false;
//		}
//	}
//
//	private class TestTableRow extends ObjectResourceTableRow {
//
//		private java.util.List	data;
//		private String			me;
//		private String			port;
//		private String			rtu;
//		private String			statusName;
//
//		private String			temporalType;
//
//		//private Test test;
//		private String			testType;
//		private String			time;
//
//		public TestTableRow(ObjectResource test) {
//			super(test);
//		}
//
//		public java.util.List getData() {
//			if ((this.data == null) || (this.data.isEmpty())) {
//				this.data = new ArrayList();
//				this.data.add(this.temporalType);
//				this.data.add(this.rtu);
//				this.data.add(this.port);
//				this.data.add(this.me);
//				this.data.add(this.testType);
//				this.data.add(this.time);
//				this.data.add(this.statusName);
//			}
//			return this.data;
//		}
//
//		public void setValue(Object value, int columnIndex) {
//			if (value instanceof String) {
//				String s = (String) value;
//				switch (columnIndex) {
//					case 1:
//						this.temporalType = s;
//						break;
//					case 2:
//						this.rtu = s;
//						break;
//					case 3:
//						this.port = s;
//						break;
//					case 4:
//						this.me = s;
//						break;
//					case 5:
//						this.testType = s;
//						break;
//					case 6:
//						this.time = s;
//						break;
//					case 7:
//						this.statusName = s;
//						break;
//				}
//				this.data.clear();
//			}
//
//		}
//
//		public void setObjectResource(ObjectResource objectResource) {
//			//DataSourceInterface dsi =
//			// TableFrame.this.aContext.getDataSourceInterface();
//			//dsi.LoadKISDescriptors();
//			if (objectResource instanceof Test) {
//				Test test = (Test) objectResource;
//				this.data = null;
//				super.setObjectResource(test);
//				switch (test.getTimeStamp().getType()) {
//					case TimeStamp.TIMESTAMPTYPE_ONETIME:
//						this.temporalType = LangModelSchedule.getString("Onetime"); //$NON-NLS-1$
//						break;
//					case TimeStamp.TIMESTAMPTYPE_CONTINUOS:
//						this.temporalType = LangModelSchedule.getString("Continual"); //$NON-NLS-1$
//						break;
//					case TimeStamp.TIMESTAMPTYPE_PERIODIC:
//						this.temporalType = LangModelSchedule.getString("Periodical"); //$NON-NLS-1$
//						break;
//				}
//
//				KIS kis = (KIS) Pool.get(KIS.typ, test.getKisId());
//				this.rtu = kis.name;
//				MonitoredElement me = (MonitoredElement) Pool.get(MonitoredElement.typ, test.getMonitoredElementId());
//				AccessPort port = null;
//				for (Iterator it = kis.access_ports.iterator(); it.hasNext();) {
//					AccessPort aport = (AccessPort) it.next();
//					if (me.access_port_id.equals(aport.getId())) {
//						port = aport;
//						break;
//					}
//				}
//				if (port != null)
//					this.port = port.name;
//				this.me = me.getName();
//				TestType testType = (TestType) Pool.get(TestType.typ, test.getTestTypeId());
//				this.testType = testType.getName();
//				this.time = UIStorage.SDF.format(new Date(test.getTimeStamp().getPeriodStart()));
//
//				//this.id = test.id;
//				//this.kis = test.kis;
//				if (test.getStatus().equals(TestStatus.TEST_STATUS_COMPLETED)) {
//					this.statusName = LangModelSchedule.getString("Done"); //$NON-NLS-1$
//				} else if (test.getStatus().equals(TestStatus.TEST_STATUS_SCHEDULED)) {
//					this.statusName = LangModelSchedule.getString("Scheduled"); //$NON-NLS-1$
//				} else if (test.getStatus().equals(TestStatus.TEST_STATUS_PROCESSING)) {
//					this.statusName = LangModelSchedule.getString("Running"); //$NON-NLS-1$
//				} else if (test.getStatus().equals(TestStatus.TEST_STATUS_ABORTED)) {
//					this.statusName = LangModelSchedule.getString("Aborted"); //$NON-NLS-1$
//				} else {
//					this.statusName = LangModelSchedule.getString("Unrecognized"); //$NON-NLS-1$
//				}
//
//			}
//		}
//
//	}

	Dispatcher			dispatcher;
	ObjectResourceTable	listTable;
	ApplicationContext	aContext;
	private JPanel		panel;
	private Test		test;
	java.util.List		rowToRemove;
	private Command		command;

	public TableFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		if (aContext != null)
			initModule(aContext.getDispatcher());
		init();
		this.command = new WindowCommand(this);
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			Test test = tue.test;
			if ((this.test == null) || (!this.test.getId().equals(test.getId()))) {
				java.util.List savedTests = ((SchedulerModel) this.aContext.getApplicationModel()).getTests();
				this.test = test;

				ObjectResourceTableModel tableModel = (ObjectResourceTableModel) this.listTable.getModel();
				int rowIndex = tableModel.getIndexOfObject(test);

				if (rowIndex<0) {
					if (test.isChanged()) {
						tableModel.getContents().add(test);
						this.listTable.repaint();
						this.listTable.revalidate();
					} else
						savedTests.add(test);
				}
				rowIndex = ((ObjectResourceTableModel) this.listTable.getModel()).getIndexOfObject(test);
				this.listTable.setRowSelectionInterval(rowIndex, rowIndex);

			}

		} else if (commandName.equals(SchedulerModel.COMMAND_NAME_ALL_TESTS)) {
			setSavedTests();
		} else if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
			ObjectResourceTableModel model = (ObjectResourceTableModel) this.listTable.getModel();
			model.clear();
			this.listTable.removeAll();
			this.listTable.revalidate();
			this.listTable.repaint();

		}
	}

	public void setSavedTests() {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				ObjectResourceTableModel model = (ObjectResourceTableModel) TableFrame.this.listTable.getModel();
				model.clear();
				TableFrame.this.listTable.removeAll();
				java.util.List tests = ((SchedulerModel) TableFrame.this.aContext.getApplicationModel()).getTests();
				//System.out.println("tests.size:"+tests.size());
				for (Iterator it = tests.iterator(); it.hasNext();) {
					Test test = (Test) it.next();
					if (model.getIndexOfObject(test) < 0) {
						//System.out.println("add test:" + test.getId());						
						model.getContents().add(test);
					}
				}

				java.util.List unsavedTests = ((SchedulerModel) TableFrame.this.aContext.getApplicationModel())
						.getUnsavedTests();
				if (unsavedTests != null) {
					for (Iterator it = unsavedTests.iterator(); it.hasNext();) {
						Test test = (Test) it.next();
						if (model.getIndexOfObject(test) < 0) {
							//System.out.println("add test:" + test.getId());						
							model.getContents().add(test);
						}
					}
				}

				//TableFrame.this.listTable.resort();
				TableFrame.this.listTable.repaint();
				TableFrame.this.listTable.revalidate();
			}
		});
	}

	private JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new BorderLayout());

			this.listTable = new ObjectResourceTable(TestController.getInstance());
			this.listTable.setDefaultTableCellRenderer();
			ListSelectionModel rowSM = this.listTable.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					if (e.getValueIsAdjusting())
						return;

					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (!lsm.isSelectionEmpty()) {
						int selectedRow = lsm.getMinSelectionIndex();

						Test test = (Test) ((ObjectResourceTableModel) TableFrame.this.listTable.getModel())
								.getObject(selectedRow);
						TableFrame.this.dispatcher.notify(new TestUpdateEvent(this, test,
																				TestUpdateEvent.TEST_SELECTED_EVENT));
					}
				}

			});

			this.listTable.addMouseListener(new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					mouseClicked(e);
				}

				public void mouseClicked(MouseEvent evt) {
					final JTable table = ((JTable) evt.getSource());
					//					if (SwingUtilities.isLeftMouseButton(evt)) {
					//						int rowIndex = table.getSelectedRow();
					//						TestTableModel model = (TestTableModel) table.getModel();
					//						TestTableRow line = (TestTableRow)
					// model.getRow(rowIndex);
					//						if (model != null) {
					//							//System.out.println("test:" + line.getTest());
					//							Test test = (Test) line.getObjectResource();
					//							//TableFrame.this.skipTestUpdate = true;
					//							TableFrame.this.dispatcher
					//									.notify(new TestUpdateEvent(this, test,
					// TestUpdateEvent.TEST_SELECTED_EVENT));
					//							//System.out.println("send test:"+test.getId());
					//							//TableFrame.this.skipTestUpdate = false;
					//						}
					//					} else
					if (SwingUtilities.isRightMouseButton(evt)) {
						final int[] rowIndices = table.getSelectedRows();
						if ((rowIndices != null) && (rowIndices.length > 0)) {
							final ObjectResourceTableModel model = (ObjectResourceTableModel) table.getModel();
							JMenuItem deleteTestMenuItem = new JMenuItem(LangModelSchedule.getString("delete_tests")); //$NON-NLS-1$
							deleteTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(ActionEvent e) {

									int temp = JOptionPane
											.showConfirmDialog(Environment.getActiveWindow(), LangModelSchedule
													.getString("AreYouShureDeleteTests"), LangModelSchedule
													.getString("ConfirmDeleting"), JOptionPane.YES_NO_OPTION);
									if (temp == JOptionPane.YES_OPTION) {
										if (TableFrame.this.rowToRemove == null)
											TableFrame.this.rowToRemove = new ArrayList();
										else
											TableFrame.this.rowToRemove.clear();
										for (int i = 0; i < rowIndices.length; i++) {											
											Test test = (Test) model.getObject(rowIndices[i]);
											TableFrame.this.rowToRemove.add(test);
										}
										for (Iterator it = TableFrame.this.rowToRemove.iterator(); it.hasNext();) {
											Test test = (Test) it.next();											
											//test.setDeleted(System.currentTimeMillis());
											TableFrame.this.dispatcher
													.notify(new OperationEvent(test, 0,
																				SchedulerModel.COMMAND_REMOVE_TEST));
											//System.out.println("remove
											// index:"+index+"\ttest:"+test.getId());
											model.getContents().remove(test);
										}
										table.revalidate();
										table.repaint();
									}
								}
							});
							/**
							 * TODO activate deleting  
							 * TODO remove comments when test will be correct
							 *       remove from other panels
							 */
							JPopupMenu popup = new JPopupMenu();
							//popup.add(deleteTestMenuItem);
							//popup.show(table, evt.getX(), evt.getY());
						}
					}

				}
			});
			/**
			 * TODO set custom renderer
			 */
			{
//				//int vColIndex = 0;
//				TestTableCellRenderer tableCellRenderer = new TestTableCellRenderer();
//				for (int vColIndex = 0; vColIndex < tableModel.getColumnCount(); vColIndex++) {
//					TableColumn col = this.listTable.getColumnModel().getColumn(vColIndex);
//					col.setCellRenderer(tableCellRenderer);
//				}
			}
			JTableHeader header = this.listTable.getTableHeader();

			this.panel.add(header, BorderLayout.NORTH);
			this.panel.add(new JScrollPane(this.listTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
											ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		}
		return this.panel;

	}

	private void init() {
		setTitle(LangModelSchedule.getString("Tests_status_and_characters")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.panel = getPanel();
		setContentPane(this.panel);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_NAME_ALL_TESTS);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, TestUpdateEvent.TYPE);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_NAME_ALL_TESTS);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CLEAN);
	}

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}
}