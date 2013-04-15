package dogwin.net.check;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;


public class UserCurrent extends PreferenceActivity{

	public EditTextPreference editText;
	public SharedPreferences preferences;
	
	public void SaveUC(String username,String password,boolean flag) {
	    preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);
	    SharedPreferences.Editor preferencesEditor = preferences.edit();
	    preferencesEditor.putString("username", username);
	    preferencesEditor.putString("password", password);
	    preferencesEditor.putBoolean("flag", flag);
	    preferencesEditor.commit();
	}
	
	
	public String Rt_username(){
		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);
	    return preferences.getString("username", null);
	}
	
	
	public String Rt_password(){
		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);
	    return preferences.getString("password", null);
	}
	
	public boolean Rt_flag(){
		preferences = getSharedPreferences("usercurrent", Activity.MODE_PRIVATE);
		return preferences.getBoolean("flag", false);
	}
}
