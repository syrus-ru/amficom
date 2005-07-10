#
# $Id: Makefile,v 1.3 2005/07/10 13:41:30 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := mscharserver_interface
IDL_SOURCES = $(IDLDIR)/mscharserver.idl

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

.PHONY: idlclean
idlclean:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/mscharserver/corba
	$(RM) .idl
