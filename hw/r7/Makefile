AKP_DIR := ../akp
TCPCONNECT_DIR := ../tcpconnect
TRC_DIR := ../trc
PTHREAD_DIR := ../../pthreads-win32
NETTEST_DIR := ../../Nettest
QP1640_DIR := $(NETTEST_DIR)/QP1640A

AKP_LIBAR = akp$(LIBAR_SUFFIX)
TCPCONNECT_LIBAR = tcpconnect$(LIBAR_SUFFIX)
TRC_LIBAR = trc$(LIBAR_SUFFIX)
PTHREAD_LIBAR = pthreadVC2$(LIBAR_SUFFIX)
QP1640_LIBAR = ont.ut.qpotdr$(LIBAR_SUFFIX)

MODULE := r7
CXX_SOURCES := r7 \
	RTUTransceiver \
	OTAUController \
	MCMTransceiver
INCLUDE_DIRS := $(AKP_DIR) \
	$(TCPCONNECT_DIR) \
	$(TRC_DIR) \
	$(PTHREAD_DIR)/include \
	$(QP1640_DIR)/OEMDemo/qpotdr
ADD_LIBARS = $(LIBAR_SOCKET) \
	$(AKP_DIR)/$(AKP_LIBAR) \
	$(TCPCONNECT_DIR)/$(TCPCONNECT_LIBAR) \
	$(TRC_DIR)/$(TRC_LIBAR) \
	$(PTHREAD_DIR)/lib/$(PTHREAD_LIBAR) \
	$(QP1640_DIR)/OEMDemo/qpotdr/$(QP1640_LIBAR)

include ../tools/Makefile.cpp.inc.mk

.PHONY:	all
all:	$(OUT_EXE)

