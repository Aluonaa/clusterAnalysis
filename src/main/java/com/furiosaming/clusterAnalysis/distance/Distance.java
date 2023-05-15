package com.furiosaming.clusterAnalysis.distance;

import com.furiosaming.clusterAnalysis.enums.TypeOfDistanceCalculation;
import com.furiosaming.clusterAnalysis.model.Cluster;
import com.furiosaming.clusterAnalysis.model.Image;

public interface Distance {

    static double calculateDistance(Cluster firstICluster, Cluster secondCluster,
                                           TypeOfDistanceCalculation typeOfDistanceCalculation){
        if(typeOfDistanceCalculation == TypeOfDistanceCalculation.CLUSTERS_CENTERS_DISTANCE){
            return Distance.clustersCentersDistance(firstICluster, secondCluster);
        }
        else if(typeOfDistanceCalculation == TypeOfDistanceCalculation.COSINE_DISTANCE){
            return Distance.cosineDistance(firstICluster, secondCluster);
        }
        else if(typeOfDistanceCalculation == TypeOfDistanceCalculation.NEAREST_NEIGHBORS_DISTANCE){
            return Distance.nearestNeighborsDistance(firstICluster, secondCluster);
        }
        else{
            return Distance.euclideanDistance(firstICluster, secondCluster);
        }
    }

    static double cosineDistance(Cluster firstCluster, Cluster secondCluster){
        double distanceBetweenClusters = 0;
        for(Image firstImage: firstCluster.getImages()) {
            for (Image secondImage : secondCluster.getImages()) {
                double vectorsScalarProduct = 0;
                double firstVectorModule = 0;
                double secondVectorModule = 0;
                for (int i = 0; i < firstImage.getCharacteristics().size(); i++) {
                    vectorsScalarProduct += firstImage.getCharacteristics().get(i) * secondImage.getCharacteristics().get(i);
                    firstVectorModule += Math.pow(firstImage.getCharacteristics().get(i), 2);
                    secondVectorModule += Math.pow(secondImage.getCharacteristics().get(i), 2);
                }
                distanceBetweenClusters += Math.asin(vectorsScalarProduct/(Math.pow(firstVectorModule, 0.5)*(Math.pow(secondVectorModule, 0.5))));
            }
        }
        return distanceBetweenClusters;
    }

    static double nearestNeighborsDistance(Cluster firstCluster, Cluster secondCluster){
        double minDistance = Integer.MAX_VALUE;
        double distanceBetweenClusters = 0;
        for(Image firstImage: firstCluster.getImages()) {
            for (Image secondImage : secondCluster.getImages()) {
                for (String key : firstImage.getCharacteristics().keySet()) {
                    Double v1 = firstImage.getCharacteristics().get(key);
                    Double v2 = secondImage.getCharacteristics().get(key);
                    if (v1 != null && v2 != null) {
                        double currentDistance = Math.abs(v1 - v2);
                        if(currentDistance < minDistance){
                            distanceBetweenClusters = currentDistance;
                        }
                    }
                }
            }
        }
        return distanceBetweenClusters;
    }

    static double euclideanDistance(Cluster firstCluster, Cluster secondCluster) {
        double distanceBetweenClusters = 0;
        for(Image firstImage: firstCluster.getImages()) {
            for (Image secondImage : secondCluster.getImages()) {
                for (String key : firstImage.getCharacteristics().keySet()) {
                    Double v1 = firstImage.getCharacteristics().get(key);
                    Double v2 = secondImage.getCharacteristics().get(key);

                    if (v1 != null && v2 != null) {
                        distanceBetweenClusters += Math.pow(v1 - v2, 2);
                    }
                }
            }
        }
        return distanceBetweenClusters;
    }

    static double clustersCentersDistance(Cluster firstCluster, Cluster secondCluster){
        double distanceBetweenClusters = 0;
        for (String key : firstCluster.getCenter().getCharacteristics().keySet()) {
            Double v1 = firstCluster.getCenter().getCharacteristics().get(key);
            Double v2 = secondCluster.getCenter().getCharacteristics().get(key);

            if (v1 != null && v2 != null) {
                distanceBetweenClusters += Math.pow(v1 - v2, 2);
            }
        }
        return distanceBetweenClusters;
    }
}
