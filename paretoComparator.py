import plotly.graph_objects as go
from fpdf import FPDF
import os
# Create random data with numpy
import numpy as np

def readPareto(x,path):
    f = open(path,'r')
    line = f.readline()
    while True:
        line = f.readline()
        if len(line)<2:
            break
        if(len(line.split('\t'))==1):
            line = line.split(';')
        else:
            line = line.split('\t')

        x.append((float(line[0].strip()),float(line[1].strip())))
    f.close()

def generatePDF(name):
    cnt = 0
    pdf = FPDF()
    # imagelist is the list with all image filenames
    for image in os.listdir('./images/'):
        pdf.add_page()
        pdf.image('./images/' + image, 0, 0)
        cnt += 1
        print('Add page ' + str(cnt))

    pdf.output(name+".pdf", "F")

def generateDiagram(finalX,finalY,finalX1,finalY1,number):
    fig = go.Figure()

    fig.update_xaxes(
        tickangle=90,
        title_text="pMedian - Instance " + str(number),
        title_font={"size": 20},
        title_standoff=25)

    fig.update_yaxes(
        title_text="pDispersion",
        title_standoff=25)

    # Add traces
    fig.add_trace(go.Scatter(x=finalX, y=finalY,
                             mode='lines+markers',
                             name=path.split('/')[-1]))
    fig.add_trace(go.Scatter(x=finalX1, y=finalY1,
                             mode='lines+markers',
                             name=path2.split('/')[-1]))

    fig.write_image("images/comparative" + str(number) + ".png")
    fig.show()

def generateAllInstances(realPath,realPath2):
    for j in range(40):
        if j == 0:
            continue
        x = []
        x1 = []

        path = realPath+"/pmed"+str(j)+".txt"
        path2 = realPath2+"/pmed"+str(j)+".txt"

        readPareto(x,path)
        readPareto(x1,path2)

        print(sorted(x))
        print(sorted(x1))

        finalX,finalY,finalX1,finalY1 = [],[],[],[]

        for i in sorted(x):
            finalX.append(i[0])
            finalY.append(i[1])

        for i in sorted(x1):
            finalX1.append(i[0])
            finalY1.append(i[1])

        generateDiagram(finalX, finalY, finalX1, finalY1, j)

def generateOneInstance(realPath, realPath2, number):
    x = []
    x1 = []

    path = realPath + "/pmed" + str(number) + ".txt"
    path2 = realPath2 + "/pmed" + str(number) + ".txt"

    readPareto(x, path)
    readPareto(x1, path2)

    print(sorted(x))
    print(sorted(x1))

    finalX, finalY, finalX1, finalY1 = [], [], [], []

    for i in sorted(x):
        finalX.append(i[0])
        finalY.append(i[1])

    for i in sorted(x1):
        finalX1.append(i[0])
        finalY1.append(i[1])

    generateDiagram(finalX,finalY,finalX1,finalY1,number)



path = "./code/MOMetricsMaven/test/pmed/pareto-NSGA2"
path2 = "./code/MOMetricsMaven/test/pmed/pareto-RPR"

#generateOneInstance(path,path2,11)
generateAllInstances(path,path2)
generatePDF("RPRvsNSGA2")



