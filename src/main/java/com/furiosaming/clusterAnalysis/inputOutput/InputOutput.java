package com.furiosaming.clusterAnalysis.inputOutput;

import com.furiosaming.clusterAnalysis.enums.TypeOfDistanceCalculation;
import com.furiosaming.clusterAnalysis.model.Cluster;
import com.furiosaming.clusterAnalysis.model.Image;
import com.furiosaming.clusterAnalysis.model.ResearchData;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static com.furiosaming.clusterAnalysis.convert.ImageConverter.dataToImage;

public class InputOutput {

    public static ResearchData gettingStartDataFromUser(){
        // можно создать класс для этой информации, например "Исследование"
        Scanner scanner = new Scanner(System.in);
        ResearchData researchData = new ResearchData();

        System.out.println("Введите путь к файлу образов:");
        researchData.setFilePath(scanner.nextLine());
        System.out.println("Введите величину порога:");
        researchData.setThresholdValue(scanner.nextInt());
        System.out.println("Введите ожидаемое число формируемых кластеров:");
        researchData.setRequiredClustersNumber(scanner.nextInt());
        System.out.println("Выберете метод вычисления расстояния:");
        System.out.println("1. COSINE_DISTANCE");
        System.out.println("2. CLUSTERS_CENTERS_DISTANCE");
        System.out.println("3. NEAREST_NEIGHBORS_DISTANCE");
        System.out.println("4. EUCLIDEAN_DISTANCE");
        int distanceType = scanner.nextInt();
        if(distanceType == 1){
            researchData.setTypeOfDistanceCalculation(TypeOfDistanceCalculation.COSINE_DISTANCE);
        }
        else if(distanceType == 2){
            researchData.setTypeOfDistanceCalculation(TypeOfDistanceCalculation.CLUSTERS_CENTERS_DISTANCE);
        }
        else if(distanceType == 3){
            researchData.setTypeOfDistanceCalculation(TypeOfDistanceCalculation.NEAREST_NEIGHBORS_DISTANCE);
        }
        else{
            researchData.setTypeOfDistanceCalculation(TypeOfDistanceCalculation.EUCLIDEAN_DISTANCE);
        }
        System.out.println("Нужна ли нормализация? (1/0):");
        researchData.setNormalization(scanner.nextInt());
        return researchData;
    }

    public static ArrayList<Image> fileDataToImages(String fileName){
        ArrayList<Image> images = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(
                new File(fileName)))){
            String inputImageCharacteristics;
            int currentImageID = 0;

            while ((inputImageCharacteristics = reader.readLine()) != null) {
                images.add(dataToImage(inputImageCharacteristics, currentImageID));
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
