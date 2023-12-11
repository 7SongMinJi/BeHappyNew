package com.hello.ourApplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hello.ourApplication.Chat.ChatMainActivity;
import com.hello.ourApplication.Diary.DiaryMainActivity;
import com.hello.ourApplication.Diary.DiaryWriteActivity;
import com.hello.ourApplication.Todo.TodoMainActivity;
import com.hello.ourApplication.CalendarMainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_slide_icon); //왼쪽 상단 버튼 아이콘 지정

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        TextView dateTextView = findViewById(R.id.dateTextView);
        TextView plannedTasksTextView = findViewById(R.id.plannedTasksTextView);
        TextView completedTasksTextView = findViewById(R.id.completedTasksTextView);

        // 현재 날짜를 가져옴
        Calendar calendar = Calendar.getInstance();

        // 날짜를 원하는 형식으로 포맷팅
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd (E)", Locale.getDefault());
        String formattedDate = formatter.format(calendar.getTime());

        // TextView에 날짜 표시
        dateTextView.setText(formattedDate);

        // 계획한 일 개수 출력
        int numberOfPlannedTasks = 5; // 계획한 할 일 개수
        String newText1 = "♥ 오늘 계획한 할 일: " + numberOfPlannedTasks + " 개";
        plannedTasksTextView.setText(newText1);

        // 완료한 일 개수 출력
        int numberOfCompletedTasks = 5; // 완료한 할 일 개수
        String newText2 = "♥ 오늘 완료한 할 일: " + numberOfCompletedTasks + " 개";
        completedTasksTextView.setText(newText2);

        ImageButton buttonDiary = findViewById(R.id.mainButton_diary); // 메인화면 버튼: 다이어리
        ImageButton buttoncalendar = findViewById(R.id.mainButton_calendar); // 메인화면 버튼: 달력
        ImageButton buttonRecommend = findViewById(R.id.mainButton_recommend); // 메인화면 버튼: 포켓가든
        ImageButton buttonChat = findViewById(R.id.mainButton_chat); // 메인화면 버튼: 채팅

        // 다이어리 버튼 클릭시
        buttonDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DiaryMainActivity.class);
                startActivity(intent);
            }
        });

        // 달력 버튼 클릭시
        buttoncalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 일단 Todo로 해놓았어요
                Intent intent = new Intent(MainActivity.this, TodoMainActivity.class);
                startActivity(intent);
            }
        });

        // 추천 버튼 클릭시
        buttonRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
                startActivity(intent);
            }
        });

        //채팅 클릭시
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatMainActivity.class);
                startActivity(intent);
            }
        });

        View headerView = navigationView.getHeaderView(0); // 0은 header의 index입니다.

        TextView userNameTextView = headerView.findViewById(R.id.user_name);
        userNameTextView.setText("안녕");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_chat: // "채팅하기" 메뉴 클릭 시
                        Intent intent = new Intent(MainActivity.this, ChatMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_recommend: // "추천받기" 메뉴 클릭 시
                        intent = new Intent(MainActivity.this, RecommendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary: // "일기 모아보기" 메뉴 클릭 시
                        intent = new Intent(MainActivity.this, DiaryMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_test: // "우울증 자가 진단" 메뉴 클릭 시
                        intent = new Intent(MainActivity.this, TestActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_diary_new: // "일기 작성하기" 메뉴 클릭 시
                        intent = new Intent(MainActivity.this, DiaryWriteActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_checklist:
                        intent = new Intent(MainActivity.this, TodoMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.item_calendar:
                        intent = new Intent(MainActivity.this, CalendarMainActivity.class);
                        startActivity(intent);
                        break;
                        //포켓가든 코드 추가
                    case R.id.item_pocket_garden:
                        intent = new Intent(MainActivity.this, getMainUnityActivityClass());
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
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                case R.id.menu_bar_chat:
                    // 채팅 버튼 클릭 시
                    startActivity(new Intent(MainActivity.this, ChatMainActivity.class));
                    return true;
                case R.id.menu_bar_calendar:
                    // 캘린더 버튼 클릭 시
                    startActivity(new Intent(MainActivity.this, CalendarMainActivity.class));
                    return true;
                default:
                    return false;
            }
        });


    }

    private Class getMainUnityActivityClass() {
        return findClassUsingReflection("com.hello.ourApplication.MainUnityActivity");
    }

    private Class findClassUsingReflection(String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navi_menu, menu);
        return true;
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