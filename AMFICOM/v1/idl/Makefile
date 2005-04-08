#
# $Id: Makefile,v 1.23 2005/04/08 08:42:24 bass Exp $
#
# vim:ft=make:
#

MODULE_NAME = idl_v1

LIBNAME = corba.jar

JAVACFLAGS = -source 1.4
SUNJAVACFLAGS = -target 1.4
JIKESFLAGS = -target 1.4.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)

.PHONY: export
export: $(LIBNAME)

.PHONY: all
all: .all

.all: .idl
	$(JAVAC) \
		$(JAVACFLAGS) \
		$(JAVAC_WARNOFF_FLAG) \
		-classpath $(AMFICOMCLASSPATH)$(CLASSDIR) \
		-sourcepath $(SRCDIR) \
	 	$(shell find $(SRCDIR) -name '*.java')
	touch $@

# Target created for IDEs compiling everything from scratch not to tie client
# code to server implementations.
.PHONY: xtra
xtra: .xtra

.xtra: .idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/arserver.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/event.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/configuration.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/measurement.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/cmserver.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/mshserver.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/map.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/scheme.idl
	if [ -f ../mcm_v1/idl/mcm.idl ]; then $(IDLJ) $(IDLJFLAGS) ../mcm_v1/idl/mcm.idl; fi
	if [ -f ../mserver_v1/idl/mserver.idl ]; then $(IDLJ) $(IDLJFLAGS) ../mserver_v1/idl/mserver.idl; fi
	touch $@

.PHONY: idl
idl: .idl

.idl: $(SRCDIR) $(CLASSDIR)
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/AMFICOM.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/admin.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/alarm.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/client.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/constants.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/general.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/report.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/resource.idl
	$(IDLJ) $(IDLJFLAGS) $(IDLDIR)/administration.idl

	touch $@

.PHONY: idlclean
idlclean:
	$(RM) -r \
		$(SRCDIR)/* \
		.idl

.PHONY: xtraclean
xtraclean: idlclean
	$(RM) -r \
		.xtra

$(SRCDIR) $(CLASSDIR):
	mkdir -p $@

.PHONY: clean
clean: xtraclean
	$(RM) \
		.all

.PHONY: dist
dist: distclean
	$(RM) ../$(MODULE_NAME)-$(MODULE_VERSION).tar*
	cd .. && tar cf $(MODULE_NAME)-$(MODULE_VERSION).tar $(MODULE_NAME)
	bzip2 -9 ../$(MODULE_NAME)-$(MODULE_VERSION).tar

.PHONY: distclean
distclean: clean
	$(RM) -r \
		$(SRCDIR) \
		$(CLASSDIR) \
		*.jar

$(LIBNAME): .all
	$(JAR) cf $@ \
		-C $(CLASSDIR) com
