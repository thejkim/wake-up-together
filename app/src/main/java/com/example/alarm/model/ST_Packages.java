package com.example.alarm.model;

import java.util.ArrayList;
import java.util.List;

public class ST_Packages {

    private static ST_Packages instance = null;

    private static List<PackageItems> packageItems;
    private ST_Packages() {
        packageItems = new ArrayList<PackageItems>();
    }

    public static ST_Packages getInstance() {
        if(instance == null) {
            instance = new ST_Packages();
        }
        return instance;
    }

    public static void addPackage(PackageItems packageItem) {
        packageItems.add(packageItems.size(), packageItem);
    }

    public void resetPackageItems() {
        packageItems = new ArrayList<>();
    }

    public List<PackageItems> getPackageItems() {
        return ST_Packages.packageItems;
    }

    public int getPackageCount() {
        return getPackageItems().size();
    }
}
