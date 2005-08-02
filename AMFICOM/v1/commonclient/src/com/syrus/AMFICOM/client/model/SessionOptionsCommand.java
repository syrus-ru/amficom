
package com.syrus.AMFICOM.client.model;

/**
 * TODO
 * 
 * @version $Revision: 1.2 $, $Date: 2005/08/02 13:03:22 $
 * @author $Author: arseniy $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public class SessionOptionsCommand extends AbstractCommand {

	private ApplicationContext	aContext;

	public SessionOptionsCommand(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public void setParameter(	String field,
								Object value) {
		if (field.equals("aContext"))
			setApplicationContext((ApplicationContext) value);
	}

	public void setApplicationContext(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public Object clone() {
		return new SessionOptionsCommand(this.aContext);
	}

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
