#
# $Id: Makefile,v 1.31 2005/10/12 10:03:33 bass Exp $
#
# vim:set ft=make:
#

MODULE_NAME = util

LIBNAME = util.jar
ARCHFILE := $(MODULE_NAME).tar.gz

include ../tools/Makefile.java.inc.mk
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)

DEPENDENCIES =

SOURCES := $(shell find $(SRCDIR) -name '*.java')


.PHONY: export
export: $(LIBNAME) $(MODULE_NAME)client.jar $(MODULE_NAME)server.jar

.mach:
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath "$(APPCLASSPATH):$(JDBCCLASSPATH):$(CLASSDIR)" \
	 	$(SOURCES)
	touch $@

.PHONY:	cleanmach
cleanmach:
	$(RM) $(subst $(SRCDIR),$(CLASSDIR),$(subst .java,*.class,$(SOURCES))) \
		.mach

.PHONY:	clean
clean: cleanmach
	$(RM) $(LIBNAME)

.PHONY: dist
dist: distclean
	$(RM) ../$(MODULE_NAME)-$(MODULE_VERSION).tar*
	cd .. && tar cf $(MODULE_NAME)-$(MODULE_VERSION).tar $(MODULE_NAME)
	bzip2 -9 ../$(MODULE_NAME)-$(MODULE_VERSION).tar

$(LIBNAME): .mach
	$(JAR) cf $@ \
		-C $(CLASSDIR) com

install: $(LIBNAME) $(LIBDIR)
	$(CP) $^

#### Archive ####
.PHONY: arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
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
