package com.lk.hotelcheck.activity.hotel;

import com.lk.hotelcheck.R;
import com.lk.hotelcheck.activity.photochosen.PhotoChosenActivity;
import com.lk.hotelcheck.bean.CheckData;
import com.lk.hotelcheck.bean.Hotel;
import com.lk.hotelcheck.bean.IssueItem;
import com.lk.hotelcheck.manager.DataManager;
import com.lk.hotelcheck.util.DrawUtil;

import common.Constance;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HotelReportFragment extends Fragment{
	
	public static HotelReportFragment newInstance(int position) {
		HotelReportFragment fragment = new HotelReportFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constance.IntentKey.INTENT_KEY_POSITION, position);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	private Hotel hotel;
	private int position;
	private Activity activity;
	private View mRootView;
	private IssueListAdapter adapter;
	private TextView mUserNameTextView;
	private TextView mCheckDateTextView;
	private TextView mRoomNumberTextView;
	private TextView mRoomInUseNumberTextView;
	private TextView mRoomCheckedNumberTextView;
	private TextView mIssueCountTextView;
	private TextView mGNumberTextView;
	private ExpandableListView mExpandableListView;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		hotel = DataManager.getInstance().getHotel(position);
		if (getUserVisibleHint() && adapter != null) {
			adapter.notifyDataSetChanged();
			initInfoData();
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(Constance.IntentKey.INTENT_KEY_POSITION);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_report, container,false);
			init(mRootView);
		}
		ViewGroup parent = (ViewGroup) mRootView.getParent();
		if (parent != null) {
			parent.removeView(mRootView);
		}
		return mRootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
		expanAll();
	}
	
	private void initInfoData() {
		if (hotel != null) {
			mCheckDateTextView.setText(hotel.getCheckDate());
			mRoomNumberTextView.setText(""+hotel.getRoomCount());
			mRoomInUseNumberTextView.setText(""+hotel.getRoomInUseCount());
			mRoomCheckedNumberTextView.setText(""+hotel.getRoomHadCheckedCount());
			mIssueCountTextView.setText(""+hotel.getIssueCount());
			mGNumberTextView.setText(hotel.getGuardianNumber());
		}
	}
	
	
	private void init(View view) {
		mExpandableListView = (ExpandableListView) view.findViewById(R.id.elv_report);
		View headerView = LayoutInflater.from(view.getContext()).inflate(R.layout.listview_report_header, null);
		mUserNameTextView = (TextView) headerView.findViewById(R.id.tv_check_men);
		mCheckDateTextView = (TextView) headerView.findViewById(R.id.tv_check_date);
		mRoomNumberTextView = (TextView) headerView.findViewById(R.id.tv_room_number);
		mRoomInUseNumberTextView = (TextView) headerView.findViewById(R.id.tv_room_in_use);
		mRoomCheckedNumberTextView = (TextView) headerView.findViewById(R.id.tv_checked_count);
		mIssueCountTextView = (TextView) headerView.findViewById(R.id.tv_issue_number);
		mGNumberTextView = (TextView) headerView.findViewById(R.id.tv_guardian_number);
		initInfoData();
		
		
		mExpandableListView.addHeaderView(headerView);
		adapter = new IssueListAdapter();
		mExpandableListView.setAdapter(adapter);
		mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				CheckData checkData = hotel.getCheckData(groupPosition);
				IssueItem issueItem = checkData.getCheckedIssue(childPosition);
				if (issueItem.getImageCount() >0 ) {
					PhotoChosenActivity.gotoPhotoChosen(activity, position,groupPosition,childPosition);
				}
				return false;
			}
		});
		expanAll();
	}
	
	
	private void expanAll(){
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			if (adapter.getChildrenCount(i) > 0) {
				mExpandableListView.expandGroup(i);
			}
		}
	}
	
	class IssueListAdapter extends BaseExpandableListAdapter {

		

		@Override
		public int getGroupCount() {
			return hotel.getCheckDataCount();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return hotel.getCheckData(groupPosition).getCheckedIssueCount();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_report_group_item, parent,false);
				viewHolder = new ViewHolder();
				viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.issueCountTextView = (TextView) convertView.findViewById(R.id.tv_number);
				viewHolder.colorImageView = (ImageView) convertView.findViewById(R.id.iv_color);
				viewHolder.mNumberView = convertView.findViewById(R.id.rl_number);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			switch (groupPosition) {
			case 0:
				viewHolder.colorImageView.setBackgroundColor(getResources().getColor(R.color.color_one));
				break;
			case 1:
				viewHolder.colorImageView.setBackgroundColor(getResources().getColor(R.color.color_two));
				break;
			case 2:
				viewHolder.colorImageView.setBackgroundColor(getResources().getColor(R.color.color_three));
				break;
			case 3:
				viewHolder.colorImageView.setBackgroundColor(getResources().getColor(R.color.color_four));
				break;
			case 4:
				viewHolder.colorImageView.setBackgroundColor(getResources().getColor(R.color.color_five));
				break;
			case 5:
				viewHolder.colorImageView.setBackgroundColor(getResources().getColor(R.color.color_six));
				break;
			default:
				viewHolder.colorImageView.setBackgroundColor(getResources().getColor(R.color.color_seven));
				break;
			}
			CheckData checkData = hotel.getCheckData(groupPosition);
			viewHolder.nameTextView.setText(checkData.getName());
			int count = getChildrenCount(groupPosition);
			if (count <= 0) {
				viewHolder.mNumberView.setVisibility(View.GONE);
			} else {
				viewHolder.mNumberView.setVisibility(View.VISIBLE);
				viewHolder.issueCountTextView.setText(""+count);
			}
			ExpandableListView mExpandableListView = (ExpandableListView) parent;
		    mExpandableListView.expandGroup(groupPosition);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_report_check_issue_item, parent,false);
				convertView.setPadding(DrawUtil.dip2px(10), 0, DrawUtil.dip2px(10), 0);
				viewHolder = new ViewHolder();
				viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.flagTextView = (TextView) convertView.findViewById(R.id.tv_flag);
				viewHolder.percentTextView = (TextView) convertView.findViewById(R.id.tv_percent);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			CheckData checkData = hotel.getCheckData(groupPosition);
			IssueItem issueItem = checkData.getCheckedIssue(childPosition);
			if (issueItem.getImageCount() >0 ) {
				viewHolder.flagTextView.setText("查看图片");
			} else {
				viewHolder.flagTextView.setText("");
			}
			viewHolder.nameTextView.setText(issueItem.getName());
			if (checkData.isGetSublist()) {
				viewHolder.percentTextView.setVisibility(View.VISIBLE);
				String percent = checkData.getSubIssuePercent(childPosition);
				viewHolder.percentTextView.setText(percent);
			} else {
				viewHolder.percentTextView.setVisibility(View.GONE);
			}
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onGroupExpanded(int groupPosition) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGroupCollapsed(int groupPosition) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public long getCombinedChildId(long groupId, long childId) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getCombinedGroupId(long groupId) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		class ViewHolder {
			private TextView nameTextView;
			private TextView issueCountTextView;
			private TextView percentTextView;
			private TextView flagTextView;
			private ImageView colorImageView;
			private View mNumberView;
		}
	}
}