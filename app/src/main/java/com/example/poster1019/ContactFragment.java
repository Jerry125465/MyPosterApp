package com.example.poster1019;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    public ContactFragment() {
        super(R.layout.fragment_contact);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView companyIntroView = view.findViewById(R.id.textCompanyIntro);

        view.findViewById(R.id.buttonPersonalIntro).setOnClickListener(v ->
                companyIntroView.setText(R.string.personal_intro)
        );

        view.findViewById(R.id.layoutEmail).setOnClickListener(v ->
                openEmailApp(getString(R.string.contact_email))
        );
    }

    private void openEmailApp(String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailAddress));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), R.string.no_email_app, Toast.LENGTH_SHORT).show();
        }
    }
}
