package kr.co.adflow.push.client.mqttv3.util;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by nadir93 on 15. 10. 14..
 */
@RunWith(MockitoJUnitRunner.class)
public class IF_A_101_Test {

    private SharedPreferenceEntry mSharedPreferenceEntry;

    private SharedPreferencesHelper mMockSharedPreferencesHelper;

    private SharedPreferencesHelper mMockBrokenSharedPreferencesHelper;

    @Mock
    SharedPreferences mMockSharedPreferences;

    @Mock
    SharedPreferences mMockBrokenSharedPreferences;

    @Mock
    SharedPreferences.Editor mMockEditor;

    @Mock
    SharedPreferences.Editor mMockBrokenEditor;

    @Before
    public void initMocks() {
        // Create SharedPreferenceEntry to persist.
        mSharedPreferenceEntry = new SharedPreferenceEntry("0123456789", "tcp://202.30.38.14", 1883, 240, false, false);

        // Create a mocked SharedPreferences.
        mMockSharedPreferencesHelper = createMockSharedPreference();

        // Create a mocked SharedPreferences that fails at saving data.
        mMockBrokenSharedPreferencesHelper = createBrokenMockSharedPreference();
    }

    @Test
    public void registerToken() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void sharedPreferencesHelper_SaveAndReadPersonalInformation() {
        // Save the personal information to SharedPreferences
        boolean success = mMockSharedPreferencesHelper.saveMqttInfo(mSharedPreferenceEntry);

        System.out.println("저장된속성 : " + mSharedPreferenceEntry);

        assertThat("Checking that SharedPreferenceEntry.save... returns true",
                success, is(true));

        // Read personal information from SharedPreferences
        SharedPreferenceEntry savedSharedPreferenceEntry =
                mMockSharedPreferencesHelper.getMqttInfo();

        // Make sure both written and retrieved personal information are equal.
        assertThat("Checking that SharedPreferenceEntry.name has been persisted and read correctly",
                mSharedPreferenceEntry.getToken(),
                is(equalTo(savedSharedPreferenceEntry.getToken())));

        assertThat("Checking that SharedPreferenceEntry.dateOfBirth has been persisted and read "
                        + "correctly",
                mSharedPreferenceEntry.getServer(),
                is(equalTo(savedSharedPreferenceEntry.getServer())));

        assertThat("Checking that SharedPreferenceEntry.email has been persisted and read "
                        + "correctly",
                mSharedPreferenceEntry.isCleanSession(),
                is(equalTo(savedSharedPreferenceEntry.isCleanSession())));
    }

    /**
     * Creates a mocked SharedPreferences.
     */
    private SharedPreferencesHelper createMockSharedPreference() {
        // Mocking reading the SharedPreferences as if mMockSharedPreferences was previously written
        // correctly.
        when(mMockSharedPreferences.getString(eq(SharedPreferenceEntry.TOKEN), anyString()))
                .thenReturn(mSharedPreferenceEntry.getToken());
        when(mMockSharedPreferences.getString(eq(SharedPreferenceEntry.SERVER), anyString()))
                .thenReturn(mSharedPreferenceEntry.getServer());
        when(mMockSharedPreferences.getInt(eq(SharedPreferenceEntry.PORT), anyInt()))
                .thenReturn(mSharedPreferenceEntry.getPort());
        when(mMockSharedPreferences.getInt(eq(SharedPreferenceEntry.KEEP_ALIVE), anyInt()))
                .thenReturn(mSharedPreferenceEntry.getKeepAlive());
        when(mMockSharedPreferences.getBoolean(eq(SharedPreferenceEntry.CLEAN_SESSION), anyBoolean()))
                .thenReturn(mSharedPreferenceEntry.isCleanSession());

        // Mocking a successful commit.
        when(mMockEditor.commit()).thenReturn(true);

        // Return the MockEditor when requesting it.
        when(mMockSharedPreferences.edit()).thenReturn(mMockEditor);
        return new SharedPreferencesHelper(mMockSharedPreferences);
    }

    /**
     * Creates a mocked SharedPreferences that fails when writing.
     */
    private SharedPreferencesHelper createBrokenMockSharedPreference() {
        // Mocking a commit that fails.
        when(mMockBrokenEditor.commit()).thenReturn(false);

        // Return the broken MockEditor when requesting it.
        when(mMockBrokenSharedPreferences.edit()).thenReturn(mMockBrokenEditor);
        return new SharedPreferencesHelper(mMockBrokenSharedPreferences);
    }
}
