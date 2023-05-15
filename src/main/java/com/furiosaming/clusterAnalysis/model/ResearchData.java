package com.furiosaming.clusterAnalysis.model;

import com.furiosaming.clusterAnalysis.enums.TypeOfDistanceCalculation;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResearchData {
    private String filePath;
    private int thresholdValue;
    private int requiredClustersNumber;
    private TypeOfDistanceCalculation typeOfDistanceCalculation;
    private int normalization;

    // Возможно его стоит перенести в другое место
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
