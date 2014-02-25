#
# General MAKEFILE for all GOLP's artifacts.
#
# This file should be stored under build/.
# 
# WXQ@XDU 2013.5
#

#
#            IMPORTENT INFORMATION
#          ^^^^^^^^^^^^^^^^^^^^^^^^^
#
# 1) 每个软件制品应该在 build/ 下存在一个组装目录，此目录
#      由这个文件自动生成，但必须在本文的 SUBDIRS 中注册.
#
# 2) 每个软件制品的组装工作由一个专用的 Makefile 和其他
#      配套文件组成。这些文件统一存储在 
#        build/scripts/backup.mk/
#      中。通过本文组装时，会自动复制、组装。
#   (a) 若某制品 A 的组装工作仅需一个 Makefile，则在 backup.mk/
#       下命名为 Makefile.A;
#   (b) 若某制品 B 的组装工作需要一个主Makefile和多个其他文件，
#       则在 backup.mk/ 下建立一个目录，Makefile.B，下面存放
#       Makefile 和其他文件。
#
# 3) 据上条，每个制品的组装定义需要修改时，请修改上述目录
#      中的文件.
#
# 4) 为保证组装的统一性，请在各自的 Makefile 的正文开始处
#    出现这个指令：
#      include ../scripts/makefile.def
#    这个文件中约定了组装命令、软件制品的组装目录和存储目录。
#
#
# 5) 每个制品的 Makefile 中必须定义以下 四个标准目标：
#     - all, 用于组装最终的软件制品
#     - clean, 清理组装过程中产生的临时文件、目标码文件和
#              build/lib|bin/下的软件制品文件
#     - mostlyclean, 和 clean 基本相同，但不删除软件制品文件
#     - install, 将 用目标 'all' 生成的软件制品复制到运行时
#                目录 $GOLPHOME/lib|bin 下。
#

# Here:
include scripts/makefile.def

#
# define directories for all GOLP's artifacts
#
# If any artifact is added or removed, 'SUBDIRS' and 'ARTIFACTS' 
# SHOULD be changed.

SUBDIRS=$(LIBLOG_DIR) $(LIBGOLP_DIR) $(LIBGOLPTP_DIR) \
  golpaop golptrad




.PHONY : all clean mostlyclean install
.DEFAULT_GOAL := all

# all dependency are defined in makefile of each artifact

all clean mostlyclean install:
	-@for f in ${SUBDIRS}; \
	do \
	mkdir $$f 2>/dev/null; \
	[ -d scripts/backup.mk/Makefile.$$f ] && cp -fpr scripts/backup.mk/Makefile.$$f/* $$f/; \
	[ -f scripts/backup.mk/Makefile.$$f ] && cp -f scripts/backup.mk/Makefile.$$f $$f/Makefile ; \
	echo ' ' && make -C $$f $@; echo ' '; \
	done
