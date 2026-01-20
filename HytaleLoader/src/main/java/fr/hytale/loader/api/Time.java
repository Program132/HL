package fr.hytale.loader.api;

/**
 * Represents specific times of day in Hytale.
 * <p>
 * This enum makes it easier to set the world time to common presets.
 * Values assume a standard 24-hour cycle where 0.0 is midnight and 0.5 is noon.
 * </p>
 *
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.6
 */
public enum Time {

    /** 06:00 AM - Early morning */
    DAWN(0.25f),
    /** 08:00 AM - Morning */
    MORNING(0.33f),
    /** 12:00 PM - High noon */
    NOON(0.50f),
    /** 16:00 PM - Afternoon */
    AFTERNOON(0.66f),
    /** 18:00 PM - Evening/Sunset */
    DUSK(0.75f),
    /** 00:00 AM - Midnight */
    MIDNIGHT(0.0f);

    private final float percent;

    Time(float percent) {
        this.percent = percent;
    }

    /**
     * Gets the time value as a percentage of the day (0.0 to 1.0).
     *
     * @return the time percentage (0.0 = midnight, 0.5 = noon)
     */
    public float getPercent() {
        return percent;
    }
}
