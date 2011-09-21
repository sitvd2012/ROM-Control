
package com.roman.tweaks.activities;

import com.roman.tweaks.R;
import com.roman.tweaks.utils.ShortcutPickHelper;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.widget.Toast;

import java.io.File;

public class LockscreenActivity extends PreferenceActivity implements OnPreferenceChangeListener,
        ShortcutPickHelper.OnPickListener {

    private static final String LOCKSCREEN_STYLE_PREF = "pref_lockscreen_style";

    private static final String LOCKSCREEN_QUADRANT_1_PREF = "pref_quadrant_1";

    private static final String LOCKSCREEN_QUADRANT_2_PREF = "pref_quadrant_2";

    private static final String LOCKSCREEN_QUADRANT_3_PREF = "pref_quadrant_3";

    private static final String LOCKSCREEN_QUADRANT_4_PREF = "pref_quadrant_4";
    
    private static final String LOCKSCREEN_CLOCK_PREF = "pref_clock";

    private LockscreenStyle mLockscreenStyle;

    private ListPreference mLockscreenStylePref;
    
    private CheckBoxPreference mShowHoneyClock;

    private Preference mHoneyQuadrant1Pref;

    private Preference mHoneyQuadrant2Pref;

    private Preference mHoneyQuadrant3Pref;

    private Preference mHoneyQuadrant4Pref;

    private Preference mCurrentCustomActivityPreference;

    private String mCurrentCustomActivityString;

    private ShortcutPickHelper mPicker;

    enum LockscreenStyle {
        Slider, Rotary, RotaryRevamped, Lense, Honeycomb;

        static public LockscreenStyle getStyleById(int id) {
            switch (id) {
                case 1:
                    return Slider;
                case 2:
                    return Rotary;
                case 3:
                    return RotaryRevamped;
                case 4:
                    return Lense;
                case 5:
                    return Honeycomb;
                default:
                    return RotaryRevamped;
            }
        }

        static public LockscreenStyle getStyleById(String id) {
            return getStyleById(Integer.valueOf(id));
        }

        static public int getIdByStyle(LockscreenStyle lockscreenstyle) {
            switch (lockscreenstyle) {
                case Slider:
                    return 1;
                case Rotary:
                    return 2;
                case RotaryRevamped:
                    return 3;
                case Lense:
                    return 4;
                case Honeycomb:
                    return 5;
                default:
                    return 3;
            }
        }
    }

    public void onCreate(Bundle ofLove) {
        super.onCreate(ofLove);
        addPreferencesFromResource(R.xml.lockscreen_prefs);

        PreferenceScreen prefSet = getPreferenceScreen();

        /* Lockscreen Style and related related settings */
        mLockscreenStylePref = (ListPreference) prefSet.findPreference(LOCKSCREEN_STYLE_PREF);
        mLockscreenStyle = LockscreenStyle.getStyleById(Settings.System.getInt(
                getContentResolver(), "tweaks_lockscreen_style", 3));
        mLockscreenStylePref
                .setValue(String.valueOf(LockscreenStyle.getIdByStyle(mLockscreenStyle)));
        mLockscreenStylePref.setOnPreferenceChangeListener(this);

        mHoneyQuadrant1Pref = prefSet.findPreference(LOCKSCREEN_QUADRANT_1_PREF);
        mHoneyQuadrant2Pref = prefSet.findPreference(LOCKSCREEN_QUADRANT_2_PREF);
        mHoneyQuadrant3Pref = prefSet.findPreference(LOCKSCREEN_QUADRANT_3_PREF);
        mHoneyQuadrant4Pref = prefSet.findPreference(LOCKSCREEN_QUADRANT_4_PREF);
        
        mShowHoneyClock = (CheckBoxPreference) prefSet.findPreference(LOCKSCREEN_CLOCK_PREF);
        
        mPicker = new ShortcutPickHelper(this, this);
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if (preference == mHoneyQuadrant1Pref) {
            mCurrentCustomActivityPreference = preference;
            mCurrentCustomActivityString = "tweaks_lockscreen_hc_activity_1";
            mPicker.pickShortcut();
            return true;

        } else if (preference == mHoneyQuadrant2Pref) {
            mCurrentCustomActivityPreference = preference;
            mCurrentCustomActivityString = "tweaks_lockscreen_hc_activity_2";
            mPicker.pickShortcut();
            return true;

        } else if (preference == mHoneyQuadrant3Pref) {
            mCurrentCustomActivityPreference = preference;
            mCurrentCustomActivityString = "tweaks_lockscreen_hc_activity_3";
            mPicker.pickShortcut();
            return true;

        } else if (preference == mHoneyQuadrant4Pref) {
            mCurrentCustomActivityPreference = preference;
            mCurrentCustomActivityString = "tweaks_lockscreen_hc_activity_4";
            mPicker.pickShortcut();
            return true;
        } else if (preference == mShowHoneyClock) {
            boolean checked = ((CheckBoxPreference) preference).isChecked();
            int value = (checked ? 1 : 0);
            
            Settings.System.putInt(getContentResolver(),
                    "tweaks_lockscreen_hc_clock_enabled", value);
        }

        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPicker.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String val = newValue.toString();
        if (preference == mLockscreenStylePref) {
            mLockscreenStyle = LockscreenStyle.getStyleById((String) newValue);
            Settings.System.putInt(getContentResolver(), "tweaks_lockscreen_style",
                    LockscreenStyle.getIdByStyle(mLockscreenStyle));
            // updateStylePrefs(mLockscreenStyle, mInCallStyle);
            return true;
        }

        return false;
    }

    public void shortcutPicked(String uri, String friendlyName, boolean isApplication) {
        if (Settings.System.putString(getContentResolver(), mCurrentCustomActivityString, uri)) {
            mCurrentCustomActivityPreference.setSummary(friendlyName);
        }
    }

}
