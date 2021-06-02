package com.intern.spacex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ClickListener{
    private final String TAG = MainActivity.class.getName();

    MainViewModel viewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
//    private Toolbar toolbar;
    private CrewAdapter adapter;
    private ViewStub emptyStateLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.refreshData();
        viewModel.setResponseListener(listener);
        subscribeToUI(viewModel.getAllCrewMember());
    }

    private void subscribeToUI(LiveData<List<CrewMember>> allCrewMember) {
        if (allCrewMember != null) {
            allCrewMember.observe(this, crewMembers -> {
                //adapter.submitList(testDifferentStatus(crewMembers));
                adapter.submitList(crewMembers);
                showEmptyStateLayout(crewMembers.isEmpty());
                if (!crewMembers.isEmpty()) {
                    Log.d(TAG, "subscribeToUI: " + crewMembers.toString());
                } else {
                    Log.d(TAG, "subscribeToUI: Empty List");
                }
            });
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
//        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);

//        setSupportActionBar(toolbar);
        adapter = new CrewAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(view -> {
            refreshData();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            showConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showConfirmationDialog() {
        AlertDialog dialog = ConfirmationDialog.getDialog(this, () -> viewModel.deleteAll());
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.red1));
        });
        dialog.show();
    }

    private void refreshData() {
        Toast.makeText(this, "Refreshing Data", Toast.LENGTH_SHORT).show();
        viewModel.refreshData();
    }

    @Override
    public void onItemClick(CrewMember member) {
        Log.d(TAG, "onItemClick: " + member.getName());
    }

    @Override
    public void openWiki(String wikiURL) {
        if (wikiURL.isEmpty()) return;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikiURL));
        startActivity(browserIntent);
    }

    private final ResponseCallback listener = new ResponseCallback() {
        @Override
        public void onSuccessful() {
            Toast.makeText(MainActivity.this, "List Has Been Updated", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onSuccessful: Data successfully fetched");
        }

        @Override
        public void onFailed(String error) {
            if(!adapter.getCurrentList().isEmpty()) {
                Toast.makeText(MainActivity.this, "Unable to fetch new data please refresh or check internet connection", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void showEmptyStateLayout(Boolean show) {
        if (show) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    /*testing the different status */
    private List<CrewMember> testDifferentStatus(List<CrewMember> crewMembers) {
        List<CrewMember> newList = new ArrayList<>();
        for (int i = 0; i < crewMembers.size(); i++) {
            int num = i % 4;
            newList.add(crewMembers.get(i));
            switch (num) {
                case 0:
                    newList.get(i).setStatus("active");
                    break;
                case 1:
                    newList.get(i).setStatus("inactive");
                    break;
                case 2:
                    newList.get(i).setStatus("retired");
                    break;
                case 3:
                    newList.get(i).setStatus("unknown");
                    break;
            }
        }
        return newList;
    }
}