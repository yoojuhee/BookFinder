package kr.or.womanup.nambu.yjh.bookfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String END_POINT = "https://dapi.kakao.com/v3/search/book?query=%s&page=%d";//?이후는 우리가 사용할 변수 (파라미터)가 간다. 두개의 변수명을 구분짓는 것은 &를 이용
    private static final String API_KEY = "512698232e3250e097be5abef0914bbd";
    SearchView searchView;
    RecyclerView recyclerView;
    BookAdapter adapter;
    int page=1;
    boolean isEnd=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new BookAdapter(this,R.layout.layout_item);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int totalItemCount = manager.getItemCount();
                int lastVisible = manager.findLastCompletelyVisibleItemPosition();
                if(lastVisible >=totalItemCount-1){
                    if(isEnd){
                        //alerDialog : 마지막 페이지입니다.
                        return;
                    }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            page++;
                            String json = search(searchView.getQuery().toString());
                            pasing(json);
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("검색어를 입력하세요");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isEnd = false;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        page=1;
                        adapter.clear();
                        String json = search(query);
                        pasing(json);
                        System.out.println(json);

                    }
                });
                thread.start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    String search(String query) {
        String strURL = String.format(END_POINT, query, page);
        String str;
        String result = null;
        try {
            URL url = new URL(strURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK " + API_KEY);
            con.connect();
            if (con.getResponseCode() == con.HTTP_OK) {
                InputStreamReader streamReader = new InputStreamReader(con.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                result = buffer.toString();
            }
        } catch (IOException e) {
            System.out.println("예외발생");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    void pasing(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONObject meta = root.getJSONObject("meta");
            isEnd = meta.getBoolean("is_end");

            JSONArray documents = root.getJSONArray("documents");
            for (int i = 0; i < documents.length(); i++) {
                JSONObject book = documents.getJSONObject(i);
                String titile = book.getString("title");
                JSONArray tmpAuthor = book.getJSONArray("authors");
                String authors = "";
                for (int j = 0; j < tmpAuthor.length(); j++) {
                    authors = authors + tmpAuthor.getString(j);
                    if (j < tmpAuthor.length() - 1) { authors = authors + " "; }
                }
                String publisher = book.getString("publisher");
                String contents = book.getString("contents");
                String url = book.getString("url");
                String isbn = book.getString("isbn");
                String date = book.getString("datetime");

                JSONArray tmpTrans = book.getJSONArray("translators");
                String translators="";
                for (int j = 0; j < tmpTrans.length(); j++) {
                    translators = translators + tmpTrans.getString(j);
                    if (j < tmpTrans.length() - 1) { translators = translators + " "; }
                }

                int price = book.getInt("price");
                int salePrice = book.getInt("sale_price");
                String thumbnail = book.getString("thumbnail");
                String status = book.getString("status");

                Book newBook = new Book(titile,contents,url,isbn,date,authors,publisher,translators,
                        price,salePrice,thumbnail,status);
                adapter.addItem(newBook);
            }
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } catch(JSONException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}