#
# $Id: Makefile,v 1.13 2004/08/04 12:59:53 arseniy Exp $
#
# vim:set ft=make:
#

MODULE_NAME = general_v1

LIBNAME = $(subst _v1,,$(MODULE_NAME)).jar
ARCHFILE = $(subst _v1,,$(MODULE_NAME)).tar.gz

IDLDIR=../idl_v1/idl

APPCLASSPATH = $(LIBDIR)/util.jar

JAVACFLAGS = -source 1.3
SUNJAVACFLAGS = -target 1.2
JIKESFLAGS = -target 1.2

include ../tools/Makefile.inc
$(call checkenve,JAVA_HOME)
$(call checkenve,ORACLE_HOME)
$(call checkenve,IDL2JAVA)


IDL2JAVA=$(JAVA_HOME)/bin/idlj
IDL2JAVAFLAGS = \
	-fall \
	-i $(IDLDIR) \
	-td $(SRCDIR) \
	-v
#IDL2JAVA=$(JDEV_HOME)/vbroker4/bin/idl2java
#IDL2JAVAFLAGS = \
	-C \
	-H \
	-I$(IDLDIR) \
	-idl_strict \
	-warn_unrecognized_pragmas \
	-warn_all \
	-no_back_compat_mapping \
	-no_boa \
	-no_obj_wrapper \
	-root_dir $(SRCDIR) \
	-no_tie \
	-servant \
	-warn_missing_define \
	-no_bind \
	-no_compile \
	-no_invoke_handler \
	-narrow_compliance \
	-no_Object_methods \
	-strict \
	-no_examples


$(LIBNAME): .mach
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	.corba $(CLASSDIR)
	$(JAVAC) \
		$(JAVACFLAGS) \
		-classpath $(APPCLASSPATH):$(CLASSDIR) \
		$(wildcard $(SRCDIR)/com/syrus/AMFICOM/general/*.java)
	touch $@

.corba:	$(CLASSDIR)
	$(IDL2JAVA) $(IDL2JAVAFLAGS) \
		$(IDLDIR)/general.idl
	$(JAVAC) \
		$(JAVACFLAGS) $(JAVAC_WARNOFF_FLAG) \
		-sourcepath $(SRCDIR) \
		$(SRCDIR)/com/syrus/AMFICOM/general/corba/*.java
	touch $@

$(CLASSDIR):
	mkdir -p $@

.PHONY:	clean
clean:	cleanmach cleancorba
	$(RM) $(LIBNAME)

.PHONY:	cleanmach
cleanmach:
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/*.class
	$(RM) .mach

.PHONY:	cleancorba
cleancorba:
	$(RM) -r $(SRCDIR)/com/syrus/AMFICOM/general/corba
	$(RM) -r $(CLASSDIR)/com/syrus/AMFICOM/general/corba
	$(RM) .corba

#### Archive ####
.PHONY:	arch
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(IDLDIR)/general.idl \
		$(SRCDIR)/com/syrus/AMFICOM/general/*.java
