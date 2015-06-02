package com.lk.hotelcheck.activity.hotel;

import java.util.Calendar;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lk.hotelcheck.R;
import com.lk.hotelcheck.bean.Hotel;
import com.lk.hotelcheck.manager.DataManager;

import common.Constance;

public class HotelBaseInfoFragment extends Fragment {
	private Context mContext;
	private int mPosition;
	private Hotel mHotel;
	private View mRootView;
	private TextView mNameTextView;
	private TextView mAddressTextView;
	private TextView mPhoneTextView;
	private TextView memoTextView;
	private TextView mOpenDateTextView;
	private TextView mLastCheckedDateTextView;
	private TextView mRoomNumberTextView;
	private TextView mRoomCheckedNumberTextView;
	private TextView mFloorTextView;
	private TextView mCheckedDateTextView;
//	private TextView mGuardianNumberTextView;
	private RelativeLayout mRoomNumberLayout;
	private RelativeLayout mRoomCheckedNumberLayout;
	private RelativeLayout mCheckedDateLayout;
	private RelativeLayout mFloorLayout;
	private DatePickerDialog mDatePickerDialog;
	private AlertDialog alertDialog;
	private static final int ALERT_DIALOG_ROOM_NUMBER = 1;
	private static final int ALERT_DIALOG_ROOM_CHECKED_NUMBER = 2;
	private EditText alertEditText;
	private AlertDialog mFloorAlertDialog;
	private EditText mFloorStartEditText;
	private EditText mFloorEndEditText;

	public static HotelBaseInfoFragment newInstance(int position) {
		HotelBaseInfoFragment fragment = new HotelBaseInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constance.IntentKey.INTENT_KEY_POSITION, position);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mPosition = getArguments().getInt(
					Constance.IntentKey.INTENT_KEY_POSITION, -1);
			mHotel = DataManager.getInstance().getHotel(mPosition);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_baseinfo, container,
					false);
			mNameTextView = (TextView) mRootView.findViewById(R.id.tv_name);
			mAddressTextView = (TextView) mRootView
					.findViewById(R.id.tv_address);
			mPhoneTextView = (TextView) mRootView.findViewById(R.id.tv_phone);
			memoTextView = (TextView) mRootView.findViewById(R.id.tv_memo);
			mOpenDateTextView = (TextView) mRootView
					.findViewById(R.id.tv_open_date);
			mLastCheckedDateTextView = (TextView) mRootView
					.findViewById(R.id.tv_last_checked_date);
			mRoomNumberTextView = (TextView) mRootView
					.findViewById(R.id.tv_room_number);
			mRoomCheckedNumberTextView = (TextView) mRootView
					.findViewById(R.id.tv_room_check_number);
			mFloorTextView = (TextView) mRootView
					.findViewById(R.id.tv_floor_number);
			mCheckedDateTextView = (TextView) mRootView
					.findViewById(R.id.tv_check_date);
			mRoomNumberLayout = (RelativeLayout) mRootView
					.findViewById(R.id.rl_room_number);
			mRoomCheckedNumberLayout = (RelativeLayout) mRootView
					.findViewById(R.id.rl_room_check_number);
			mCheckedDateLayout = (RelativeLayout) mRootView
					.findViewById(R.id.rl_check_date);
			mFloorLayout = (RelativeLayout) mRootView
					.findViewById(R.id.rl_floor_number);
//			mGuardianNumberTextView = (TextView) mRootView.findViewById(R.id.tv_guardian_number);
			init();
		}
		return mRootView;
	}

	@Override
	public void onPause() {
		super.onPause();
		DataManager.getInstance().setHotel(mPosition, mHotel);
	}
	
//	public void setGuardianNumber(String number) {
//		if (number == null) {
//			return;
//		}
//		mGuardianNumberTextView.setText(number);
//	}

	private void init() {
		mNameTextView.setText(mHotel.getName());
		mAddressTextView.setText(mHotel.getAddress());
		mPhoneTextView.setText(mHotel.getPhone());
		memoTextView.setText(mHotel.getMemo());
		mOpenDateTextView.setText("开业时间：" + mHotel.getLastCheckedDate());
		mLastCheckedDateTextView.setText("上次检查：" + mHotel.getLastCheckedDate());
		mRoomNumberTextView.setText("" + mHotel.getRoomCount());
		mRoomCheckedNumberTextView.setText("" + mHotel.getRoomCheckedCount());
		mFloorTextView.setText(mHotel.getFloorStart() + "F -- " + mHotel.getFloorEnd() + "F");
//		mGuardianNumberTextView.setText(mHotel.getGuardianNumber());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String date = calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH);
		mHotel.setCheckDate(date);
		mCheckedDateTextView.setText(date);
		mFloorLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showFloor();
			}
		});
		mRoomNumberLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(ALERT_DIALOG_ROOM_NUMBER);
			}
		});
		mRoomCheckedNumberLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(ALERT_DIALOG_ROOM_CHECKED_NUMBER);
			}
		});

		mCheckedDateLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(System.currentTimeMillis());
				mDatePickerDialog = new DatePickerDialog(mContext,
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								String date = year + "-" + (monthOfYear + 1)
										+ "-" + dayOfMonth;
								mCheckedDateTextView.setText(date);
								mHotel.setCheckDate(date);
							}
						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
								.get(Calendar.DAY_OF_MONTH));
				mDatePickerDialog.show();
			}
		});
	}

	private void showDialog(final int type) {
		LayoutInflater factory = LayoutInflater.from(mContext);// 提示框
		View view = factory.inflate(R.layout.alert_dialog_edit, null);// 这里必须是final的
		alertEditText = (EditText) view.findViewById(R.id.et_content);// 获得输入框对象
		alertDialog = new AlertDialog.Builder(mContext)
				// .setTitle(title)//提示框标题
				.setView(view)
				.setPositiveButton("确定",// 提示框的两个按钮
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String number = alertEditText.getText()
										.toString();
								if (TextUtils.isEmpty(number)) {
									Toast.makeText(mContext, "请输入内容", Toast.LENGTH_SHORT).show();
									return;
								}
								switch (type) {
								case ALERT_DIALOG_ROOM_NUMBER:
									mRoomNumberTextView.setText(number);
									mHotel.setRoomCount(Integer.valueOf(number));
									break;
								case ALERT_DIALOG_ROOM_CHECKED_NUMBER:
									mRoomCheckedNumberTextView.setText(number);
									mHotel.setRoomCheckedCount(Integer
											.valueOf(number));
									break;
								default:
									break;
								}
								alertEditText.setText("");
								// DataManager.getInstance().setHotel(mPosition,
								// mHotel);
							}
						})
				.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								alertEditText.setText("");

							}
						}).create();
		switch (type) {
		case ALERT_DIALOG_ROOM_NUMBER:
			alertDialog.setTitle("请输入房间数量");
			break;
		case ALERT_DIALOG_ROOM_CHECKED_NUMBER:
			alertDialog.setTitle("请输入在住房数");
			break;
		default:
			break;
		}
		alertDialog.show();
	}

	private void showFloor() {
		if (mFloorAlertDialog == null) {
			LayoutInflater factory = LayoutInflater.from(mContext);// 提示框
			View view = factory.inflate(R.layout.alert_dialog_floor, null);// 这里必须是final的
			mFloorStartEditText = (EditText) view.findViewById(R.id.et_start);// 获得输入框对象
			mFloorEndEditText = (EditText) view.findViewById(R.id.et_end);
			mFloorAlertDialog = new AlertDialog.Builder(mContext)
					 .setTitle("请输入楼层范围")//提示框标题
					.setView(view)
					.setPositiveButton(
							"确定",// 提示框的两个按钮
							new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String floorStart = mFloorStartEditText
											.getText().toString();
									String floorEnd = mFloorEndEditText
											.getText().toString();
									if (TextUtils.isEmpty(floorStart)) {
										Toast.makeText(mContext, "请输入开始楼层", Toast.LENGTH_SHORT).show();
										return;
									}
									if (TextUtils.isEmpty(floorEnd)) {
										Toast.makeText(mContext, "请输入结束楼层", Toast.LENGTH_SHORT).show();
										return;
									}
									mFloorTextView.setText(floorStart + " - "
											+ floorEnd);
									mFloorEndEditText.setText("");
									mFloorStartEditText.setText("");
									mHotel.setFloorStart(Integer
											.valueOf(floorStart));
									mHotel.setFloorEnd(Integer
											.parseInt(floorEnd));
									// DataManager.getInstance().setHotel(mPosition,
									// mHotel);
								}
							})
					.setNegativeButton(
							"取消",
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mFloorEndEditText.setText("");
									mFloorStartEditText.setText("");
								}
							}).create();
		}
		mFloorAlertDialog.show();
	}
}