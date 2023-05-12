package com.furiosaming.clusterAnalysis;

import com.furiosaming.clusterAnalysis.model.Cluster;
import com.furiosaming.clusterAnalysis.model.Image;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import static com.furiosaming.clusterAnalysis.distance.Distance.clustersCentersDistance;
import static com.furiosaming.clusterAnalysis.distance.Distance.euclideanDistance;
import static com.furiosaming.clusterAnalysis.distance.Distance.cosineDistance;
import static com.furiosaming.clusterAnalysis.distance.Distance.nearestNeighborsDistance;
import static com.furiosaming.clusterAnalysis.inputOutput.InputOutput.fileDataToImages;
import static com.furiosaming.clusterAnalysis.inputOutput.InputOutput.writeToFile;

public class ClusterAnalysisApp {
    public static void main(String[] args) {

        //TODO добавить нормирование признаков
        // src/main/resources/input/1.txt

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите путь к файлу образов:");
        String fileName = scanner.nextLine();
        System.out.println("Введите величину порога:");
        int thresholdValue = scanner.nextInt();
        System.out.println("Введите ожидаемое число формируемых кластеров:");
        int requiredClustersNumber = scanner.nextInt();
        System.out.println("Выберете метод вычисления расстояния:");
        System.out.println("1. cosineDistance");
        System.out.println("2. clustersCentersDistance");
        System.out.println("3. nearestNeighborsDistance");
        System.out.println("4. euclideanDistance");
        int distanceType = scanner.nextInt();
        System.out.println("Нужна ли нормализация? (1/0):");
        int normalization = scanner.nextInt();

        ArrayList<Image> images = fileDataToImages(fileName, distanceType);
        // TODO сделать билдер
        ArrayList<Cluster> clusters = mergeClustering(requiredClustersNumber, thresholdValue, images, normalization, distanceType);
        writeToFile(clusters);
    }


    public static ArrayList<Cluster> mergeClustering(int requiredClustersNumber, int thresholdValue, ArrayList<Image> images, int normalization, int distanceType){
        if(normalization == 1){
            normalization(images);
        }
        ArrayList<Cluster> clusters = new ArrayList<>();
        int currentClusterID = 0;
        int currentClusterCount = images.size();

        for(Image image: images){
            Cluster cluster = new Cluster(currentClusterID);
            // может эту логику внести в класс?
            if(distanceType == 2){
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
                    if(distanceType == 2){
                        distanceMatrix[i][j] = clustersCentersDistance(clusters.get(i).getCenter(), clusters.get(j).getCenter());
                    }
                    else{
                        for(Image first: clusters.get(i).getImages()){
                            for (Image second: clusters.get(j).getImages()){
                                double currentDistance = 0;
                                if(distanceType == 1){
                                    currentDistance = cosineDistance(first,
                                            second);
                                }
                                else if(distanceType == 3){
                                    currentDistance = nearestNeighborsDistance(first, second);
                                }
                                else if(distanceType == 4){
                                    currentDistance = euclideanDistance(first,
                                            second);
                                }
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
                if(distanceType == 2){
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

//куда его прибрать?
    public static void normalization(ArrayList<Image> images){
        for(Image image: images){
            double vectorLength = 0;
            for(double value : image.getCharacteristics().values()){
                vectorLength+=value;
            }
            for (Map.Entry<String, Double> entry : image.getCharacteristics().entrySet()) {
                entry.setValue(entry.getValue()/vectorLength);
            }
        }
    }
}

