package cm2.listeners;

import android.app.Activity;
import android.view.View;
import android.view.View.OnLongClickListener;

public class CustomOnLongClickListener implements OnLongClickListener {

	//
	Activity actv;
	
	//
	int position;
	
	public CustomOnLongClickListener(Activity actv, int position) {
		
		this.actv = actv;
		this.position = position;
		
	}

	@Override
	public boolean onLongClick(View v) {
		
//		Methods.ItemLongClickTags tag = (Methods.ItemLongClickTags) v.getTag();
		
//			switch (tag) {
//			
//			}//switch (tag)

		return true;
	}//public boolean onLongClick(View arg0)

}//public class CustomOnLongClickListener implements OnLongClickListener
