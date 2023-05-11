package com.furiosaming.clusterAnalysis;

import com.furiosaming.clusterAnalysis.model.Cluster;
import com.furiosaming.clusterAnalysis.model.Image;

import java.io.*;
import java.util.ArrayList;

public class ClusterAnalysisApp {
    public static void main(String[] args) {

        ArrayList<Image> images = convertingFileDataToImages();
        int thresholdValue = Integer.MAX_VALUE;
        ArrayList<Cluster> clusters = mergeClustering(7, thresholdValue, images);
        writeToFile(clusters);
    }


    public static ArrayList<Cluster> mergeClustering(int requiredClustersNumber, int thresholdValue, ArrayList<Image> images){
        ArrayList<Cluster> clusters = new ArrayList<>();
        int currentClusterID = 0;
        int currentClusterCount = images.size();

        for(Image image: images){
            Cluster cluster = new Cluster(currentClusterID);
            cluster.addImage(image);
            clusters.add(cluster);
            currentClusterID++;
        }


        while (currentClusterCount > requiredClustersNumber){

            int[][] distanceMatrix = new int[clusters.size()][clusters.size()];
            int distance = Integer.MAX_VALUE;

            Cluster firstMergingCluster = null;
            Cluster secondMergingCluster = null;
            for(int i=0; i<clusters.size(); i++){
                for(int j=0; j<clusters.size(); j++){
                    if(j==i){
                        distanceMatrix[i][j] = 0;
                        continue;
                    }
                    for(Image first: clusters.get(i).getImages()){
                        for (Image second: clusters.get(j).getImages()){
                            int currentDistance = euclideanDistance(first,
                                    second);
                            distanceMatrix[i][j] = currentDistance;
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
                firstMergingCluster.getImages().addAll(secondMergingCluster.getImages());
                clusters.remove(secondMergingCluster);
                currentClusterCount = clusters.size();
            }
            else{
                break;
            }
        }

        return clusters;
    }


    public static Integer centersDistanceBetween(Cluster firstCluster, Cluster secondCluster){
        int distance = 0;
        for (String key : firstCluster.getCenter().getCharacteristics().keySet()) {
            Integer v1 = firstCluster.getCenter().getCharacteristics().get(key);
            Integer v2 = secondCluster.getCenter().getCharacteristics().get(key);

            if (v1 != null && v2 != null) {
                distance += Math.pow(v1 - v2, 2);
            }
        }
        return distance;
    }

    public static Integer euclideanDistance(Image firstImage, Image secondImage) {
        int distance = 0;
        for (String key : firstImage.getCharacteristics().keySet()) {
            Integer v1 = firstImage.getCharacteristics().get(key);
            Integer v2 = secondImage.getCharacteristics().get(key);

            if (v1 != null && v2 != null) {
                distance += Math.pow(v1 - v2, 2);
            }
        }
        return distance;
    }

    public static Integer nearestNeighborsDistance(Image firstImage, Image secondImage){
        int distance = Integer.MAX_VALUE;

        for (String key : firstImage.getCharacteristics().keySet()) {
            Integer v1 = firstImage.getCharacteristics().get(key);
            Integer v2 = secondImage.getCharacteristics().get(key);
            if (v1 != null && v2 != null) {
                int currentDistance = Math.abs(v1 - v2);
                if(currentDistance < distance){
                    distance = currentDistance;
                }
            }
        }
        return distance;
    }

    public static Double cosineDistance(Image firstImage, Image secondImage){
        int vectorsScalarProduct = 0;
        double firstVectorModule = 0;
        double secondVectorModule = 0;
        for(int i=0; i<firstImage.getCharacteristics().size(); i++){
            vectorsScalarProduct += firstImage.getCharacteristics().get(i) * secondImage.getCharacteristics().get(i);
            firstVectorModule += Math.pow(firstImage.getCharacteristics().get(i), 2);
            secondVectorModule += Math.pow(secondImage.getCharacteristics().get(i), 2);
        }
        return Math.asin(vectorsScalarProduct/(Math.pow(firstVectorModule, 0.5)*(Math.pow(secondVectorModule, 0.5))));
    }


    public static ArrayList<Image> convertingFileDataToImages(){
        ArrayList<Image> images = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(
                new File("src/main/resources/input/1.txt")))){
            String inputImageCharacteristics;
            int currentImageID = 0;

            while ((inputImageCharacteristics = reader.readLine()) != null) {
                int currentCharacteristicID = 0;
                Image image = new Image(currentImageID);

                inputImageCharacteristics = inputImageCharacteristics.replaceAll("[^-?0-9,]", "");
                String[] filteredCharacteristics = inputImageCharacteristics.split(",");
                for(String s: filteredCharacteristics){
                    image.addCharacteristics(String.valueOf(currentCharacteristicID), Integer.parseInt(s));
                    currentCharacteristicID++;
                }
                images.add(image);
                currentImageID++;
            }

        }
        catch (IOException io){
            io.printStackTrace();
        }
        return images;
    }

    public static void writeToFile(ArrayList<Cluster> clusters){
        try(PrintWriter out = new PrintWriter("result.txt")){
            for(Cluster cluster: clusters){
                out.println(cluster);
            }
        }
        catch (IOException io){
            io.printStackTrace();
        }
    }
}

