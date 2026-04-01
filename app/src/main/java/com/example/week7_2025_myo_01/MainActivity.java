package com.example.week7_2025_myo_01;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  String imgSrc[]= new String[120];
  String authorName[]= new String[120];


   public class myTask implements Runnable {


       @Override
       public void run() {
           try {
               Document doc = Jsoup.connect("https://www.milliyet.com.tr/yazarlar/").get();
               Elements imgs=doc.select("div.card-listing a img");
               Elements spans=doc.select("span.card-listing__author-name");
                Log.d("Sonuc", doc.title());
               Log.d("Sonuc",""+imgs.size());
               Log.d("Sonuc",""+spans.size());
               for (int i=0;i<imgs.size()*10;i++){
                   Log.d("Sonuc",spans.get(i%12).text() +" "+imgs.get(i%12).attr("src") );
                   authorName[i]=spans.get(i%12).text();
                   imgSrc[i]=imgs.get(i%12).attr("src");
               }
               Log.d("Sonuc", doc.title());
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
       }
   }



   class




public void BastanBasla(){


}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       Thread t = new Thread( new myTask());
       t.start();
        try {
            t.join();


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        BastanBasla();
    }
}