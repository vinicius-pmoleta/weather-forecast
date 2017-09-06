package com.weatherforecast.features.common.data.converter;

import com.weatherforecast.features.common.data.entity.ForecastEntity;
import com.weatherforecast.features.common.data.model.City;
import com.weatherforecast.features.common.data.model.Forecast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ForecastConverterTest {

    @Test
    public void verifyModelFromEntityConversion() {
        final ForecastEntity entity = new ForecastEntity();
        entity.date = "10-01-2017 12:00:00";
        entity.cityId = 0L;
        entity.currentTemperature = 20.0;
        entity.minimumTemperature = 10.0;
        entity.maximumTemperature = 30.0;
        entity.condition = "Sunny";
        entity.icon = "sunny-00";

        final Forecast model = ForecastConverter.fromEntity(entity);

        assertNotNull(model);
        assertEquals("10-01-2017 12:00:00", model.date());
        assertNotNull(model.temperature());
        assertEquals(20.0, model.temperature().current(), 0);
        assertEquals(10.0, model.temperature().minimum(), 0);
        assertEquals(30.0, model.temperature().maximum(), 0);
        assertNotNull(model.conditions());
        assertFalse(model.conditions().isEmpty());
        assertNotNull(model.conditions().get(0));
        assertEquals("Sunny", model.conditions().get(0).name());
        assertEquals("sunny-00", model.conditions().get(0).icon());
    }

    @Test
    public void verifyEntityFromModel() {
        final City city = City.create(0L, "City");

        final Forecast.Temperature temperature = Forecast.Temperature.create(20.0, 10.0, 30.0);
        final Forecast.Condition condition = Forecast.Condition.create("Sunny", "sunny-00");
        final Forecast model = Forecast.create("10-01-2017 12:00:00", temperature, Collections.singletonList(condition));

        final ForecastEntity entity = ForecastConverter.toEntity(model, city);

        assertNotNull(entity);
        assertEquals("10-01-2017 12:00:00", entity.date);
        assertEquals(0L, entity.cityId);
        assertEquals(20.0, entity.currentTemperature, 0);
        assertEquals(10.0, entity.minimumTemperature, 0);
        assertEquals(30.0, entity.maximumTemperature, 0);
        assertEquals("Sunny", entity.condition);
        assertEquals("sunny-00", entity.icon);
    }

}
