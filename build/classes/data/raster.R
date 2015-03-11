#!/usr/bin/env Rscript
require(raster)


######################## MY VERSION ####################################################

args <- commandArgs(TRUE)

setwd(args[1])

bilFile=args[2]

data=read.table(args[3], sep=",", header=TRUE)
dataFrame=data.frame(data$id_,data$decimalLatitude_,data$decimalLongitude_)
coordinates(dataFrame)<-c("data.decimalLongitude_","data.decimalLatitude_")

merged.absData <- as.data.frame(data$id_)
#merged.absData <- as.data.frame(merged.absData)

rasterFile <- raster(bilFile)
absData <- as.data.frame(extract(rasterFile,dataFrame))
merged.absData <- cbind(merged.absData,absData)



colnames(merged.absData) <- c("id_", names(rasterFile)[1])  #change colnames with .bil basenames list

merged.absData <- merged.absData[complete.cases(merged.absData),]
print(merged.absData)
write.csv(merged.absData, file="outputRaster.csv")
