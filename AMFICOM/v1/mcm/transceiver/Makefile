AKP_DIR := ../../../hardware/akp
TCPCONNECT_DIR := ../../../hardware/tcpconnect

AKP_LIBAR = akp$(LIBAR_SUFFIX)
TCPCONNECT_LIBAR = tcpconnect$(LIBAR_SUFFIX)

MODULE := mcmtransceiver
CXX_SOURCES := tcpkisconnection
CXXFLAGS = -fPIC
INCLUDE_DIRS := $(AKP_DIR) \
	$(TCPCONNECT_DIR)
ADD_LIBARS = $(LIBAR_SOCKET) \
	$(AKP_DIR)/$(AKP_LIBAR) \
	$(TCPCONNECT_DIR)/$(TCPCONNECT_LIBAR)
USE_JNI := 1

include ../../tools/Makefile.cpp.inc.mk


.PHONY:	all
all:	$(OUT_SO)

