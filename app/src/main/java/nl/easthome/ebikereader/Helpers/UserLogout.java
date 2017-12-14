package nl.easthome.ebikereader.Helpers;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import nl.easthome.ebikereader.MainActivity;
import nl.easthome.ebikereader.R;

public class UserLogout {

    public static void showUserLogoutDialogs(Activity dashboardActivity) {
        final Activity context = dashboardActivity;

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(context)
                        .icon(context.getDrawable(R.drawable.ic_warning_orange))
                        .title("You are about to logout.")
                        .content("You can decide how we handle your data: \n 1. Remove only the data on your phone. \n 2. Remove the data on your phone and the server.")
                        .positiveText("1. REMOVE PHONEDATA")
                        .negativeText("2. REMOVE SERVERDATA")
                        .neutralText("Do not logout")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new MaterialDialog.Builder(context)
                                        .icon(context.getDrawable(R.drawable.ic_warning_orange))
                                        .title("Are you sure?")
                                        .content("If you proceed you will remove ALL data from your phone, but leave the data on the server. \n So when you log back in you still have your rides.")
                                        .positiveText("LOGOUT")
                                        .negativeText("Go back")
                                        .cancelable(false)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                UserLogout.executeLogout(false, context);
                                            }
                                        })
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new MaterialDialog.Builder(context)
                                        .icon(context.getDrawable(R.drawable.ic_warning_orange))
                                        .title("Are you sure?")
                                        .content("If you proceed you will remove ALL data from your phone and the server. \n Next time you login, you will be treated as a new user.")
                                        .positiveText("LOGOUT")
                                        .negativeText("Go back")
                                        .cancelable(false)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                UserLogout.executeLogout(true, context);
                                            }
                                        })
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        })
                        .show();
            }
        });
    }

    private static void returnToMainActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    private static void executeLogout(boolean removeServerData, final Activity context) {
        if (removeServerData) {
            String UID = FirebaseAuth.getInstance().getUid();
            FirebaseSaver.removeUserData(UID, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    FirebaseAuth.getInstance().signOut();
                    returnToMainActivity(context);
                }
            });
        } else {
            FirebaseAuth.getInstance().signOut();
            returnToMainActivity(context);
        }
    }
}
