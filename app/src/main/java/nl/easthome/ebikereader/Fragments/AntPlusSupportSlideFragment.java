package nl.easthome.ebikereader.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import nl.easthome.antpluslibary.AntPlusSupportChecker;
import nl.easthome.ebikereader.R;

public class AntPlusSupportSlideFragment extends Fragment {


    public AntPlusSupportSlideFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ant_plus_support_slide, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            checkAntSupport();
        }
    }

    private void checkAntSupport() {
        switch (AntPlusSupportChecker.getAntSupportedState(getContext())){
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
                                Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.ant_service_app_market_link) ));
                                startActivity(i);
                                getActivity().finish();
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
                                Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.ant_plugin_app_market_link) ));
                                startActivity(i);
                                getActivity().finish();
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
                                Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.ant_service_app_market_link) ));
                                startActivity(i);
                                getActivity().finish();
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
                                Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.ant_plugin_app_market_link) ));
                                startActivity(i);
                                getActivity().finish();
                            }
                        }).show();
                break;
            case ANT_SUPPORTED:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.intro_dialog_antsupport_title)
                        .content(R.string.intro_dialog_antsupport_positive_content)
                        .positiveText(R.string.intro_dialog_antsupport_positive_button_text)
                        .show();
                break;
            default:
                break;
        }
    }
}
