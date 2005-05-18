/*
 * $Id: MeasurementTypePane.java,v 1.3 2005/05/18 14:59:43 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Configure.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.PopupNameFrame;
import com.syrus.AMFICOM.client_.general.ui_.ObjectResourcePropertiesPane;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/18 14:59:43 $
 * @module schemeclient_v1
 */

public class MeasurementTypePane  extends JPanel implements ObjectResourcePropertiesPane {
	private ApplicationContext aContext;
	protected MeasurementType measurementType;
	private static ObjectResourcePropertiesPane instance;
	
	private MeasurementTypeGeneralPanel gPanel = new MeasurementTypeGeneralPanel();
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	protected MeasurementTypePane() {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected MeasurementTypePane(MeasurementType type) {
		this();
		setObject(type);
	}

	public static ObjectResourcePropertiesPane getInstance() {
		if (instance == null)
			instance = new MeasurementTypePane();
		return instance;
	}
	
	private void jbInit() throws Exception {
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setTabPlacement(SwingConstants.TOP);
		tabbedPane.add(gPanel.getName(), gPanel);
	}
	
	public Object getObject() {
		return measurementType;
	}

	public void setObject(Object or) {
		this.measurementType = (MeasurementType)or;
		gPanel.setObject(measurementType);
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		gPanel.setContext(aContext);
	}

	public boolean modify() {
		if (gPanel.modify())
			return true;
		return false;
	}

	public boolean save() {
		if (modify()) {
			try {
				StorableObjectPool.putStorableObject(measurementType);
				MeasurementStorableObjectPool.flush(true);
				return true;
			} catch (ApplicationException ex) {
				ex.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				LangModelConfig.getString("err_incorrect_data_input"));
		return false;
	}

	public boolean open() {
		return false;
	}

	public boolean cancel() {
		return false;
	}

	public boolean delete() {
		return false;
	}

	public boolean create() {
		PopupNameFrame dialog = new PopupNameFrame(Environment.getActiveWindow(), "Новый тип");
		dialog.setSize(dialog.preferredSize);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((screenSize.width - dialog.getPreferredSize().width) / 2,
											 (screenSize.height - dialog.getPreferredSize().height) / 2);
		dialog.setVisible(true);

		if (dialog.getStatus() == PopupNameFrame.OK && !dialog.getName().equals("")) {
			String name = dialog.getName();
			Identifier user_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).getAccessIdentifier().user_id);
			try {
				MeasurementType new_type = MeasurementType.createInstance(
						user_id,
						name,
						name,
						new HashSet(),
						new HashSet(),
						new HashSet());

				setObject(new_type);
				return true;
			}
			catch (CreateObjectException ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}
}
