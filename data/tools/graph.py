import matplotlib.pyplot as plt; plt.rcdefaults()
import matplotlib.pyplot as plt
import os
import sys

clientFileName = "traceview.txt"
successArr = []
failureArr = []
rootdir = sys.argv[1]
dryRun = ("dryrun" in sys.argv)
Effects = {}

# traverse root directory, and list directories as dirs and files as files
for root, dirs, files in os.walk(rootdir):
    path = root.split(os.sep)
    dirLen = len(dirs)
    if (dirLen >= 4 and "avg" in dirs
    and "01" in dirs and "02" in dirs 
    and "03"  in dirs):
        curdir = os.path.abspath(root)
        avgFile = os.path.join(curdir,"avg", clientFileName)
        if os.path.isfile(avgFile) :
            with open(avgFile, "r") as file_out:
                data=file_out.read()
                findStart = data.find('avg')
                if findStart != -1:
                    s = data[findStart:]
                    avgResult = s.split("-")
                    avgResultCast = float(avgResult[1])
                    Effects[os.path.basename(root)] = avgResultCast


plt.bar(Effects.keys(), Effects.values())
plt.ylabel('Time')
plt.title('Effects')
plt.savefig('example.pgf')
plt.show()