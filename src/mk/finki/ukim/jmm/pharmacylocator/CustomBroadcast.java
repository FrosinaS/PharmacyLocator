package mk.finki.ukim.jmm.pharmacylocator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CustomBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, FillProviderService.class));
		
	}

}
