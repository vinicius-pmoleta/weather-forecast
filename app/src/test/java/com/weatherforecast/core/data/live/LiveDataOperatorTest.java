package com.weatherforecast.core.data.live;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class LiveDataOperatorTest {

    @Test
    public void assertContentIsNotAvailableOnHolderWhenLiveDataIsNull() {
        assertFalse(LiveDataOperator.isDataAvailable(null));
    }

    @Test
    public void assertContentIsNotAvailableWhenLiveResultDataIsNull() {
        final LiveResult<String> result = mock(LiveResult.class);
        when(result.data()).thenReturn(null);
        assertFalse(LiveDataOperator.isDataAvailable(result));
    }

    @Test
    public void assertContentIsNotAvailableWhenLiveResultDataHasNoValue() {
        final LiveData<String> data = mock(LiveData.class);
        when(data.getValue()).thenReturn(null);
        final LiveResult<String> result = mock(LiveResult.class);
        when(result.data()).thenReturn(data);
        assertFalse(LiveDataOperator.isDataAvailable(result));
    }

    @Test
    public void assertDataIsAvailableOnHolder() {
        final LiveData<String> data = mock(LiveData.class);
        when(data.getValue()).thenReturn("");
        final LiveResult<String> result = mock(LiveResult.class);
        when(result.data()).thenReturn(data);
        assertTrue(LiveDataOperator.isDataAvailable(result));
    }

    @Test
    public void verifyObserversRemovedFromValidaData() {
        final LiveData<?> data = mock(LiveData.class);
        when(data.hasObservers()).thenReturn(true);
        final LifecycleOwner owner = mock(LifecycleOwner.class);

        LiveDataOperator.removeDataObservers(data, owner);
        verify(data, times(1)).removeObservers(owner);
    }

    @Test
    public void verifyObserversNotRemovedFromInvalidData() {
        final LiveData<?> data = mock(LiveData.class);
        final LifecycleOwner owner = mock(LifecycleOwner.class);

        LiveDataOperator.removeDataObservers(data, owner);
        verify(data, never()).removeObservers(owner);
    }

    @Test
    public void verifyObserversNotRemovedFromValidDataWhenNotAvailable() {
        final LiveData<?> data = mock(LiveData.class);
        when(data.hasObservers()).thenReturn(false);
        final LifecycleOwner owner = mock(LifecycleOwner.class);

        LiveDataOperator.removeDataObservers(data, owner);
        verify(data, never()).removeObservers(owner);
    }

    @Test
    public void verifyObserversRemovedWhenResultIsValid() {
        final LiveData<?> data = mock(LiveData.class);
        when(data.hasObservers()).thenReturn(true);
        final LiveData<Throwable> error = mock(LiveData.class);
        when(error.hasObservers()).thenReturn(true);

        final LifecycleOwner owner = mock(LifecycleOwner.class);
        final LiveResult<?> result = new LiveResult<>(data, error);
        LiveDataOperator.removeResultObservers(result, owner);

        verify(data, times(1)).removeObservers(owner);
        verify(error, times(1)).removeObservers(owner);
    }

}
