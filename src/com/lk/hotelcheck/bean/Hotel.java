package com.lk.hotelcheck.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.lk.hotelcheck.bean.dao.AreaIssue;
import com.lk.hotelcheck.bean.dao.CheckIssue;
import com.lk.hotelcheck.bean.dao.DymicIssue;
import com.lk.hotelcheck.bean.dao.IssueCheckedImage;
import com.lk.hotelcheck.manager.DataManager;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import common.Constance;
import common.Constance.CheckDataType;
import common.Constance.CheckType;
import common.Constance.DefQueType;
import common.Constance.ImageUploadState;
import common.Constance.PreQueType;

public class Hotel extends SugarRecord<Hotel>{

//	private int id;/
	private int branchNumber;
	private String name;
	private String address;
	private String phone;
	private String memo;
	private String openDate;
	private transient String lastCheckedDate;
	private String checkDate;
	private int roomCount;
	private int roomCheckedCount;
//	private String floorStart;
//	private String floorEnd;
	private String floor;
	private boolean imageStatus;
	private boolean dataStatus;
	private boolean status;
	private String guardianNumber;
	@Ignore
	private transient String regionalManager;
	private int checkType;
	private transient String branchManager;
	private transient String branchManagerTele;
	private transient String brand;
	@Ignore
	private transient List<CheckData> checkDatas;
	@Ignore
	private transient List<CheckData> roomArray;
	@Ignore
	private transient List<CheckData> passwayArray;
	@Ignore
	private transient List<AreaIssue> questionList;
	@Ignore
	private transient SparseArray<IssueItem> roomCheckedIsuueArray = new SparseArray<IssueItem>();
	@Ignore
	private transient SparseArray<IssueItem> passwayCheckedIsuueArray = new SparseArray<IssueItem>();
	@Ignore
	private transient List<IssueItem> roomDymicIssueArray;
	@Ignore
	private transient List<IssueItem> passwayDymicIssueArray;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCheckId() {
		return getId().intValue();
	}
	public void setCheckId(int id) {
		setId((long) id);
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getLastCheckedDate() {
		return lastCheckedDate;
	}
	public void setLastCheckedDate(String lastCheckedDate) {
		this.lastCheckedDate = lastCheckedDate;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public int getRoomCount() {
		return roomCount;
	}
	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}
	public int getRoomCheckedCount() {
		return roomCheckedCount;
	}
	public void setRoomCheckedCount(int roomCheckedCount) {
		this.roomCheckedCount = roomCheckedCount;
	}
	public boolean isImageStatus() {
		return imageStatus;
	}
	public void setImageStatus(boolean imageStatus) {
		this.imageStatus = imageStatus;
	}
	public boolean isDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(boolean dataStatus) {
		this.dataStatus = dataStatus;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public List<CheckData> getCheckDatas() {
		return checkDatas;
	}
	public void setCheckDatas(List<CheckData> checkDatas) {
		this.checkDatas = checkDatas;
	}
	
	
	public int getCheckDataCount() {
		int count = checkDatas == null ? 0 : checkDatas.size();
		return count;
	}
	
	public CheckData getCheckData(int position) {
		if (checkDatas == null || position < 0 || checkDatas.size() <= position) {
			return null;
		}
		return checkDatas.get(position);
	}
	
	public void setCheckDatas(CheckData mCheckData, int mCheckDataPosition) {
		if (checkDatas == null) {
			checkDatas = new ArrayList<CheckData>();
		}
		checkDatas.set(mCheckDataPosition, mCheckData);
		
	}
	
	public int getRoomInUseCount() {
		return roomCheckedCount;
	}
	
	public int getRoomHadCheckedCount() {
		return roomArray == null ? 0 : roomArray.size();
	}
	
	public int getIssueCount() {
//		int count = 0;
//		if (checkDatas != null) {
//			for (CheckData checkData : checkDatas) {
//				if (checkData.getCheckId() != Constance.CHECK_DATA_ID_PASSWAY 
//						&& checkData.getCheckId() != Constance.CHECK_DATA_ID_ROOM) {
//					count += checkData.getCheckedIssueCount();
//				}
//			}
//		}
//		Log.d("lxk", "");
//		count += getDymicRoomCheckedIssueCount();
//		count += getDymicPasswayCheckedIssueCount();
//		if (roomCheckedIsuueArray != null) {
//			count += roomCheckedIsuueArray.size();
//		}
//		if (passwayCheckedIsuueArray != null) {
//			count += passwayCheckedIsuueArray.size();
//		}
		int count = 0;
		if (checkDatas != null) {
			for (CheckData checkData : checkDatas) {
				if (checkData.getType() == CheckDataType.TYPE_ROOM) {
					count += getDymicRoomCheckedIssueCount();
				} else if (checkData.getType() == CheckDataType.TYPE_PASSWAY) {
					count += getDymicPasswayCheckedIssueCount();
				} else {
					count += checkData.getCheckedIssueCount();
				}
			}
		}

		
		
		return count;
	}
	
	public int getImageCount() {
		int count = 0;
		for (CheckData checkData : checkDatas) {
				for (IssueItem issueItem : checkData.getIssuelist()) {
					if (issueItem.isCheck()) {
						count += issueItem.getImageCount();
					}
				}
		}
		if (roomArray != null) {
			for (CheckData checkData : roomArray) {
				for (IssueItem issueItem : checkData.getIssuelist()) {
					if (issueItem.isCheck()) {
						count += issueItem.getImageCount();
					}
				}
			}
		}
		if (passwayArray != null) {
			for (CheckData checkData : passwayArray) {
				for (IssueItem issueItem : checkData.getIssuelist()) {
					if (issueItem.isCheck()) {
						count += issueItem.getImageCount();
					}
				}
			}
		}
		return count;
	}
	
	public String getGuardianNumber() {
		return guardianNumber;
	}
	
	public void setGuardianNumber(String guardianNumber) {
		this.guardianNumber = guardianNumber;
	}
	
	public List<ImageItem> getAllImage() {
		List<ImageItem> allImageItems = new ArrayList<ImageItem>();
		for (CheckData checkData : checkDatas) {
				for (IssueItem issueItem : checkData.getIssuelist()) {
					if (issueItem.isCheck()) {
						if (issueItem.getImageCount() >0) {
							allImageItems.addAll(issueItem.getImagelist());
						}
					}
				}
		}
		return allImageItems;
	}

	public void addCheckData(CheckData checkData) {
		if (checkDatas == null) {
			checkDatas = new ArrayList<CheckData>();
		}
		checkDatas.add(checkData);
	}
	
	public void addRoom(CheckData checkData) {
		if (checkData == null) {
			return;
		}
		if (roomArray == null) {
			roomArray = new ArrayList<CheckData>();
		}
		if (checkType == CheckType.CHECK_TYPE_REVIEW) {
			initReviewQuestionCheckData(checkData);
		}
		roomArray.add(0, checkData);
		initDymicRoomCheckedData();
	}
		
	public boolean hasRoom(long areaId) {
		if (roomArray != null) {
			for (CheckData checkData : roomArray) {
				if (checkData.getId() == areaId) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void addPassway(CheckData checkData) {
		if (checkData == null) {
			return;
		}
		if (passwayArray == null) {
			passwayArray = new ArrayList<CheckData>();
		}
		if (checkType == CheckType.CHECK_TYPE_REVIEW) {
			initReviewQuestionCheckData(checkData);
		}
		passwayArray.add(0, checkData);
		initDymicPasswayCheckedData();
	}
	
	public boolean hasPassway(long areaId) {
		if (passwayArray != null) {
			for (CheckData checkData : passwayArray) {
				if (checkData.getId() == areaId) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public int getDymicRoomCount() {
		return roomArray == null ? 0 : roomArray.size();
	}
	
	public int getDymicPasswayCount() {
		return passwayArray == null ? 0 : passwayArray.size();
	}
	public CheckData getDymicRoomData(int position) {
		if (roomArray == null) {
			return null;
		}
		return roomArray.get(position);
	}
	public CheckData getDymicPasswayData(int position) {
		if (passwayArray == null) {
			return null;
		}
		return passwayArray.get(position);
	}
	public void removeDymicRoom(int position) {
		if (roomArray != null) {
			CheckData checkData = roomArray.get(position);
			checkData.delete();
			roomArray.remove(position);
		}
	}
	public void removeDymicPassway(int position) {
		if (passwayArray != null) {
			CheckData checkData = passwayArray.get(position);
			checkData.delete();
			passwayArray.remove(position);
		}
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	
	public CheckData getRoomData(int position) {
		if (roomArray != null) {
			return roomArray.get(position);
		}
		return null;
	}
	
	public CheckData getPasswayData(int position) {
		if (passwayArray != null) {
			return passwayArray.get(position);
		}
		return null;
	}
	
	
	public CheckData getCheckData(int type, int position) {
		switch (type) {
		case Constance.CheckDataType.TYPE_NORMAL:
			return getCheckData(position);
		case Constance.CheckDataType.TYPE_ROOM:
			return getRoomData(position);
		case Constance.CheckDataType.TYPE_PASSWAY:
			return getPasswayData(position);
		default:
			return null;
		}
	}
	
	@Override
	public long save() {
		if (roomArray != null) {
			for (int i = 0; i < roomArray.size(); i++) {
				roomArray.get(i).save();
			}
		}

		if (passwayArray != null) {
			for (int i = 0; i < passwayArray.size(); i++) {
				passwayArray.get(i).save();
			}
		}
		return super.save();
	}
	public void addQuestion(AreaIssue issueItem) {
		if (issueItem == null) {
			return;
		}
		if (questionList == null) {
			questionList = new ArrayList<AreaIssue>();
		}
		questionList.add(issueItem);
	}
	public int getCheckType() {
		return checkType;
	}
	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}
	
	public List<AreaIssue> getQuestionList() {
		return questionList;
	}
	public List<CheckData> getRoomList() {
		if (roomArray == null) {
			return Collections.emptyList();
		}
		return roomArray;
	}
	public List<CheckData> getPasswayList() {
		if (passwayArray == null) {
			return Collections.emptyList();
		}
		return passwayArray;
	}
	public void setBaseInfo(Hotel hotel) {
		if (hotel == null) {
			return;
		}
		this.roomCount = hotel.getRoomCount();
		this.roomCheckedCount = hotel.getRoomCheckedCount();
		this.floor = hotel.getFloor();
		this.imageStatus = hotel.isImageStatus();
		this.guardianNumber = hotel.getGuardianNumber();
		this.checkDate = hotel.getCheckDate();
	}
	public void setRoom(long areaId, CheckData checkData) {
		if (roomArray == null) {
			roomArray = new ArrayList<CheckData>();
		}
		for (int i = 0; i < roomArray.size(); i++) {
			CheckData roomData = roomArray.get(i);
			if (roomData.getId() == areaId) {
				roomArray.set(i, checkData);
			}
		}
		
	}
	public void setPassway(long areaId, CheckData checkData) {
		if (passwayArray == null) {
			passwayArray = new ArrayList<CheckData>();
		}
		for (int i = 0; i < passwayArray.size(); i++) {
			CheckData passwayData = passwayArray.get(i);
			if (passwayData.getId() == areaId) {
				passwayArray.set(i, checkData);
			}
		}
	}
	
	public int getFixIssueCount(int issueState) {
		int count = 0;
		if (checkDatas != null) {
			for (CheckData checkData : checkDatas) {
				if (checkData.getType() == CheckDataType.TYPE_NORMAL) {
					count += checkData.getFixIssueCount(issueState);
				}
			}
		}
		if (roomCheckedIsuueArray != null) {
			for (int i = 0; i < roomCheckedIsuueArray.size(); i++) {
				IssueItem issueItem = roomCheckedIsuueArray.valueAt(i);
				if (issueItem.getReformState() == issueState) {
					count++;
				}
			}
			
		}
		if (passwayCheckedIsuueArray != null) {
			for (int i = 0; i < passwayCheckedIsuueArray.size(); i++) {
				IssueItem issueItem = passwayCheckedIsuueArray.valueAt(i);
				if (issueItem.getReformState() == issueState) {
					count++;
				}
			}
			
		}
		return count;
	}
	
	
	public int getNewIssueCount() {
		int count = 0;
		if (checkDatas != null) {
			for (CheckData checkData : checkDatas) {
				if (checkData.getType() == CheckDataType.TYPE_NORMAL) {
					count += checkData.getNewIssueCount();
				}
			}
		}
		if (roomCheckedIsuueArray != null) {
			for (int i = 0; i < roomCheckedIsuueArray.size(); i++) {
				IssueItem issueItem = roomCheckedIsuueArray.valueAt(i);
				if (issueItem.getPreQueType() != PreQueType.TYPE_REVIEW && issueItem.isCheck()) {
					count++;
				}
			}
		}
		if (passwayCheckedIsuueArray != null) {
			for (int i = 0; i < passwayCheckedIsuueArray.size(); i++) {
				IssueItem issueItem = passwayCheckedIsuueArray.valueAt(i);
				if (issueItem.getPreQueType() != PreQueType.TYPE_REVIEW && issueItem.isCheck()) {
					count++;
				}
			}
		}
		return count;
	}
	
	public String getDymicAreaCheckedIssuePercent(int checkType, int issueId) {
		List<CheckData> dataList = null;
		if (checkType == CheckDataType.TYPE_PASSWAY) {
			dataList = passwayArray;
		} else if (checkType == CheckDataType.TYPE_ROOM) {
			dataList = roomArray;
		}
		if (dataList == null ) {
			return "";
		}
		String percent = "";
		int count = 0;
		for (CheckData checkData : dataList) {
			for (IssueItem issueItem : checkData.getCheckedIssue()) {
				if (issueItem.getId() == issueId && issueItem.isCheck()) {
					count++;
					break;
				}
			}
		}
		int total = dataList.size();
		if (count >0 && total >0) {
			
			percent = String.format(Locale.CHINA, "%.2f%s", (count*1.00) /total * 100, "%");
		}
		return percent ;
	}
	
	public String getRoomIssuePercent(int issueId) {
		return getDymicAreaCheckedIssuePercent(CheckDataType.TYPE_ROOM, issueId);
	}
	
	public String getPasswayIssuePercent(int issueId) {
		return getDymicAreaCheckedIssuePercent(CheckDataType.TYPE_PASSWAY, issueId);
	}
	
//	public String getRoomIssuePercent(int issueId) {
//		if (roomArray == null ) {
//			return "";
//		}
//		String percent = "";
//		int count = 0;
//		for (CheckData checkData : roomArray) {
//			for (IssueItem issueItem : checkData.getCheckedIssue()) {
//				if (issueItem.getId() == issueId) {
//					count++;
//					break;
//				}
//			}
//		}
//		int total = roomArray.size();
//		if (count >0 && total >0) {
//			
//			percent = String.format(Locale.CHINA, "%.2f%s", (count*1.00) /total * 100, "%");
//		}
//		return percent ;
//	}
//	
//	public String getPasswayIssuePercent(int issueId) {
//		if (passwayArray == null ) {
//			return "";
//		}
//		String percent = "";
//		int count = 0;
//		for (CheckData checkData : passwayArray) {
//			for (IssueItem issueItem : checkData.getCheckedIssue()) {
//				if (issueItem.getId() == issueId) {
//					count++;
//					break;
//				}
//			}
//		}
//		int total = passwayArray.size();
//		if (count >0 && total >0) {
//			
//			percent = String.format(Locale.CHINA, "%.2f%s", (count*1.00) /total * 100, "%");
//		}
//		return percent ;
//	}
	
	public int getBranchNumber() {
		return branchNumber;
	}
	public void setBranchNumber(int branchNumber) {
		this.branchNumber = branchNumber;
	}
	
	public int getDymicRoomCheckedIssueCount() {
		return roomCheckedIsuueArray == null ? 0 : roomCheckedIsuueArray.size();
	}
	public int getDymicPasswayCheckedIssueCount() {
		return passwayCheckedIsuueArray == null ? 0 : passwayCheckedIsuueArray.size();
	}
	
	
	public void initDymicCheckedData(List<CheckData> dymicDatas,
			SparseArray<IssueItem> checkedIssueDatas) {
		if (dymicDatas != null && checkedIssueDatas != null) {
			for (CheckData checkData : dymicDatas) {
				for (IssueItem issueItem : checkData.getCheckedIssue()) {
					if (checkedIssueDatas.indexOfKey(issueItem.getId()) < 0) {
						IssueItem tempItem = new IssueItem();
						tempItem.clone(issueItem);
						checkedIssueDatas.put(issueItem.getId(), tempItem);
					}
				}
			}
		}
	}
	
	public void initDymicRoomCheckedData() {
		initDymicCheckedData(roomArray, roomCheckedIsuueArray);
	}

	public void initDymicPasswayCheckedData() {
		initDymicCheckedData(passwayArray, passwayCheckedIsuueArray);
	}
	
	public void updateDymicCheckedData(int position, CheckData checkData, IssueItem issueItem) {
		if (checkData.getType() == CheckDataType.TYPE_PASSWAY) {
			updateDymicCheckedIssue(position, checkData, passwayArray, passwayCheckedIsuueArray, issueItem);
		} else if (checkData.getType() == CheckDataType.TYPE_ROOM) {
			updateDymicCheckedIssue(position, checkData, roomArray, roomCheckedIsuueArray, issueItem);
		}
	}
	
	private void updateDymicCheckedIssue(int position, CheckData checkData, List<CheckData> checkDatas, SparseArray<IssueItem> checkedIssueArray, IssueItem issueItem) {
		if (checkDatas != null && issueItem != null) {
			if (checkedIssueArray.indexOfKey(issueItem.getId()) > -1) {
				IssueItem tempIssueItem = checkedIssueArray.get(issueItem.getId());
				if (tempIssueItem.getPreQueType() == PreQueType.TYPE_REVIEW) {
					if (tempIssueItem.getReformState() != issueItem.getReformState()) {
						if (issueItem.getReformState() == IssueItem.REFORM_STATE_DEFAULT) {
							tempIssueItem.setReformState(IssueItem.REFORM_STATE_DEFAULT);
							tempIssueItem.setCheck(issueItem.isCheck());
							DataManager.getInstance().saveIssueCheck(getId().intValue(), checkData.getId().intValue(), tempIssueItem.getId(), tempIssueItem.isCheck(), tempIssueItem.getReformState());
						} else {
								for (CheckData tempCheckData : checkDatas) {
									IssueItem issueItem2 = tempCheckData.getIssue(position);
									if (issueItem2.getReformState() != IssueItem.REFORM_STATE_FIXED) {
										return;
									}
								}
								tempIssueItem.setReformState(issueItem.getReformState());
								tempIssueItem.setCheck(issueItem.isCheck());
								DataManager.getInstance().saveIssueCheck(getId().intValue(), checkData.getId().intValue(), tempIssueItem.getId(), tempIssueItem.isCheck(), tempIssueItem.getReformState());
						}
					}
				} else {
					if (!issueItem.isCheck()) {
						for (CheckData tempCheckData : checkDatas) {
							IssueItem issueItem2 = tempCheckData.getIssue(position);
							if (issueItem2.isCheck()) {
								return;
							}
						}
						checkedIssueArray.remove(tempIssueItem.getId());
					}
				}
				
			} else if (issueItem.isCheck()) {
				IssueItem tempItem = new IssueItem();
				tempItem.clone(issueItem);
				checkedIssueArray.put(tempItem.getId(), tempItem);
			}
		}
	}
	
	public void updateDymicCheckDataIssueReform(int dymicCheckDataType, int issuePosition,int reformState) {
		if (dymicCheckDataType == CheckDataType.TYPE_ROOM) {
			IssueItem issueItem = roomCheckedIsuueArray.valueAt(issuePosition);
			issueItem.setReformState(reformState);
		} else if (dymicCheckDataType == CheckDataType.TYPE_PASSWAY) {
			IssueItem issueItem = passwayCheckedIsuueArray.valueAt(issuePosition);
			issueItem.setReformState(reformState);
		}
	}
	
	
	
	
	public IssueItem getDymicPasswayCheckedIssue(int position) {
		if (passwayCheckedIsuueArray != null) {
			return passwayCheckedIsuueArray.valueAt(position);
		}
		return null;
	}
	public IssueItem getDymicRoomCheckedIssue(int position) {
		if (roomCheckedIsuueArray != null) {
			return roomCheckedIsuueArray.valueAt(position);
		}
		return null;
	}
	
	public List<ImageItem> getAllRoomCheckedImageList() {
//		List<ImageItem> dataList = new ArrayList<ImageItem>();
//		for (int i = 0; i < roomCheckedIsuueArray.size(); i++) {
//			if (roomCheckedIsuueArray.valueAt(i).getImageCount() >0) {
//				dataList.addAll(roomCheckedIsuueArray.valueAt(i).getImagelist());
//			}
//		}
//		return dataList;
		return getAllDymicCheckedImage(roomCheckedIsuueArray, roomArray);
	}
	
	public List<ImageItem> getAllPasswayCheckedImageList() {
//		List<ImageItem> dataList = new ArrayList<ImageItem>();
//		for (int i = 0; i < passwayCheckedIsuueArray.size(); i++) {
//			if (passwayCheckedIsuueArray.valueAt(i).getImageCount() >0) {
//				dataList.addAll(passwayCheckedIsuueArray.valueAt(i).getImagelist());
//			}
//		}
//		return dataList;
		return getAllDymicCheckedImage(passwayCheckedIsuueArray, passwayArray);
	}
	
	public List<ImageItem> getAllDymicCheckedImage(SparseArray<IssueItem> dymicCheckedIssue, List<CheckData> dymicDatas) {
		if (dymicCheckedIssue == null || dymicDatas == null) {
			return Collections.emptyList();
		}
		List<ImageItem> dataList = new ArrayList<ImageItem>();
		if (dymicCheckedIssue != null) {
			for (int i = 0; i < dymicCheckedIssue.size(); i++) {
				IssueItem issueItem = dymicCheckedIssue.valueAt(i);
				if (issueItem != null) {
					for (CheckData checkData : dymicDatas) {
						for (IssueItem tempItem : checkData.getCheckedIssue()) {
							if (tempItem != null && tempItem.getId() == issueItem.getId()) {
								if (tempItem.getImagelist() != null && tempItem.getImagelist().size() >0) {
									dataList.addAll(tempItem.getImagelist());
								}
								
							}
						}
					}
				}
			}
			
		}
		return dataList;
	}
	
	
		public void deleteDymicCheckedIssueImage(ImageItem imageItem, int type) {
		List<CheckData> data = null;
		if (type == CheckDataType.TYPE_ROOM) {
			data = roomArray;
		} else if (type == CheckDataType.TYPE_PASSWAY) {
			data = passwayArray;
		}
		if (data != null) {
			for (CheckData checkData : data) {
				if (checkData.getCheckedIssueCount() > 0) {
					int index = 0;
					for (IssueItem issueItem : checkData.getCheckedIssue()) {
						if (issueItem.getImageCount() > 0) {
							for (int i = 0; i < issueItem.getImageCount(); i++) {
								ImageItem temp = issueItem.getImageItem(i);
								if (temp.getLocalImagePath().equals(imageItem.getLocalImagePath())) {
									issueItem.removeImageItem(i);
									IssueCheckedImage hotelCheck = IssueCheckedImage.deleteItemByImageLocalPath(imageItem.getLocalImagePath());
									if (TextUtils.isEmpty(issueItem.getContent())
											&& issueItem.getImageCount() == 0) {
										issueItem.setCheck(false);
										checkData.initCheckedIssue();
										DataManager.getInstance().saveIssueCheck(getCheckId(), checkData.getId().intValue(), issueItem.getId(), issueItem.isCheck(), issueItem.getReformState());
										if (type == CheckDataType.TYPE_ROOM) {
											updateDymicCheckedIssue(index, checkData, roomArray, roomCheckedIsuueArray, issueItem);
//											initDymicCheckedData(roomArray, roomCheckedIsuueArray);
										} else if (type == CheckDataType.TYPE_PASSWAY) {
											updateDymicCheckedIssue(index, checkData, passwayArray, passwayCheckedIsuueArray, issueItem);
//											initDymicCheckedData(passwayArray, passwayCheckedIsuueArray);
										}
										
									}
									return;
								}
							}
						}
						index++;
					}
				}
				
			}
		}
	}
	
	
	
	public List<ImageItem> getAllRoomCheckedImageList(int i) {
		List<ImageItem> dataList = new ArrayList<ImageItem>();
		if (roomCheckedIsuueArray != null) {
			IssueItem issueItem = roomCheckedIsuueArray.valueAt(i);
			if (issueItem != null) {
				for (CheckData checkData : roomArray) {
					for (IssueItem tempItem : checkData.getCheckedIssue()) {
						if (tempItem != null && tempItem.getId() == issueItem.getId()) {
							if (tempItem.getImagelist() != null && tempItem.getImagelist().size() >0) {
								dataList.addAll(tempItem.getImagelist());
							}
							
						}
					}
				}
			}
		}
		return dataList;
	}
	
	public List<ImageItem> getAllPasswayCheckedImageList(int i) {
		List<ImageItem> dataList = new ArrayList<ImageItem>();
		IssueItem issueItem = passwayCheckedIsuueArray.valueAt(i);
		if (issueItem != null) {
			for (CheckData checkData : passwayArray) {
				for (IssueItem tempItem : checkData.getCheckedIssue()) {
					if (tempItem.getId() == issueItem.getId()) {
						if (tempItem.getImagelist() != null && tempItem.getImagelist().size() >0) {
							dataList.addAll(tempItem.getImagelist());
						}
					}
				}
			}
		}
		return dataList;
	}
	
	public void initCheckedData() {
			initDymicCheckedData(roomArray, roomCheckedIsuueArray);
			initDymicCheckedData(passwayArray, passwayCheckedIsuueArray);
			//提取在报告页修改过的问题整改状态
			if (roomCheckedIsuueArray != null) {
				for (int i = 0; i < roomCheckedIsuueArray.size(); i++) {
					IssueItem issueItem = roomCheckedIsuueArray.valueAt(i);
					//问题是否已经检查过
					CheckIssue checkIssue = DataManager.getInstance().getCheckIssue(id.intValue(),
							(long)Constance.CHECK_DATA_ID_ROOM, issueItem.getId());
					if (checkIssue != null) {
						issueItem.setCheck(checkIssue.isCheck());
						issueItem.setReformState(checkIssue.getReformState());
					}
				}
			}
			if (passwayCheckedIsuueArray != null) {
				for (int i = 0; i < passwayCheckedIsuueArray.size(); i++) {
					IssueItem issueItem = passwayCheckedIsuueArray.valueAt(i);
					//问题是否已经检查过
					CheckIssue checkIssue = DataManager.getInstance().getCheckIssue(id.intValue(),
							(long)Constance.CHECK_DATA_ID_PASSWAY, issueItem.getId());
					if (checkIssue != null) {
						issueItem.setCheck(checkIssue.isCheck());
						issueItem.setReformState(checkIssue.getReformState());
					}
				}
			}
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public int getDymicRoomCheckedIssueImageCount(int id) {
		int count = 0;
		if (roomCheckedIsuueArray != null && roomCheckedIsuueArray.indexOfKey(id) > -1) {
			IssueItem issueItem = roomCheckedIsuueArray.get(id);
			if (issueItem != null) {
				for (CheckData checkData : roomArray) {
					for (IssueItem tempItem : checkData.getCheckedIssue()) {
						if (tempItem != null && tempItem.getId() == issueItem.getId()) {
							if (tempItem.getImagelist() != null && tempItem.getImagelist().size() >0) {
								count += tempItem.getImageCount();
							}
							
						}
					}
				}
			}
		}
		return count;
	}
	public int getDymicPasswayCheckedIssueImageCount(int id) {
		int count = 0;
		if (passwayCheckedIsuueArray != null && passwayCheckedIsuueArray.indexOfKey(id) > -1) {
			IssueItem issueItem = passwayCheckedIsuueArray.get(id);
			if (issueItem != null) {
				for (CheckData checkData : passwayArray) {
					for (IssueItem tempItem : checkData.getCheckedIssue()) {
						if (tempItem != null && tempItem.getId() == issueItem.getId()) {
							if (tempItem.getImagelist() != null && tempItem.getImagelist().size() >0) {
								count += tempItem.getImageCount();
							}
							
						}
					}
				}
			}
		}
		return count;
	}
	
	
	public boolean isAllImageUploaded() {
		boolean result = false;
		int imageCount = getImageCount();
		List<UploadBean> uploadTaskList = UploadBean.find(UploadBean.class,
				"CHECK_ID = ? and IMAGE_STATE = ?", String.valueOf(id),
				String.valueOf(ImageUploadState.STATE_FINISH));
		if (uploadTaskList != null && uploadTaskList.size() == imageCount) {
			result = true;
		}
		return result;
	}
	public String getBranchManager() {
		return branchManager;
	}
	public void setBranchManager(String branchManager) {
		this.branchManager = branchManager;
	}
	public String getBranchManagerTele() {
		return branchManagerTele;
	}
	public void setBranchManagerTele(String branchManagerTele) {
		this.branchManagerTele = branchManagerTele;
	}
	
	public void initReviewQuestionCheckData(CheckData checkData) {
		if (checkData == null || questionList == null) {
			return;
		}
		Log.d("lxk", "initReviewQuestionCheckData");
		for (AreaIssue areaIssue : questionList) {
			if (areaIssue.getType() == checkData.getType()) {
				if (areaIssue.getIsDefQue() == DefQueType.TYPE_DYMIC) {
					IssueItem issueItem = new IssueItem();
					int id = Math.abs(areaIssue.getIssueName().hashCode());
					issueItem.setId(id);
					issueItem.setName(areaIssue.getIssueName());
					issueItem.setPreQueType(areaIssue.getIsPreQue());
					issueItem.setIsDefQue(areaIssue.getIsDefQue());
					issueItem.setDimOneId(areaIssue.getDimOneId());
					issueItem.setDimOneName(areaIssue.getDimOneName());
					CheckIssue checkIssue = DataManager.getInstance().getCheckIssue(getCheckId(),
							checkData.getId(), id);
					if (checkIssue != null) {
						issueItem.setCheck(checkIssue.isCheck());
						issueItem.setContent(checkIssue.getContent());
						issueItem.setReformState(checkIssue.getReformState());
					}
					checkData.addIssue(issueItem);
				} else {
					if (areaIssue.getIsPreQue() == PreQueType.TYPE_REVIEW) {
						for (IssueItem issueItem : checkData.getIssuelist()) {
							if (issueItem.getId() == areaIssue.getIssueId()) {
								issueItem.setPreQueType(areaIssue.getIsPreQue());
								issueItem.setIsDefQue(areaIssue.getIsDefQue());
								issueItem.setCheck(issueItem.isCheck());
							}
						}
					}
				}
				
			}
		}
		checkData.initCheckedIssue();
	}
	
	public boolean checkReformState() {
		boolean result = false;
		if (checkType == CheckType.CHECK_TYPE_REVIEW ) {
			if (checkDatas != null) {
				for (CheckData checkData : checkDatas) {
					for (IssueItem issueItem : checkData.getCheckedIssue()) {
						if (issueItem.getPreQueType() == PreQueType.TYPE_REVIEW 
								&& issueItem.getReformState() == IssueItem.REFORM_STATE_DEFAULT) {
							return false;
						}
					}
				}
			}
			if (roomCheckedIsuueArray != null) {
				for (int i = 0; i < roomCheckedIsuueArray.size(); i++) {
					if (roomCheckedIsuueArray.valueAt(i).getPreQueType() == PreQueType.TYPE_REVIEW  
							&& roomCheckedIsuueArray.valueAt(i).getReformState() == IssueItem.REFORM_STATE_DEFAULT) {
						return false;
					}
				}
			}
			if (passwayCheckedIsuueArray != null) {
				for (int i = 0; i < passwayCheckedIsuueArray.size(); i++) {
					if (passwayCheckedIsuueArray.valueAt(i).getPreQueType() == PreQueType.TYPE_REVIEW  
							&& passwayCheckedIsuueArray.valueAt(i).getReformState() == IssueItem.REFORM_STATE_DEFAULT) {
						return false;
					}
				}
			}
			result = true;
		} else {
			result = true;
		}
		return result;
	}
	public String getRegionalManager() {
		return regionalManager;
	}
	public void setRegionalManager(String regionalManager) {
		this.regionalManager = regionalManager;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	
	public void addRoomDymicIssue(IssueItem issueItem) {
		if (roomDymicIssueArray == null) {
			roomDymicIssueArray = new ArrayList<IssueItem>();
		}
		if (roomDymicIssueArray.size() > 0) {
			for (IssueItem temp : roomDymicIssueArray) {
				if (temp.getId() == issueItem.getId()) {
					return;
				}
			}
		}
		roomDymicIssueArray.add(issueItem);
	}
	
	public void addPasswayDymicIssue(IssueItem issueItem) {
		if (passwayDymicIssueArray == null) {
			passwayDymicIssueArray = new ArrayList<IssueItem>();
		}
		if (passwayDymicIssueArray.size() > 0) {
			for (IssueItem temp : passwayDymicIssueArray) {
				if (temp.getId() == issueItem.getId()) {
					return;
				}
			}
			
		}
		passwayDymicIssueArray.add(issueItem);
	}
	
	public void initRoomDymicIssue(CheckData checkData) {
		if (checkData == null || roomDymicIssueArray == null) {
			return;
		}
		for (int i = 0; i < roomDymicIssueArray.size(); i++) {
			IssueItem tempIssueItem = roomDymicIssueArray.get(i);
			IssueItem issueItem = createDymicIssueItem(tempIssueItem.getName());
			DymicIssue dymicIssue = new DymicIssue(getId(), checkData.getId(), issueItem);
			dymicIssue.save();
			checkData.addIssue(issueItem);
		}
	}
	
	public void initPasswayDymicIssue(CheckData checkData) {
		if (checkData == null || passwayDymicIssueArray == null) {
			return;
		}
		for (int i = 0; i < passwayDymicIssueArray.size(); i++) {
			IssueItem tempIssueItem = passwayDymicIssueArray.get(i);
			IssueItem issueItem = createDymicIssueItem(tempIssueItem.getName());
			DymicIssue dymicIssue = new DymicIssue(getId(), checkData.getId(), issueItem);
			dymicIssue.save();
			checkData.addIssue(issueItem);
		}
	}
	
	private IssueItem createDymicIssueItem(String name) {
		IssueItem issueItem = new IssueItem();
		issueItem.setName(name);
		issueItem.setContent("");
		issueItem.setIsDefQue(DefQueType.TYPE_DYMIC);
		issueItem.setPreQueType(PreQueType.TYPE_NEW);
		issueItem.setDimOneId(1013);
		issueItem.setDimOneName("其他");
		int id = Math.abs(name.hashCode());
		issueItem.setId(id);
		return issueItem;
	}
	
}
