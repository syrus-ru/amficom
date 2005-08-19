/*-
 * $Id: ReflectogrammLoadDialog.java,v 1.28 2005/08/19 14:23:55 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Analysis.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterTypeEnum;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.IconPopulatableItem;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeIconLabelCellRenderer;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.logic.Populatable;
import com.syrus.AMFICOM.logic.SelectionListener;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

/**
 * @version $Revision: 1.28 $, $Date: 2005/08/19 14:23:55 $
 * @author $Author: arseniy $
 * @module analysis
 */
public class ReflectogrammLoadDialog extends JDialog {
	private static final long serialVersionUID = 3544388106469259320L;

	JButton okButton;
	Result result;
	int resultCode = JOptionPane.CANCEL_OPTION;
	JScrollPane scrollPane = new JScrollPane();

	private ApplicationContext aContext;
	private Identifier domainId;

	private JButton cancelButton;
	private JButton updateButton = new JButton();

	ResultChildrenFactory childrenFactory;
	PopulatableIconedNode rootItem;
	LogicalTreeUI treeUI;

	public ReflectogrammLoadDialog(final ApplicationContext aContext) {
		super(Environment.getActiveWindow());
		this.aContext = aContext;
		this.domainId = LoginManager.getDomainId();

		this.setModal(true);

		setTitle(LangModelAnalyse.getString("trace"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		{
			final int width = (int) (screenSize.getWidth() / 3);
			final int height = (int) (2 * screenSize.getHeight() / 3);
			this.setSize(width, height);
			this.setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
		}

		this.setResizable(true);

		final JPanel ocPanel = new JPanel();
		ocPanel.setLayout(new FlowLayout());

		this.okButton = new JButton();
		this.okButton.setText(LangModelAnalyse.getString("okButton"));
		this.okButton.setEnabled(false);
		this.okButton.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				ReflectogrammLoadDialog.this.resultCode = JOptionPane.OK_OPTION;
				ReflectogrammLoadDialog.this.dispose();
			}
		});
		this.cancelButton = new JButton();
		this.cancelButton.setText(LangModelAnalyse.getString("cancelButton"));
		this.cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				ReflectogrammLoadDialog.this.resultCode = JOptionPane.CANCEL_OPTION;
				ReflectogrammLoadDialog.this.dispose();

			}
		});

		this.updateButton.setText(LangModelAnalyse.getString("refreshButton"));
		this.updateButton.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {		
					
				// XXX no need to refresh pool? //Stas
				/*try {
						StorableObjectPool.refresh();
					} catch (ApplicationException e1) {
						JOptionPane.showMessageDialog(Environment.getActiveWindow(), e1.getMessage(), LangModelAnalyse.getString("Error"),
								JOptionPane.OK_OPTION);
					}*/
						
//				ReflectogrammLoadDialog.this.setTree();
				final TreePath selectedPath = ReflectogrammLoadDialog.this.treeUI.getTree().getSelectionModel().getSelectionPath();
				final PopulatableIconedNode itemToRefresh = selectedPath != null 
						? (PopulatableIconedNode)selectedPath.getLastPathComponent()
						: ReflectogrammLoadDialog.this.rootItem;
				
				updateRecursively(itemToRefresh);
//					treeUI.getTree().collapsePath(selectedPath != null ? selectedPath : new TreePath(ReflectogrammLoadDialog.this.rootItem));
						
				ReflectogrammLoadDialog.this.setVisible(true);
			}
		});
		ocPanel.add(Box.createHorizontalGlue());
		ocPanel.add(this.okButton);
		ocPanel.add(this.updateButton);
		ocPanel.add(this.cancelButton);
		this.getContentPane().setLayout(new BorderLayout());

		this.getContentPane().add(this.scrollPane, BorderLayout.CENTER);

		this.setTree();

		this.getContentPane().add(ocPanel, BorderLayout.SOUTH);
	}

	void updateRecursively(final Item item) {
		if (item instanceof Populatable) {
			final Populatable populatable = (Populatable) item;
			if (populatable.isPopulated()) {
				populatable.populate();
			}
			for (final Item item2 : item.getChildren()) {
				this.updateRecursively(item2);
			}
		}
	}

	@Override
	public void setVisible(final boolean key) {
		if (!this.domainId.equals(LoginManager.getDomainId())) {
			this.setTree();
		}
		super.setVisible(key);
	}

	void setTree() {
		this.domainId = LoginManager.getDomainId();

//		getContentPane().remove(scrollPane);

		try {
			if (this.rootItem == null) {
				final Domain domain = (Domain)StorableObjectPool.getStorableObject(this.domainId, true);

//				childrenFactory = ArchiveChildrenFactory.getInstance();
				this.childrenFactory = new ResultChildrenFactory();
				this.rootItem = this.childrenFactory.getRoot();
				this.rootItem.setObject(ArchiveChildrenFactory.ROOT);
				this.rootItem.setName(LangModelAnalyse.getString("Archive"));
				this.rootItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
				this.rootItem.setChildrenFactory(this.childrenFactory);
				this.treeUI = new LogicalTreeUI(this.rootItem, false);
				this.treeUI.setRenderer(IconPopulatableItem.class, new ItemTreeIconLabelCellRenderer());
				this.treeUI.getTreeModel().setAllwaysSort(false);
				this.scrollPane.getViewport().add(this.treeUI.getTree(), null);
				this.treeUI.addSelectionListener(new SelectionListener() {

					public void selectedItems(final Collection<Item> items) {
						if (!items.isEmpty()) {
							for (final Item item : items) {
								final Object object = item.getObject();
								if (object instanceof Result) {
									ReflectogrammLoadDialog.this.okButton.setEnabled(true);
									ReflectogrammLoadDialog.this.result = (Result) object;
								} else {
									ReflectogrammLoadDialog.this.okButton.setEnabled(false);
								}
							}
						}

					}
				});

				getContentPane().add(this.scrollPane, BorderLayout.CENTER);
				setTitle(LangModelAnalyse.getString("Choose reflectogram") + " (" + domain.getName() + ")");
			}
			this.rootItem.populate();

		} catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public int showDialog() {
		super.setVisible(true);
		return this.resultCode;
	}

	public Result getResult() {
		return this.result;
	}

	public BellcoreStructure getBellcoreStructure() {
		BellcoreStructure bs = null;

		if (this.result == null) {
			return null;
		}

		final Parameter[] parameters = this.result.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			final Parameter param = parameters[i];
			final ParameterTypeEnum type = param.getType();
			if (type.equals(ParameterTypeEnum.REFLECTOGRAMMA)) {
				bs = new BellcoreReader().getData(param.getValue());
			}
		}

		try {
			if (AnalysisUtil.hasMeasurementByResult(this.result)) {
				final Measurement measurement = AnalysisUtil.getMeasurementByResult(this.result);
				final Test test = (Test) StorableObjectPool.getStorableObject(measurement.getTestId(), true);
				bs.monitoredElementId = test.getMonitoredElement().getId().getIdentifierString();
				bs.title = test.getDescription();
			}
		} catch (ApplicationException ex) {
			// nothing ?
		}
		return bs;
	}
}
