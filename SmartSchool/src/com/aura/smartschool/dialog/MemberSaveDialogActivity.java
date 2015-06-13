package com.aura.smartschool.dialog;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.aura.smartschool.Constant;
import com.aura.smartschool.MainActivity;
import com.aura.smartschool.R;
import com.aura.smartschool.utils.Util;
import com.aura.smartschool.vo.MemberVO;

public class MemberSaveDialogActivity extends Activity {
	private int mMode; //0: add, 1:update
	
	private Context mContext;
	private AQuery mAq;
	
	TextView tvTitle, tvParent, tvStudent;
	LinearLayout school_info;
	FrameLayout fl_user_image;
	EditText et_id, et_name, et_relation;
	ImageView iv_user_image;
	EditText et_school_name, et_school_grade, et_school_class;
	Button btn_register;
	
	private MemberVO mMember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.6f;
		getWindow().setAttributes(lpWindow);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		setContentView(R.layout.dialog_register);

		mAq = new AQuery(this);
		mContext = this;
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		school_info = (LinearLayout) findViewById(R.id.school_info);
		tvParent = (TextView) findViewById(R.id.tvParent);
		tvStudent = (TextView) findViewById(R.id.tvStudent);
		iv_user_image = (ImageView) findViewById(R.id.iv_user_image);
		fl_user_image = (FrameLayout) findViewById(R.id.fl_user_image);
		et_id = (EditText) findViewById(R.id.et_id);
		et_name = (EditText) findViewById(R.id.et_name);
		et_relation = (EditText) findViewById(R.id.et_relation);
		et_school_name = (EditText) findViewById(R.id.et_school_name);
		et_school_grade = (EditText) findViewById(R.id.et_school_grade);
		et_school_class = (EditText) findViewById(R.id.et_school_class);
		btn_register = (Button) findViewById(R.id.btn_register);
		
		tvParent.setOnClickListener(mClick);
		tvStudent.setOnClickListener(mClick);
		btn_register.setOnClickListener(mClick);
		fl_user_image.setOnClickListener(mClick);
		
		//et_id : mdn 필드로 활용
		et_id.setHint("phone number");
		
		//update or add 인지 판단
		mMode = getIntent().getExtras().getInt("mode");
		mMember = (MemberVO)getIntent().getSerializableExtra("member");
		if (mMode == MainActivity.MOD_UPDATE) {
			tvTitle.setText("가족 구성원 수정");
			
			et_id.setText(mMember.mdn);
			et_name.setText(mMember.name);
			et_relation.setText(mMember.relation);
			if(!TextUtils.isEmpty(mMember.photo)) {
				iv_user_image.setImageBitmap(Util.StringToBitmap(mMember.photo));
			}
			if(mMember.is_parent == 0) {
				et_school_name.setText(mMember.school_name);
				et_school_grade.setText(mMember.school_grade);
				et_school_class.setText(mMember.school_ban);
				tvStudent.callOnClick(); //학생탭 선택
			} 
			
		} else {
			tvTitle.setText("가족 구성원 추가");
		}
	}
	
    protected void onActivityResult(int requestCode, int resultCode,
            Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
 
        switch (requestCode) {
        case MainActivity.REQ_CODE_PICK_IMAGE:
            if (resultCode == RESULT_OK) {
                if (imageData != null) {
                    String filePath = Environment.getExternalStorageDirectory() + "/temp.jpg";

                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    // temp.jpg파일을 Bitmap으로 디코딩한다.
                    mMember.photo = Util.BitmapToString(selectedImage);

                    //iv_user_image.setImageBitmap(selectedImage); 
                    iv_user_image.setImageBitmap(selectedImage);
                    //temp.jpg파일을 이미지뷰에 씌운다.
                }
            }
            break;
        }
    }
    
    private void getSave() {
    	LoadingDialog.showLoading(this);
		try {
			String url;
			JSONObject json = new JSONObject();
			
			if(mMode == MainActivity.MOD_ADD) {
				url = Constant.HOST + Constant.API_ADD_MEMBER;
				json.put("home_id", mMember.home_id);

			} else {
				url = Constant.HOST + Constant.API_UPDATE_MEMBER;
				json.put("member_id", mMember.member_id);
			}
			
			json.put("mdn", mMember.mdn);
			json.put("is_parent", mMember.is_parent);
			json.put("name", mMember.name);
			json.put("relation", mMember.relation);
			json.put("photo", mMember.photo);
			
			if(mMember.is_parent == 0) {
				json.put("school_name", mMember.school_name);
				json.put("school_grade", mMember.school_grade);
				json.put("school_ban", mMember.school_ban);
			}
			
			Log.d("LDK", "url:" + url);
			Log.d("LDK", "input parameter:" + json.toString(1));
	
			mAq.post(url, json, JSONObject.class, new AjaxCallback<JSONObject>(){
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					LoadingDialog.hideLoading();
					try {
						if(status.getCode() != 200) {
							
							return;
						}
						
						Log.d("LDK", "result:" + object.toString(1));
						
						if("0".equals(object.getString("result"))) {
							setResult(RESULT_OK);
							finish();
						} else {

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
	
    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }
    
    private boolean isSdcardMounted() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED))
            return true;
 
        return false;
    }
	
    private File getTempFile() {
        if (isSdcardMounted()) {
            File f = new File(Environment.getExternalStorageDirectory(), // 외장메모리 경로
            		MainActivity.TEMP_PHOTO_FILE);
            try {
                f.createNewFile();      // 외장메모리에 temp.jpg 파일 생성
            } catch (IOException e) {
            }
 
            return f;
        } else
            return null;
    }
	
	View.OnClickListener mClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tvParent:
				tvParent.setBackgroundColor(0xFF930D03);
				tvParent.setTextColor(0xFFF0F0F0);
				mMember.is_parent = 1;
				tvStudent.setBackgroundColor(0xFFF0F0F0);
				tvStudent.setTextColor(0xFF930D03);
				school_info.setVisibility(View.GONE);
				break;
			case R.id.tvStudent:
				tvParent.setBackgroundColor(0xFFF0F0F0);
				tvParent.setTextColor(0xFF930D03);
				mMember.is_parent = 0;
				tvStudent.setBackgroundColor(0xFF930D03);
				tvStudent.setTextColor(0xFFF0F0F0);
				school_info.setVisibility(View.VISIBLE);
				break;
				
			case R.id.fl_user_image:
				Intent intent = new Intent(Intent.ACTION_PICK,
	                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	            intent.setType("image/*");              // 모든 이미지
	            intent.putExtra("crop", "true");        // Crop기능 활성화
	            intent.putExtra("scale", true);
	            intent.putExtra("outputX",  200);
	            intent.putExtra("outputY",  200);
	            intent.putExtra("aspectX",  1);
	            intent.putExtra("aspectY",  1);

	            intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());     // 임시파일 생성
	            intent.putExtra("outputFormat",         // 포맷방식
	                    Bitmap.CompressFormat.JPEG.toString());

	            startActivityForResult(intent, MainActivity.REQ_CODE_PICK_IMAGE);
	            break;
				
			case R.id.btn_register:
				String id = et_id.getText().toString();
				if(TextUtils.isEmpty(et_id.getText().toString())){
					Util.showToast(mContext, "전화번호를 입력하세요.");
					return;
				}
				if(TextUtils.isEmpty(et_name.getText().toString())){
					Util.showToast(mContext, "이름을 입력하세요.");
					return;
				}
				if(TextUtils.isEmpty(et_relation.getText().toString())){
					Util.showToast(mContext, "관계를 입력하세요.");
					return;
				}
				if(mMember.is_parent == 0) {
					if(TextUtils.isEmpty(et_school_name.getText().toString())){
						Util.showToast(mContext, "학교명을 입력하세요.");
						return;
					}
					if(TextUtils.isEmpty(et_school_grade.getText().toString())){
						Util.showToast(mContext, "학년을 입력하세요.");
						return;
					}
					if(TextUtils.isEmpty(et_school_class.getText().toString())){
						Util.showToast(mContext, "반을 입력하세요.");
						return;
					}
				}
				
				mMember.mdn= et_id.getText().toString();
				mMember.name = et_name.getText().toString();
				mMember.relation = et_relation.getText().toString();
				//mMember.mdn = Util.getMdn(mContext);
				mMember.school_name = et_school_name.toString();
				mMember.school_grade = et_school_grade.toString();
				mMember.school_ban = et_school_class.toString();
				
				getSave();
				
				break;
				
			default:
				break;
			}
		}
	};
}
