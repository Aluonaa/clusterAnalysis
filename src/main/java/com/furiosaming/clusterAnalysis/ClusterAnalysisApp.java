package com.furiosaming.clusterAnalysis;

import com.furiosaming.clusterAnalysis.model.Image;
import com.furiosaming.clusterAnalysis.model.Cluster;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ClusterAnalysisApp {
    public static void main(String[] args) {
        ArrayList<Image> images = convertingFileDataToImages();
        System.out.println(mergeClustering(4, images));

//        PrintStream out = new PrintStream(new File("output.txt"));
    }


    public static ArrayList<Cluster> mergeClustering(int requiredClustersNumber, ArrayList<Image> images){
        ArrayList<Cluster> clusters = new ArrayList<>();
        int currentClusterID = 0;
        int currentClusterCount = images.size();
        Set<Cluster> clustersPair = new HashSet<>();
        int characteristicCount = images.get(0).getCharacteristics().size();


        for(Image image: images){
            Cluster cluster = new Cluster(currentClusterID);
            cluster.addImage(image);
            clusters.add(cluster);
            currentClusterID++;
        }

        while (currentClusterCount > requiredClustersNumber){

            Cluster mergingCluster1 = null;
            Cluster mergingCluster2 = null;
            for(int i=0; i<clusters.size()-1; i++){
                int distance = Integer.MAX_VALUE;
                for(int j=0; j<clusters.size(); j++){
                    if(j==i){
                        continue;
                    }
                    for(Image first: clusters.get(i).getImages()){
                        for (Image second: clusters.get(j).getImages()){
                            int currentDistance = euclideanDistance(first,
                                    second);
                            System.out.println(currentDistance);
                            if(currentDistance < distance){
                                distance = currentDistance;
                                mergingCluster1 = clusters.get(i);
                                mergingCluster2 = clusters.get(j);
                            }
                        }
                    }
                }

            }
            if(mergingCluster1 != null && mergingCluster2 != null){
                mergingCluster1.getImages().addAll(mergingCluster2.getImages());
                clusters.remove(mergingCluster2);
                currentClusterCount = clusters.size();
            }
        }

        return clusters;
    }

    // TODO НУЖНО ДОБАВИТЬ КОСИНУСНОЕ И МЕЖДУ ЦЕНТРАМИ

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
}
