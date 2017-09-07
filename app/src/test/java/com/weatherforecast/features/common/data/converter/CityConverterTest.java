package com.weatherforecast.features.common.data.converter;

import com.weatherforecast.features.common.data.entity.CityEntity;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.search.data.Weather;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class CityConverterTest {

    @Test
    public void validateEntityHasSameValuesAsCityModel() {
        final City model = City.create(1L, "City");
        final CityEntity entity = CityConverter.toEntity(model);
        assertNotNull(entity);
        assertEquals(1L, entity.id);
        assertEquals("City", entity.name);
    }

    @Test
    public void validateEntityHasSameValuesAsWeatherModel() {
        final Weather model = Weather.create(1L, "City", mock(Forecast.Temperature.class), mock(List.class));
        final CityEntity entity = CityConverter.toEntity(model);
        assertNotNull(entity);
        assertEquals(1L, entity.id);
        assertEquals("City", entity.name);
    }

    @Test
    public void validateCityModelHasSameValueAsEntity() {
        final CityEntity entity = new CityEntity();
        entity.id = 1L;
        entity.name = "City";

        final City model = CityConverter.fromEntity(entity);
        assertNotNull(model);
        assertEquals(1L, model.id());
        assertEquals("City", model.name());
    }

}
