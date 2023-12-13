package com.hello.ourApplication;

import static com.hello.ourApplication.Diary.DiarySelectKeywordActivity.emotions;
import static com.hello.ourApplication.Diary.DiaryWriteActivity.recentEmotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.hello.ourApplication.Chat.ChatMainActivity;
import com.hello.ourApplication.DTO.DiaryResponse;
import com.hello.ourApplication.DTO.Recommend;
import com.hello.ourApplication.DTO.RecommendResponse;
import com.hello.ourApplication.Diary.DiaryMainActivity;
import com.hello.ourApplication.Diary.DiaryWriteActivity;
import com.hello.ourApplication.Retrofit.RetrofitAPI;
import com.hello.ourApplication.Retrofit.RetrofitClient;
import com.hello.ourApplication.Todo.TodoMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendActivity extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private RetrofitAPI retrofitAPI;
    private RecommendResponse recommendResult;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView userName;
    TextView musicRecommend;
    private static final int DELAY_TIME = 5000; // 2초

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfunc_recommend);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_slide_icon); //왼쪽 상단 버튼 아이콘 지정

        recommendResponse();

        userName = findViewById(R.id.mainUserName);
        userName.setText("행복아! 오늘 이런 활동을 하는 것은 어떨까?");


        musicRecommend = findViewById(R.id.music_recommend);

//        if (recommendResult != null) {
//            musicRecommend.setText(recommendResult.getToken());
//        } else {
//            musicRecommend.setText("추천 정보를 불러오는 중입니다.");
//        }
////        musicRecommend.setText(recommendResult.getToken());

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);

        // 왼쪽 상단 버튼 클릭 시 drawer 열기
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_chat: // "채팅하기" 메뉴 클릭 시
                        Intent intent = new Intent(RecommendActivity.this, ChatMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_recommend: // "추천받기" 메뉴 클릭 시
                        intent = new Intent(RecommendActivity.this, RecommendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary: // "일기 모아보기" 메뉴 클릭 시
                        intent = new Intent(RecommendActivity.this, DiaryMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_test: // "우울증 자가 진단" 메뉴 클릭 시
                        intent = new Intent(RecommendActivity.this, TestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary_new: // "일기 작성하기" 메뉴 클릭 시
                        intent = new Intent(RecommendActivity.this, DiaryWriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_checklist:
                        intent = new Intent(RecommendActivity.this, TodoMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_calendar:
                        intent = new Intent(RecommendActivity.this, CalendarMainActivity.class);
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
                    startActivity(new Intent(this, MainActivity.class));
                    return true;
                case R.id.menu_bar_chat:
                    // 채팅 버튼 클릭 시
                    startActivity(new Intent(this, ChatMainActivity.class));
                    return true;
                case R.id.menu_bar_calendar:
                    // 캘린더 버튼 클릭 시
                    startActivity(new Intent(this, CalendarMainActivity.class));
                    return true;
                default:
                    return false;
            }
        });
    }

    private void recommendResponse() {
        String emotion = "행복";
        //recommend에 값 저장
        Recommend recommend = new Recommend(emotion);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI = RetrofitClient.getRetrofitInterface();

        retrofitAPI.getRecommendResponse(recommend).enqueue(new Callback<RecommendResponse>() {
            @Override
            public void onResponse(Call<RecommendResponse> call, Response<RecommendResponse> response) {

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    recommendResult = response.body();

                    //받은 코드 저장
                    String resultCode = recommendResult.getStatusCode();

                    String success = "200"; // 받기 성공


                    if (resultCode.equals(success)) {
                        showRecommendInfo(recommendResult);

                    } else {
                        Toast.makeText(RecommendActivity.this, "추천을 불러오는 과정에서 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<RecommendResponse> call, Throwable t) {
                Toast.makeText(RecommendActivity.this, "추천을 불러오는 과정에서 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showRecommendInfo(RecommendResponse recommendResponse) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(recommendResponse);

            JSONObject jsonObject = new JSONObject(json);

            int statusCode = jsonObject.getInt("statusCode");
            if (statusCode == 200) {
                String body = jsonObject.getString("body");
                JSONObject bodyObject = new JSONObject(body);

                String title = bodyObject.getString("title");
                musicRecommend.setText(title);
            } else {
                // 에러 처리
                musicRecommend.setText("추천을 불러오는 과정에서 오류가 발생했습니다.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // JSON 파싱 오류 처리
            musicRecommend.setText("데이터를 처리하는 중 오류가 발생했습니다.");
        }
    }

    public void onToolbarMainButtonClick(View view) { // 오른쪽 상단 홈 버튼
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
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