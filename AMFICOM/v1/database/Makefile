#
# $Id: Makefile,v 1.2 2006/04/26 09:59:46 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := database
ARCHFILE := $(MODULE_NAME).tar.gz

arch:
	tar -czvf $(ARCHFILE) \
		$(shell find -name \*.sql) \
		$(wildcard dbexport/*.expdp) \
		$(wildcard dbexport/*.impdp) \
		Makefile
