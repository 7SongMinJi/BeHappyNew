package com.hello.ourApplication.Diary;

import static com.hello.ourApplication.Registration.LoginActivity.idText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hello.ourApplication.CalendarMainActivity;
import com.hello.ourApplication.Chat.ChatMainActivity;
import com.hello.ourApplication.DTO.DiaryResponse;
import com.hello.ourApplication.DTO.ReadDiary;
import com.hello.ourApplication.MainActivity;
import com.hello.ourApplication.R;
import com.hello.ourApplication.RecommendActivity;
import com.hello.ourApplication.Retrofit.RetrofitAPI;
import com.hello.ourApplication.Retrofit.RetrofitClient;
import com.hello.ourApplication.TestActivity;
import com.hello.ourApplication.Todo.TodoMainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.TypedValue;
import android.graphics.Typeface;

public class DiaryMainActivity extends AppCompatActivity {
    private LinearLayout parentLayout;
    private RetrofitClient retrofitClient;
    private RetrofitAPI retrofitAPI;
    public DiaryResponse result;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_main);

        parentLayout = findViewById(R.id.parentLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_slide_icon); //왼쪽 상단 버튼 아이콘 지정

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

//        TextView dateTextView = findViewById(R.id.dateTextView);

        // 현재 날짜를 가져옴
        Calendar calendar = Calendar.getInstance();

        // 날짜를 원하는 형식으로 포맷팅
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd (E)", Locale.getDefault());
        String formattedDate = formatter.format(calendar.getTime());

        // TextView에 날짜 표시
//        dateTextView.setText(formattedDate);

        ImageButton writeDiaryButton = findViewById(R.id.write_diary);

        DiaryResponse();



        writeDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryMainActivity.this, DiaryWriteActivity.class);
                startActivity(intent);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_chat: // "채팅하기" 메뉴 클릭 시
                        Intent intent = new Intent(DiaryMainActivity.this, ChatMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_recommend: // "추천받기" 메뉴 클릭 시
                        intent = new Intent(DiaryMainActivity.this, RecommendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary: // "일기 모아보기" 메뉴 클릭 시
                        intent = new Intent(DiaryMainActivity.this, DiaryMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_test: // "우울증 자가 진단" 메뉴 클릭 시
                        intent = new Intent(DiaryMainActivity.this, TestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary_new: // "일기 작성하기" 메뉴 클릭 시
                        intent = new Intent(DiaryMainActivity.this, DiaryWriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_checklist:
                        intent = new Intent(DiaryMainActivity.this, TodoMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_calendar:
                        intent = new Intent(DiaryMainActivity.this, CalendarMainActivity.class);
                        startActivity(intent);
                        break;
                }

                // 네비게이션 드로어 닫기
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);

        // BottomNavigationView의 아이템 클릭 리스너 설정
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_bar_home:
                    // 홈 버튼 클릭 시
                    startActivity(new Intent(DiaryMainActivity.this, MainActivity.class));
                    return true;
                case R.id.menu_bar_chat:
                    // 채팅 버튼 클릭 시
                    startActivity(new Intent(DiaryMainActivity.this, ChatMainActivity.class));
                    return true;
                case R.id.menu_bar_calendar:
                    // 캘린더 버튼 클릭 시
                    startActivity(new Intent(DiaryMainActivity.this, CalendarMainActivity.class));
                    return true;
                default:
                    return false;
            }
        });
    }

    public void DiaryResponse(){
        String userID = idText.getText().toString().trim();

        // diary에 값 저장하기
        ReadDiary readDiary = new ReadDiary(userID);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI = RetrofitClient.getRetrofitInterface();

        retrofitAPI.getReadDiaryResponse(readDiary).enqueue(new Callback<DiaryResponse>() {
            @Override
            public void onResponse(Call<DiaryResponse> call, Response<DiaryResponse> response) {

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    result = response.body();

                    //받은 코드 저장
                    String resultCode = result.getStatusCode();

                    String success = "200"; //로그인 성공


                    if (resultCode.equals(success)) {
                        processDiaryEntries();
                        return;

                    } else {
                        Toast.makeText(DiaryMainActivity.this, "일기를 불러오는 과정에서 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<DiaryResponse> call, Throwable t) {
                Toast.makeText(DiaryMainActivity.this, "일기를 불러오는 과정에서 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processDiaryEntries() {
        String bodyString = result.getToken();
        try {
            JSONObject bodyObject = new JSONObject(bodyString);
            JSONArray diariesArray = bodyObject.getJSONArray("diaries");

            // Clear the parentLayout before adding new entries
            parentLayout.removeAllViews();

            for (int i = 0; i < diariesArray.length(); i++) {
                JSONObject diary = diariesArray.getJSONObject(i);

                String date = diary.getString("date");
                String content = diary.getString("content");

                LinearLayout entryLayout = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 40); // Set margin bottom to create space between entries
                entryLayout.setLayoutParams(params);
                entryLayout.setOrientation(LinearLayout.VERTICAL);

                TextView dateTextView = new TextView(this);
                dateTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                dateTextView.setText(date);
                dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                dateTextView.setTypeface(null, Typeface.BOLD_ITALIC);
                entryLayout.addView(dateTextView);

                TextView contentTextView = new TextView(this);
                contentTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                contentTextView.setText(content);
                dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                contentTextView.setTypeface(null, Typeface.NORMAL);
                entryLayout.addView(contentTextView);

                parentLayout.addView(entryLayout);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onToolbarMainButtonClick(View view) { // 오른쪽 상단 홈 버튼
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { // 뒤로 가기 했을 때
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
