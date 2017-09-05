package com.weatherforecast.features.common.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "city")
public class CityEntity {

    @PrimaryKey
    public long id;

    @ColumnInfo(name = "name")
    public String name;

}
