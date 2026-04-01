package com.example.week7_2025_myo_01;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

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



   class myImageDownloader extends AsyncTask<String,Void, Bitmap> {


       @Override
       protected Bitmap doInBackground(String... strings) {

           try {
               URL src= new URL(strings[0]);
               HttpsURLConnection conn= (HttpsURLConnection) src.openConnection();
               conn.setRequestMethod("GET");
               conn.connect();
               InputStream is=conn.getInputStream();
               Bitmap bitmap= BitmapFactory.decodeStream(is);
               return bitmap;


           } catch (MalformedURLException e) {
               throw new RuntimeException(e);
           } catch (ProtocolException e) {
               throw new RuntimeException(e);
           } catch (IOException e) {
               throw new RuntimeException(e);
           }


           //return null;
       }
   }





String Cevap="";

public void BastanBasla() throws ExecutionException, InterruptedException {
    Random rnd= new Random(System.currentTimeMillis());
    int randomIndex=rnd.nextInt(120);

    myImageDownloader myImageDownloader= new myImageDownloader();
    Bitmap bitmap=myImageDownloader.execute(imgSrc[randomIndex]).get();
    ImageView imageView=findViewById(R.id.imageView);
    imageView.setImageBitmap(bitmap);
    Cevap=authorName[randomIndex];
    int answer=rnd.nextInt(4);


    btns[answer].setText(authorName[randomIndex]);

    for (int i=0;i<4;i++){
        int myRandom=rnd.nextInt(120);

        if (i!=answer){
            for (int j=0;j<4;j++){
                if (btns[j].getText().equals(authorName[myRandom])){
                    myRandom=rnd.nextInt(120);
                    j=0;
                }

            }
            btns[i].setText(authorName[myRandom]);
        }
    }






}

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button[] btns;

 public void Tiklandi(View view) throws ExecutionException, InterruptedException {
     Button btn= (Button) view;
     if (btn.getText().equals(Cevap)){
         Toast.makeText(this,"Doğru Cevap",Toast.LENGTH_SHORT).show();
      BastanBasla();
     }
     else{
         Toast.makeText(this,"Yanlış Cevap doğrusu " +Cevap +" olmalıydı",Toast.LENGTH_SHORT).show();
     }


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
       btn1 =(Button) findViewById(R.id.btn1);
        btn2 =(Button) findViewById(R.id.btn2);
        btn3 =(Button) findViewById(R.id.btn3);
        btn4 =(Button) findViewById(R.id.btn4);
        btns= new Button[]{btn1,btn2,btn3,btn4};


       Thread t = new Thread( new myTask());
       t.start();
        try {
            t.join();
            BastanBasla();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }


    }
}