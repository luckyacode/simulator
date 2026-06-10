package com.praveen.simulator.other;

import com.praveen.simulator.helper.Utils;

public class Read {
    public static void main(String[] args) {
        System.out.println("praveen");
//        Utils.fetchPassengerFromFile(6);
        Utils.fetchFlightFromFile(5).stream().forEach(System.out::println);
    }
}
