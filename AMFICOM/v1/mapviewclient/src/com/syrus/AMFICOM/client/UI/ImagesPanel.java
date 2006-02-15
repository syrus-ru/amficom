/*-
 * $$Id: ImagesPanel.java,v 1.17 2006/02/15 11:27:49 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.AbstractImageResource;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ImageResourceWrapper;
import com.syrus.AMFICOM.resource.corba.IdlImageResourcePackage.IdlImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2006/02/15 11:27:49 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class ImagesPanel extends JPanel
		implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	public static final String SELECT_IMAGE_RESOURCE = "selectir"; //$NON-NLS-1$
	public static final String SELECT_IMAGE = "select"; //$NON-NLS-1$

	public AbstractImageResource ir = null;

	Dispatcher disp;
	private JPanel imagesPanel;
	private JScrollPane jScrollPane1;

	public ImagesPanel() {
		super(new BorderLayout());

		this.disp = new Dispatcher();

		jbInit();
		init();
		initImages();
	}

	public AbstractImageResource getImageResource() {
		return this.ir;
	}

	void init() {
		this.disp.addPropertyChangeListener(SELECT_IMAGE, this);
		this.disp.addPropertyChangeListener(SELECT_IMAGE_RESOURCE, this);
	}
	
	void deinit() {
		this.disp.removePropertyChangeListener(SELECT_IMAGE, this);
		this.disp.removePropertyChangeListener(SELECT_IMAGE_RESOURCE, this);
		
		Component[] components = this.imagesPanel.getComponents();
		for(int i = 0; i < components.length; i++) {
			Component component = components[i];
			if(component instanceof ImagesPanelLabel) {
				ImagesPanelLabel ipl = (ImagesPanelLabel) component;
				ipl.deinit();
			}
		}
	}
	
	public void initImages() {
		try {
			TypicalCondition condition = new TypicalCondition(
				ImageResourceSort._BITMAP,
				ImageResourceSort._BITMAP,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.IMAGERESOURCE_CODE,
				ImageResourceWrapper.COLUMN_SORT);
			
			Collection bitMaps = StorableObjectPool.getStorableObjectsByCondition(condition, true);

			for (Iterator it = bitMaps.iterator(); it.hasNext(); ) {
				BitmapImageResource bitmapIR = (BitmapImageResource)it.next();
				ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(bitmapIR.getImage()).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
				ImagesPanelLabel ipl = new ImagesPanelLabel(this.disp, icon, bitmapIR);
				this.imagesPanel.add(ipl);
			}

			condition = new TypicalCondition(
					ImageResourceSort._FILE,
					ImageResourceSort._FILE,
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.IMAGERESOURCE_CODE,
					ImageResourceWrapper.COLUMN_SORT);
				
				Collection fileMaps = StorableObjectPool.getStorableObjectsByCondition(condition, true);

				for (Iterator it = fileMaps.iterator(); it.hasNext(); ) {
					FileImageResource fileIR = (FileImageResource)it.next();
					ImageIcon icon = new ImageIcon(
							Toolkit.getDefaultToolkit().createImage(fileIR.getFileName()).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
					ImagesPanelLabel ipl = new ImagesPanelLabel(this.disp, icon, fileIR);
					this.imagesPanel.add(ipl);
				}
		}
		catch (ApplicationException ex) {
			Log.errorMessage(ex);
		}
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if(pce.getPropertyName().equals(SELECT_IMAGE)) {
			ImagesPanelLabel ipl = (ImagesPanelLabel )pce.getNewValue();
			this.ir = ipl.ir;
		}
		else
			if(pce.getPropertyName().equals(SELECT_IMAGE_RESOURCE)) {
				this.ir = (AbstractImageResource )pce.getNewValue();
			}
	}

	public void setImageResource(AbstractImageResource ir) {
		this.disp.firePropertyChange(new PropertyChangeEvent(
				this,
				SELECT_IMAGE_RESOURCE,
				null,
				ir));
	}

	public void showAddImageDialog() {
		JFileChooser chooser = new JFileChooser();
		chooser.addChoosableFileFilter(new ChoosableFileFilter(MapEditorResourceKeys.EXTENSION_GIF, I18N.getString(MapEditorResourceKeys.FILE_CHOOSER_PICTURE)));
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				File file = chooser.getSelectedFile();
				FileInputStream in = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				in.read(data);
				in.close();

				BitmapImageResource bitmapImageResource = BitmapImageResource.createInstance(
						LoginManager.getUserId(),
						file.getAbsolutePath(),
						data);

				try {
					StorableObjectPool.flush(bitmapImageResource, LoginManager.getUserId(), true);
				} catch(ApplicationException e) {
					Log.errorMessage(e);
				}

				this.imagesPanel.add(new ImagesPanelLabel(
						this.disp,
						new ImageIcon(
							Toolkit
							.getDefaultToolkit()
							.createImage(bitmapImageResource.getImage())
							.getScaledInstance(30, 30, Image.SCALE_SMOOTH)),
						bitmapImageResource));
				if (this.disp != null)
					this.disp.firePropertyChange(new PropertyChangeEvent(this, SELECT_IMAGE_RESOURCE, null, bitmapImageResource));
				this.imagesPanel.revalidate();
			} catch(IOException ioe) {
				Log.errorMessage(ioe);
			} catch(CreateObjectException coe) {
				Log.errorMessage(coe);
			}
		}
	}

	private void jbInit() {
		this.imagesPanel = new JPanel(new FlowLayout());
		this.jScrollPane1 = new JScrollPane();

		((FlowLayout)this.imagesPanel.getLayout()).setAlignment(FlowLayout.LEFT);
		this.imagesPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		this.imagesPanel.setBackground(Color.white);
		this.imagesPanel.setAutoscrolls(false);
		this.imagesPanel.setMaximumSize(new Dimension(0, 1300));
		this.imagesPanel.setMinimumSize(new Dimension(0, 100));
		this.imagesPanel.setPreferredSize(new Dimension(0, 100));

		this.jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.jScrollPane1.getViewport().add(this.imagesPanel);
		this.jScrollPane1.setWheelScrollingEnabled(true);
		this.add(this.jScrollPane1, BorderLayout.CENTER);

	}

}
