/*
 * $Id: ImagesPanelLabel.java,v 1.2 2005/06/22 13:20:07 krupenn Exp $
 *
 * Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.resource.AbstractImageResource;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $
 * @module commonclient_v1
 */
public class ImagesPanelLabel extends JLabel
		implements PropertyChangeListener {
	public AbstractImageResource ir;
	Dispatcher disp;

	void init() {
		this.disp.addPropertyChangeListener(ImagesPanel.SELECT_IMAGE, this);
		this.disp.addPropertyChangeListener(ImagesPanel.SELECT_IMAGE_RESOURCE, this);
	}
	
	void deinit() {
		this.disp.removePropertyChangeListener(ImagesPanel.SELECT_IMAGE, this);
		this.disp.removePropertyChangeListener(ImagesPanel.SELECT_IMAGE_RESOURCE, this);
	}
	
	public ImagesPanelLabel(Dispatcher disp, ImageIcon myIcon, AbstractImageResource ir) {
		this.disp = disp;
		this.ir = ir;

		init();
		
//		this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.gray, Color.gray ) );
		this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.white, Color.white ) );
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setSize(myIcon.getIconWidth(), myIcon.getIconHeight());
		this.setIcon(myIcon);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(ImagesPanelLabel.this.disp != null)
					ImagesPanelLabel.this.disp.firePropertyChange(
							new PropertyChangeEvent(
									ImagesPanelLabel.this,
									ImagesPanel.SELECT_IMAGE,
									null,
									ImagesPanelLabel.this));
			}
		});
		this.setEnabled(true);
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if(pce.getPropertyName().equals(ImagesPanel.SELECT_IMAGE)) {
			if(pce.getNewValue().equals(this)) {
				this.setBackground(Color.blue);
//			    this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.red ));
				this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.blue, Color.blue ));
			}
			else {
				this.setBackground(Color.white);
//			    this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.gray, Color.gray ) );
				this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.white, Color.white ) );
			}
		}
		else
		if(pce.getPropertyName().equals(ImagesPanel.SELECT_IMAGE_RESOURCE))
			if(pce.getNewValue().equals(this.ir)) {
				this.setBackground(Color.blue);
//			    this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.red ));
				this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.blue, Color.blue ));
			}
			else {
				this.setBackground(Color.white);
//			    this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.gray, Color.gray ) );
				this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.white, Color.white ) );
			}
	}

	public String getToolTipText() {
		return "";//ir.description();
	}

}
