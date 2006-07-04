/*-
 * $Id: SynchronousWorkerDemo2.java,v 1.1 2006/07/04 15:01:09 saa Exp $
 * 
 * Copyright © 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.util.SynchronousWorker;

/**
 * This demo is intended to show SynchronousWorker within
 * application framework so you can see StatusBar.
 * 
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2006/07/04 15:01:09 $
 * @module
 */
public class SynchronousWorkerDemo2 extends AbstractApplication {
	public SynchronousWorkerDemo2(String applicationName) {
		super(applicationName);
	}
	private static class MMB extends AbstractMainMenuBar {
		public MMB(ApplicationModel applicationModel) {
			super(applicationModel);
			// @todo Auto-generated constructor stub
		}
		@Override
		protected void addMenuItems() {
			// empty
		}
	}

	private static class MTB extends AbstractMainToolBar {
		public MTB() {
			// empty
		}
	}

	@Override
	protected void init() {
		final ApplicationModel aModel = new ApplicationModel(){
			//
		};
		super.aContext.setApplicationModel(aModel);
		super.startMainFrame(new AbstractMainFrame(this.aContext,
				"appTitle",
				new MMB(aModel),
				new MTB())
		{
			{
				setSize(getWidth(), 200);
			}

			@Override
			public void loggedIn() {
				throw new Error("Expected");
			}

			@Override
			public void loggedOut() {
				throw new Error("Expected");
			}
		}, null);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new SynchronousWorker(null, "title", "text", true) {
						@Override
						public Object construct()
								throws Exception {
							Thread.sleep(1000);
							setPercents(90);
							Thread.sleep(1000);
							setTitleAndText("a moment...", "coming soon...");
							Thread.sleep(1000);
							return aModel;
						}
					}.execute();
				} catch (ExecutionException e) {
					// @todo Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {
		new SynchronousWorkerDemo2("test");
	}
}
