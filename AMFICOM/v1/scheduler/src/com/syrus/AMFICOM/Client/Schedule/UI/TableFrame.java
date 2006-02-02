/*-
 * $Id: TableFrame.java,v 1.79 2006/02/02 09:06:56 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client.UI.ADefaultTableCellRenderer;
import com.syrus.AMFICOM.client.UI.ProcessingDialog;
import com.syrus.AMFICOM.client.UI.StubLabelCellRenderer;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestView;
import com.syrus.AMFICOM.measurement.TestViewAdapter;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.79 $, $Date: 2006/02/02 09:06:56 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
@SuppressWarnings("serial")
public final class TableFrame extends JInternalFrame implements PropertyChangeListener {
	
	Dispatcher dispatcher;
	SchedulerModel schedulerModel;
	WrapperedTable<TestView> listTable;
	ApplicationContext aContext;
	PropertyChangeEvent propertyChangeEvent;
	ListSelectionEvent	listSelectionEvent;
	
	private JPanel panel;

	public TableFrame(final ApplicationContext aContext) {
		this.aContext = aContext;
		if (aContext != null) {
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
			this.dispatcher = aContext.getDispatcher();
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_ADD_TEST, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REMOVE_TEST, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TESTS, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEST, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_CLEAN, this);
		}
		this.init();
	}

	private void updateTest() {
		final Set<Identifier> selectedTestIds = this.schedulerModel.getSelectedTestIds();
		if (selectedTestIds.isEmpty()) {
			this.listTable.clearSelection();
		} else {
			final Set<TestView> testViews = new HashSet<TestView>(selectedTestIds.size());
			for (final Identifier testId : selectedTestIds) {
				final Test test = TestView.valueOf(testId).getTest();
				final Identifier groupTestId = test.getGroupTestId();
				if (!groupTestId.isVoid()) {
					final Test groupTest = TestView.valueOf(groupTestId).getTest();
					testViews.add(TestView.valueOf(groupTest));
				} else {
					testViews.add(TestView.valueOf(test));
				}
			}
			this.listTable.setSelectedValues(testViews);
		}
	}

	private void updateTests(final Set<Identifier> testIds) {
		this.setTests();
		this.updateTest();
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		if (this.listSelectionEvent != null) {
			return;
		}
		this.propertyChangeEvent = evt;
		final String propertyName = evt.getPropertyName().intern();
		if (propertyName == SchedulerModel.COMMAND_ADD_TEST) {
			this.addTest((Set<Identifier>) evt.getNewValue(), false);
		} else if (propertyName == SchedulerModel.COMMAND_REFRESH_TESTS) {
			final Set<Identifier> testIds = (Set<Identifier>)evt.getNewValue();
			this.updateTests(testIds);		
		} else if (propertyName == SchedulerModel.COMMAND_REFRESH_TEST) {
			this.updateTest();
		} else if (propertyName == SchedulerModel.COMMAND_REMOVE_TEST) {
//			this.updateTests(null);
			final Set<Identifier> testIds = (Set<Identifier>)evt.getNewValue();
			final WrapperedTableModel<TestView> model = this.listTable.getModel();
			model.removeObjects(this.getTestViews(testIds));
		} else if (propertyName == SchedulerModel.COMMAND_CLEAN) {
			this.clearTests();
		}
		this.propertyChangeEvent = null;
	}

	private List<TestView> getTestViews(final Set<Identifier> testIds){
		final List<TestView> testViews = new ArrayList<TestView>(testIds.size());
		for (final Identifier testId : testIds) {
			final TestView testView = TestView.valueOf(testId);
			final Test test = testView.getTest();
			final Identifier groupTestId = test.getGroupTestId();
			if (groupTestId.isVoid() || test.getId().equals(groupTestId)) {
				testViews.add(TestView.valueOf(test));				
			}
		}
		return testViews;
	}
	
	private void addTest(final Set<Identifier> testIds, boolean clear) {
//		final long time0 = System.currentTimeMillis();
		final WrapperedTableModel<TestView> model = this.listTable.getModel();
		final Set<TestView> selectedValues = this.listTable.getSelectedValues();
		if (clear) {
			model.clear();
		}
//		final long time01 = System.currentTimeMillis();
//		assert Log.debugMessage(testIds + ", 01 it takes " + (time01 - time0) + " ms", Log.DEBUGLEVEL03);
		final List<TestView> testViews = this.getTestViews(testIds);
//		final long time02 = System.currentTimeMillis();
//		assert Log.debugMessage(testIds + ", 02 it takes " + (time02 - time0) + " ms", Log.DEBUGLEVEL03);
		
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				model.addObjects(testViews);				
				listTable.setSelectedValues(selectedValues);
			}
		});
		
//		final long time1 = System.currentTimeMillis();
//		assert Log.debugMessage(testIds + ", it takes " + (time1 - time0) + " ms", Log.DEBUGLEVEL03);
	}
	
	private void clearTests() {
		final Set<Identifier> emptySet = Collections.emptySet();
		this.addTest(emptySet, true);
	}
	
	private void setTests() {
		final Set<Identifier> testIds = this.schedulerModel.getMainTestIds();
		final WrapperedTableModel<TestView> model = this.listTable.getModel();
		final List<TestView> values = model.getValues();
		final List<TestView> testViews = this.getTestViews(testIds);
		boolean contains = true;
		for (final TestView view : testViews) {
			contains &= values.contains(view);
		}
		for (final TestView view : values) {
			contains &= testViews.contains(view);
		}

		if (!contains) {
			this.addTest(testIds, true);
		} else {
			this.listTable.getModel().fireTableDataChanged();
		}	
	}

	private JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new BorderLayout());

			this.listTable = new WrapperedTable<TestView>(TestViewAdapter.getInstance(), new String[] {
					TestViewAdapter.KEY_TEMPORAL_TYPE, 
					TestViewAdapter.KEY_MONITORED_ELEMENT,
					TestViewAdapter.KEY_PORT, 
					TestViewAdapter.KEY_MEASUREMENT_TYPE, 
					TestViewAdapter.KEY_START_TIME,
					TestViewAdapter.KEY_STATUS,
					TestViewAdapter.KEY_D,
					TestViewAdapter.KEY_Q});
			this.listTable.setRenderer(StubLabelCellRenderer.getInstance(), TestViewAdapter.KEY_STATUS);
			final SimpleDateFormat simpleDateFormat = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
			this.listTable.setRenderer(
				new ADefaultTableCellRenderer.DateRenderer(simpleDateFormat), 
				TestViewAdapter.KEY_START_TIME);
			this.listTable.setAllowAutoResize(true);
			this.listTable.setAutoscrolls(true);
			final ListSelectionModel rowSM = this.listTable.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(final ListSelectionEvent e) {
					if (e.getValueIsAdjusting() || TableFrame.this.propertyChangeEvent != null) {
						return;
					}
					
					listSelectionEvent = e;
					
					final TestView selectedValue = TableFrame.this.listTable.getSelectedValue();
					if (selectedValue != null) {
						new ProcessingDialog(new Runnable() {

							public void run() {
								TableFrame.this.schedulerModel.unselectTests(TableFrame.this);
								try {
									TableFrame.this.schedulerModel.addSelectedTest(TableFrame.this, 
										selectedValue.getTest());
								} catch (final ApplicationException ae) {
									AbstractMainFrame.showErrorMessage(
										I18N.getString("Scheduler.Error.CannotSelectTest"));
								}
							}
						}, I18N.getString("Common.ProcessingDialog.PlsWait"));
					} else {
						TableFrame.this.schedulerModel.unselectTests(TableFrame.this);
					}
					
					listSelectionEvent = null;
				}

			});

			this.listTable.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent evt) {
					final  WrapperedTable<TestView> table = (WrapperedTable<TestView>) evt.getSource();
					if (SwingUtilities.isRightMouseButton(evt)) {
						
						final int[] rowIndices = table.getSelectedRows();
						if (rowIndices == null || rowIndices.length == 0) {
							return;
						}

						final WrapperedTableModel<TestView> model = table.getModel();

						for (final int index : rowIndices) {
							final TestView testView = model.getObject(index);
							final int status = testView.getTest().getStatus().value();
							if ((!testView.isTestNewer() || 
									status != TestStatus._TEST_STATUS_NEW) &&
								status != TestStatus._TEST_STATUS_PROCESSING &&
								status != TestStatus._TEST_STATUS_SCHEDULED &&
								status != TestStatus._TEST_STATUS_STOPPED) {
								return;
							}
						}

						final JPopupMenu popup = new JPopupMenu();

						boolean enableDeleting = true;
						boolean enableStopping = true;
						boolean enableResuming = true;

						for (int i = 0; i < rowIndices.length; i++) {
							final TestView testView = model.getObject(rowIndices[i]);
							final int status = testView.getTest().getStatus().value();
							if (!testView.isTestNewer() || status != TestStatus._TEST_STATUS_NEW) {
								enableDeleting = false;
							}
							
							if (status != TestStatus._TEST_STATUS_PROCESSING &&
								status != TestStatus._TEST_STATUS_SCHEDULED) {
								enableStopping = false;
							}
							
							if (status != TestStatus._TEST_STATUS_STOPPED) {
								enableResuming = false;
							}

							if (!enableDeleting && !enableStopping && !enableResuming) {
								break;
							}
						}
						
//						// bypass
//						enableDeleting = enableStopping = enableResuming = true;

						if (enableDeleting) {
							final JMenuItem deleteTestMenuItem = new JMenuItem(I18N.getString(rowIndices.length == 1
							? "Scheduler.Text.Table.DeleteTest"
								: "Scheduler.Text.Table.DeleteTests"));
							deleteTestMenuItem.setIcon(UIManager.getIcon(UIStorage.ICON_DELETE));
							
							deleteTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(final ActionEvent e) {
									final int temp = JOptionPane.showConfirmDialog(AbstractMainFrame.getActiveMainFrame(),
											I18N.getString("Scheduler.Text.Table.DeleteTest.ConfirmMessage"),
											I18N.getString("Scheduler.Text.Table.DeleteTest.ConfirmTitle"),
											JOptionPane.YES_NO_OPTION);
									if (temp == JOptionPane.YES_OPTION) {
										TestView[] testViews = new TestView[rowIndices.length];
										for (int i = 0; i < rowIndices.length; i++) {
											testViews[i] = model.getObject(rowIndices[i]);
										}
										for (final TestView testView : testViews) {											
											try {
												TableFrame.this.schedulerModel.removeTest(testView.getTest());
											} catch (final ApplicationException e1) {
												AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.CannotRemoveTest") 
													+ " " 
													+ testView.getTest().getDescription());
												return;
											}
											model.removeObject(testView);
										}
										table.revalidate();
										table.repaint();
									}
								}
							});
							popup.add(deleteTestMenuItem);
							if (enableResuming || enableStopping) {
								popup.addSeparator();
							}
						}
						
						if (enableResuming) {
							final JMenuItem resumeTestingMenuItem = new JMenuItem(I18N.getString("Scheduler.Text.Table.ResumeTesting"));
							resumeTestingMenuItem.setIcon(UIManager.getIcon(UIStorage.ICON_RESUME));
							resumeTestingMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(final ActionEvent e) {
									for (int i = 0; i < rowIndices.length; i++) {
										final TestView testView = model.getObject(rowIndices[i]);
										// XXX add checking available place
										final Test test = testView.getTest();
										final String reason;
										try {
											reason = 
												schedulerModel.isValid(testView.getTest(), 
													new Date(), 
													test.getEndTime());
										} catch (final ApplicationException ae) {
											try {
												AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.CannotResumeTest") 
													+ " " + schedulerModel.getExtendedTestDescription(test));
											} catch (ApplicationException e1) {
												Log.errorMessage(e1);
												AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.CannotResumeTest") 
													+ " " + test.getDescription());
											}
											Log.errorMessage(ae);
											return;
										}
										if (reason != null) {
											AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.CannotResumeTest") 
												+ " " + reason);
											return;
										}
									}
									
									for (int i = 0; i < rowIndices.length; i++) {
										final TestView testView = model.getObject(rowIndices[i]);
										final Test test = testView.getTest();
										test.setStatus(TestStatus.TEST_STATUS_NEW);
									}
									
									TableFrame.this.dispatcher.firePropertyChange(new PropertyChangeEvent(TableFrame.this,
										SchedulerModel.COMMAND_REFRESH_TESTS,
										null,
										null));
									table.revalidate();
									table.repaint();
								}
							});
							popup.add(resumeTestingMenuItem);
						}

						if (enableStopping) {
							final JMenuItem stopTestMenuItem = new JMenuItem(I18N.getString("Scheduler.Text.Table.StopTesting"));
							stopTestMenuItem.setIcon(UIManager.getIcon(UIStorage.ICON_PAUSE));
							stopTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(final ActionEvent e) {
									final Object reason = JOptionPane.showInputDialog(AbstractMainFrame.getActiveMainFrame(),
											I18N.getString("Scheduler.Text.Table.StopTesting.StoppingReason"),
											I18N.getString("Scheduler.Text.Table.StopTesting.Title"),
											JOptionPane.PLAIN_MESSAGE,
											null,
											null,
											null);
									if (reason != null) {
										for (int i = 0; i < rowIndices.length; i++) {
											final Test test = model.getObject(rowIndices[i]).getTest();
											test.setStatus(TestStatus.TEST_STATUS_STOPPING);
											test.addStopping(reason.toString());
										}
										TableFrame.this.dispatcher.firePropertyChange(new PropertyChangeEvent(TableFrame.this,
												SchedulerModel.COMMAND_REFRESH_TESTS,
												null,
												null));
										table.revalidate();
										table.repaint();
									}
								}
							});

							popup.add(stopTestMenuItem);
						}
						popup.show(table, evt.getX(), evt.getY());
					}

				}
			});

			final JTableHeader header = this.listTable.getTableHeader();

			this.listTable.sortColumn(4);

			this.panel.add(header, BorderLayout.NORTH);
			this.panel.add(new JScrollPane(this.listTable,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);

		}
		return this.panel;

	}

	private void init() {
		super.setTitle(I18N.getString("Scheduler.Text.Table.Title")); //$NON-NLS-1$
		super.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
		super.setResizable(true);
		super.setClosable(false);
		super.setIconifiable(true);
		this.panel = getPanel();
		super.setContentPane(this.panel);
	}

}
