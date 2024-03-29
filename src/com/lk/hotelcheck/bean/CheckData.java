package com.lk.hotelcheck.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.R.integer;
import android.text.TextUtils;
import android.util.SparseArray;

import com.lk.hotelcheck.bean.dao.AreaIssue;
import com.lk.hotelcheck.bean.dao.CheckIssue;
import com.lk.hotelcheck.bean.dao.IssueCheckedImage;
import com.lk.hotelcheck.manager.DataManager;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import common.Constance;
import common.Constance.PreQueType;

public class CheckData extends SugarRecord<CheckData> implements Serializable{
	/**
	 * 
	 */
	@Ignore
	private static final long serialVersionUID = 2005075015087879505L;
	@Ignore
	private List<IssueItem> mIssuelist;
	private String name;
	private int type;
	private long checkId;
	private int preQueType = PreQueType.TYPE_NEW;
	
	@Ignore
	private SparseArray<IssueItem> checkedIssueArray;
	
	public List<IssueItem> getIssuelist() {
		return mIssuelist;
	}
	public void setIssuelist(List<IssueItem> issuelist) {
		this.mIssuelist = issuelist;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public IssueItem getIssue(int position) {
		if (mIssuelist == null) {
			return null;
		}
		return mIssuelist.get(position);

	}
	
	public int getIssueCount() {
		return mIssuelist == null ? 0 :mIssuelist.size();
	}
	
	
	public int getCheckedIssueCount() {
		return checkedIssueArray == null ? 0 :checkedIssueArray.size();
	}
	
	
	public void setIssueItem(int position, IssueItem issueItem) {
		mIssuelist.set(position, issueItem);
		
	}
	
	
	public void addIssue(IssueItem issueItem) {
		if (issueItem == null) {
			return;
		}
		if (mIssuelist == null) {
			mIssuelist = new ArrayList<IssueItem>();
		}
		mIssuelist.add(issueItem);
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getCheckId() {
		return checkId;
	}
	public void setCheckId(long checkId) {
		this.checkId = checkId;
	}
	
	public void updateIssueCheck(IssueItem issueItem) {
		if (issueItem == null) {
			return;
		}
		if (checkedIssueArray == null) {
			checkedIssueArray = new SparseArray<IssueItem>();
		}
		if (issueItem.isCheck()) {
				checkedIssueArray.put(issueItem.getId(), issueItem);
		} else if (!issueItem.isCheck() && issueItem.getPreQueType() != Constance.PreQueType.TYPE_REVIEW) {
			if (checkedIssueArray.indexOfKey(issueItem.getId()) > -1) {
				checkedIssueArray.remove(issueItem.getId());
			}
		}
	}
	
	public void initCheckedIssue() {
		if (checkedIssueArray == null) {
			checkedIssueArray = new SparseArray<IssueItem>();
		} else {
			checkedIssueArray.clear();
		}
		for (IssueItem issueItem : mIssuelist) {
			if (issueItem.isCheck() || issueItem.getPreQueType() == PreQueType.TYPE_REVIEW) {
				checkedIssueArray.put(issueItem.getId(), issueItem);
			}
		}
	}
	
	public IssueItem getCheckedIssue(int position) {
		if (checkedIssueArray == null ) {
			return null;
		}
		return checkedIssueArray.valueAt(position);
	}
	
	public List<ImageItem> getCheckedPointImageList(int issuePosition) {
		List<ImageItem> imageList = null;
		if (checkedIssueArray == null || checkedIssueArray.size() == 0) {
			imageList = Collections.emptyList();
		} else {
			if (imageList == null) {
				imageList = new ArrayList<ImageItem>();
			}
			IssueItem issueItem = checkedIssueArray.valueAt(issuePosition);
			if (issueItem.getImageCount() > 0) {
				imageList.addAll(issueItem.getImagelist());
			}
		}
		return imageList;
	}

	public List<ImageItem> getAllCheckedImage() {
		List<ImageItem> imageList = null;
		if (checkedIssueArray == null || checkedIssueArray.size() == 0) {
			imageList = Collections.emptyList();
		} else {
			if (imageList == null) {
				imageList = new ArrayList<ImageItem>();
			}
			for (int i = 0; i < checkedIssueArray.size(); i++) {
				IssueItem issueItem = checkedIssueArray.valueAt(i);
				if (issueItem.getImageCount() > 0) {
					imageList.addAll(issueItem.getImagelist());
				}
			}
		}
		return imageList;
	}

	public void deleteCheckedIssueImage(int issuePosition, ImageItem imageItem) {
		IssueItem issueItem = checkedIssueArray.valueAt(issuePosition);
		for (int i = 0; i < issueItem.getImageCount(); i++) {
			ImageItem temp = issueItem.getImageItem(i);
			if (temp.getLocalImagePath().equals(imageItem.getLocalImagePath())) {
				issueItem.removeImageItem(i);
				IssueCheckedImage hotelCheck = IssueCheckedImage.deleteItemByImageLocalPath(imageItem.getLocalImagePath());
				if (TextUtils.isEmpty(issueItem.getContent())
						&& issueItem.getImageCount() == 0) {
					issueItem.setCheck(false);
					if (hotelCheck != null) {
					}
					initCheckedIssue();
				}
				return;
			}
		}
	}

	public void deleteCheckedIssueImage(ImageItem imageItem) {
		for (int j = 0; j < checkedIssueArray.size(); j++) {
			IssueItem issueItem = checkedIssueArray.valueAt(j);
			for (int i = 0; i < issueItem.getImageCount(); i++) {
				ImageItem temp = issueItem.getImageItem(i);
				if (temp.getLocalImagePath().equals(
						imageItem.getLocalImagePath())) {
					issueItem.removeImageItem(i);
					IssueCheckedImage.deleteItemByImageLocalPath(imageItem.getLocalImagePath());
					if (TextUtils.isEmpty(issueItem.getContent())
							&& issueItem.getImageCount() == 0) {
						issueItem.setCheck(false);
						initCheckedIssue();
					}
					return;
				}
			}
		}
		
	}
	
	public List<IssueItem> getCheckedIssue() {
		if (checkedIssueArray == null) {
			return Collections.emptyList();
		}
		List<IssueItem> data = new ArrayList<IssueItem>();
		for (int i = 0; i < checkedIssueArray.size(); i++) {
			IssueItem issueItem = checkedIssueArray.valueAt(i);
			data.add(issueItem);
		}
		return data;
	}
	
//	public int getFixedIssueCount() {
//		int count = 0;
//		if (checkedIssueArray != null) {
//			for (IssueItem issueItem : mIssuelist) {
//				if (issueItem.getIsPreQue() == PreQueType.TYPE_REVIEW) {
//					if (issueItem.getReformState() == IssueItem.REFORM_STATE_FIXED) {
//						count++;
//					}
//				}
//			}
//		}
//		return count;
//	}
//	
//	public int getFixingIssueCount() {
//		int count = 0;
//		if (checkedIssueArray != null) {
//			for (IssueItem issueItem : mIssuelist) {
//				if (issueItem.getIsPreQue() == PreQueType.TYPE_REVIEW ) {
//					if (issueItem.getReformState() == IssueItem.REFORM_STATE_FIXING) {
//						count++;
//					}
//				}
//			}
//		}
//		return count;
//	}
//	
//	public int getUnFixIssueCount() {
//		int count = 0;
//		if (checkedIssueArray != null) {
//			for (IssueItem issueItem : mIssuelist) {
//				if (issueItem.getIsPreQue() == PreQueType.TYPE_REVIEW ) {
//					if (issueItem.getReformState() == IssueItem.REFORM_STATE_UN_FIX) {
//						count++;
//					}
//				}
//			}
//		}
//		return count;
//	}
	public int getFixIssueCount(int issueState) {
		int count = 0;
		if (checkedIssueArray != null) {
			for (int i = 0; i < checkedIssueArray.size(); i++) {
				IssueItem issueItem = checkedIssueArray.valueAt(i);
				if (issueItem.getReformState() == issueState) {
						count++;
				}
			}
		}
		return count;
	}
	
	
	
	public int getNewIssueCount() {
		int count = 0;
		if (checkedIssueArray != null) {
			for (IssueItem issueItem : mIssuelist) {
				if (issueItem.getPreQueType() != PreQueType.TYPE_REVIEW && issueItem.isCheck()) {
					count++;
				}
			}
		}
		return count;
	}
	public boolean isCanDelete() {
		if (preQueType == PreQueType.TYPE_REVIEW) {
			return false;
		} else {
			if (checkedIssueArray != null) {
				for (int i = 0; i < checkedIssueArray.size(); i++) {
					IssueItem issueItem = checkedIssueArray.valueAt(i);
					if (issueItem.isCheck()) {
						return false;
					}
				}
			}
		}
		return true;
	}
	public int getPreQueType() {
		return preQueType;
	}
	public void setPreQueType(int preQueType) {
		this.preQueType = preQueType;
	}
	public void addReviewDymicIssue(int checkId, AreaIssue areaIssue) {
		if (areaIssue == null) {
			return;
		}
		if (mIssuelist == null) {
			mIssuelist = new ArrayList<IssueItem>();
		} 
		for (IssueItem temp : mIssuelist) {
			if (temp.getName().contentEquals(areaIssue.getIssueName())) {
				temp.setPreQueType(areaIssue.getIsPreQue());
				temp.setIsDefQue(areaIssue.getIsDefQue());
				temp.setDimOneId(areaIssue.getDimOneId());
				temp.setDimOneName(areaIssue.getDimOneName());
				return;
			}
		}
		IssueItem issueItem = new IssueItem();
		int id = Math.abs(areaIssue.getIssueName().hashCode());
		issueItem.setId(id);
		issueItem.setName(areaIssue.getIssueName());
		issueItem.setPreQueType(areaIssue.getIsPreQue());
		issueItem.setIsDefQue(areaIssue.getIsDefQue());
		issueItem.setDimOneId(areaIssue.getDimOneId());
		issueItem.setDimOneName(areaIssue.getDimOneName());
		CheckIssue checkIssue = DataManager.getInstance().getCheckIssue(checkId,
				getId(), id);
		if (checkIssue != null) {
			issueItem.setCheck(checkIssue.isCheck());
			issueItem.setContent(checkIssue.getContent());
			issueItem.setReformState(checkIssue.getReformState());
		}
		mIssuelist.add(issueItem);
		return;
	}
	
}
