package com.example.thuchanh1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText nameTxt, mssvTxt, classTxt, phoneTxt, planTxt;
    private RadioGroup yearGroup;
    private CheckBox mthtnBox, dtBox, vtBox;
    private Button sendBtn;

    @SuppressLint("MissingInflatedId")
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

        // Ánh xạ các thành phần giao diện
        nameTxt = findViewById(R.id.name_txt);
        mssvTxt = findViewById(R.id.mssv_txt);
        classTxt = findViewById(R.id.class_txt);
        phoneTxt = findViewById(R.id.editTextPhone);
        planTxt = findViewById(R.id.plan_txt);

        yearGroup = findViewById(R.id.year);
        mthtnBox = findViewById(R.id.mthtn_box);
        dtBox = findViewById(R.id.dt_box);
        vtBox = findViewById(R.id.vt_box);
        sendBtn = findViewById(R.id.send_btn);

        sendBtn.setOnClickListener(v -> {
            sendStudentInfo();
        });
    }

    private void sendStudentInfo() {
        String name = nameTxt.getText().toString();
        String mssv = mssvTxt.getText().toString();
        String studentClass = classTxt.getText().toString();
        String phone = phoneTxt.getText().toString();
        String plan = planTxt.getText().toString();

        // Lấy thông tin năm học
        int selectedYearId = yearGroup.getCheckedRadioButtonId();
        String selectedYear = "";
        if (selectedYearId != -1) {
            selectedYear = ((RadioButton) findViewById(selectedYearId)).getText().toString();
        }

        // Lấy chuyên ngành đã chọn
        StringBuilder majors = new StringBuilder();
        if (mthtnBox.isChecked()) majors.append("Máy tính - HTN, ");
        if (dtBox.isChecked()) majors.append("Điện tử, ");
        if (vtBox.isChecked()) majors.append("Viễn thông, ");

        // Xóa dấu phẩy cuối cùng nếu có
        if (majors.length() > 0) {
            majors.setLength(majors.length() - 2);
        }

        // Kiểm tra dữ liệu trước khi gửi
        if (name.isEmpty() || mssv.isEmpty() || studentClass.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi dữ liệu sang StudentInfoActivity
        Intent intent = new Intent(this, StudentInfo.class);
        intent.putExtra("name", name);
        intent.putExtra("mssv", mssv);
        intent.putExtra("class", studentClass);
        intent.putExtra("phone", phone);
        intent.putExtra("year", selectedYear);
        intent.putExtra("majors", majors.toString());
        intent.putExtra("plan", plan);
        startActivity(intent);
    }
}