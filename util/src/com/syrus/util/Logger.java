package com.syrus.util;

public interface Logger {

	void debugMessage(String mesg, int debugLevel);

	void debugException(Throwable throwable, int debugLevel);

	void errorMessage(String mesg);

	void errorException(Throwable throwable);
}
