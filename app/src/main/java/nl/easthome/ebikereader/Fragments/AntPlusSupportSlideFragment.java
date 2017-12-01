package nl.easthome.ebikereader.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.paolorotolo.appintro.ISlidePolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.easthome.antpluslibary.AntPlusSupportChecker;
import nl.easthome.ebikereader.R;

public class AntPlusSupportSlideFragment extends Fragment implements ISlidePolicy {
    @BindView(R.id.antCheckButton) Button mAntCheckButton;
    private boolean isAntCheckDone = false;

    public AntPlusSupportSlideFragment() {
    }

    @OnClick(R.id.antCheckButton) public void onAntCheckButtonClick(){
        checkAntSupport();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ant_plus_support_slide, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    //TODO move text to strings.xml
    private void checkAntSupport() {
        if (AntPlusSupportChecker.isDeviceEmulator()) {
            new MaterialDialog.Builder(getContext())
                    .title("This looks like an Emulator.")
                    .content("That means it has no ant+ support, but im still gonna let you continue for testing.")
                    .positiveText("OK")
                    .negativeText("Exit app")
                    .cancelable(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            isAntCheckDone = true;
                            mAntCheckButton.setText("Done!");
                            mAntCheckButton.setEnabled(false);
                            dialog.dismiss();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            getActivity().finish();
                        }
                    }).show();
        } else {
            switch (AntPlusSupportChecker.getAntSupportedState(getContext())) {
                case ANT_NO_CHIP_OR_USB:
                    new MaterialDialog.Builder(getContext())
                            .title(R.string.intro_dialog_antsupport_title)
                            .content(R.string.intro_dialog_antsupport_negative_content)
                            .positiveText(R.string.intro_dialog_antsupport_negative_finddevice_button_text)
                            .negativeText(R.string.intro_dialog_antsupport_negative_exit_button_text)
                            .cancelable(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.intro_dialog_antsupport_negative_finddevice_link)));
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    getActivity().finish();
                                }
                            }).show();
                    break;
                case ANT_SUPPORTED_BUT_NO_RADIO:
                    new MaterialDialog.Builder(getContext())
                            .title(R.string.dialog_antcheck_ant_service_missing_title)
                            .content(R.string.dialog_antcheck_ant_service_missing_content)
                            .positiveText(R.string.dialog_antcheck_button_download_service)
                            .neutralText(R.string.dialog_antcheck_button_exit_app)
                            .cancelable(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.ant_service_app_market_link)));
                                    startActivity(i);
                                }
                            })
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    getActivity().finish();
                                }
                            })
                            .show();
                    break;
                case ANT_SUPPORTED_BUT_NO_PLUGIN:
                    new MaterialDialog.Builder(getContext())
                            .title(R.string.dialog_antcheck_ant_plugin_missing_title)
                            .content(R.string.dialog_antcheck_ant_plugin_missing_content)
                            .negativeText(R.string.dialog_antcheck_button_download_plugin)
                            .neutralText(R.string.dialog_antcheck_button_exit_app)
                            .cancelable(false)
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    getActivity().finish();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.ant_plugin_app_market_link)));
                                    startActivity(i);
                                }
                            }).show();
                    break;
                case ANT_SUPPORTED_BUT_NO_RADIO_AND_PLUGIN:
                    new MaterialDialog.Builder(getContext())
                            .title(R.string.dialog_antcheck_ant_service_and_plugin_missing_title)
                            .content(R.string.dialog_antcheck_ant_service_and_plugin_missing_content)
                            .positiveText(R.string.dialog_antcheck_button_download_service)
                            .negativeText(R.string.dialog_antcheck_button_download_plugin)
                            .cancelable(false)
                            .neutralText(R.string.dialog_antcheck_button_exit_app)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.ant_service_app_market_link)));
                                    startActivity(i);
                                }
                            })
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    getActivity().finish();
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.ant_plugin_app_market_link)));
                                    startActivity(i);
                                }
                            }).show();
                    break;
                case ANT_SUPPORTED:
                    new MaterialDialog.Builder(getContext())
                            .title(R.string.intro_dialog_antsupport_title)
                            .content(R.string.intro_dialog_antsupport_positive_content)
                            .positiveText(R.string.intro_dialog_antsupport_positive_button_text)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mAntCheckButton.setText("Done!");
                                    isAntCheckDone = true;
                                    mAntCheckButton.setEnabled(false);
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean isPolicyRespected() {
        return isAntCheckDone;
    }

    //TODO move text to strings.xml
    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(getContext(), "Please check Ant+ support before continuing.", Toast.LENGTH_LONG).show();

    }
}
