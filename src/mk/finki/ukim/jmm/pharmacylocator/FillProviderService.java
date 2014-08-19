package mk.finki.ukim.jmm.pharmacylocator;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;

public class FillProviderService extends Service{

	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		LongOperation lng=new LongOperation(getApplicationContext());
		  lng.execute("http://www.replek.com.mk/Farm/lekoviRezultati.asp?nacin=6", "http://www.replek.com.mk/Farm/lekoviRezultati.asp?nacin=7");
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class LongOperation extends AsyncTask<String, Void, Elements> {
        Context context;
			public LongOperation(Context con)
			{
				context=con;
			}
    	@Override
        protected Elements doInBackground(String... params) {
    		 try {
    	   			Document doc = Jsoup.connect(params[0]).get();
    	   			Document doc1=Jsoup.connect(params[1]).get();
    	   			Elements divs = doc.select("td .tekstFarm");
    	   			divs.addAll(doc1.select("td .tekstFarm"));
    	   			return divs;
    	   			
    	   			} catch (IOException e) {
    	   			// TODO Auto-generated catch block
    	   			e.printStackTrace();
    	   		}
            return new Elements();
        }

        @Override
        protected void onPostExecute(Elements result) {
        	
        	for (Element st : result)
   			{
        		ContentValues values = new ContentValues();
        		String name=st.select("b").text().toString();
        		String latinName=st.select("i").text().toString();
        		Element a = st.select("br").get(1);
        	    Node dose = a.nextSibling();
        	    Element b = st.select("br").get(2);
        	    String lista = b.nextSibling().toString();
        	    
   			
	   			values.put(DrugsProvider.KEY_DRUGNAME, name);
	   			values.put(DrugsProvider.KEY_DRUGLATINNAME, latinName);
	   			values.put(DrugsProvider.KEY_DRUGDOSE, dose.toString());
	   			values.put(DrugsProvider.KEY_DRUGDETAILS, lista.substring(7)); 
	   			Uri uri = context.getContentResolver().insert(
	        		      DrugsProvider.CONTENT_URI, values);
   			}
        	
        	
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
