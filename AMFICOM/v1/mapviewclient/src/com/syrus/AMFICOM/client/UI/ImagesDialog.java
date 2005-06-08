/*
 * $Id: ImagesDialog.java,v 1.1 2005/06/08 13:44:06 krupenn Exp $
 *
 * Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.resource.BitmapImageResource;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $
 * @module commonclient_v1
 */
public class ImagesDialog extends JDialog {
	public static final int RET_CANCEL = 2;

	public static final int RET_OK = 1;

	ApplicationContext aContext;

	ImagesPanel imagesPanel;

	private BorderLayout borderLayout1 = new BorderLayout();

	public int retCode = 0;

	public ImagesDialog(ApplicationContext aContext) {
		this(aContext, null, "", false);
	}

	public ImagesDialog(
			ApplicationContext aContext,
			Frame parent,
			String title,
			boolean modal) {
		super(parent, title, modal);
		this.aContext = aContext;
		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.imagesPanel = new ImagesPanel(this.aContext);
		this.setSize(new Dimension(400, 300));
		this.setResizable(false);
		this.getContentPane().setLayout(this.borderLayout1);
		this.getContentPane().add(this.imagesPanel, BorderLayout.CENTER);

		this.imagesPanel.chooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImagesDialog.this.retCode = ImagesDialog.RET_OK;
				ImagesDialog.this.imagesPanel.deinit();
				ImagesDialog.this.dispose();
			}
		});
		this.imagesPanel.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImagesDialog.this.retCode = ImagesDialog.RET_CANCEL;
				ImagesDialog.this.imagesPanel.deinit();
				ImagesDialog.this.dispose();
			}
		});
	}

	public BitmapImageResource getImageResource() {
		return this.imagesPanel.getImageResource();
	}

	public void setImageResource(BitmapImageResource ir) {
		this.imagesPanel.setImageResource(ir);
	}
}
