########################################################################
# $Id: Makefile.cpp.inc.mk,v 1.2 2005/10/08 17:02:24 arseniy Exp $
# $Author: arseniy $
# Author: Tashoyan Arseniy Feliksovich
# Description: Include this file to your Makefile of any C++ project
########################################################################

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
OBJ_SUFFIX := obj
LIB_SUFFIX := lib
SO_PREFIX := 
SO_SUFFIX := dll

# C++ compiler and options
CXX := cl
CXX_COMPILE_ONLY_FLAG := /c
CXX_OUTFILE_FLAG := /o
CXX_WARN_OFF_FLAG := /w
CXXFLAGS := /nologo /ML /W4 /O2 /D "WIN32" /D "NDEBUG" /D "_MBCS" /D "_LIB"

# Archieve tool and options
AR := lib
AR_OUTFILE_FLAG = /OUT:
ARFLAGS := /nologo

# Linker and options
LD := cl
LD_SHARED_FLAG := /LD
LD_OUTFILE_FLAG := /o
LDFLAGS := /nologo

# Various libraries
LIB_SOCKET := ws2_32.lib

else
ifeq ($(ARCH),Linux)

# Various suffixes and prefixes
OBJ_SUFFIX := o
LIB_SUFFIX := a
SO_PREFIX := lib
SO_SUFFIX := so

# C++ compiler and options
CXX := g++
CXX_COMPILE_ONLY_FLAG = -c
CXX_OUTFILE_FLAG = -o$(SPACE)
CXX_WARN_OFF_FLAG := -Wno-all
CXXFLAGS := -O3 -Wall -W

# Archieve tool and options
AR := ar
AR_OUTFILE_FLAG = $(EMPTY)
ARFLAGS := rcsv

# Linker and options
LD := g++
LD_SHARED_FLAG := -shared
LD_OUTFILE_FLAG := -o$(SPACE)
LDFLAGS :=

else
ifeq ($(ARCH),SunOS)

# Various suffixes and prefixes
OBJ_SUFFIX := o
LIB_SUFFIX := a
SO_PREFIX := lib
SO_SUFFIX := so

# C++ compiler and options
CXX := g++
CXX_COMPILE_ONLY_FLAG = -c
CXX_OUTFILE_FLAG = -o$(SPACE)
CXX_WARN_OFF_FLAG := -Wno-all
CXXFLAGS := -O3 -Wall -W

# Archieve tool and options
AR := ar
AR_OUTFILE_FLAG = $(EMPTY)
ARFLAGS := rcsv

# Linker and options
LD := g++
LD_SHARED_FLAG := -G
LD_OUTFILE_FLAG := -o$(SPACE)
LDFLAGS :=

endif
endif
endif


# Common targets

%.$(OBJ_SUFFIX):	%.cpp
	$(CXX) $(CXX_COMPILE_ONLY_FLAG) $(CXXFLAGS) $< $(CXX_OUTFILE_FLAG)$@
