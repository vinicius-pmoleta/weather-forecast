package com.weatherforecast.features.common.data.converter;

import com.weatherforecast.features.common.data.entity.CityEntity;
import com.weatherforecast.features.common.data.model.City;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class CityConverterTest {

    @Test
    public void validateEntityHasSameValuesAsModel() {
        final City model = City.create(1L, "City");
        final CityEntity entity = CityConverter.toEntity(model);
        assertNotNull(entity);
        assertEquals(1L, entity.id);
        assertEquals("City", entity.name);
    }

}
