#
# Common definations for make GOLP and applications.
#
# WXQ@Xidian , 2013

SHELL = /bin/sh

CC  = g++
  # warning all, default optimize
CFLAGS = -Wall -O1 -c -fmessage-length=0
AR  = ar
AROPT= -crus
LD  = g++
CP  = cp -f
RM = rm -rf
RANLIB = ranlib

ECHO = /bin/echo
ECHOF = -n
STRIP   = strip
POSTN_AR = .a
SLLEXT=$(POSTN_AR)
POSTN_SO = .so
DLLEXT=$(POSTN_SO)

#   Shared object library flag
SOFLAG  = -shared -fPIC 
DLLLDOPT = $(SOFLAG)



# TODO: 修改 TUXEDO 的头文件目录
TUXDIR:=$(if $(TUXDIR), $(TUXDIR),/cygdrive/u/MiddleWare/Tuxedo8.1_redflag)
TUXLIB=-L$(TUXDIR)/lib -ltux -lwsc -lfml32 -lengine
# -lwsc -lbuft -lwsc  -lgpnet  -lfml -lfml32 -lengine

#  libs needed at link time
LKLIBS  = $(SOCKLIB) $(DEFLIBS)
LIBGOLP = golp
LIBGOLP_DIR=lib$(LIBGOLP)
LIBGOLP_AR=lib$(LIBGOLP)$(POSTN_AR)
LIBGOLP_SO=lib$(LIBGOLP)$(POSTN_SO)

# lib for logging system

LIBLOG = pclog
LIBLOG_DIR=lib$(LIBLOG)
LIBLOG_AR=lib$(LIBLOG)$(POSTN_AR)
LIBLOG_SO=lib$(LIBLOG)$(POSTN_SO)

# lib for trade-engine
LIBGOLPTP = golptp
LIBGOLPTP_DIR=lib$(LIBGOLPTP)
LIBGOLPTP_AR = lib$(LIBGOLPTP)$(POSTN_AR)
LIBGOLPTP_SO = lib$(LIBGOLPTP)$(POSTN_SO)

# TODO: change RUNROOT to root-directory for deploy
RUNROOT=../..

TOPDIR=../..
  # the top-dir of source codes
SRCTOP=$(TOPDIR)/src

  # dir for store object files for compiler
TO_OBJ=.
  # dir for store libraries for linker
TO_LIB=../lib
  # dir for store executable file for linker
TO_BIN=../bin

  # dir for released libraries
INSTLIBDIR=$(RUNROOT)/lib
  # dir for released executable file
INSTBINDIR=$(RUNROOT)/bin

# dir for all headers
  # public interface
INC_PUB=$(SRCTOP)/incPUB
  # GOLP internal headers
INC_GOLP=$(SRCTOP)/incSELF
# testor app. headers
INC_GOLPAPP=$(SRCTOP)/incTEST
SYSINC  = -I$(INC_PUB) -I$(INC_GOLP) -I$(INC_GOLPAPP) -I$(TUXDIR)/include

#BDDEF = -D BUILDDATE=\"\\nBUILD=`date +\\"%Y/%m/%d %H:%M:%S\\"`\\n\"
_BD=`date +"%Y/%m/%d %H:%M:%S"`
BDDEF = -D BUILDDATE="\"\\nBUILD=$(_BD)\\n\""

##########################################################
#
#   Server parts
#
##########################################################

ORAHOME=$(ORACLE_HOME)
# for application based on GOLP
SYSTYPE =
#ORALIBS  = -L$(INSTLIBDIR) -locilib -L$(ORAHOME)/lib -lclntsh -ldl -lm

# for GOLP build
SYSTYPE = -DGOLPSVR -DHAS_ORACLE=1

# TODO: in linux+oracle, use me
#ORALIBS  = -L$(TO_LIB) -locilib -L$(ORAHOME)/lib -lclntsh -ldl -lm
ORALIBS  = -L$(TO_LIB) -locilib  -ldl -lm

# for compiler
SYSDEF = $(SYSINC) $(SYSTYPE) $(BDDEF)

# for linker with trade engine
#SYSLIB = -L$(TO_LIB) -l$(LIBGOLP) -l$(LIBLOG) -l$(LIBGOLPTP) $(ORALIBS) $(TUXLIB) -dl -lpthead -lm -lc
# for linker without trade engine
SYSLIB = -L$(TO_LIB) -l$(LIBGOLP) -l$(LIBLOG) $(ORALIBS) $(TUXLIB) -dl -lpthead -lm -lc


#this object file should be linked within application server
TUXBOOT=tuxboot0.o

#used to make tuxedo-based server, like this
#  CFLAGS="${TUXSVR_CFLAG}" \
#  $(TUXSVR_MK) -o$@ ... -f ...

TUXSVR_CFLAG=$(SYSLIB)
TUXSVR_MK=buildserver

##########################################################
#
#   Client parts
#
##########################################################

# SYSTYPE = -DGOLPCLT

# for compiler
# SYSDEF = $(SYSINC) $(SYSTYPE) $(BDDEF)
# for linker
# SYSLIB = -L$(TO_LIB) -l$(LIBGOLP) -dl -lpthead -lm -lc


#
# define all phony targets
#  - all, make all artifacts
#  - clean, clean all artifacts and intermedia files
#           (object file, dependency file,...)
#  - mostlyclean, clean all intermedia files
#  - install, copy all artifacts into release directory
#
.PHONY : all clean mostlyclean install
#
# define default make goal(target)
#
.DEFAULT_GOAL := all


#
# end of the common Makefile.
# <-- Here is the Last Line.
