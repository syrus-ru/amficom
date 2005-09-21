/*-
 * $Id: TableFrame.java,v 1.44 2005/09/21 12:15:00 bob Exp $
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
import java.net.URL;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.UI.ProcessingDialog;
import com.syrus.AMFICOM.client.UI.StubLabelCellRenderer;
import com.syrus.AMFICOM.client.UI.WrapperedTable;
import com.syrus.AMFICOM.client.UI.WrapperedTableModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestController;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;

/**
 * @version $Revision: 1.44 $, $Date: 2005/09/21 12:15:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
@SuppressWarnings("serial")
public final class TableFrame extends JInternalFrame implements PropertyChangeListener {
	
	Dispatcher dispatcher;
	SchedulerModel schedulerModel;
	WrapperedTable<Test> listTable;
	ApplicationContext aContext;
	private JPanel panel;
	
	

	PropertyChangeEvent propertyChangeEvent;
	Icon	deleteIcon;
	Icon	resumeIcon;
	Icon	pauseIcon;

	public TableFrame(final ApplicationContext aContext) {
		this.aContext = aContext;
		if (aContext != null) {
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
			this.dispatcher = aContext.getDispatcher();
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TESTS, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEST, this);
		}
		this.init();
	}

	private void updateTest() {
		final Set<Identifier> selectedTestIds = this.schedulerModel.getSelectedTestIds();
		if (selectedTestIds == null || selectedTestIds.isEmpty()) {
			this.listTable.clearSelection();
		} else {
			try {
				final WrapperedTableModel<Test> tableModel = this.listTable.getModel();
				final Set<Test> tests = this.schedulerModel.getSelectedTests();
				for (final Test selectedTest : tests) {
					Identifier identifier = selectedTest.getId();
					final Identifier groupTestId = selectedTest.getGroupTestId();
					if (!groupTestId.isVoid()) {
						identifier = groupTestId;
					}
					
					for (int i = 0; i < tableModel.getRowCount(); i++) {
						final Test testInTable = tableModel.getObject(i);
						Identifier testId = testInTable.getGroupTestId();
						testId = !testId.isVoid() ? testId : testInTable.getId();
						if (testId.equals(identifier)) {
							this.listTable.setRowSelectionInterval(i, i);
							break;
						}
					}
				}
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(LangModelGeneral.getString("Error.CannotAcquireObject"));
				return;
			}
		}
	}

	private void updateTests() {
		this.setTests();
		this.updateTest();
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		this.propertyChangeEvent = evt;
		final String propertyName = evt.getPropertyName();
		if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TESTS)) {
			this.updateTests();
		} else if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
			this.updateTest();
		}
		this.propertyChangeEvent = null;
	}

	private void setTests() {
		this.listTable.removeAll();
		final WrapperedTableModel<Test> model = this.listTable.getModel();
		model.clear();
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(this.schedulerModel.getTestIds(), true);
			for (final Test test : tests) {
				final Identifier groupTestId = test.getGroupTestId();
				if (!groupTestId.isVoid() && !groupTestId.equals(test)) {
					continue;
				}
				if (model.getIndexOfObject(test) < 0) {
					model.addObject(test);
				}
			}
		} catch (final ApplicationException e) {
			AbstractMainFrame.showErrorMessage(this, e);
		}
		this.listTable.revalidate();
		this.listTable.repaint();
	}

	private JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new BorderLayout());

			this.listTable = new WrapperedTable<Test>(TestController.getInstance(), new String[] {
					TestController.KEY_TEMPORAL_TYPE, TestController.KEY_MONITORED_ELEMENT,
					TestController.KEY_TEST_OBJECT, TestController.KEY_MEASUREMENT_TYPE, TestController.KEY_START_TIME,
					TestController.KEY_STATUS});
			this.listTable.setRenderer(StubLabelCellRenderer.getInstance(), TestController.KEY_STATUS);
			this.listTable.setAllowAutoResize(true);
			this.listTable.setAutoscrolls(true);
			final ListSelectionModel rowSM = this.listTable.getSelectionModel();
			rowSM.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(final ListSelectionEvent e) {
					if (e.getValueIsAdjusting() || TableFrame.this.propertyChangeEvent != null) {
						return;
					}

					final ListSelectionModel lsm = (ListSelectionModel) e.getSource();
					if (!lsm.isSelectionEmpty()) {
						final int selectedRow = lsm.getMinSelectionIndex();
						new ProcessingDialog(new Runnable() {

							public void run() {
								TableFrame.this.schedulerModel.unselectTests();
								final WrapperedTableModel<Test> model = TableFrame.this.listTable.getModel();
								TableFrame.this.schedulerModel.addSelectedTest(model.getObject(selectedRow));
							}
						}, LangModelGeneral.getString("Message.Information.PlsWait"));
					} else {
						TableFrame.this.schedulerModel.unselectTests();
					}
				}

			});

			this.listTable.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent evt) {
					final JTable table = ((JTable) evt.getSource());
					if (SwingUtilities.isRightMouseButton(evt)) {
						
						final int[] rowIndices = table.getSelectedRows();
						if (rowIndices == null || rowIndices.length == 0) {
							return;
						}

						final WrapperedTableModel<Test> model = (WrapperedTableModel<Test>) table.getModel();

						for (final int index : rowIndices) {
							final Test test = model.getObject(index);
							final int status = test.getStatus().value();
							if ((!test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION) || 
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
							final Test test = model.getObject(rowIndices[i]);
							final int status = test.getStatus().value();
							if (!test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION) || status != TestStatus._TEST_STATUS_NEW) {
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
						
//						// TODO bypass
//						enableDeleting = enableStopping = enableResuming = true;

						if (enableDeleting) {
							final JMenuItem deleteTestMenuItem = new JMenuItem(LangModelSchedule.getString(rowIndices.length == 1
									? "Text.Table.DeleteTest"
										: "Text.Table.DeleteTests"));
							if (TableFrame.this.deleteIcon != null) {
								deleteTestMenuItem.setIcon(TableFrame.this.deleteIcon);
							}
							
							deleteTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(final ActionEvent e) {
									final int temp = JOptionPane.showConfirmDialog(Environment.getActiveWindow(),
											LangModelSchedule.getString("Text.Table.DeleteTest.ConfirmMessage"),
											LangModelSchedule.getString("Text.Table.DeleteTest.ConfirmTitle"),
											JOptionPane.YES_NO_OPTION);
									if (temp == JOptionPane.YES_OPTION) {										
										for (int i = 0; i < rowIndices.length; i++) {
											final Test test = model.getObject(rowIndices[i]);
											try {
												TableFrame.this.schedulerModel.removeTest(test);
											} catch (final ApplicationException e1) {
												AbstractMainFrame.showErrorMessage(LangModelSchedule.getString("Error.CannotRemoveTest") 
													+ " " 
													+ test.getDescription());
												return;
											}
											model.removeObject(test);
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
							final JMenuItem resumeTestingMenuItem = new JMenuItem(LangModelSchedule.getString("Text.Table.ResumeTesting"));
							if (TableFrame.this.resumeIcon != null) {
								resumeTestingMenuItem.setIcon(TableFrame.this.resumeIcon);
							}
							resumeTestingMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(final ActionEvent e) {
									for (int i = 0; i < rowIndices.length; i++) {
										final Test test = model.getObject(rowIndices[i]);
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
							final JMenuItem stopTestMenuItem = new JMenuItem(LangModelSchedule.getString("Text.Table.StopTesting"));
							if (TableFrame.this.pauseIcon != null) {
								stopTestMenuItem.setIcon(TableFrame.this.pauseIcon);
							}
							stopTestMenuItem.addActionListener(new ActionListener() {

								public void actionPerformed(final ActionEvent e) {
									final Object reason = JOptionPane.showInputDialog(Environment.getActiveWindow(),
											LangModelSchedule.getString("Text.Table.StopTesting.StoppingReason"),
											LangModelSchedule.getString("Text.Table.StopTesting.Title"),
											JOptionPane.PLAIN_MESSAGE,
											null,
											null,
											null);
									if (reason != null) {
										for (int i = 0; i < rowIndices.length; i++) {
											final Test test = model.getObject(rowIndices[i]);
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

	private Icon createIcons(final String iconUrl) {
		URL resource = TableFrame.class.getClassLoader().getResource(iconUrl);
		if (resource != null) {
			return new ImageIcon(resource);
		}
		return null;
	}
	
	private void init() {
		super.setTitle(LangModelSchedule.getString("Text.Table.Title")); //$NON-NLS-1$
		super.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		super.setResizable(true);
		super.setClosable(false);
		super.setIconifiable(true);
		this.panel = getPanel();
		super.setContentPane(this.panel);
		
		this.deleteIcon = this.createIcons("com/syrus/AMFICOM/Client/Schedule/UI/delete.gif");
		this.resumeIcon = this.createIcons("com/syrus/AMFICOM/Client/Schedule/UI/resume.gif");
		this.pauseIcon = this.createIcons("com/syrus/AMFICOM/Client/Schedule/UI/pause.gif");
	}

}
