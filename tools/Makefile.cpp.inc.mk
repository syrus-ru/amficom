########################################################################################
# $Id: Makefile.cpp.inc.mk,v 1.11 2006/03/07 09:45:36 arseniy Exp $
# $Author: arseniy $
# Author: Tashoyan Arseniy Feliksovich
# Description: Include this file to your Makefile of any C++ project
# Operating system suggested: Any Unix or Windows+Cygwin
# Software required:
#  Windows:
#   Cygwin
#   GNU Make
#   Microsoft Visual Studio -- compiler, linker, library tool, includes and libraries
#  Unix:
#   GNU Make
#   GNU Binutils
#   GNU GCC
#########################################################################################

EMPTY :=
SPACE := $(EMPTY) $(EMPTY)

# Detect arch if not set
ifeq ($(ARCH),)
ifeq ($(OS), Windows_NT)
ARCH := WinNT
else
ARCH := $(shell uname -s)
endif
endif


# Setup common variables

ifeq ($(ARCH),WinNT)

# Various suffixes and prefixes
OBJ_SUFFIX := .obj
LIBAR_SUFFIX := .lib
SO_PREFIX := 
SO_SUFFIX := .dll
EXE_SUFFIX := .exe

# C++ compiler and options
CXX := cl
CXX_INCLUDE_DIR_FLAG := /I$(SPACE)
CXX_COMPILE_ONLY_FLAG := /c
CXX_OUTFILE_FLAG := /o
CXX_WARN_OFF_FLAG := /w
CXXFLAGS := /nologo /ML /W3 /O2 /D "WIN32" /D "NDEBUG" /D "_MBCS" /D "_LIB" /D "_CONSOLE" /Ze /GX $(CXXFLAGS)
# /O1 /Gy /Gf

# Library archive tool and options
LIBAR := lib
LIBAR_OUTFILE_FLAG := /OUT:
LIBAR_FLAGS := /nologo
ARFLAGS := $(LIBAR_FLAGS)

# Linker and options
LD := link
LD_SHARED_FLAG := /DLL
LD_LIBDIR_FLAG := /LIBPATH:
LD_OUTFILE_FLAG := /OUT:
LDFLAGS := /NOLOGO /INCREMENTAL:NO /OPT:REF
#/DEBUG /PDB:$(MODULE).pdb

# JNI
JNI_INCLUDE_PATH := $(JAVA_HOME)/include $(JAVA_HOME)/include/win32

# Various libraries
LIBAR_SOCKET := ws2_32.lib

else
ifeq ($(ARCH),Linux)

# Various suffixes and prefixes
OBJ_SUFFIX := .o
LIBAR_SUFFIX := .a
SO_PREFIX := lib
SO_SUFFIX := .so
EXE_SUFFIX :=

# C++ compiler and options
CXX := g++
CXX_INCLUDE_DIR_FLAG := -I
CXX_COMPILE_ONLY_FLAG = -c
CXX_OUTFILE_FLAG = -o$(SPACE)
CXX_WARN_OFF_FLAG := -Wno-all
CXXFLAGS := -O3 -Wall -W $(CXXFLAGS)

# Library archive tool and options
LIBAR := ar
LIBAR_OUTFILE_FLAG = $(EMPTY)
LIBAR_FLAGS := rcsv
ARFLAGS := $(LIBAR_FLAGS)

# Linker and options
LD := g++
LD_SHARED_FLAG := -shared
LD_LIBDIR_FLAG := -L
LD_OUTFILE_FLAG := -o$(SPACE)
LDFLAGS :=

# JNI
JNI_INCLUDE_PATH := $(JAVA_HOME)/include $(JAVA_HOME)/include/linux

else
ifeq ($(ARCH),SunOS)

# Various suffixes and prefixes
OBJ_SUFFIX := .o
LIBAR_SUFFIX := .a
SO_PREFIX := lib
SO_SUFFIX := .so
EXE_SUFFIX :=

# C++ compiler and options
CXX := g++
CXX_INCLUDE_DIR_FLAG := -I
CXX_COMPILE_ONLY_FLAG = -c
CXX_OUTFILE_FLAG = -o$(SPACE)
CXX_WARN_OFF_FLAG := -Wno-all
CXXFLAGS := -O3 -Wall -W $(CXXFLAGS)

# Library archive tool and options
LIBAR := ar
LIBAR_OUTFILE_FLAG = $(EMPTY)
LIBAR_FLAGS := rcsv
ARFLAGS := $(LIBAR_FLAGS)

# Linker and options
LD := g++
LD_SHARED_FLAG := -G
LD_LIBDIR_FLAG := -L
LD_OUTFILE_FLAG := -o$(SPACE)
LDFLAGS :=

# JNI
JNI_INCLUDE_PATH := $(JAVA_HOME)/include $(JAVA_HOME)/include/solaris

endif
endif
endif


# Common variables. User can define variables:
#  MODULE (*)
#  CXX_SOURCES (*)
#  INCLUDE_DIRS
#  ADD_LIBARS
#  USE_JNI

ifeq ($(MODULE),)
$(error Variable MODULE is undefined)
endif

ifeq ($(CXX_SOURCES),)
$(error Variable CXX_SOURCES is undefined)
endif

OBJS := $(addsuffix $(OBJ_SUFFIX),$(CXX_SOURCES))

OUT_LIBAR := $(MODULE)$(LIBAR_SUFFIX)
OUT_SO := $(SO_PREFIX)$(MODULE)$(SO_SUFFIX)
OUT_EXE := $(MODULE)$(EXE_SUFFIX)

ARCHIVE_FILE := $(MODULE).tar.gz

ifneq ($(INCLUDE_DIRS),)
CXX_INCLUDE_PATHS := $(addprefix $(CXX_INCLUDE_DIR_FLAG),$(INCLUDE_DIRS))
endif

ifneq ($(USE_JNI),)
CXX_INCLUDE_PATHS := $(CXX_INCLUDE_PATHS) $(addprefix $(CXX_INCLUDE_DIR_FLAG),$(JNI_INCLUDE_PATH))
endif


# Common targets

.PHONY:	default
default:	all

%$(OBJ_SUFFIX):	%.cpp
	$(CXX) $(CXX_COMPILE_ONLY_FLAG) $(CXXFLAGS) $(CXX_INCLUDE_PATHS) $< $(CXX_OUTFILE_FLAG)$@

$(OUT_LIBAR):	$(OBJS)
	$(LIBAR) $(LIBAR_FLAGS) $(LIBAR_OUTFILE_FLAG)$@ $(OBJS)

$(OUT_SO):	$(OBJS)
	$(LD) $(LD_SHARED_FLAG) $(LDFLAGS) $(OBJS) $(ADD_LIBARS) $(LD_OUTFILE_FLAG)$@

$(OUT_EXE):	$(OBJS)
	$(LD) $(LDFLAGS) $(OBJS) $(ADD_LIBARS) $(LD_OUTFILE_FLAG)$@

.PHONY:	clean
clean:
	$(RM) $(OUT_LIBAR) $(OUT_SO) $(OUT_EXE) $(OBJS)

.PHONY:	arch
arch:	$(ARCHIVE_FILE)
$(ARCHIVE_FILE):	$(OBJS)	Makefile
	tar -czvf $@ *.cpp *.h Makefile
