package com.furiosaming.clusterAnalysis;

import com.furiosaming.clusterAnalysis.distance.Distance;
import com.furiosaming.clusterAnalysis.enums.TypeOfDistanceCalculation;
import com.furiosaming.clusterAnalysis.inputOutput.InputOutput;
import com.furiosaming.clusterAnalysis.model.Cluster;
import com.furiosaming.clusterAnalysis.model.Image;
import com.furiosaming.clusterAnalysis.model.ResearchData;

import java.util.ArrayList;

public class ClusterAnalysisApp {
    public static void main(String[] args) {

        /** src/main/resources/input/1.txt **/
        // TODO сделать билдер
        // TODO внести методы рассчета расстояний в класс ENUM
        ResearchData researchData = InputOutput.gettingStartDataFromUser();
        ArrayList<Image> images = InputOutput.fileDataToImages(researchData.getFilePath());
        ArrayList<Cluster> clusters = mergeClustering(researchData.getRequiredClustersNumber(), researchData.getThresholdValue(),
                images, researchData.getNormalization(), researchData.getTypeOfDistanceCalculation());
        InputOutput.writeToFile(clusters);
    }


    public static ArrayList<Cluster> mergeClustering(int requiredClustersNumber, int thresholdValue, ArrayList<Image> images,
                                                     int normalization, TypeOfDistanceCalculation typeOfDistanceCalculation){
        if(normalization == 1){
            ResearchData.normalization(images);
        }
        ArrayList<Cluster> clusters = new ArrayList<>();
        int currentClusterID = 0;
        int currentClusterCount = images.size();

        for(Image image: images){
            Cluster cluster = new Cluster(currentClusterID);
            // может эту логику внести в класс?
            if(typeOfDistanceCalculation == TypeOfDistanceCalculation.CLUSTERS_CENTERS_DISTANCE){
                cluster.addImageWithCenterComputing(image);
            }
            else{
                cluster.addImage(image);
            }
            clusters.add(cluster);
            currentClusterID++;
        }


        while (currentClusterCount > requiredClustersNumber){

            double[][] distanceMatrix = new double[clusters.size()][clusters.size()];
            double distance = Double.MAX_VALUE;

            Cluster firstMergingCluster = null;
            Cluster secondMergingCluster = null;
            for(int i=0; i<clusters.size(); i++){
                for(int j=0; j<clusters.size(); j++){
                    if(j==i){
                        distanceMatrix[i][j] = 0;
                        continue;
                    }
                    if(typeOfDistanceCalculation == TypeOfDistanceCalculation.CLUSTERS_CENTERS_DISTANCE){
                        distanceMatrix[i][j] = Distance.clustersCentersDistance(clusters.get(i).getCenter(), clusters.get(j).getCenter());
                    }
                    else{
                        for(Image first: clusters.get(i).getImages()){
                            for (Image second: clusters.get(j).getImages()){
                                double currentDistance = 0;
                                currentDistance = TypeOfDistanceCalculation.calculateDistance(first, second, typeOfDistanceCalculation);
//                                if(typeOfDistanceCalculation == TypeOfDistanceCalculation.COSINE_DISTANCE){
//                                    currentDistance = Distance.cosineDistance(first,
//                                            second);
//                                }
//                                else if(typeOfDistanceCalculation == TypeOfDistanceCalculation.NEAREST_NEIGHBORS_DISTANCE){
//                                    currentDistance = Distance.nearestNeighborsDistance(first, second);
//                                }
//                                else if(typeOfDistanceCalculation == TypeOfDistanceCalculation.EUCLIDEAN_DISTANCE){
//                                    currentDistance = Distance.euclideanDistance(first,
//                                            second);
//                                }
                                distanceMatrix[i][j] = currentDistance;
                            }
                        }
                    }
                }
            }
            for (int i=0; i<distanceMatrix.length; i++){
                for (int j=0; j<distanceMatrix.length; j++){
                    if(j==i){
                        continue;
                    }
                    if((distanceMatrix[i][j] < distance) && (distanceMatrix[i][j] < thresholdValue)){
                        distance = distanceMatrix[i][j];
                        firstMergingCluster = clusters.get(i);
                        secondMergingCluster = clusters.get(j);
                    }
                }
            }
            if(firstMergingCluster != null && secondMergingCluster != null){
                if(typeOfDistanceCalculation == TypeOfDistanceCalculation.CLUSTERS_CENTERS_DISTANCE){
                    for(Image image: secondMergingCluster.getImages()){
                        firstMergingCluster.addImageWithCenterComputing(image);
                    }
                }
                else{
                    firstMergingCluster.getImages().addAll(secondMergingCluster.getImages());
                }
                clusters.remove(secondMergingCluster);
                currentClusterCount = clusters.size();
            }
            else{
                break;
            }
        }
        return clusters;
    }
}

