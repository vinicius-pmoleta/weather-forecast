package com.weatherforecast.features.common.data.repository;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.weatherforecast.features.common.data.entity.CityEntity;

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(final CityEntity city);

}
