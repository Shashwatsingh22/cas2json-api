#Importing Modules
import casparser
from sys import argv
from os import path
import json
from os import makedirs

def parseCAS(filePath,password):
    try:
        json_str = casparser.read_cas_pdf(filePath,password, output="json")

        print(json_str)
        #Writing in an JSON file
        json_fileName=path.splitext(path.basename(filePath))[0]+".json";

        if not path.exists("output/"):
            makedirs("output/")

        with open("output/"+json_fileName,"w") as jsonFile:
            json.dump(json_str,jsonFile)
        
    except Exception as err:
        print(err)
    

if __name__ == "__main__":
    filePath = argv[1]
    password = argv[2]
    print(filePath," ",password)
    parseCAS(filePath,password);