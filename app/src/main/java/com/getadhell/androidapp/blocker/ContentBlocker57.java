package com.getadhell.androidapp.blocker;

import android.app.enterprise.EnterpriseDeviceManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.getadhell.androidapp.App;
import com.getadhell.androidapp.utils.DeviceUtils;
import com.sec.enterprise.AppIdentity;
import com.sec.enterprise.firewall.DomainFilterRule;
import com.sec.enterprise.firewall.Firewall;

import java.util.ArrayList;
import java.util.List;

public class ContentBlocker57 implements ContentBlocker {
    private static final String TAG = ContentBlocker57.class.getCanonicalName();
    private static ContentBlocker57 mInstance = null;
    private Firewall mFirewall;
    private ContentBlocker56 contentBlocker56;

    private ContentBlocker57() {
        Context context = App.get().getApplicationContext();
        EnterpriseDeviceManager mEnterpriseDeviceManager = DeviceUtils.getEnterpriseDeviceManager();
        mFirewall = mEnterpriseDeviceManager.getFirewall();
        contentBlocker56 = ContentBlocker56.getInstance();
    }

    private static synchronized ContentBlocker57 getSync() {
        if (mInstance == null) {
            mInstance = new ContentBlocker57();
        }
        return mInstance;
    }

    public static ContentBlocker57 getInstance() {
        if (mInstance == null) {
            mInstance = getSync();
        }
        return mInstance;
    }

    @Override
    public boolean enableBlocker() {
        if (contentBlocker56.enableBlocker()) {
            SharedPreferences sharedPreferences = App.get().getApplicationContext().getSharedPreferences("dnsAddresses", Context.MODE_PRIVATE);
            if (sharedPreferences.contains("dns1") && sharedPreferences.contains("dns2")) {
                String dns1 = sharedPreferences.getString("dns1", "8.8.8.8");
                String dns2 = sharedPreferences.getString("dns2", "8.8.4.4");
                this.setDns(dns1, dns2);
                Log.d(TAG, "Previous dns addresses has been applied. " + dns1 + " " + dns2);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean disableBlocker() {
        return contentBlocker56.disableBlocker();
    }

    @Override
    public boolean isEnabled() {
        return contentBlocker56.isEnabled();
    }

    public void setDns(String dns1, String dns2) {
        DomainFilterRule domainFilterRule = new DomainFilterRule(new AppIdentity(Firewall.FIREWALL_ALL_PACKAGES, null));
        domainFilterRule.setDns1(dns1);
        domainFilterRule.setDns2(dns2);
        List<DomainFilterRule> rules = new ArrayList<>();
        rules.add(domainFilterRule);
        mFirewall.addDomainFilterRules(rules);
        Log.d(TAG, "DNS1: " + domainFilterRule.getDns1());
        Log.d(TAG, "DNS2: " + domainFilterRule.getDns2());
    }
}
