package com.furiosaming.clusterAnalysis.convert;

import com.furiosaming.clusterAnalysis.model.Image;


public class ImageConverter {

    public static Image dataToImage(String inputImageCharacteristics, int imageId){
        int currentCharacteristicID = 0;
        Image image = new Image(imageId);
        String[] filteredCharacteristics = filterInputData(inputImageCharacteristics);
        for(String s: filteredCharacteristics){
            image.addCharacteristics(String.valueOf(currentCharacteristicID), Double.parseDouble(s));
            currentCharacteristicID++;
        }
        return image;
    }

    public static String[] filterInputData(String inputImageCharacteristics){
        inputImageCharacteristics = inputImageCharacteristics.replaceAll("[^-?0-9,]", "");
        return inputImageCharacteristics.split(",");
    }

}
