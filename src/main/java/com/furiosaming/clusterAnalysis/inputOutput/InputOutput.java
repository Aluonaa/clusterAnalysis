package com.furiosaming.clusterAnalysis.inputOutput;

import com.furiosaming.clusterAnalysis.model.Cluster;
import com.furiosaming.clusterAnalysis.model.Image;

import java.io.*;
import java.util.ArrayList;

import static com.furiosaming.clusterAnalysis.convert.ImageConverter.dataToImage;

public class InputOutput {

    public static ArrayList<Image> fileDataToImages(String fileName, int distanceType){
        ArrayList<Image> images = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(
                new File(fileName)))){
            String inputImageCharacteristics;
            int currentImageID = 0;

            if(distanceType == 4){

            }else{
                while ((inputImageCharacteristics = reader.readLine()) != null) {
                    images.add(dataToImage(inputImageCharacteristics, currentImageID));
                    currentImageID++;
                }
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
