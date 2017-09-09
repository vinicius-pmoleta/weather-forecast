package com.weatherforecast.features.search.presentation.model;

import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.search.data.Weather;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class WeatherScreenModelConverterTest {

    private static final String TEMPERATURE_PATTERN = "%.1f C";
    private static final String ICON_URL_PATTERN = "api.com/ic-%s.png";

    private WeatherScreenConverter converter;

    @Before
    public void setup() {
        converter = new WeatherScreenConverter(TEMPERATURE_PATTERN, ICON_URL_PATTERN);
    }

    @Test
    public void verifyWeatherConversionForPresentation() {
        final Forecast.Temperature temperature = Forecast.Temperature.create(20.0, 10.0, 30.0);
        final Forecast.Condition condition = Forecast.Condition.create("Condition", "c00");
        final Weather model = Weather.create(1L, "Location", temperature, Collections.singletonList(condition));

        final WeatherScreenModel result = converter.prepareForPresentation(model);
        assertNotNull(result);

        final WeatherScreenModel expected = WeatherScreenModel.create(1L, "Location", "Condition",
                "api.com/ic-c00.png", "30.0 C", "20.0 C", "10.0 C");
        assertEquals(expected, result);
    }

}
