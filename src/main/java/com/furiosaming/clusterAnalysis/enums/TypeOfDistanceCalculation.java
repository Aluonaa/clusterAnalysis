package com.furiosaming.clusterAnalysis.enums;

import com.furiosaming.clusterAnalysis.distance.Distance;
import com.furiosaming.clusterAnalysis.model.Image;

public enum TypeOfDistanceCalculation {
    COSINE_DISTANCE,
    CLUSTERS_CENTERS_DISTANCE,
    NEAREST_NEIGHBORS_DISTANCE,
    EUCLIDEAN_DISTANCE;

    public static double calculateDistance(Image firstImage, Image secondImage, TypeOfDistanceCalculation typeOfDistanceCalculation){
        if(typeOfDistanceCalculation == CLUSTERS_CENTERS_DISTANCE){
            return Distance.clustersCentersDistance(firstImage, secondImage);
        }
        else if(typeOfDistanceCalculation == COSINE_DISTANCE){
            return Distance.cosineDistance(firstImage, secondImage);
        }
        else if(typeOfDistanceCalculation == NEAREST_NEIGHBORS_DISTANCE){
            return Distance.nearestNeighborsDistance(firstImage, secondImage);
        }
        else{
            return Distance.euclideanDistance(firstImage, secondImage);
        }
    }
}
