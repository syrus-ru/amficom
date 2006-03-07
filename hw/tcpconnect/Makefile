AKP_DIR := ../akp
AKP_LIBAR = akp$(LIBAR_SUFFIX)

MODULE := tcpconnect
CXX_SOURCES := tcpconnect \
	connectX \
	connectWin \
	akptcp \
	SocketReader \
	SocketWriter \
	SocketConnectionAcceptor
CXXFLAGS = -fPIC
INCLUDE_DIRS := $(AKP_DIR)
ADD_LIBARS = $(LIBAR_SOCKET) $(AKP_DIR)/$(AKP_LIBAR)

include ../tools/Makefile.cpp.inc.mk


.PHONY:	all
all:	$(OUT_LIBAR)

