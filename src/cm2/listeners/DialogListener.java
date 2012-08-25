package cm2.listeners;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogListener implements OnClickListener {

	Activity actv;
//	Dialog dlg;
	Builder dialog;
	int type;
	
	public DialogListener(Activity actv, Builder dialog, int type) {
		
		this.actv = actv;
		this.dialog = dialog;
		this.type = type;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		/*----------------------------
		 * Steps
			----------------------------*/
		switch (type) {
		
		case 0:	// OK
			
			dialog.dismiss();
			actv.finish();
			
			actv.overridePendingTransition(0, 0);
			
			break;
			
		case 1: // Cancel
			
			dialog.dismiss();
			
			break;
		
		}//switch (type)
		
	}//public void onClick(DialogInterface dialog, int which)
}
