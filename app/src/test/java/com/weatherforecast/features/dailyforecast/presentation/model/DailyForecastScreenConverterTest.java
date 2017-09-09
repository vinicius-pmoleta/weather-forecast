package com.weatherforecast.features.dailyforecast.presentation.model;

import com.weatherforecast.features.common.data.model.Forecast;
import com.weatherforecast.features.dailyforecast.data.model.DailyForecast;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DailyForecastScreenConverterTest {

    private static final String TEMPERATURE_PATTERN = "%.1f C";
    private static final String DATE_PARSER_PATTERN = "yyyy-MM-dd";
    private static final String DATE_FORMATTER_PATTERN = "dd/MM";
    private static final String TIME_PARSER_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_FORMATTER_PATTERN = "HH:mm";
    private static final String ICON_URL_PATTERN = "api.com/ic-%s.png";

    private DailyForecastScreenConverter converter;

    @Before
    public void setup() {
        converter = new DailyForecastScreenConverter(TEMPERATURE_PATTERN, DATE_PARSER_PATTERN,
                DATE_FORMATTER_PATTERN, TIME_PARSER_PATTERN, TIME_FORMATTER_PATTERN, ICON_URL_PATTERN);
    }

    @Test
    public void assertValidDateFormatted() {
        final String result = converter.formatDate("2017-01-10");
        assertEquals("10/01", result);
    }

    @Test
    public void assertInvalidDateFormatted() {
        final String result = converter.formatDate("10-01-2017");
        assertEquals("", result);
    }

    @Test
    public void assertEmptyDateFormatted() {
        final String result = converter.formatDate("");
        assertEquals("", result);
    }

    @Test
    public void assertValidTimeFormatted() {
        final String result = converter.formatTime("2017-01-10 15:00:00");
        assertEquals("15:00", result);
    }

    @Test
    public void assertInvalidTimeFormatted() {
        final String result = converter.formatTime("2017-01-10 10:00PM");
        assertEquals("", result);
    }

    @Test
    public void assertEmptyTimeFormatted() {
        final String result = converter.formatTime("");
        assertEquals("", result);
    }

    @Test
    public void verifyForecastConversionForPresentation() {
        final Forecast.Temperature temperature = Forecast.Temperature.create(20.0, 10.0, 30.0);
        final List<Forecast.Condition> conditions = Collections.singletonList(
                Forecast.Condition.create("Condition", "c00"));
        final Forecast model = Forecast.create("2017-01-10 15:00:00", temperature, conditions);

        final ForecastScreenModel result = converter.prepareForPresentation(model);
        assertNotNull(result);

        final ForecastScreenModel expected = ForecastScreenModel.create("15:00", "api.com/ic-c00.png", "20.0 C");
        assertEquals(expected, result);
    }

    @Test
    public void verifySingleDailyForecastConversionForPresentation() {
        final Forecast.Temperature temperature = Forecast.Temperature.create(20.0, 10.0, 30.0);
        final List<Forecast.Condition> conditions = Collections.singletonList(
                Forecast.Condition.create("Condition", "c00"));
        final Forecast forecast = Forecast.create("2017-01-10 15:00:00", temperature, conditions);
        final DailyForecast model = DailyForecast.create("2017-01-10", Collections.singletonList(forecast));

        final DailyForecastScreenModel result = converter.prepareForPresentation(model);
        assertNotNull(result);

        final DailyForecastScreenModel expected = DailyForecastScreenModel.create("10/01", Collections.singletonList(
                ForecastScreenModel.create("15:00", "api.com/ic-c00.png", "20.0 C")));
        assertEquals(expected, result);
    }

    @Test
    public void verifyMultipleDailyForecastConversionForPresentation() {
        final Forecast.Temperature temperature = Forecast.Temperature.create(20.0, 10.0, 30.0);
        final List<Forecast.Condition> conditions = Collections.singletonList(
                Forecast.Condition.create("Condition", "c00"));
        final Forecast forecast = Forecast.create("2017-01-10 15:00:00", temperature, conditions);
        final DailyForecast dailyForecast = DailyForecast.create("2017-01-10", Collections.singletonList(forecast));
        final List<DailyForecast> models = Collections.singletonList(dailyForecast);

        final List<DailyForecastScreenModel> result = converter.prepareForPresentation(models);
        assertNotNull(result);

        final List<DailyForecastScreenModel> expected = Collections.singletonList(
                DailyForecastScreenModel.create("10/01", Collections.singletonList(
                        ForecastScreenModel.create("15:00", "api.com/ic-c00.png", "20.0 C"))));
        assertEquals(expected, result);
    }

}
