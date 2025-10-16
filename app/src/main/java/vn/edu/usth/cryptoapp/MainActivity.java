package vn.edu.usth.cryptoapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import vn.edu.usth.cryptoapp.main.Home;
import vn.edu.usth.cryptoapp.main.Overview;
import vn.edu.usth.cryptoapp.main.Exchange;
import vn.edu.usth.cryptoapp.main.WatchList;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (navController != null) {
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                if (getSupportActionBar() == null) return;

                int id = destination.getId();

                if (id == R.id.nav_home) {
                    getSupportActionBar().setTitle("Top Crypto");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                } else if (id == R.id.nav_watch) {
                    getSupportActionBar().setTitle("My Watchlist");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                } else if (id == R.id.nav_exchange) {
                    getSupportActionBar().setTitle("Top Exchanges");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                } else if (id == R.id.nav_overview) {
                    getSupportActionBar().setTitle("Marker Overview");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                } else {
                    getSupportActionBar().setTitle("CryptoApp");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
            refreshCurrentFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void refreshCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment)
                .getChildFragmentManager()
                .getFragments()
                .get(0);

        if (currentFragment instanceof Home) {
            ((Home) currentFragment).onResume();
        } else if (currentFragment instanceof Overview) {
            ((Overview) currentFragment).onResume();
        } else if (currentFragment instanceof WatchList) {
            ((WatchList) currentFragment).onResume();
        } else if (currentFragment instanceof Exchange) {
            ((Exchange) currentFragment).onResume();
        }
    }
}
