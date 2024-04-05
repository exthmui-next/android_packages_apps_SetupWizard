package net.hearnsoft.setupwizard.views;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import net.hearnsoft.setupwizard.R;
import net.hearnsoft.setupwizard.databinding.FragmentMiscViewBinding;

import java.util.List;

public class OtherSettingsFragment extends Fragment {

    private FragmentMiscViewBinding binding;
    private String mGmsSettingsActivityName;
    private String mGmsSettingsPkgName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMiscViewBinding.inflate(getLayoutInflater());

        mGmsSettingsPkgName = getResources().getString(R.string.gms_package_name);
        mGmsSettingsActivityName = getResources().getString(R.string.google_settings_activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = binding.getRoot();
        binding.lockSettings.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.app.action.SET_NEW_PASSWORD");
            startActivity(intent);
        });
        binding.backupSettings.setOnClickListener(v -> {
            Intent backupIntent = new Intent();
            backupIntent.setClassName("com.android.settings",
                    "com.android.settings.backup.UserBackupSettingsActivity");
            startActivity(backupIntent);
        });
        binding.googleSettings.setOnClickListener(v -> {
            if (areGmsAvailable()) {
                Intent gmsIntent = new Intent().setComponent(getGmsComponentName());
                startActivity(gmsIntent);
            } else {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.gms_not_support)
                        .setMessage(R.string.gms_not_support_summary)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
        return view;
    }

    /** Returns whether Styles & Wallpaper is enabled and available. */
    public boolean areGmsAvailable() {
        return !TextUtils.isEmpty(mGmsSettingsActivityName)
                && canResolveGoogleComponent(mGmsSettingsActivityName);
    }

    public ComponentName getGmsComponentName() {
        return new ComponentName(mGmsSettingsPkgName, mGmsSettingsActivityName);
    }

    private boolean canResolveGoogleComponent(String className) {
        final ComponentName componentName = new ComponentName(mGmsSettingsPkgName, className);
        final PackageManager pm = requireContext().getPackageManager();
        final Intent intent = new Intent().setComponent(componentName);
        final List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0 /* flags */);
        return resolveInfos != null && !resolveInfos.isEmpty();
    }

}
