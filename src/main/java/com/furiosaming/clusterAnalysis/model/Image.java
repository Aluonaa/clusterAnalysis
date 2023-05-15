package com.furiosaming.clusterAnalysis.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class  Image implements Serializable {

    private int id;
    private Map<String, Double> characteristics;

    public Image(int id){
        this.id = id;
        this.characteristics = new HashMap<>();
    }

    public void addCharacteristics(String key, Double value){
        characteristics.put(key, value);
    }

}
