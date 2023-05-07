package com.furiosaming.clusterAnalysis.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class  Image {
    private int id;
    private Map<String, Integer> characteristics;

    public Image(int id){
        this.id = id;
        this.characteristics = new HashMap<>();
    }

    public void addCharacteristics(String key, Integer value){
        characteristics.put(key, value);
    }

}
