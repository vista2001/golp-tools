#!/bin/bash

TUXDIR=${TUXDIR :- /home/tuxdeo}; export TUXDIR

checkENV()
{
    if [ x"$2" = x ]; then
       echo "Environment variable '$1' should be set."
       exit 1
    fi
    return 0;
}

# compilers and flags
# These parameters should be defined as makefile.appdefs
#
checkENV "TUXDIR" $TUXDIR
checkENV "CC" $CC
checkENV "CFLAGS" $CFLAGS
checkENV "LD_LIBRARY_PATH" $LD_LIBRARY_PATH 



buildserver $*
exit $?
