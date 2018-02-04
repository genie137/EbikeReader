package nl.easthome.ebikereader.Helpers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;


/**
 * Utility class to help Firebase Database events get processed on a thread
 * determined by an Executor, rather than the main thread.  Use this when
 * your processing may block or take more time on the main thread than is
 * healthy for your app.
 * <p>
 * The {@link #onDataChange(DataSnapshot)} callback will trigger a call
 * to {@link #onDataChangeExecutor(DataSnapshot)}, executing on the Executor
 * with the same DataSnapshot argument.  Similarly, the
 * {@link #onCancelled(DatabaseError)} callback will trigger a call to
 * {@link #onDataChangeExecutor(DataSnapshot)}.
 * <p>
 * <p>
 * SOURCE: https://github.com/CodingDoug/white-label-event-app/blob/917ff279febce1977635226fe9181cc1ff099656/app/src/main/java/com/hyperaware/conference/android/fdb/ExecutorValueEventListener.java
 */
public abstract class FirebaseExecutorEventListener implements ValueEventListener {

    protected final Executor executor;

    public FirebaseExecutorEventListener(final Executor executor) {
        this.executor = executor;
    }

    @Override
    public final void onDataChange(final DataSnapshot dataSnapshot) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                onDataChangeExecutor(dataSnapshot);
            }
        });
    }

    @Override
    public final void onCancelled(final DatabaseError databaseError) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                onCancelledExecutor(databaseError);
            }
        });
    }

    protected abstract void onDataChangeExecutor(DataSnapshot dataSnapshot);

    protected abstract void onCancelledExecutor(DatabaseError databaseError);

}

