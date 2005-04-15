#!/bin/bash

echo "`find src -name '*\.java' -exec cat \{\} \; | wc -l` lines of code (LOC)"
echo "`find src -name '*\.java' -exec grep -H 'throw new UnsupportedOperationException();' \{\} \; | grep -vE 'Loader|Database|ConditionImpl' | wc -l` unimplemented methods (entities)"
echo "`find src -name '*\.java' -exec grep -H 'throw new UnsupportedOperationException();' \{\} \; | grep -E 'Loader|Database' | wc -l` unimplemented methods (loaders and databases)"
echo "`find src -name '*\.java' -exec grep -H 'throw new UnsupportedOperationException();' \{\} \; | grep 'ConditionImpl' | wc -l` unimplemented methods (conditions)"
