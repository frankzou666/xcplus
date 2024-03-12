package com.example.media;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Testm {

    @Test
    public void testMode(){
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(i);
        }
        list.stream().forEach(item->
            System.out.println(item%3));

    }
}
