#!/bin/sh
#
# find all source file(*.cpp *.c) in source folder, $HOME/src,
# and generates a list 
#

# this script needs an argument for top dir of source files.
if [ x"$1" = x"" ]; then
    echo "Error: Source directory should be specified."
    exit 1
fi

####################################
#  define basename as function
####################################


LST=.tmp.lst
find $1 -name *.cpp > $LST
find $1 -name *.c >> $LST
cat $LST | while read fp
do
    echo `basename $fp`
done
# >$LST
rm -f $LST 2>/dev/null
