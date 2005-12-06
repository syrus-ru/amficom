#
# $Id: Makefile,v 1.32 2005/12/06 13:00:24 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME := util

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

IDL_SOURCES = 

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,JDBCCLASSPATH)
$(call checkenve,XMLBEANSCLASSPATH)

DEPENDENCIES =

EXTCLASSPATH := $(JDBCCLASSPATH):$(XMLBEANSCLASSPATH)
XMLCLASSPATH :=

SOURCES := $(shell find $(SRCDIR) -name '*.java')


$(LIBNAME): .mach
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

.mach: $(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(XMLCLASSPATH):$(EXTCLASSPATH):$(CLASSDIR) \
		$(SOURCES)
	touch $@


.PHONY: clean
clean: cleanmach
	$(RM) $(LIBNAME)

.PHONY: cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES))) \
		.mach

.PHONY: dist
dist: distclean
	$(RM) ../$(MODULE_NAME)-$(MODULE_VERSION).tar*
	cd .. && tar cf $(MODULE_NAME)-$(MODULE_VERSION).tar $(MODULE_NAME)
	bzip2 -9 ../$(MODULE_NAME)-$(MODULE_VERSION).tar


.PHONY: install
install: $(LIBDIR) $(LIBDIR)/$(LIBNAME)

$(LIBDIR)/$(LIBNAME): $(LIBNAME)
	install -m 0644 -p $(LIBNAME) $(LIBDIR)

.PHONY: uninstall
uninstall:
	$(RM) $(LIBDIR)/$(LIBNAME)

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDL_SOURCES) \
		$(SOURCES)

.javadocs: $(JAVADOCDIR)
	$(JAVADOC) \
		$(JAVADOCFLAGS) \
		-link "http://$(JAVADOCHOST)/apidocs/oraclejdbc/" \
		-link "http://$(JAVADOCHOST)/apidocs/junit/" \
		-sourcepath $(SRCDIR):test \
		-classpath "$(APPCLASSPATH):$(JDBCCLASSPATH):$(JUNIT_HOME)/junit.jar" \
		-subpackages "com.syrus.io:com.syrus.util"
	touch $@
