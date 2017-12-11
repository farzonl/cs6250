#!/usr/bin/python

import os
import sys
import pathlib
from subprocess import call

clientIperfFileName = "iperf.json"
successArr = []
failureArr = []
rootdir = sys.argv[1]
dryRun = ("dryrun" in sys.argv)
printFailures = ("printfails" in sys.argv)
# traverse root directory, and list directories as dirs and files as files
for root, dirs, files in os.walk(rootdir):
    path = root.split(os.sep)
    dirLen = len(dirs)
    if (dirLen >= 4 and "avg" in dirs):
        continue
    if (dirLen >= 3 and "01" in dirs and "02" in dirs and "03"  in dirs):
        curdir = os.path.abspath(root)
        #print((len(path) - 1) * '---', curdir)
        #print((len(path) - 1) * '---', os.path.basename(root))
        arriperfLogs = []
        for dir in dirs:
            #print(len(path) * '---', dir)
            numberedDir = root+"/"+dir
            for file in os.listdir(numberedDir):
                bFound = file == clientIperfFileName
                if bFound :
                    arriperfLogs.append(numberedDir+"/"+clientIperfFileName)
                #if file.endswith(".json"):
                #    FoundStr = "warning" if not bFound else "Found"
                #    print(FoundStr + len(path) * '---', file)
        
        perfLogsFound = len(arriperfLogs) 
        if perfLogsFound == 3 :
            successArr.append(arriperfLogs)
        else:
           failureArr.append("FAILURE: " + curdir + " had "+ str(perfLogsFound) +" of 3 perf logs found")
        
iTotalSuccess = len(successArr)
iTotalFailure = len(failureArr)

if(printFailures):
    for fail in failureArr :
        print (fail)

print("Total success is " + str(iTotalSuccess) + " out of " + str(iTotalSuccess+ iTotalFailure))
print(dryRun)
exe_path = os.path.dirname(os.path.realpath(__file__))
for success in successArr:
    strCommand = "node average.js " + success[0] + " " + success[1] + " " + success[2]
    if(dryRun):
        print (strCommand)
    else:
        two_up = (pathlib.Path(success[0]) / ".." / "..").resolve()
        avgDir = os.path.join(two_up,"avg")
        if not os.path.exists(avgDir):
            print(avgDir)
            os.makedirs(avgDir)
        file_outName = os.path.join(avgDir,"avg.json")
        with open(file_outName, "w+") as file_out:
            call(["node", exe_path+"/average.js",*success],stdout=file_out) 