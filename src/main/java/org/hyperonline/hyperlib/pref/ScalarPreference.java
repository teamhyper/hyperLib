package org.hyperonline.hyperlib.pref;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Scalar;

/**
 * {@link ScalarPreference}
 * 
 * @author James
 *
 */
public class ScalarPreference {

    private List<DoublePreference> m_prefs = new ArrayList<>();

    /**
     * Construct a scalar preference with the given name and components.
     * 
     * @param name
     *            The name of the preferences object.
     * @param components
     *            A sequence of letters indicating the components. For example,
     *            "RGB", "HSV", "XYZ", etc. It may be any length.
     * @param defaults
     *            Default settings for components
     */
    public ScalarPreference(String name, String components, double... defaults) {
        if (defaults.length != components.length()) {
            throw new IllegalArgumentException("Components and defaults have different lengths");
        }

        for (int i = 0; i < defaults.length; i++) {
            String childName = name + PreferencesSet.SEPARATOR + components.charAt(i);
            m_prefs.add(new DoublePreference(childName, defaults[i]));
        }
    }

    /**
     * Construct a scalar preference, while also updating the right info inside
     * the parent set.
     */
    ScalarPreference(PreferencesSet parent, String name, String components, double[] defaults) {
        if (defaults.length != components.length()) {
            throw new IllegalArgumentException("Components and defaults have different lengths");
        }

        for (int i = 0; i < defaults.length; i++) {
            String childName = name + PreferencesSet.SEPARATOR + components.charAt(i);
            m_prefs.add(parent.addDouble(childName, defaults[i]));
        }
    }

    /**
     * Get the current value of the preference.
     * 
     * @return The current value of the preference.
     */
    public Scalar get() {
        return new Scalar(m_prefs.stream().mapToDouble(DoublePreference::get).toArray());
    }

}
