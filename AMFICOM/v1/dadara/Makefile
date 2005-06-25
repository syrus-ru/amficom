MODULE_NAME := dadara

LIBNAME := $(MODULE_NAME).jar
ARCHFILE := $(MODULE_NAME).tar.gz

NATIVE_DIR := ./native/common

JAVAC = javac

include ../tools/Makefile.inc

APPCLASSPATH = $(LIBDIR)/util.jar

JAVAH := javah
JAVAH_FLAGS := \
	-classpath $(CLASSDIR) \
	-force

$(call checkenve,JAVA_HOME)

.PHONY:	clean cleanmach arch install

PACKAGES = com/syrus/AMFICOM/analysis com/syrus/AMFICOM/analysis/dadara com/syrus/AMFICOM/analysis/dadara/events

$(LIBNAME):	.mach .native
	$(JAR) -cf $@ \
		-C $(CLASSDIR) com

.mach:	$(CLASSDIR)
	$(JAVAC) $(JAVACFLAGS) \
		-classpath $(APPCLASSPATH) \
		$(foreach dir,$(PACKAGES),$(wildcard $(SRCDIR)/$(dir)/*.java))

	touch $@

.native:
	$(MAKE) -C native

$(CLASSDIR):
	mkdir $@


clean:	cleanmach
	$(RM) $(LIBNAME)
	$(MAKE) -C native $@

cleanmach:
	$(RM) \
		$(foreach dir,$(PACKAGES),$(CLASSDIR)/$(dir)/*.class)
	$(RM) .mach

#### Install ####
.PHONY:	install
install: $(LIBNAME)
	$(CP) $(LIBNAME) $(LIBDIR)

#### Archive ####
arch:
	tar -czvf $(ARCHFILE) \
		Makefile \
		$(foreach dir,$(PACKAGES),$(wildcard $(SRCDIR)/$(dir)/*.java))

