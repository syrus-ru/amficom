/*-
 * $Id: TemplateTypeChooser.java,v 1.1 2005/11/16 18:49:04 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.resource.I18N;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/11/16 18:49:04 $
 * @module reportbuilder
 */

public class TemplateTypeChooser extends JDialog {
	
	private static final String WINDOW_NAME = "report.Template.TypeChooser.WindowName";
	private static final String LABLE = "report.Template.TypeChooser.Lable";
	
	
	JList moduleList;
	private static TemplateTypeChooser chooser;
	String moduleName;
	
	public TemplateTypeChooser() {
		this(true, true);
	}
	
	public TemplateTypeChooser(boolean modal, boolean visible) {
		super(Environment.getActiveWindow(), modal);
		init();
		setVisible(visible);
	}
	
	private void init() {
		
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel selectionListLable = new JLabel(I18N.getString(LABLE));
		this.moduleList = new JList();
		
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		int length = ReportModels.values().length;
		String[] localNames = new String[length];
		
		for (int i = 0; i < length; i++) {
			localNames[i] = I18N.getString(ReportModels.values()[i].getName());
		}
		this.moduleList.setListData(localNames);
		
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.insets = new Insets(10,10,10,10);
		pane.add(selectionListLable, gbc);
		
		
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		pane.add(this.moduleList, gbc);
		
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		pane.add(okButton, gbc);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(cancelButton, gbc);
		
		this.moduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.moduleList.setSelectedIndex(0);
				
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final int index = TemplateTypeChooser.this.moduleList.getSelectedIndex();
				TemplateTypeChooser.this.moduleName = ReportModels.values()[index].getName();
				TemplateTypeChooser.this.setVisible(false);		
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TemplateTypeChooser.this.moduleName = null;
				TemplateTypeChooser.this.setVisible(false);		
			}
		});
		
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.pack();
		Rectangle parentBounds = this.getOwner().getBounds();
		int locationX = parentBounds.x + parentBounds.width/2 - this.getWidth()/2;
		int locationY = parentBounds.y + parentBounds.height/2 - this.getHeight()/2; 
		this.setLocation(locationX >= 0 ? locationX : parentBounds.x,
				locationY >= 0 ? locationY : parentBounds.y);
		this.setTitle(I18N.getString(WINDOW_NAME));
	}
	
	public static ReportModel getSelectedModule() {
		int moduleNameIndex = getSelectedModuleIndex();
		return ReportModels.values()[moduleNameIndex].getReportModel();		
	}
	
	public static int getSelectedModuleIndex() {
		return chooser.moduleList.getSelectedIndex();
	}
	
	public static String chooseModule() {
		if(chooser == null) {
			chooser = new TemplateTypeChooser();
		} else {
			chooser.setModal(true);
			chooser.setVisible(true);
		}		
		return chooser.moduleName;
	}

	public static void setType(String destinationModule) {
		if (chooser == null) {
			chooser = new TemplateTypeChooser(false, false);
		}		
		int index = ReportModels.getIndex(destinationModule);
		chooser.moduleList.setSelectedIndex(index);
	}
}
