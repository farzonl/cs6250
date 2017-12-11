# run this sscript in a directory with the tracefile in it

dmtracedump.exe -o *.trace | tr -d '\000' | egrep 'ent.*CompressedListener\.handleResult \(Ljava\/l' | sort > handleResult-timestamps.txt

LAST_RECV_DIFF=0
LAST_RECV_VAL=0
RECV_DIFF_SUM=0
RECV_COUNT=0
RECV_DIFF_COUNT=0

for i in $( cat handleResult-timestamps.txt | egrep -o 'ent.*CompressedListener\.handleResult \(Ljava\/l' | tr -d ' ent' | cut -d '.' -f 1 ); do
    RECV_COUNT=$((RECV_COUNT + 1))
    if [ $LAST_RECV_VAL -gt "0" ]
    then
        RECV_DIFF_SUM=$((RECV_DIFF_SUM + i - LAST_RECV_VAL))
    fi
    #echo $i
    LAST_RECV_VAL=$i
done

RECV_DIFF_COUNT=$((RECV_COUNT - 1))

#echo
#echo $RECV_DIFF_SUM
#echo --------
#echo $RECV_DIFF_COUNT
#echo

num=$(awk "BEGIN {print $RECV_DIFF_SUM/$RECV_DIFF_COUNT; exit}")
echo >> handleResult-timestamps.txt
echo "Average Difference: $num" >> handleResult-timestamps.txt

###############################################################################

dmtracedump.exe -o *.trace | tr -d '\000' | egrep 'xit.*CloudClient\.addCompressedFrames' | sort > addFrames-timestamps.txt

LAST_SEND_DIFF=0
LAST_SEND_VAL=0
SEND_DIFF_SUM=0
SEND_COUNT=0
SEND_DIFF_COUNT=0

for j in $( cat addFrames-timestamps.txt | egrep -o 'xit.*CloudClient\.addCompressedFrames' | tr -d ' xit' | cut -d '.' -f 1 ); do
    SEND_COUNT=$((SEND_COUNT + 1))
    if [ $LAST_SEND_VAL -gt "0" ]
    then
        SEND_DIFF_SUM=$((SEND_DIFF_SUM + j - LAST_SEND_VAL))
    fi
    #echo $j
    LAST_SEND_VAL=$j
done

SEND_DIFF_COUNT=$((SEND_COUNT - 1))

#echo
#echo $SEND_DIFF_SUM
#echo --------
#echo $SEND_DIFF_COUNT
#echo

num=$(awk "BEGIN {print $SEND_DIFF_SUM/$SEND_DIFF_COUNT; exit}")
echo >> addFrames-timestamps.txt
echo "Average Difference: $num" >> addFrames-timestamps.txt