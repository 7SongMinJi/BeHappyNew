package com.hello.ourApplication.Diary;

import static com.hello.ourApplication.Registration.LoginActivity.idText;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.hello.ourApplication.DTO.LoginResponse;
import com.hello.ourApplication.DTO.WriteDiary;
import com.hello.ourApplication.MainActivity;
import com.hello.ourApplication.R;
import com.hello.ourApplication.RecommendActivity;
import com.hello.ourApplication.Registration.LoginActivity;
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

public class DiaryWriteActivity extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private RetrofitAPI retrofitAPI;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    public static DiaryResponse result;
    // Initialize variables to hold the most recent emotion's date and emotion
    public static String recentDate = "";
    public static String recentEmotion = "";
    TextView diaryContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write);

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

        ImageButton goToPhotoButton = findViewById(R.id.diary_content_complete);

        // 일기 내용 작성
        diaryContent = findViewById(R.id.diary_content);

        goToPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = diaryContent.getText().toString().trim();

                // Check if diary content is empty
                if (content.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DiaryWriteActivity.this);
                    builder.setTitle("알림")
                            .setMessage("일기 내용을 작성해주세요")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    DiaryResponse();
//                    // If diary content is not empty, proceed to DiaryPhotoActivity
//                    Intent intent = new Intent(DiaryWriteActivity.this, DiaryPhotoActivity.class);
//                    startActivity(intent);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_chat: // "채팅하기" 메뉴 클릭 시
                        Intent intent = new Intent(DiaryWriteActivity.this, ChatMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_recommend: // "추천받기" 메뉴 클릭 시
                        intent = new Intent(DiaryWriteActivity.this, RecommendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary: // "일기 모아보기" 메뉴 클릭 시
                        intent = new Intent(DiaryWriteActivity.this, DiaryMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_test: // "우울증 자가 진단" 메뉴 클릭 시
                        intent = new Intent(DiaryWriteActivity.this, TestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary_new: // "일기 작성하기" 메뉴 클릭 시
                        intent = new Intent(DiaryWriteActivity.this, DiaryWriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_checklist:
                        intent = new Intent(DiaryWriteActivity.this, TodoMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_calendar:
                        intent = new Intent(DiaryWriteActivity.this, CalendarMainActivity.class);
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
                    startActivity(new Intent(DiaryWriteActivity.this, MainActivity.class));
                    return true;
                case R.id.menu_bar_chat:
                    // 채팅 버튼 클릭 시
                    startActivity(new Intent(DiaryWriteActivity.this, ChatMainActivity.class));
                    return true;
                case R.id.menu_bar_calendar:
                    // 캘린더 버튼 클릭 시
                    startActivity(new Intent(DiaryWriteActivity.this, CalendarMainActivity.class));
                    return true;
                default:
                    return false;
            }
        });

        diaryContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            private boolean isKeyboardVisible = false;

            @Override
            public boolean onPreDraw() {
                Rect r = new Rect();
                View rootView = getWindow().getDecorView();
                int screenHeight = rootView.getHeight();
                rootView.getWindowVisibleDisplayFrame(r);
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    // 자판이 올라올 때
                    if (!isKeyboardVisible) {
                        hideBottomNavigation();
                        isKeyboardVisible = true;
                    }
                } else {
                    // 자판이 사라질 때
                    if (isKeyboardVisible) {
                        showBottomNavigation();
                        isKeyboardVisible = false;
                    }
                }

                return true;
            }
        });

    }

    public void DiaryResponse(){
        String userID = idText.getText().toString().trim();
        String content = diaryContent.getText().toString().trim();

        // Diary에 사용자가 입력한 content 저장
        WriteDiary writeDiary = new WriteDiary(userID, content);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        retrofitAPI = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        retrofitAPI.getWriteDiaryResponse(writeDiary).enqueue(new Callback<DiaryResponse>() {
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

                        Toast.makeText(DiaryWriteActivity.this, "일기가 작성 완료되었습니다.", Toast.LENGTH_LONG).show();
                        returnEmotion();
                        Intent intent = new Intent(DiaryWriteActivity.this, DiarySelectKeywordActivity.class);
                        intent.putExtra("userId", userID);
                        startActivity(intent);
                        DiaryWriteActivity.this.finish();

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryWriteActivity.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            //통신 실패
            @Override
            public void onFailure(Call<DiaryResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryWriteActivity.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }

    private void returnEmotion() {
        // Your JSON response as a string
        String bodyString = result.getToken();

        try {
            // Parse the body string from JSON
            JSONObject emotion = new JSONObject(bodyString);
            String emotionStr = emotion.optString("emotion", null); // Fetch emotion or null if not present
            recentEmotion = emotionStr;

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

    private void hideBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void showBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }
}
