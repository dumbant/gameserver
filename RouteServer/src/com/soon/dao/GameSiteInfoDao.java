package com.soon.dao;

import java.util.List;

import com.soon.core.config.GameSiteInfo;
import com.soon.core.db.GlobalDao;

public class GameSiteInfoDao extends GlobalDao {

	public List<GameSiteInfo> getSiteInfoList(){
		String sql = "select * from serverinfo;";
		return this.selectList(sql,GameSiteInfo.class);
	}
}
