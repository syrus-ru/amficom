package com.syrus.util;

public interface Logger {

	void debugMessage(String mesg, int debugLevel);

	void debugException(Exception exception, int debugLevel);

	void errorMessage(String mesg);

	void errorException(Exception exception);
}
