package com.hello.ourApplication.Diary;

import static com.hello.ourApplication.Registration.LoginActivity.idText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hello.ourApplication.CalendarMainActivity;
import com.hello.ourApplication.Chat.ChatMainActivity;
import com.hello.ourApplication.DTO.DiaryResponse;
import com.hello.ourApplication.DTO.EmotionResponse;
import com.hello.ourApplication.DTO.ReadDiary;
import com.hello.ourApplication.DTO.ReadEmotion;
import com.hello.ourApplication.Diary.DiaryMainActivity;
import com.hello.ourApplication.Diary.DiaryWriteActivity;
import com.hello.ourApplication.MainActivity;
import com.hello.ourApplication.R;
import com.hello.ourApplication.RecommendActivity;
import com.hello.ourApplication.Retrofit.RetrofitAPI;
import com.hello.ourApplication.Retrofit.RetrofitClient;
import com.hello.ourApplication.TestActivity;
import com.hello.ourApplication.Todo.TodoMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiarySelectKeywordActivity extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private RetrofitAPI retrofitAPI;
    public EmotionResponse result;
    // Initialize variables to hold the most recent emotion's date and emotion
    public String recentDate = "";
    public String recentEmotion = "";
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    boolean isButton1, isButton2, isButton3, isButton4, isButton5, isButton6, isButton7 = true;
    ImageButton angrykeyword;
    ImageButton disgustkeyword;
    ImageButton fearkeyword;
    ImageButton happykeyword;
    ImageButton neutralkeyword;
    ImageButton sadkeyword;
    ImageButton suprisekeyword;
    ImageButton selectKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_select_keyword);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_slide_icon); //왼쪽 상단 버튼 아이콘 지정

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        TextView dateTextView = findViewById(R.id.dateTextView);

        // 현재 날짜를 가져옴
        Calendar calendar = Calendar.getInstance();

        // 날짜를 원하는 형식으로 포맷팅
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd (E)", Locale.getDefault());
        String formattedDate = formatter.format(calendar.getTime());

        // TextView에 날짜 표시
        dateTextView.setText(formattedDate);

        angrykeyword = findViewById(R.id.keyword_button_3);
        disgustkeyword  = findViewById(R.id.keyword_button_4);
        fearkeyword = findViewById(R.id.keyword_button_5);
        happykeyword = findViewById(R.id.keyword_button_6);
        neutralkeyword= findViewById(R.id.keyword_button_7);
        sadkeyword = findViewById(R.id.keyword_button_8);
        suprisekeyword = findViewById(R.id.keyword_button_9);
        selectKeyword = findViewById(R.id.selectKeyword);
        
        EmotionGetResponse();

        angrykeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButton1) {
                    // 버튼 상태가 1일 때 눌렸을 경우
                    angrykeyword.setImageResource(R.drawable.btn_knang);
                } else {
                    // 버튼 상태가 2일 때 눌렸을 경우
                    angrykeyword.setImageResource(R.drawable.btn_ksang);
                }

                // 버튼 상태 변경
                isButton1 = !isButton1;
            }
        });

        disgustkeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButton2) {
                    // 버튼 상태가 1일 때 눌렸을 경우
                    disgustkeyword.setImageResource(R.drawable.btn_kndis);
                } else {
                    // 버튼 상태가 2일 때 눌렸을 경우
                    disgustkeyword.setImageResource(R.drawable.btn_ksdis);
                }

                // 버튼 상태 변경
                isButton2 = !isButton2;
            }
        });

        fearkeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButton3) {
                    // 버튼 상태가 1일 때 눌렸을 경우
                    fearkeyword.setImageResource(R.drawable.btn_knfear);
                } else {
                    // 버튼 상태가 2일 때 눌렸을 경우
                    fearkeyword.setImageResource(R.drawable.btn_ksfear);
                }

                // 버튼 상태 변경
                isButton3 = !isButton3;
            }
        });

        happykeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButton4) {
                    // 버튼 상태가 1일 때 눌렸을 경우
                    happykeyword.setImageResource(R.drawable.btn_knhap);
                } else {
                    // 버튼 상태가 2일 때 눌렸을 경우
                    happykeyword.setImageResource(R.drawable.btn_kshap);
                }

                // 버튼 상태 변경
                isButton4 = !isButton4;
            }
        });

        neutralkeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButton5) {
                    // 버튼 상태가 1일 때 눌렸을 경우
                    neutralkeyword.setImageResource(R.drawable.btn_knneu);
                } else {
                    // 버튼 상태가 2일 때 눌렸을 경우
                    neutralkeyword.setImageResource(R.drawable.btn_ksneu);
                }

                // 버튼 상태 변경
                isButton5 = !isButton5;
            }
        });

        sadkeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButton6) {
                    // 버튼 상태가 1일 때 눌렸을 경우
                    sadkeyword.setImageResource(R.drawable.btn_knsad);
                } else {
                    // 버튼 상태가 2일 때 눌렸을 경우
                    sadkeyword.setImageResource(R.drawable.btn_kssad);
                }

                // 버튼 상태 변경
                isButton6 = !isButton6;
            }
        });

        suprisekeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButton7) {
                    // 버튼 상태가 1일 때 눌렸을 경우
                    suprisekeyword.setImageResource(R.drawable.btn_knsup);
                } else {
                    // 버튼 상태가 2일 때 눌렸을 경우
                    suprisekeyword.setImageResource(R.drawable.btn_kssup);
                }

                // 버튼 상태 변경
                isButton7 = !isButton7;
            }
        });
        
        EmotionPostResponse();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_chat: // "채팅하기" 메뉴 클릭 시
                        Intent intent = new Intent(DiarySelectKeywordActivity.this, ChatMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_recommend: // "추천받기" 메뉴 클릭 시
                        intent = new Intent(DiarySelectKeywordActivity.this, RecommendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary: // "일기 모아보기" 메뉴 클릭 시
                        intent = new Intent(DiarySelectKeywordActivity.this, DiaryMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_test: // "우울증 자가 진단" 메뉴 클릭 시
                        intent = new Intent(DiarySelectKeywordActivity.this, TestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary_new: // "일기 작성하기" 메뉴 클릭 시
                        intent = new Intent(DiarySelectKeywordActivity.this, DiaryWriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_checklist:
                        intent = new Intent(DiarySelectKeywordActivity.this, TodoMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_calendar:
                        intent = new Intent(DiarySelectKeywordActivity.this, CalendarMainActivity.class);
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
                    startActivity(new Intent(DiarySelectKeywordActivity.this, MainActivity.class));
                    return true;
                case R.id.menu_bar_chat:
                    // 채팅 버튼 클릭 시
                    startActivity(new Intent(DiarySelectKeywordActivity.this, ChatMainActivity.class));
                    return true;
                case R.id.menu_bar_calendar:
                    // 캘린더 버튼 클릭 시
                    startActivity(new Intent(DiarySelectKeywordActivity.this, CalendarMainActivity.class));
                    return true;
                default:
                    return false;
            }
        });

        selectKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent를 사용하여 CalendarMainActivity로 recentEmotion 전달
                Intent intent = new Intent(DiarySelectKeywordActivity.this, CalendarMainActivity.class);
                intent.putExtra("recentEmotion", recentEmotion);
                startActivity(intent);
            }
        });
    }

    private void EmotionPostResponse() {
    }

    private void EmotionGetResponse() {
        String userID = idText.getText().toString().trim();

        // diary에 값 저장하기
        ReadEmotion readEmotion = new ReadEmotion(userID);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI = RetrofitClient.getRetrofitInterface();

        retrofitAPI.getReadEmotionResponse(readEmotion).enqueue(new Callback<EmotionResponse>() {
            @Override
            public void onResponse(Call<EmotionResponse> call, Response<EmotionResponse> response) {

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    result = response.body();

                    //받은 코드 저장
                    String resultCode = result.getStatusCode();

                    String success = "200"; //로그인 성공


                    if (resultCode.equals(success)) {
                        returnEmotion();
                        return;

                    } else {
                        Toast.makeText(DiarySelectKeywordActivity.this, "감정을 분석하는 과정에서 문제가 발생했습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<EmotionResponse> call, Throwable t) {
                Toast.makeText(DiarySelectKeywordActivity.this, "감정을 분석하는 과정에서 문제가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void returnEmotion() {
        // Your JSON response as a string
        String bodyString = result.getToken();

        try {
            // Parse the body string from JSON
            JSONObject bodyObject = new JSONObject(bodyString);
            JSONArray emotionsArray = bodyObject.getJSONArray("emotions");

            // Iterate through emotions to find the most recent one
            for (int i = 0; i < emotionsArray.length(); i++) {
                JSONObject emotion = emotionsArray.getJSONObject(i);
                String date = emotion.getString("date");
                String emotionStr = emotion.optString("emotion", null); // Fetch emotion or null if not present

                // Check if the date is not empty and is more recent than the current recentDate
                if (!date.isEmpty() && (recentDate.isEmpty() || date.compareTo(recentDate) > 0)) {
                    recentDate = date;
                    recentEmotion = emotionStr;
                }
            }

            switch (recentEmotion){
                case "행복":
                    happykeyword.setImageResource(R.drawable.btn_kshap);
                    isButton4 = !isButton4;
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
