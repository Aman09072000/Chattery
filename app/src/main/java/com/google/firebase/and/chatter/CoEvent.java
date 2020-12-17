package com.google.firebase.and.chatter;

public class CoEvent {
    /** Title of the earthquake event */
    public final String title;

    /** Time that the earthquake happened (in milliseconds) */
    public final String time;

    /** Whether or not a tsunami alert was issued (1 if it was issued, 0 if no alert was issued) */
    public final String tsunamiAlert;
    public final String recoveredAlert;
    public final String activeAlert;

    public CoEvent(String title, String time, String tsunamiAlert, String recoveredAlert, String activeAlert) {
        this.title = title;
        this.time = time;
        this.tsunamiAlert = tsunamiAlert;
        this.recoveredAlert = recoveredAlert;
        this.activeAlert = activeAlert;
    }



}
