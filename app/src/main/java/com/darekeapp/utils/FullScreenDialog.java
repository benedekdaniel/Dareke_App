package com.darekeapp.utils;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.darekeapp.R;

public class FullScreenDialog extends DialogFragment {
    private Toolbar toolbar;

    public static FullScreenDialog display(FragmentManager fragmentManager) {
        FullScreenDialog fullScreenDialog = new FullScreenDialog();
        fullScreenDialog.show(fragmentManager, "example_dialog");
        return fullScreenDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.example_dialog, container, false);

        toolbar = view.findViewById(R.id.toolbar);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenDialog.this.dismiss();
            }
        });
        toolbar.setTitle("Some Title");
        toolbar.inflateMenu(R.menu.full_screen_dialog);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FullScreenDialog.this.dismiss();
                return true;
            }
        });
    }
}
