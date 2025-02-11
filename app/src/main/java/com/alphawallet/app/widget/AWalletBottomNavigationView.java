package com.alphawallet.app.widget;

import static com.alphawallet.app.entity.WalletPage.ACTIVITY;
import static com.alphawallet.app.entity.WalletPage.SETTINGS;
import static com.alphawallet.app.entity.WalletPage.WALLET;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.alphawallet.app.R;
import com.alphawallet.app.entity.WalletPage;
import com.alphawallet.app.security.SecurityManager;

import java.util.ArrayList;

public class AWalletBottomNavigationView extends LinearLayout
{
    private final TextView walletLabel;
    private final TextView settingsBadge;
    private final TextView settingsLabel;
    private final RelativeLayout settingsTab;
    private final TextView activityLabel;
    private final Typeface regularTypeface;
    private final Typeface semiboldTypeface;
    private final ArrayList<String> settingsBadgeKeys = new ArrayList<>();
    private OnBottomNavigationItemSelectedListener listener;
    private WalletPage selectedItem;
    private SecurityManager securityManager;
    private boolean isAuthenticated = false;

    public AWalletBottomNavigationView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        inflate(context, R.layout.layout_bottom_navigation, this);
        walletLabel = findViewById(R.id.nav_wallet_text);
        activityLabel = findViewById(R.id.nav_activity_text);
        settingsTab = findViewById(R.id.settings_tab);
        settingsLabel = findViewById(R.id.nav_settings_text);
        settingsBadge = findViewById(R.id.settings_badge);

        walletLabel.setOnClickListener(v -> selectItem(WALLET));
        activityLabel.setOnClickListener(v -> selectItem(ACTIVITY));
        settingsTab.setOnClickListener(v -> selectItem(SETTINGS));

        regularTypeface = ResourcesCompat.getFont(getContext(), R.font.font_regular);
        semiboldTypeface = ResourcesCompat.getFont(getContext(), R.font.font_semibold);

        // set wallet fragment selected on start
        setSelectedItem(WALLET);

        securityManager = new SecurityManager(context);
        initializeSecurityFeatures();
    }

    private void initializeSecurityFeatures() {
        // Prevent screen recording/screenshots
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                           WindowManager.LayoutParams.FLAG_SECURE);
                           
        // Start inactivity timer
        startInactivityMonitor();
    }

    public void setListener(OnBottomNavigationItemSelectedListener listener)
    {
        this.listener = listener;
    }

    private void selectItem(WalletPage index)
    {
        // Add security check before navigation
        if (requiresAuth(index) && !securityManager.isAuthenticated()) {
            securityManager.requestAuthentication(success -> {
                if (success && listener != null) {
                    listener.onBottomNavigationItemSelected(index);
                }
            });
            return;
        }
        if (listener != null)
        {
            listener.onBottomNavigationItemSelected(index);
        }
    }

    private boolean requiresAuth(WalletPage page) {
        return page == WALLET || page == SETTINGS;
    }

    public WalletPage getSelectedItem()
    {
        return selectedItem;
    }

    public void setSelectedItem(WalletPage index)
    {
        deselectAll();
        selectedItem = index;
        switch (index)
        {
            case WALLET:
                walletLabel.setSelected(true);
                walletLabel.setTypeface(semiboldTypeface);
                break;
            case ACTIVITY:
                activityLabel.setSelected(true);
                activityLabel.setTypeface(semiboldTypeface);
                break;
            case SETTINGS:
                settingsLabel.setSelected(true);
                settingsLabel.setTypeface(semiboldTypeface);
                break;
        }
    }

    private void deselectAll()
    {
        walletLabel.setSelected(false);
        walletLabel.setTypeface(regularTypeface);
        settingsLabel.setSelected(false);
        settingsLabel.setTypeface(regularTypeface);
        activityLabel.setSelected(false);
        activityLabel.setTypeface(regularTypeface);
    }

    public void setSettingsBadgeCount(int count)
    {
        if (count > 0)
        {
            settingsBadge.setVisibility(View.VISIBLE);
        }
        else
        {
            settingsBadge.setVisibility(View.GONE);
        }
        settingsBadge.setText(String.valueOf(count));
    }

    public void addSettingsBadgeKey(String key)
    {
        if (!settingsBadgeKeys.contains(key))
        {
            settingsBadgeKeys.add(key);
        }
        showOrHideSettingsBadge();
    }

    public void removeSettingsBadgeKey(String key)
    {
        settingsBadgeKeys.remove(key);
        showOrHideSettingsBadge();
    }

    private void showOrHideSettingsBadge()
    {
        if (settingsBadgeKeys.size() > 0)
        {
            settingsBadge.setVisibility(View.VISIBLE);
        }
        else
        {
            settingsBadge.setVisibility(View.GONE);
        }
        settingsBadge.setText(String.valueOf(settingsBadgeKeys.size()));
    }

    public void hideBrowserTab() 
    {
        // Implementation can be added based on requirements
    }

    public interface OnBottomNavigationItemSelectedListener
    {
        boolean onBottomNavigationItemSelected(WalletPage index);
    }
}

public class SecurityManager {
    private static final long SESSION_TIMEOUT = 5 * 60 * 1000; // 5 minutes
    private static final int MAX_ATTEMPTS = 3;
    
    public void encryptWalletData(byte[] data) {
        // Use AndroidKeyStore for key storage
        // Implement AES-256 encryption
    }
}
i
public class TransactionManager {
    public void confirmTransaction(Transaction tx) {
        // Require biometric/PIN verification
        // Implement transaction signing
        // Show confirmation dialog with details
    }
}
