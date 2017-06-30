// IServicePlayer.aidl
package sam.musicplayer;

// Declare any non-default types here with import statements

interface IServicePlayer {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    void play(int args);
    void pause();
    void stop();
    int getDuration();
    int getCurrentPosition();
    void seekTo(int current);
    boolean setLoop(boolean loop);
}
