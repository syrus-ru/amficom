#
# $Id: Makefile,v 1.1 2006/02/14 06:56:14 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME := database
ARCHFILE := $(MODULE_NAME).tar.gz

arch:
	tar -czvf $(ARCHFILE) \
		$(shell find -name \*.sql) \
		Makefile
