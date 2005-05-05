/*-
 * $Id: ReflectogrammLoadDialog.java,v 1.16 2005/05/05 12:40:44 bob Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Analysis.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.logic.IconPopulatableItem;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeIconLabelCellRenderer;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.logic.PopulatableItem;
import com.syrus.AMFICOM.logic.SelectionListener;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;

/**
 * @version $Revision: 1.16 $, $Date: 2005/05/05 12:40:44 $
 * @author $Author: bob $
 * @module analysis_v1
 */
public class ReflectogrammLoadDialog extends JDialog {

	JButton						okButton;
	Result						result;
	int							resultCode		= JOptionPane.CANCEL_OPTION;
	JScrollPane					scrollPane		= new JScrollPane();

	private ApplicationContext	aContext;
	private Identifier			domainId;

	private JButton				cancelButton;
	private JButton				updateButton	= new JButton();
	
	private PopulatableItem rootItem;

	public ReflectogrammLoadDialog(ApplicationContext aContext) {
		super(Environment.getActiveWindow());
		this.aContext = aContext;
		this.domainId = LoginManager.getDomainId();
//		this.domainId = new Identifier(
//										((RISDSessionInfo) aContext.getSessionInterface()).getAccessIdentifier().domain_id);

		this.setModal(true);

		setTitle(LangModelAnalyse.getString("trace"));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		{
			int width = (int) (screenSize.getWidth() / 3);
			int height = (int) (2 * screenSize.getHeight() / 3);
			this.setSize(width, height);
			setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
		}

		this.setResizable(true);

		JPanel ocPanel = new JPanel();
		ocPanel.setLayout(new FlowLayout());

		this.okButton = new JButton();
		this.okButton.setText(LangModelAnalyse.getString("okButton"));
		this.okButton.setEnabled(false);
		this.okButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ReflectogrammLoadDialog.this.resultCode = JOptionPane.OK_OPTION;
				ReflectogrammLoadDialog.this.dispose();
			}
		});
		this.cancelButton = new JButton();
		this.cancelButton.setText(LangModelAnalyse.getString("cancelButton"));
		this.cancelButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ReflectogrammLoadDialog.this.resultCode = JOptionPane.CANCEL_OPTION;
				ReflectogrammLoadDialog.this.dispose();

			}
		});

		this.updateButton.setText(LangModelAnalyse.getString("refreshButton"));
		this.updateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {		
				try {
					MeasurementStorableObjectPool.refresh();
				} catch (ApplicationException e1) {
					JOptionPane.showMessageDialog((Component) e.getSource(), e1.getMessage(), LangModelAnalyse.getString("Error"),
						JOptionPane.OK_OPTION);
				}
				ReflectogrammLoadDialog.this.setTree();
				ReflectogrammLoadDialog.this.okButton.setEnabled(false);
				ReflectogrammLoadDialog.this.show();
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

	public void setVisible(boolean key) {
		if (!domainId.equals(LoginManager.getDomainId()))
			setTree();
		super.setVisible(key);
	}

	public void show() {
		if (!this.domainId.equals(LoginManager.getDomainId()))
			setTree();
		super.show();
	}

	void setTree() {
		this.domainId = LoginManager.getDomainId();

		
//		getContentPane().remove(scrollPane);
	
			
		try {
			if (this.rootItem == null) {
				Domain domain = (Domain) AdministrationStorableObjectPool.getStorableObject(this.domainId, true);

				ArchiveChildrenFactory childrenFactory = ArchiveChildrenFactory.getInstance();
				childrenFactory.setDomainId(this.domainId);
				this.rootItem = new PopulatableItem();
				this.rootItem.setObject(ArchiveChildrenFactory.ROOT);
				this.rootItem.setName(LangModelAnalyse.getString("Archive"));
				this.rootItem.setChildrenFactory(childrenFactory);
				LogicalTreeUI treeUI = new LogicalTreeUI(this.rootItem, false);
				treeUI.setRenderer(IconPopulatableItem.class, new ItemTreeIconLabelCellRenderer());
				treeUI.getTreeModel().setAllwaysSort(false);
				this.scrollPane.getViewport().add(treeUI.getTree(), null);
				treeUI.addSelectionListener(new SelectionListener() {

					public void selectedItems(Collection items) {
						if (!items.isEmpty()) {
							for (Iterator it = items.iterator(); it.hasNext();) {
								Item item = (Item) it.next();
								Object object = item.getObject();
								if (object instanceof Result) {
									okButton.setEnabled(true);
									result = (Result) object;
								} else {
									okButton.setEnabled(false);
								}
							}
						}

					}
				});

				getContentPane().add(this.scrollPane, BorderLayout.CENTER);
				setTitle(LangModelAnalyse.getString("Choose reflectogram") + " (" + domain.getName() + ")");
			}
			this.rootItem.removeAllChildren();
			this.rootItem.populate();

		} catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	public int showDialog() {
		super.show();
		return this.resultCode;
	}

	public Result getResult() {
		return this.result;
	}

	public BellcoreStructure getBellcoreStructure() {
		BellcoreStructure bs = null;

		if (this.result == null)
			return null;

		SetParameter[] parameters = this.result.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			SetParameter param = parameters[i];
			ParameterType type = (ParameterType) param.getType();
			if (type.getCodename().equals(ParameterTypeCodenames.REFLECTOGRAMMA))
				bs = new BellcoreReader().getData(param.getValue());
		}

		try {
			if (this.result.getSort().equals(ResultSort.RESULT_SORT_MEASUREMENT)) {
				Measurement measurement = (Measurement) this.result.getAction();
				Test test = (Test) MeasurementStorableObjectPool.getStorableObject(measurement.getTestId(), true);
				bs.monitoredElementId = test.getMonitoredElement().getId().getIdentifierString();
				bs.title = test.getDescription();
			}
		} catch (ApplicationException ex) {
			// nothing ?
		}
		return bs;
	}
}
