package com.weatherforecast.features.common.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

@Entity(tableName = "forecast",
        primaryKeys = {"date", "cityId"},
        foreignKeys = @ForeignKey(entity = CityEntity.class, parentColumns = "id", childColumns = "cityId"),
        indices = @Index("cityId")
)
public class ForecastEntity {

    public String date;

    public long cityId;

    public Double currentTemperature;

    public Double minimumTemperature;

    public Double maximumTemperature;

    public String condition;

    public String icon;

}
