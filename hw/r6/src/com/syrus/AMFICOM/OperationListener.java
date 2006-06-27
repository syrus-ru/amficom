package com.syrus.AMFICOM;

import java.util.EventListener;

public interface OperationListener extends EventListener {
	public void operationPerformed(OperationEvent e);
}
