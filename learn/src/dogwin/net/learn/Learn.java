package dogwin.net.learn;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Learn extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_learn, menu);
        return true;
    }
}
