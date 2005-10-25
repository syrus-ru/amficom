AKP_DIR := ../akp
TCPCONNECT_DIR := ../tcpconnect
TRC_DIR := ../trc
PTHREAD_DIR := ../../pthreads-win32
NETTEST_DIR := ../../Nettest
PK7600_DIR := $(NETTEST_DIR)/PK7600
QP1640_DIR := $(NETTEST_DIR)/QP1640A

AKP_LIBAR = akp$(LIBAR_SUFFIX)
TCPCONNECT_LIBAR = tcpconnect$(LIBAR_SUFFIX)
TRC_LIBAR = trc$(LIBAR_SUFFIX)
PTHREAD_LIBAR = pthreadVC2$(LIBAR_SUFFIX)
PK7600_LIBARS = pk76otdr$(LIBAR_SUFFIX) mkfile32$(LIBAR_SUFFIX) pkltb32$(LIBAR_SUFFIX) pktfault$(LIBAR_SUFFIX)
QP1640_LIBAR = ont.ut.qpotdr$(LIBAR_SUFFIX)

MODULE := r8
CXX_SOURCES := r8 \
	RTU \
	OTDRController \
	PK7600OTDRController \
	PK7600Management \
	QP1640OTDRController \
	QP1640Management \
	OTAUController \
	MCMTransceiver
INCLUDE_DIRS := $(AKP_DIR) \
	$(TCPCONNECT_DIR) \
	$(TRC_DIR) \
	$(PTHREAD_DIR)/include \
	$(PK7600_DIR)/Include \
	$(QP1640_DIR)/OEMDemo/qpotdr
ADD_LIBARS = $(LIBAR_SOCKET) \
	$(AKP_DIR)/$(AKP_LIBAR) \
	$(TCPCONNECT_DIR)/$(TCPCONNECT_LIBAR) \
	$(TRC_DIR)/$(TRC_LIBAR) \
	$(PTHREAD_DIR)/lib/$(PTHREAD_LIBAR) \
	$(addprefix $(PK7600_DIR)/Libs/,$(PK7600_LIBARS)) \
	$(QP1640_DIR)/OEMDemo/qpotdr/$(QP1640_LIBAR)

include ../tools/Makefile.cpp.inc.mk

.PHONY:	all
all:	$(OUT_EXE)

