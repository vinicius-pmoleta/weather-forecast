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
        assertFalse(LiveDataOperator.isContentAvailable(null));
    }

    @Test
    public void assertContentIsNotAvailableWhenLiveDataValueIsNull() {
        final LiveData<String> data = mock(LiveData.class);
        when(data.getValue()).thenReturn(null);
        assertFalse(LiveDataOperator.isContentAvailable(data));
    }

    @Test
    public void assertDataIsAvailableOnHolder() {
        final LiveData<String> data = mock(LiveData.class);
        when(data.getValue()).thenReturn("");
        assertTrue(LiveDataOperator.isContentAvailable(data));
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


}
