
package com.syrus.AMFICOM.client.model;

import com.syrus.util.Shitlet;

/**
 * TODO
 * 
 * @version $Revision: 1.3 $, $Date: 2005/09/08 14:25:57 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
@Shitlet
public class SessionOptionsCommand extends AbstractCommand {
	
	@SuppressWarnings("unused")
	private ApplicationContext	aContext;

	public SessionOptionsCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public void setParameter(	String field,
								Object value) {
		if (field.equals("aContext"))
			setApplicationContext((ApplicationContext) value);
	}

	public void setApplicationContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return (SessionOptionsCommand) super.clone();
	}

	@Override
	public void execute() {
//		SessionInfoDialog sDialog = new SessionInfoDialog(aContext.getSessionInterface());
//
//		sDialog.setModal(true);
//
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		Dimension frameSize = sDialog.getSize();
//		sDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
//
//		sDialog.show();
	}

}
