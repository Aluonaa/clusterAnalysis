package com.furiosaming.clusterAnalysis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Cluster {
    private int id;
    private ArrayList<Image> images;

    public Cluster(int id){
        this.id = id;
        this.images = new ArrayList<>();
    }

    public void addImage(Image image){
        images.add(image);
    }
}
