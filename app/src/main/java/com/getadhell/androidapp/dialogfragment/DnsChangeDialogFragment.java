package com.getadhell.androidapp.dialogfragment;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getadhell.androidapp.R;
import com.getadhell.androidapp.blocker.ContentBlocker57;
import com.getadhell.androidapp.utils.DeviceUtils;

public class DnsChangeDialogFragment extends DialogFragment {


    private EditText mDns1EditText;
    private EditText mDns2EditText;
    private Button mSetDnsButton;
    private Button mCancelButton;

    public DnsChangeDialogFragment() {

    }

    public static DnsChangeDialogFragment newInstance(String title) {
        DnsChangeDialogFragment dnsChangeDialogFragment = new DnsChangeDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        dnsChangeDialogFragment.setArguments(args);
        return dnsChangeDialogFragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_dns, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDns1EditText = (EditText) view.findViewById(R.id.dns_address_1);
        mDns2EditText = (EditText) view.findViewById(R.id.dns_address_2);
        final SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("dnsAddresses", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("dns1") && sharedPreferences.contains("dns2")) {
            Toast.makeText(view.getContext(), "Loading saved dns addresses", Toast.LENGTH_SHORT).show();
            mDns1EditText.setText(sharedPreferences.getString("dns1", "8.8.8.8"));
            mDns2EditText.setText(sharedPreferences.getString("dns2", "8.8.4.4"));
        }


        mSetDnsButton = (Button) view.findViewById(R.id.changeDnsOkButton);
        mCancelButton = (Button) view.findViewById(R.id.changeDnsCancelButton);
        mDns1EditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        mSetDnsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dns1 = mDns1EditText.getText().toString();
                String dns2 = mDns2EditText.getText().toString();
                if (!Patterns.IP_ADDRESS.matcher(dns1).matches() || !Patterns.IP_ADDRESS.matcher(dns2).matches()) {
                    Toast.makeText(v.getContext(), "Check your input data. ", Toast.LENGTH_LONG).show();
                    return;
                }
                ContentBlocker57 contentBlocker57 = (ContentBlocker57) DeviceUtils.getContentBlocker(getActivity());
                if (!contentBlocker57.isEnabled()) {
                    Toast.makeText(v.getContext(), "Adhell Must be enabled. ", Toast.LENGTH_LONG).show();
                }
                contentBlocker57.setDns(dns1, dns2);
                Toast.makeText(v.getContext(), "Dns addresses has been changed", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("dns1", dns1);
                editor.putString("dns2", dns2);
                editor.apply();
                dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
