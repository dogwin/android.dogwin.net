package dogwin.net.apps;

import java.sql.Date;

import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Example extends Activity {
	
	private TextView showv;
	private EditText v1;
	private EditText v2;
	private Button bt;
	private final static int REQUESTCODE=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example);
		showv = (TextView)this.findViewById(R.id.showv);
		v1 = (EditText)this.findViewById(R.id.v1);
		v2 = (EditText)this.findViewById(R.id.v2);
		bt = (Button)this.findViewById(R.id.bt);
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Example.this,other.class);
				startActivityForResult(intent, REQUESTCODE);
				
				int v1_G = Integer.parseInt(v1.getText().toString());
				int v2_G = Integer.parseInt(v2.getText().toString());
				intent.putExtra("v1_G", v1_G);
				intent.putExtra("v2_G", v2_G);
				startActivity(intent);
				
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			if(requestCode==REQUESTCODE){
				//data.getIntExtra("res", 0);
				showv.setText(data.getIntExtra("res", 0));
			}
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.example, menu);
		return true;
	}

}
