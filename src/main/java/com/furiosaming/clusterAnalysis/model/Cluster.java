package com.furiosaming.clusterAnalysis.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Cluster  implements Serializable {
    private int id;
    private ArrayList<Image> images;
    private Image center;

    public Cluster(int id){
        this.id = id;
        this.images = new ArrayList<>();
    }

    public void addImage(Image image){
        images.add(image);
    }

    public void addImageWithCenterComputing(Image image){
        images.add(image);
        int imagesCount = images.size();
        if(imagesCount == 1){
            center = image;
            return;
        }
        int distance = Integer.MAX_VALUE;
        for(int i=0; i<imagesCount; i++){
            int currentDistance = 0;
            for(int j=0; j<imagesCount; j++){
                if(i==j){
                    continue;
                }
                for (String key : images.get(i).getCharacteristics().keySet()) {
                    Integer currentImageCharacteristic = images.get(i).getCharacteristics().get(key);
                    Integer anotherImageCharacteristic = images.get(j).getCharacteristics().get(key);

                    if (currentImageCharacteristic != null && anotherImageCharacteristic != null) {
                        currentDistance += Math.pow(currentImageCharacteristic - anotherImageCharacteristic, 2);
                    }
                }
            }
            if(currentDistance<distance){
                distance = currentDistance;
                center = images.get(i);
            }
        }
    }
}
