#
# $Id: Makefile,v 1.4 2005/10/09 12:58:13 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mscharserver_interface
IDL_SOURCES = $(IDLDIR)/mscharserver.idl

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mscharserver/corba
	$(RM) .idl
