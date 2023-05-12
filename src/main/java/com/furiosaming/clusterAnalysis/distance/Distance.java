package com.furiosaming.clusterAnalysis.distance;

import com.furiosaming.clusterAnalysis.model.Cluster;
import com.furiosaming.clusterAnalysis.model.Image;

public interface Distance {
    static double cosineDistance(Image firstImage, Image secondImage){
        double vectorsScalarProduct = 0;
        double firstVectorModule = 0;
        double secondVectorModule = 0;
        for(int i=0; i<firstImage.getCharacteristics().size(); i++){
            vectorsScalarProduct += firstImage.getCharacteristics().get(i) * secondImage.getCharacteristics().get(i);
            firstVectorModule += Math.pow(firstImage.getCharacteristics().get(i), 2);
            secondVectorModule += Math.pow(secondImage.getCharacteristics().get(i), 2);
        }
        return Math.asin(vectorsScalarProduct/(Math.pow(firstVectorModule, 0.5)*(Math.pow(secondVectorModule, 0.5))));
    }

    static double nearestNeighborsDistance(Image firstImage, Image secondImage){
        double distance = Integer.MAX_VALUE;

        for (String key : firstImage.getCharacteristics().keySet()) {
            Double v1 = firstImage.getCharacteristics().get(key);
            Double v2 = secondImage.getCharacteristics().get(key);
            if (v1 != null && v2 != null) {
                double currentDistance = Math.abs(v1 - v2);
                if(currentDistance < distance){
                    distance = currentDistance;
                }
            }
        }
        return distance;
    }
// это вызывается для всех элементов кластера
    static double euclideanDistance(Image firstImage, Image secondImage) {
        double distance = 0;
        for (String key : firstImage.getCharacteristics().keySet()) {
            Double v1 = firstImage.getCharacteristics().get(key);
            Double v2 = secondImage.getCharacteristics().get(key);

            if (v1 != null && v2 != null) {
                distance += Math.pow(v1 - v2, 2);
            }
        }
        return distance;
    }
// это должно вызываться вне цикла только один раз для центров
    static double clustersCentersDistance(Image firstClusterCenter, Image secondClusterCenter){
        return euclideanDistance(firstClusterCenter, secondClusterCenter);
    }
}
