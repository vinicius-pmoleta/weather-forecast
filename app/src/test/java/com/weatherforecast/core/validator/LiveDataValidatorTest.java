package com.weatherforecast.core.validator;

import android.arch.lifecycle.LiveData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class LiveDataValidatorTest {

    @Test
    public void assertContentIsNotAvailableOnHolderWhenLiveDataIsNull() {
        assertFalse(LiveDataValidator.isContentAvailable(null));
    }

    @Test
    public void assertContentIsNotAvailableWhenLiveDataValueIsNull() {
        final LiveData<String> data = mock(LiveData.class);
        when(data.getValue()).thenReturn(null);
        assertFalse(LiveDataValidator.isContentAvailable(data));
    }

    @Test
    public void assertDataIsAvailableOnHolder() {
        final LiveData<String> data = mock(LiveData.class);
        when(data.getValue()).thenReturn("");
        assertTrue(LiveDataValidator.isContentAvailable(data));
    }

}
