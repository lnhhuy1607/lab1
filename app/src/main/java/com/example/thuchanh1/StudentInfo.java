package com.example.thuchanh1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StudentInfo extends AppCompatActivity {
    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final int REQUEST_SMS_PERMISSION = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 3;
    private static final int REQUEST_IMAGE_CAPTURE = 4;

    private TextView info;
    private Button back, callBtn, smsBtn, cameraBtn;
    private ImageView capturedImage;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        info = findViewById(R.id.info_txt);
        back = findViewById(R.id.back_btn);
        callBtn = findViewById(R.id.call_btn);
        smsBtn = findViewById(R.id.sms_btn);
        cameraBtn = findViewById(R.id.camera_btn);
        capturedImage = findViewById(R.id.captured_image);

        // Nhận dữ liệu từ Intent
        String name = getIntent().getStringExtra("name");
        String mssv = getIntent().getStringExtra("mssv");
        String studentClass = getIntent().getStringExtra("class");
        phoneNumber = getIntent().getStringExtra("phone");
        String year = getIntent().getStringExtra("year");
        String majors = getIntent().getStringExtra("majors");
        String plan = getIntent().getStringExtra("plan");

        // Hiển thị dữ liệu
        info.setText("Họ và tên: " + name + "\n" +
                "MSSV: " + mssv + "\n" +
                "Lớp: " + studentClass + "\n" +
                "Số điện thoại: " + phoneNumber + "\n" +
                "Năm học: " + year + "\n" +
                "Chuyên ngành: " + majors + "\n" +
                "Kế hoạch phát triển: " + plan);

        // Xử lý sự kiện các nút bấm
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        callBtn.setOnClickListener(v -> makePhoneCall());
        smsBtn.setOnClickListener(v -> sendSMS());
        cameraBtn.setOnClickListener(v -> openCamera());
    }

    // Chức năng gọi điện thoại
    private void makePhoneCall() {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Không có số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        }
    }

    // Chức năng gửi SMS
    private void sendSMS() {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Không có số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
        } else {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("smsto:" + phoneNumber));
            smsIntent.putExtra("sms_body", "Xin chào!");
            startActivity(smsIntent);
        }
    }

    // Chức năng mở camera
    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Nhận kết quả trả về từ camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            capturedImage.setImageBitmap(imageBitmap);
        }
    }

    // Xử lý quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Quyền gọi điện bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(this, "Quyền gửi tin nhắn bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Quyền sử dụng camera bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
