package dogwin.net.apps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;


public class other extends Activity{
	
	private myapp myapp;
	private EditText editText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		editText = (EditText)this.findViewById(R.id.edit);
		myapp = (myapp)getApplication();
		editText.setText(myapp.getName());
		
	}
}
